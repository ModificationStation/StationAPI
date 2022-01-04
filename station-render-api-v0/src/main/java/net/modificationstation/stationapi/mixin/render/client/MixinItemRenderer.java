package net.modificationstation.stationapi.mixin.render.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockBase;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.TextRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.ItemRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.entity.Item;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.client.model.item.ItemWithRenderer;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.CustomAtlasProvider;
import net.modificationstation.stationapi.api.client.texture.plugin.ItemRendererPlugin;
import net.modificationstation.stationapi.api.client.texture.plugin.RenderPlugin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.image.*;
import java.util.*;

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
    private void render_initCustomLocals(Item arg, double d, double d1, double d2, float f, float f1, CallbackInfo ci) {
        plugin.render(arg, d, d1, d2, f, f1, ci);
    }

    @Unique
    private final Stack<Atlas> renderItemOnGui_customLocals_atlas = new Stack<>();

    @Inject(
            method = "renderItemOnGui(Lnet/minecraft/client/render/TextRenderer;Lnet/minecraft/client/texture/TextureManager;IIIII)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void renderItemOnGui_initCustomLocals(TextRenderer textRenderer, TextureManager textureManagerArg, int itemId, int damage, int textureIndex, int textureX, int textureY, CallbackInfo ci) {
        Object itemOrBlock = itemId < BlockBase.BY_ID.length ? BlockBase.BY_ID[itemId] : ItemBase.byId[itemId];
        ItemBase item = ItemBase.byId[itemId];
        if (item instanceof ItemWithRenderer) {
            ((ItemWithRenderer) item).renderItemOnGui(textRenderer, textureManagerArg, itemId, damage, textureIndex, textureX, textureY);
            ci.cancel();
        } else
            renderItemOnGui_customLocals_atlas.push(itemOrBlock instanceof CustomAtlasProvider ? ((CustomAtlasProvider) itemOrBlock).getAtlas().of(textureIndex) : null);
    }

    @Redirect(
            method = "renderItemOnGui(Lnet/minecraft/client/render/TextRenderer;Lnet/minecraft/client/texture/TextureManager;IIIII)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/texture/TextureManager;getTextureId(Ljava/lang/String;)I",
                    ordinal = 2
            )
    )
    private int modifyItemTextureID(TextureManager textureManager, String string, TextRenderer textRenderer, TextureManager textureManagerArg, int itemId, int damage, int textureIndex, int textureX, int textureY) {
        Atlas atlas = renderItemOnGui_customLocals_atlas.peek();
        return atlas == null ? textureManager.getTextureId(string) : atlas.getAtlasTextureID();
    }

    @Redirect(
            method = "renderItemOnGui(Lnet/minecraft/client/render/TextRenderer;Lnet/minecraft/client/texture/TextureManager;IIIII)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/entity/ItemRenderer;method_1483(IIIIII)V"
            )
    )
    private void redirectItemDraw(ItemRenderer itemRenderer, int renderX, int renderY, int textureX, int textureY, int width, int height, TextRenderer textRenderer, TextureManager textureManagerArg, int itemId, int damage, int textureIndex, int renderYArg, int renderXArg) {
        Atlas atlas = renderItemOnGui_customLocals_atlas.peek();
        if (atlas != null) {
            Atlas.Sprite texture = atlas.getTexture(textureIndex);
            textureX = texture.getX();
            textureY = texture.getY();
            width = texture.getWidth();
            height = texture.getHeight();
            method_1483_customArguments_customImage = atlas.getImage();
        }
        itemRenderer.method_1483(renderX, renderY, textureX, textureY, width, height);
    }

    @Inject(
            method = "renderItemOnGui(Lnet/minecraft/client/render/TextRenderer;Lnet/minecraft/client/texture/TextureManager;IIIII)V",
            at = @At("RETURN")
    )
    private void renderItemOnGui_clearCustomLocals(TextRenderer arg, TextureManager arg1, int i, int j, int k, int i1, int j1, CallbackInfo ci) {
        renderItemOnGui_customLocals_atlas.pop();
    }

    @Inject(
            method = "method_1487(Lnet/minecraft/client/render/TextRenderer;Lnet/minecraft/client/texture/TextureManager;Lnet/minecraft/item/ItemInstance;II)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/entity/ItemRenderer;renderItemOnGui(Lnet/minecraft/client/render/TextRenderer;Lnet/minecraft/client/texture/TextureManager;IIIII)V"
            ),
            cancellable = true
    )
    private void onRenderItemOnGuiItemInstance(TextRenderer arg1, TextureManager arg2, ItemInstance i, int j, int par5, CallbackInfo ci) {
        ItemBase itemBase = i.getType();
        if (itemBase instanceof ItemWithRenderer) {
            ((ItemWithRenderer) itemBase).renderItemOnGui(arg1, arg2, i, j, par5);
            ci.cancel();
        }
    }

    @Unique
    private BufferedImage method_1483_customArguments_customImage;

    @Unique
    private final Stack<BufferedImage> method_1483_customLocals_customImage = new Stack<>();
    @Unique
    private final Stack<Boolean> method_1483_customLocals_vanillaRender = new Stack<>();

    @Inject(
            method = "method_1483(IIIIII)V",
            at = @At("HEAD")
    )
    private void method_1483_initCustomLocals(int i, int j, int k, int i1, int j1, int k1, CallbackInfo ci) {
        method_1483_customLocals_customImage.push(method_1483_customArguments_customImage);
        method_1483_customLocals_vanillaRender.push(method_1483_customArguments_customImage == null);
        method_1483_customArguments_customImage = null;
    }

    @ModifyVariable(
            method = "method_1483(IIIIII)V",
            index = 8,
            at = @At("STORE")
    )
    private float modifyHorizontalScale(float originalScale) {
        return method_1483_customLocals_vanillaRender.peek() ? originalScale : 1F / method_1483_customLocals_customImage.peek().getWidth();
    }

    @ModifyVariable(
            method = "method_1483(IIIIII)V",
            index = 9,
            at = @At("STORE")
    )
    private float modifyVerticalScale(float originalScale) {
        BufferedImage image = method_1483_customLocals_customImage.pop();
        return method_1483_customLocals_vanillaRender.peek() ? originalScale : 1F / image.getHeight();
    }

    @Redirect(
            method = "method_1483(IIIIII)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/Tessellator;vertex(DDDDD)V",
                    ordinal = 0
            )
    )
    private void modVertexHeightAndUV(Tessellator tessellator, double d, double d1, double d2, double d3, double d4, int x, int y, int textureX, int textureY, int width, int height) {
        tessellator.vertex(d, method_1483_customLocals_vanillaRender.peek() ? d1 : y + 16, d2, d3, d4);
    }

    @Redirect(
            method = "method_1483(IIIIII)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/Tessellator;vertex(DDDDD)V",
                    ordinal = 1
            )
    )
    private void modVertexWidthHeightAndUV(Tessellator tessellator, double d, double d1, double d2, double d3, double d4, int x, int y, int textureX, int textureY, int width, int height) {
        if (method_1483_customLocals_vanillaRender.peek())
            tessellator.vertex(d, d1, d2, d3, d4);
        else
            tessellator.vertex(x + 16, y + 16, d2, d3, d4);
    }

    @Redirect(
            method = "method_1483(IIIIII)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/Tessellator;vertex(DDDDD)V",
                    ordinal = 2
            )
    )
    private void modVertexWidthAndUV(Tessellator tessellator, double d, double d1, double d2, double d3, double d4, int x, int y, int textureX, int textureY, int width, int height) {
        tessellator.vertex(method_1483_customLocals_vanillaRender.pop() ? d : x + 16, d1, d2, d3, d4);
    }
}
