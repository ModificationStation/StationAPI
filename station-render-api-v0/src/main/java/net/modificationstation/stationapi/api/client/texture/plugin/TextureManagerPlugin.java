package net.modificationstation.stationapi.api.client.texture.plugin;

import net.minecraft.client.texture.TextureManager;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.image.*;

public class TextureManagerPlugin {

    protected final TextureManager textureManager;

    public TextureManagerPlugin(TextureManager textureManager) {
        this.textureManager = textureManager;
    }

    public void getTextureId(String par1, CallbackInfoReturnable<Integer> cir) {}

    public void tick(CallbackInfo ci) {}

    public void bindImageToId(BufferedImage imageToBind, int targetId, CallbackInfo ci) {}

    public void bindImageToId(int[] pixels, int width, int height, int targetId, CallbackInfo ci) {}

    public interface Provider {

        <T extends TextureManagerPlugin> T getPlugin();
    }
}
