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
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.CustomAtlasProvider;
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
    private final Stack<Atlas> render_customLocals_atlas = new Stack<>();

    @Inject(
            method = "render(Lnet/minecraft/entity/Item;DDDFF)V",
            at = @At("HEAD")
    )
    private void render_initCustomLocals(Item arg, double d, double d1, double d2, float f, float f1, CallbackInfo ci) {
        int itemId = arg.item.itemId;
        Object item = itemId < BlockBase.BY_ID.length ? BlockBase.BY_ID[itemId] : ItemBase.byId[itemId];
        render_customLocals_atlas.push(item instanceof CustomAtlasProvider ? ((CustomAtlasProvider) item).getAtlas() : null);
    }

    @Unique
    private final Stack<Atlas.Texture> render_customLocals_texture = new Stack<>();

    @ModifyVariable(
            method = "render(Lnet/minecraft/entity/Item;DDDFF)V",
            index = 14,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemInstance;getTexturePosition()I",
                    shift = At.Shift.BY,
                    by = 2
            )
    )
    private int captureTexturePosition(int texturePosition) {
        Atlas atlas = render_customLocals_atlas.pop();
        render_customLocals_atlas.push(atlas = (atlas == null ? null : atlas.of(texturePosition)));
        render_customLocals_texture.push(atlas == null ? null : atlas.getTexture(texturePosition));
        return texturePosition;
    }

    @Redirect(
            method = "render(Lnet/minecraft/entity/Item;DDDFF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/entity/ItemRenderer;bindTexture(Ljava/lang/String;)V",
                    ordinal = 1
            )
    )
    private void bindTerrainAtlas(ItemRenderer itemRenderer, String texture, Item itemEntity, double d, double d1, double d2, float f, float f1) {
        Atlas atlas = render_customLocals_atlas.peek();
        if (atlas == null)
            ((EntityRendererAccessor) itemRenderer).invokeBindTexture(texture);
        else
            atlas.bindAtlas();
    }

    @Redirect(
            method = "render(Lnet/minecraft/entity/Item;DDDFF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/entity/ItemRenderer;bindTexture(Ljava/lang/String;)V",
                    ordinal = 2
            )
    )
    private void bindItemAtlas(ItemRenderer itemRenderer, String texture, Item itemEntity, double d, double d1, double d2, float f, float f1) {
        Atlas atlas = render_customLocals_atlas.peek();
        if (atlas == null)
            ((EntityRendererAccessor) itemRenderer).invokeBindTexture(texture);
        else
            atlas.bindAtlas();
    }

    @ModifyVariable(
            method = "render(Lnet/minecraft/entity/Item;DDDFF)V",
            index = 16,
            at = @At("STORE")
    )
    private float modifyStartU(float originalStartU) {
        Atlas.Texture texture = render_customLocals_texture.peek();
        return texture == null ? originalStartU : texture.getStartU();
    }

    @ModifyVariable(
            method = "render(Lnet/minecraft/entity/Item;DDDFF)V",
            index = 17,
            at = @At("STORE")
    )
    private float modifyEndU(float originalEndU) {
        Atlas.Texture texture = render_customLocals_texture.peek();
        return texture == null ? originalEndU : texture.getEndU();
    }

    @ModifyVariable(
            method = "render(Lnet/minecraft/entity/Item;DDDFF)V",
            index = 18,
            at = @At("STORE")
    )
    private float modifyStartV(float originalStartV) {
        Atlas.Texture texture = render_customLocals_texture.peek();
        return texture == null ? originalStartV : texture.getStartV();
    }

    @ModifyVariable(
            method = "render(Lnet/minecraft/entity/Item;DDDFF)V",
            index = 19,
            at = @At("STORE")
    )
    private float modifyEndV(float originalEndV) {
        Atlas.Texture texture = render_customLocals_texture.pop();
        return texture == null ? originalEndV : texture.getEndV();
    }

    @Inject(
            method = "render(Lnet/minecraft/entity/Item;DDDFF)V",
            at = @At("RETURN")
    )
    private void render_clearCustomLocals(Item arg, double d, double d1, double d2, float f, float f1, CallbackInfo ci) {
        render_customLocals_atlas.pop();
    }

    @Unique
    private final Stack<Atlas> renderItemOnGui_customLocals_atlas = new Stack<>();

    @Inject(
            method = "renderItemOnGui(Lnet/minecraft/client/render/TextRenderer;Lnet/minecraft/client/texture/TextureManager;IIIII)V",
            at = @At("HEAD")
    )
    private void renderItemOnGui_initCustomLocals(TextRenderer textRenderer, TextureManager textureManagerArg, int itemId, int damage, int textureIndex, int textureX, int textureY, CallbackInfo ci) {
        Object item = itemId < BlockBase.BY_ID.length ? BlockBase.BY_ID[itemId] : ItemBase.byId[itemId];
        renderItemOnGui_customLocals_atlas.push(item instanceof CustomAtlasProvider ? ((CustomAtlasProvider) item).getAtlas().of(textureIndex) : null);
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
            Atlas.Texture texture = atlas.getTexture(textureIndex);
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
