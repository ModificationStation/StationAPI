package net.modificationstation.stationapi.mixin.effects;

import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.effect.EntityEffect;
import net.modificationstation.stationapi.api.effect.EntityEffectType;
import net.modificationstation.stationapi.api.effect.EntityEffectTypeRegistry;
import net.modificationstation.stationapi.api.entity.StationEffectsEntity;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.impl.effect.StationEffectsEntityImpl;
import net.modificationstation.stationapi.impl.effect.packet.EffectAddS2CPacket;
import net.modificationstation.stationapi.impl.effect.packet.EffectRemoveAllS2CPacket;
import net.modificationstation.stationapi.impl.effect.packet.EffectRemoveS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.Map;

@Mixin(Entity.class)
public class EntityMixin implements StationEffectsEntity, StationEffectsEntityImpl {
    @Unique private final Map<EntityEffectType<?>, EntityEffect<?>> stationapi_effects = new Reference2ReferenceOpenHashMap<>();
    
    @Shadow public int id;
    
    @Override
    @Unique
    public void addEffect(EntityEffectType<?> effectType, int ticks) {
        Entity thiz = Entity.class.cast(this);
        PacketHelper.dispatchLocallyAndToAllTracking(
                thiz, new EffectAddS2CPacket(thiz, effectType.factory.create(thiz, ticks))
        );
    }

    @Override
    @Unique
    public <EFFECT_INSTANCE extends EntityEffect<EFFECT_INSTANCE>> EFFECT_INSTANCE getEffect(
            EntityEffectType<EFFECT_INSTANCE> effectType
    ) {
        //noinspection unchecked
        return (EFFECT_INSTANCE) stationapi_effects.get(effectType);
    }

    @Override
    @Unique
    public Collection<EntityEffect<?>> getEffects() {
        return stationapi_effects.values();
    }

    @Override
    @Unique
    public boolean hasEffect(EntityEffectType<?> effectType) {
        return stationapi_effects.containsKey(effectType);
    }

    @Override
    @Unique
    public void removeEffect(EntityEffectType<?> effectType) {
        if (!stationapi_effects.containsKey(effectType)) return;
        PacketHelper.dispatchLocallyAndToAllTracking(
                Entity.class.cast(this), new EffectRemoveS2CPacket((Entity) (Object) this, effectType)
        );
    }
    
    @Override
    @Unique
    public void removeAllEffects() {
        PacketHelper.dispatchLocallyAndToAllTracking(
                Entity.class.cast(this), new EffectRemoveAllS2CPacket((Entity) (Object) this)
        );
    }

    @Override
    @Unique
    public void stationapi_addEffect(EntityEffect<?> effect, boolean appliedNow) {
        stationapi_effects.put(effect.getType(), effect);
        effect.onAdded(appliedNow);
    }

    @Override
    @Unique
    public void stationapi_removeAllEffects() {
        stationapi_effects.keySet().stream().map(stationapi_effects::remove).forEach(EntityEffect::onRemoved);
    }

    @Override
    @Unique
    public void stationapi_removeEffect(EntityEffectType<?> effectType) {
        stationapi_effects.remove(effectType).onRemoved();
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void stationapi_tickEffects(CallbackInfo info) {
        stationapi_effects.values().forEach(EntityEffect::tick);
    }
    
    @Inject(method = "write", at = @At("HEAD"))
    private void stationapi_writeEntityEffect(NbtCompound tag, CallbackInfo info) {
        if (stationapi_effects.isEmpty()) return;
        NbtList effects = new NbtList();
        stationapi_effects.forEach((type, effect) -> {
            var nbt = new NbtCompound();
            effect.write(nbt);
            nbt.putString("id", type.registryEntry.registryKey().getValue().toString());
            nbt.putInt("ticks", effect.getTicks());
            effects.add(nbt);
        });
        tag.put("stationapi:effects", effects);
    }
    
    @Inject(method = "read", at = @At("HEAD"))
    private void stationapi_readEntityEffect(NbtCompound tag, CallbackInfo info) {
        if (!tag.contains("stationapi:effects")) return;
        NbtList effects = tag.getList("stationapi:effects");
        for (int i = 0; i < effects.size(); i++) {
            NbtCompound effectTag = (NbtCompound) effects.get(i);
            Identifier id = Identifier.of(effectTag.getString("id"));
            EntityEffectType<?> effectType = EntityEffectTypeRegistry.INSTANCE.get(id);
            if (effectType == null) {
                StationAPI.LOGGER.warn("Effect " + id + " is not registered, skipping");
                continue;
            }
            int ticks = effectTag.getInt("ticks");
            EntityEffect<?> effect = effectType.factory.create(Entity.class.cast(this), ticks);
            effect.read(effectTag);
            stationapi_effects.put(effectType, effect);
        }
    }
}
