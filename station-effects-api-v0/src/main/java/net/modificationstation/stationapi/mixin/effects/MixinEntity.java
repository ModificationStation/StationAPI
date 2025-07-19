package net.modificationstation.stationapi.mixin.effects;

import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.objects.ReferenceIntPair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.effect.EntityEffect;
import net.modificationstation.stationapi.api.effect.EntityEffectType;
import net.modificationstation.stationapi.api.effect.EntityEffectTypeRegistry;
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

import java.util.*;

@Mixin(Entity.class)
public class MixinEntity implements StationEffectEntity {
    @Unique private Map<EntityEffectType<?>, EntityEffect<?>> stationapi_effects;
    
    @Shadow public int id;
    
    @Override
    public void addEffect(EntityEffectType<?> effectType, int ticks) {
        Entity thiz = Entity.class.cast(this);
        EntityEffect<?> effect = effectType.factory.create(thiz, ticks);
        if (stationapi_effects == null)
            stationapi_effects = new Reference2ReferenceOpenHashMap<>();
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER)
            PacketHelper.sendToAllTracking(thiz, new EffectAddRemovePacket(id, effectType, ticks));
        stationapi_effects.put(effectType, effect);
        effect.onAdded();
    }
    
    @Override
    public void removeEffect(EntityEffectType<?> effectType) {
        if (stationapi_effects == null) return;
        EntityEffect<?> effect = stationapi_effects.get(effectType);
        if (effect == null) return;
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
            PacketHelper.sendToAllTracking(Entity.class.cast(this), new EffectAddRemovePacket(id, effectType, 0));
        }
        effect.onRemoved();
        if (stationapi_effects != null) {
            stationapi_effects.remove(effectType, effect);
            if (stationapi_effects.isEmpty()) stationapi_effects = null;
        }
    }
    
    @Override
    public Collection<EntityEffectType<?>> getEffects() {
        return stationapi_effects == null ? Collections.emptySet() : stationapi_effects.keySet();
    }
    
    @Override
    public void removeAllEffects() {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
            PacketHelper.sendToAllTracking(Entity.class.cast(this), new EffectRemoveAllPacket(id));
        }
        if (stationapi_effects == null) return;
        stationapi_effects.values().forEach(EntityEffect::onRemoved);
        stationapi_effects = null;
    }
    
    @Override
    public boolean hasEffect(EntityEffectType<?> effectType) {
        if (stationapi_effects == null) return false;
        return stationapi_effects.containsKey(effectType);
    }
    
    @Override
    public int getEffectTicks(EntityEffectType<?> effectType) {
        EntityEffect<?> effect = stationapi_effects.get(effectType);
        return effect == null ? 0 : effect.getTicks();
    }
    
    @Override
    @Environment(EnvType.CLIENT)
    public Collection<EntityEffect<?>> getRenderEffects() {
        return stationapi_effects == null ? null : stationapi_effects.values();
    }
    
    @Override
    @Environment(EnvType.CLIENT)
    public void addEffect(EntityEffect<?> effect) {
        if (stationapi_effects == null) stationapi_effects = new Reference2ReferenceOpenHashMap<>();
        stationapi_effects.put(effect.getType(), effect);
    }
    
    @Override
    @Environment(EnvType.SERVER)
    public Collection<ReferenceIntPair<EntityEffectType<?>>> getServerEffects() {
        if (stationapi_effects == null || stationapi_effects.isEmpty()) return null;
        List<ReferenceIntPair<EntityEffectType<?>>> effectPairs = new ArrayList<>(stationapi_effects.size());
        for (EntityEffect<?> effect : stationapi_effects.values()) {
            effectPairs.add(ReferenceIntPair.of(effect.getType(), effect.getTicks()));
        }
        return effectPairs;
    }
    
    @Inject(method = "tick", at = @At("HEAD"))
    private void stationapi_tickEffects(CallbackInfo info) {
        if (stationapi_effects == null) return;
        for (EntityEffect<?> effect : stationapi_effects.values()) {
            effect.tick();
        }
    }
    
    @Inject(method = "write", at = @At("HEAD"))
    private void stationapi_writeEntityEffect(NbtCompound tag, CallbackInfo info) {
        if (stationapi_effects == null || stationapi_effects.isEmpty()) return;
        NbtList effects = new NbtList();
        stationapi_effects.forEach((type, effect) -> {
            NbtCompound effectTag = effect.write();
            effectTag.putString("id", type.registryEntry.registryKey().getValue().toString());
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
