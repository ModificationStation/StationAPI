package net.modificationstation.stationapi.mixin.resourceloader.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.event.resource.AssetsReloadEvent;
import net.modificationstation.stationapi.api.client.event.resource.AssetsResourceReloaderRegisterEvent;
import net.modificationstation.stationapi.api.client.resource.ReloadableAssetsManager;
import net.modificationstation.stationapi.api.event.resource.DataReloadEvent;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
@Environment(EnvType.CLIENT)
public class MixinMinecraft {

    @Shadow public Level level;

    @Inject(method = "init()V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;textureManager:Lnet/minecraft/client/texture/TextureManager;", opcode = Opcodes.PUTFIELD, shift = At.Shift.AFTER))
    private void textureManagerInit(CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(
                AssetsResourceReloaderRegisterEvent.builder()
                        .resourceManager(ReloadableAssetsManager.INSTANCE)
                        .build()
        );
        StationAPI.EVENT_BUS.post(AssetsReloadEvent.builder().build());
    }

    @Inject(
            method = "showLevelProgress(Lnet/minecraft/level/Level;Ljava/lang/String;Lnet/minecraft/entity/player/PlayerBase;)V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/Minecraft;level:Lnet/minecraft/level/Level;",
                    opcode = Opcodes.PUTFIELD,
                    shift = At.Shift.AFTER
            )
    )
    private void worldInit(Level string, String arg2, PlayerBase par3, CallbackInfo ci) {
        if (this.level != null)
            StationAPI.EVENT_BUS.post(DataReloadEvent.builder().build());
    }
}
