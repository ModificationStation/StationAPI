package net.modificationstation.stationapi.mixin.resourceloader.client;

import com.oath.cyclops.util.ExceptionSoftener;
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
public class MixinMinecraft {
    @Shadow public World level;

    @Unique
    private static final String STATIONAPI$MINECRAFT_LOCATION_FORMAT = "Minecraft: %s";

    @Inject(
            method = "init",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/client/TexturePackManager;Lnet/minecraft/client/options/GameOptions;)Lnet/minecraft/client/texture/TextureManager;"
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
                    target = "(Lnet/minecraft/client/options/GameOptions;Ljava/lang/String;Lnet/minecraft/client/texture/TextureManager;)Lnet/minecraft/client/render/TextRenderer;"
            )
    )
    private void stationapi_location_textRenderer(CallbackInfo ci) {
        ReloadScreenManager.pushLocation(STATIONAPI$MINECRAFT_LOCATION_FORMAT.formatted("text_renderer"));
    }

    @Inject(
            method = "init",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/texture/TextureManager;getColorMap(Ljava/lang/String;)[I",
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
                    target = "(Lnet/minecraft/client/util/Session;Ljava/io/File;)Lnet/minecraft/util/io/StatsFileWriter;"
            )
    )
    private void stationapi_location_stats(CallbackInfo ci) {
        ReloadScreenManager.pushLocation(STATIONAPI$MINECRAFT_LOCATION_FORMAT.formatted("stats"));
    }

    @Inject(
            method = "init",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Minecraft;method_2150()V"
            )
    )
    private void stationapi_applyReloadsAndWait(CallbackInfo ci) {
        ReloadScreenManagerImpl.isMinecraftDone = true;
        while (!ReloadScreenManager.isReloadComplete()) {
            val command = ReloadScreenApplicationExecutor.INSTANCE.poll();
            if (command != null) command.run();
        }
        ReloadScreenManager.getThread().peek(ExceptionSoftener.softenConsumer(Thread::join));
    }
}
