package net.modificationstation.stationloader.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_66;
import net.minecraft.client.Minecraft;
import net.modificationstation.stationloader.api.client.texture.TextureRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(class_66.class)
public class Mixinclass_66 {

    @Inject(method = "method_296()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Tessellator;start()V", shift = At.Shift.BEFORE))
    private void tempFix(CallbackInfo ci) {
        TextureRegistry.getRegistry(TextureRegistry.Vanilla.TERRAIN).bindAtlas(((Minecraft) FabricLoader.getInstance().getGameInstance()).textureManager, 0);
    }
}
