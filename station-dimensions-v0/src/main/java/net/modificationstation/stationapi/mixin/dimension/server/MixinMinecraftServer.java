package net.modificationstation.stationapi.mixin.dimension.server;

import net.minecraft.level.storage.LevelStorage;
import net.minecraft.server.MinecraftServer;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.registry.DimensionRegistryEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MixinMinecraftServer {

    @Inject(
            method = "prepareLevel(Lnet/minecraft/level/storage/LevelStorage;Ljava/lang/String;J)V",
            at = @At("HEAD")
    )
    private void registerDimensions(LevelStorage arg, String levelName, long seed, CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(new DimensionRegistryEvent());
    }
}
