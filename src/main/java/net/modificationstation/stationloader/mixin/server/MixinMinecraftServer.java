package net.modificationstation.stationloader.mixin.server;

import net.minecraft.server.MinecraftServer;
import net.modificationstation.stationloader.api.common.StationLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.net.URISyntaxException;

@Mixin(MinecraftServer.class)
public class MixinMinecraftServer {

    @Inject(method = "main([Ljava/lang/String;)V", at = @At("HEAD"), remap = false)
    private static void beforeMain(String[] strings, CallbackInfo ci) throws IllegalAccessException, InstantiationException, ClassNotFoundException, IOException, URISyntaxException, NoSuchFieldException {
        StationLoader.INSTANCE.setup();
    }
}
