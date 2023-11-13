package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.WorldProperties;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.impl.world.StationWorldProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;
import static net.modificationstation.stationapi.api.util.Identifier.of;

@Mixin(WorldProperties.class)
class WorldPropertiesMixin implements StationWorldProperties {
    @Unique private static final String STATIONAPI$DIMENSIONS_KEY = of(NAMESPACE, "dimensions").toString();
    @Unique private static NbtCompound stationapi_dimensionsRoot;

    @Inject(
            method = "<init>(Lnet/minecraft/nbt/NbtCompound;)V",
            at = @At("TAIL")
    )
    private void stationapi_onPropertiesLoad(NbtCompound worldTag, CallbackInfo info) {
        stationapi_dimensionsRoot = worldTag.getCompound(STATIONAPI$DIMENSIONS_KEY);
    }

    @Inject(
            method = "<init>(JLjava/lang/String;)V",
            at = @At("TAIL")
    )
    private void stationapi_onPropertiesCreate(long seed, String name, CallbackInfo info) {
        stationapi_dimensionsRoot = new NbtCompound();
    }

    @Inject(
            method = "updateProperties",
            at = @At("HEAD")
    )
    private void stationapi_updateProperties(NbtCompound worldTag, NbtCompound playerTag, CallbackInfo ci) {
        worldTag.put(STATIONAPI$DIMENSIONS_KEY, stationapi_dimensionsRoot);
    }

    @Override
    @Unique
    public NbtCompound getDimensionTag(Identifier id) {
        String key = id.toString();
        if (stationapi_dimensionsRoot.contains(key)) {
            return stationapi_dimensionsRoot.getCompound(key);
        }
        else {
            NbtCompound tag = new NbtCompound();
            stationapi_dimensionsRoot.put(key, tag);
            return tag;
        }
    }
}
