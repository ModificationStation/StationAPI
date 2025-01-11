package net.modificationstation.stationapi.mixin.effects;

import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.effect.EffectRegistry;
import net.modificationstation.stationapi.api.effect.EntityEffect;
import net.modificationstation.stationapi.api.effect.StationEffectEntity;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.impl.effect.packet.EffectAddRemovePacket;
import net.modificationstation.stationapi.impl.effect.packet.EffectRemoveAllPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Mixin(Entity.class)
public class MixinEntity implements StationEffectEntity {
	@Unique private Map<Identifier, EntityEffect<? extends Entity>> stationapi_effects;
	
	@Shadow public int id;
	
	@Override
	public void addEffect(Identifier effectID, int ticks) {
		EntityEffect<? extends Entity> effect = EffectRegistry.makeEffect(Entity.class.cast(this), effectID, ticks);
		if (effect == null) return;
		if (stationapi_effects == null) {
			stationapi_effects = new Reference2ReferenceOpenHashMap<>();
		}
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
			PacketHelper.send(new EffectAddRemovePacket(id, effectID, ticks));
		}
		stationapi_effects.put(effectID, effect);
		effect.onAdded();
	}
	
	@Override
	public void removeEffect(Identifier effectID) {
		if (stationapi_effects == null) return;
		EntityEffect<? extends Entity> effect = stationapi_effects.get(effectID);
		if (effect == null) return;
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
			PacketHelper.send(new EffectAddRemovePacket(id, effectID, 0));
		}
		effect.onRemoved();
		if (stationapi_effects != null) {
			stationapi_effects.remove(effectID, effect);
			if (stationapi_effects.isEmpty()) stationapi_effects = null;
		}
	}
	
	@Override
	public Collection<Identifier> getEffects() {
		return stationapi_effects == null ? Collections.emptySet() : stationapi_effects.keySet();
	}
	
	@Override
	public void removeAllEffects() {
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
			PacketHelper.send(new EffectRemoveAllPacket());
		}
		if (stationapi_effects == null) return;
		stationapi_effects = null;
	}
	
	@Override
	public boolean hasEffect(Identifier effectID) {
		if (stationapi_effects == null) return false;
		return stationapi_effects.containsKey(effectID);
	}
	
	@Override
	public int getEffectTicks(Identifier effectID) {
		EntityEffect<? extends Entity> effect = stationapi_effects.get(effectID);
		return effect == null ? 0 : effect.getTicks();
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public Collection<EntityEffect<? extends Entity>> getRenderEffects() {
		return stationapi_effects == null ? null : stationapi_effects.values();
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void addEffect(EntityEffect<? extends Entity> effect) {
		if (stationapi_effects == null) stationapi_effects = new Reference2ReferenceOpenHashMap<>();
		stationapi_effects.put(effect.getEffectID(), effect);
	}
	
	@Override
	@Environment(EnvType.SERVER)
	public Collection<Pair<Identifier, Integer>> getServerEffects() {
		if (stationapi_effects == null || stationapi_effects.isEmpty()) return null;
		List<Pair<Identifier, Integer>> effectPairs = new ArrayList<>(stationapi_effects.size());
		for (EntityEffect<? extends Entity> effect : stationapi_effects.values()) {
			effectPairs.add(Pair.of(effect.getEffectID(), effect.getTicks()));
		}
		return effectPairs;
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
			effectTag.putString("id", id.toString());
			effectTag.putInt("ticks", effect.getTicks());
			effects.add(effectTag);
		});
		tag.put("stationapi:effects", effects);
	}
	
	@Inject(method = "read", at = @At("HEAD"))
	private void stationapi_readEntityEffect(NbtCompound tag, CallbackInfo info) {
		if (!tag.contains("stationapi:effects")) return;
		NbtList effects = tag.getList("stationapi:effects");
		if (stationapi_effects == null) stationapi_effects = new Reference2ReferenceOpenHashMap<>();
		for (int i = 0; i < effects.size(); i++) {
			NbtCompound effectTag = (NbtCompound) effects.get(i);
			Identifier id = Identifier.of(effectTag.getString("id"));
			if (!EffectRegistry.hasEffect(id)) {
				StationAPI.LOGGER.warn("Effect " + id + " is not registered, skipping");
				continue;
			}
			int ticks = effectTag.getInt("ticks");
			EntityEffect<? extends Entity> effect = EffectRegistry.makeEffect(Entity.class.cast(this), id, ticks);
			effect.read(effectTag);
			stationapi_effects.put(id, effect);
		}
	}
}
