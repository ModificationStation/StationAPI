package net.modificationstation.stationapi.mixin.flattening;

import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.impl.level.StationLevelProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.modificationstation.stationapi.api.StationAPI.MODID;
import static net.modificationstation.stationapi.api.registry.Identifier.of;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.WorldProperties;

@Mixin(WorldProperties.class)
public class MixinLevelProperties implements StationLevelProperties {
    @Unique private static final String DIMENSIONS_KEY = of(MODID, "dimensions").toString();
    @Unique private static NbtCompound dimensionsRoot;

    @Inject(method = "<init>(Lnet/minecraft/util/io/CompoundTag;)V", at = @At("TAIL"))
    private void onPropertiesLoad(NbtCompound worldTag, CallbackInfo info) {
        dimensionsRoot = worldTag.getCompound(DIMENSIONS_KEY);
    }

    @Inject(method = "<init>(JLjava/lang/String;)V", at = @At("TAIL"))
    private void onPropertiesCreate(long seed, String name, CallbackInfo info) {
        dimensionsRoot = new NbtCompound();
    }

    @Inject(method = "updateProperties", at = @At("HEAD"))
    private void updateProperties(NbtCompound worldTag, NbtCompound playerTag, CallbackInfo ci) {
        worldTag.put(DIMENSIONS_KEY, dimensionsRoot);
    }

    @Override
    public NbtCompound getDimensionTag(Identifier id) {
        String key = id.toString();
        if (dimensionsRoot.contains(key)) {
            return dimensionsRoot.getCompound(key);
        }
        else {
            NbtCompound tag = new NbtCompound();
            dimensionsRoot.put(key, tag);
            return tag;
        }
    }
}
