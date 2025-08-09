package net.modificationstation.stationapi.mixin.arsenic.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.render.ArsenicItemRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
@Environment(EnvType.CLIENT)
abstract class ItemRendererMixin extends EntityRenderer {
    @Unique
    private final ArsenicItemRenderer arsenic_plugin = new ArsenicItemRenderer((ItemRenderer) (Object) this);

    /**
     * @reason there's no saving notch code
     * @author mine_diver
     */
    @Overwrite
    public void render(ItemEntity arg, double d, double d1, double d2, float f, float f1) {
        arsenic_plugin.render(arg, d, d1, d2, f, f1);
    }

    /**
     * @reason there's no saving notch code
     * @author mine_diver
     */
    @Overwrite
    public void renderGuiItem(TextRenderer textRenderer, TextureManager textureManagerArg, int itemId, int damage, int textureIndex, int textureX, int textureY) {
        arsenic_plugin.renderItemOnGui(textRenderer, textureManagerArg, itemId, damage, textureIndex, textureX, textureY);
    }

    @Inject(
            method = "renderGuiItem(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/client/texture/TextureManager;Lnet/minecraft/item/ItemStack;II)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void stationapi_onRenderItemOnGuiItemStack(TextRenderer arg1, TextureManager arg2, ItemStack i, int j, int par5, CallbackInfo ci) {
        arsenic_plugin.renderItemOnGui(arg1, arg2, i, j, par5, ci);
    }

    /**
     * @reason there's no saving notch code
     * @author mine_diver
     */
    @Overwrite
    public void drawTexture(int i, int j, int k, int i1, int j1, int k1) {
        arsenic_plugin.renderItemQuad(i, j, k, i1, j1, k1);
    }
}
