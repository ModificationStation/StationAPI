package net.modificationstation.stationapi.mixin.resourceloader.client;

import lombok.val;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.event.resource.AssetsReloadEvent;
import net.modificationstation.stationapi.api.client.event.resource.AssetsResourceReloaderRegisterEvent;
import net.modificationstation.stationapi.api.client.resource.ReloadScreenManager;
import net.modificationstation.stationapi.api.client.resource.ReloadableAssetsManager;
import net.modificationstation.stationapi.api.event.resource.DataReloadEvent;
import net.modificationstation.stationapi.api.event.resource.DataResourceReloaderRegisterEvent;
import net.modificationstation.stationapi.api.resource.DataManager;
import net.modificationstation.stationapi.impl.client.resource.ReloadScreenApplicationExecutor;
import net.modificationstation.stationapi.impl.client.resource.ReloadScreenManagerImpl;
import org.lwjgl.LWJGLException;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
@Environment(EnvType.CLIENT)
class MinecraftMixin {
    @Shadow public World world;

    @Unique
    private static final String STATIONAPI$MINECRAFT_LOCATION_FORMAT = "Minecraft: %s";

    @Inject(
            method = "init",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/client/Minecraft;Ljava/io/File;)Lnet/minecraft/class_303;"
            )
    )
    private void stationapi_location_textureManager(CallbackInfo ci) {
        ReloadScreenManager.pushLocation(STATIONAPI$MINECRAFT_LOCATION_FORMAT.formatted("texture_manager"));
    }

    @Inject(
            method = "init",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/Minecraft;textureManager:Lnet/minecraft/client/texture/TextureManager;",
                    opcode = Opcodes.PUTFIELD,
                    shift = At.Shift.AFTER
            )
    )
    private void stationapi_textureManagerInit(CallbackInfo ci) throws LWJGLException {
        ReloadScreenManager.openEarly();
        StationAPI.EVENT_BUS.post(
                AssetsResourceReloaderRegisterEvent.builder()
                        .resourceManager(ReloadableAssetsManager.INSTANCE)
                        .build()
        );
        StationAPI.EVENT_BUS.post(AssetsReloadEvent.builder().build());
        StationAPI.EVENT_BUS.post(
                DataResourceReloaderRegisterEvent.builder()
                        .resourceManager(DataManager.INSTANCE)
                        .build()
        );
        StationAPI.EVENT_BUS.post(DataReloadEvent.builder().build());
    }

    @Inject(
            method = "init",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/client/option/GameOptions;Ljava/lang/String;Lnet/minecraft/client/texture/TextureManager;)Lnet/minecraft/client/font/TextRenderer;"
            )
    )
    private void stationapi_location_textRenderer(CallbackInfo ci) {
        ReloadScreenManager.pushLocation(STATIONAPI$MINECRAFT_LOCATION_FORMAT.formatted("text_renderer"));
    }

    @Inject(
            method = "init",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/texture/TextureManager;getColors(Ljava/lang/String;)[I",
                    ordinal = 0
            )
    )
    private void stationapi_location_colorizers(CallbackInfo ci) {
        ReloadScreenManager.pushLocation(STATIONAPI$MINECRAFT_LOCATION_FORMAT.formatted("colorizers"));
    }

    @Inject(
            method = "init",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/client/util/Session;Ljava/io/File;)Lnet/minecraft/class_96;"
            )
    )
    private void stationapi_location_stats(CallbackInfo ci) {
        ReloadScreenManager.pushLocation(STATIONAPI$MINECRAFT_LOCATION_FORMAT.formatted("stats"));
    }

    @Inject(
            method = "init",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Minecraft;renderLoadingScreen()V"
            )
    )
    private void stationapi_applyReloadsAndWait(CallbackInfo ci) {
        ReloadScreenManagerImpl.isMinecraftDone = true;
        while (!ReloadScreenManager.isReloadComplete()) {
            val command = ReloadScreenApplicationExecutor.INSTANCE.poll();
            if (command != null) command.run();
        }
        ReloadScreenManager.getThread().ifPresent(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
