package net.modificationstation.stationapi.mixin.render.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.TextRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.ItemRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.entity.Item;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.client.texture.plugin.ItemRendererPlugin;
import net.modificationstation.stationapi.api.client.texture.plugin.RenderPlugin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
@Environment(EnvType.CLIENT)
public abstract class MixinItemRenderer extends EntityRenderer {

    @Unique
    private final ItemRendererPlugin plugin = RenderPlugin.PLUGIN.createItemRenderer((ItemRenderer) (Object) this);

    @Inject(
            method = "render(Lnet/minecraft/entity/Item;DDDFF)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void render(Item arg, double d, double d1, double d2, float f, float f1, CallbackInfo ci) {
        plugin.render(arg, d, d1, d2, f, f1, ci);
    }

    @Inject(
            method = "renderItemOnGui(Lnet/minecraft/client/render/TextRenderer;Lnet/minecraft/client/texture/TextureManager;IIIII)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void renderItemOnGui_initCustomLocals(TextRenderer textRenderer, TextureManager textureManagerArg, int itemId, int damage, int textureIndex, int textureX, int textureY, CallbackInfo ci) {
        plugin.renderItemOnGui(textRenderer, textureManagerArg, itemId, damage, textureIndex, textureX, textureY, ci);
    }

    @Inject(
            method = "method_1487(Lnet/minecraft/client/render/TextRenderer;Lnet/minecraft/client/texture/TextureManager;Lnet/minecraft/item/ItemInstance;II)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onRenderItemOnGuiItemInstance(TextRenderer arg1, TextureManager arg2, ItemInstance i, int j, int par5, CallbackInfo ci) {
        plugin.renderItemOnGui(arg1, arg2, i, j, par5, ci);
    }

    @Inject(
            method = "method_1483(IIIIII)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void renderItemQuad(int i, int j, int k, int i1, int j1, int k1, CallbackInfo ci) {
        plugin.renderItemQuad(i, j, k, i1, j1, k1, ci);
    }
}
