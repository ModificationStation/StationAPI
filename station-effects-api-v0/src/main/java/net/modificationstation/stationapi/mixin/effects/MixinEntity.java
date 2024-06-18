package net.modificationstation.stationapi.mixin.effects;

import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.effect.EffectRegistry;
import net.modificationstation.stationapi.api.effect.EntityEffect;
import net.modificationstation.stationapi.api.effect.StationEffectEntity;
import net.modificationstation.stationapi.api.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Mixin(Entity.class)
public class MixinEntity implements StationEffectEntity {
	@Unique private Map<Identifier, EntityEffect<? extends Entity>> stationapi_effects;
	
	@Override
	public void addEffect(Identifier effectID) {
		EntityEffect<? extends Entity> effect = EffectRegistry.makeEffect(Entity.class.cast(this), effectID);
		if (effect == null) return;
		if (stationapi_effects == null) {
			stationapi_effects = new Reference2ReferenceOpenHashMap<>();
		}
		stationapi_effects.put(effectID, effect);
		effect.onStart();
	}
	
	@Override
	public void removeEffect(Identifier effectID) {
		if (stationapi_effects == null) return;
		stationapi_effects.remove(effectID);
		if (stationapi_effects.isEmpty()) stationapi_effects = null;
	}
	
	@Override
	public Collection<Identifier> getEffects() {
		return stationapi_effects == null ? Collections.emptySet() : stationapi_effects.keySet();
	}
	
	@Override
	public void removeAllEffects() {
		if (stationapi_effects == null) return;
		stationapi_effects = null;
	}
	
	@Override
	public boolean hasEffect(Identifier effectID) {
		if (stationapi_effects == null) return false;
		return stationapi_effects.containsKey(effectID);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public Collection<EntityEffect<? extends Entity>> getRenderEffects() {
		return stationapi_effects == null ? null : stationapi_effects.values();
	}
	
	@Inject(method = "tick", at = @At("HEAD"))
	private void stationapi_tickEffects(CallbackInfo info) {
		if (stationapi_effects == null) return;
		for (EntityEffect<? extends Entity> effect : stationapi_effects.values()) {
			effect.tick();
		}
	}
	
	@Inject(method = "write", at = @At("HEAD"))
	private void stationapi_writeEntityEffect(NbtCompound tag, CallbackInfo info) {
		if (stationapi_effects == null || stationapi_effects.isEmpty()) return;
		NbtList effects = new NbtList();
		stationapi_effects.forEach((id, effect) -> {
			NbtCompound effectTag = effect.write();
			effectTag.putString("effect_id", id.toString());
			effects.add(effectTag);
		});
		tag.put("stationapi_effects", effects);
	}
	
	@Inject(method = "read", at = @At("HEAD"))
	private void stationapi_readEntityEffect(NbtCompound tag, CallbackInfo info) {
		if (!tag.contains("stationapi_effects")) return;
		NbtList effects = tag.getList("stationapi_effects");
		if (stationapi_effects == null) stationapi_effects = new Reference2ReferenceOpenHashMap<>();
		for (int i = 0; i < effects.size(); i++) {
			NbtCompound effectTag = (NbtCompound) effects.get(i);
			Identifier id = Identifier.of(effectTag.getString("effect_id"));
			EntityEffect<? extends Entity> effect = EffectRegistry.makeEffect(Entity.class.cast(this), id);
			effect.read(effectTag);
			stationapi_effects.put(id, effect);
		}
	}
}
