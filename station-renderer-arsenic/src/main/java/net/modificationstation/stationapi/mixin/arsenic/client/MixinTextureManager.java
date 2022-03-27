package net.modificationstation.stationapi.mixin.arsenic.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.texture.TextureManager;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.render.ArsenicTextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.image.BufferedImage;

@Mixin(TextureManager.class)
@Environment(EnvType.CLIENT)
public class MixinTextureManager {

    @Unique
    private final ArsenicTextureManager arsenic_plugin = new ArsenicTextureManager((TextureManager) (Object) this);

    /**
     * @reason there's no saving notch code
     * @author mine_diver
     */
    @Overwrite
    public void bindImageToId(BufferedImage image, int targetId) {
        arsenic_plugin.bindImageToId(image, targetId);
    }

    /**
     * @reason there's no saving notch code
     * @author mine_diver
     */
    @Overwrite
    public void bindImageToId(int[] pixels, int width, int height, int targetId) {
        arsenic_plugin.bindImageToId(pixels, width, height, targetId);
    }

    /**
     * @reason there's no saving notch code
     * @author mine_diver
     */
    @Overwrite
    public void tick() {
        arsenic_plugin.tick();
    }

    @Inject(
            method = "getTextureId(Ljava/lang/String;)I",
            at = @At("HEAD"),
            cancellable = true
    )
    private void getTextureId(String par1, CallbackInfoReturnable<Integer> cir) {
        arsenic_plugin.getTextureId(par1, cir);
    }
}
