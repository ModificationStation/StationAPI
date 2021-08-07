package net.modificationstation.stationapi.mixin.render.client;

import com.sun.org.apache.xml.internal.utils.IntStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockBase;
import net.minecraft.class_556;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.entity.Living;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.CustomAtlasProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@Mixin(class_556.class)
@Environment(EnvType.CLIENT)
public class Mixinclass_556 {

//    @Shadow
//    private Minecraft field_2401;
//
//    @Redirect(method = "method_1862(Lnet/minecraft/entity/Living;Lnet/minecraft/item/ItemInstance;)V", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glBindTexture(II)V", ordinal = 0, remap = false))
//    private void bindBlockTexture1(int target, int texture) {
//        TextureRegistryOld.getRegistry(TextureRegistryOld.Vanilla.TERRAIN).bindAtlas(field_2401.textureManager, 0);
//    }
//
//    @Redirect(method = "method_1862(Lnet/minecraft/entity/Living;Lnet/minecraft/item/ItemInstance;)V", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glBindTexture(II)V", ordinal = 1, remap = false))
//    private void bindBlockTexture2(int target, int texture) {
//        TextureRegistryOld.getRegistry(TextureRegistryOld.Vanilla.TERRAIN).bindAtlas(field_2401.textureManager, 0);
//    }
//
//    @Redirect(method = "method_1862(Lnet/minecraft/entity/Living;Lnet/minecraft/item/ItemInstance;)V", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glBindTexture(II)V", ordinal = 2, remap = false))
//    private void bindItemTexture(int target, int texture) {
//        TextureRegistryOld.getRegistry(TextureRegistryOld.Vanilla.GUI_ITEMS).bindAtlas(field_2401.textureManager, 0);
//    }
//
//    @Redirect(method = "method_1862(Lnet/minecraft/entity/Living;Lnet/minecraft/item/ItemInstance;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Living;method_917(Lnet/minecraft/item/ItemInstance;)I"))
//    private int rebindItemTexture(Living living, ItemInstance arg) {
//        int texID = living.method_917(arg);
//        TextureRegistryOld.currentRegistry().bindAtlas(field_2401.textureManager, texID / TextureRegistryOld.currentRegistry().texturesPerFile());
//        return texID % TextureRegistryOld.currentRegistry().texturesPerFile();
//    }

    @Unique
    private final Stack<Atlas> method_1862_customLocals_atlas = new Stack<>();

    @Inject(
            method = "method_1862(Lnet/minecraft/entity/Living;Lnet/minecraft/item/ItemInstance;)V",
            at = @At("HEAD")
    )
    private void method_1862_initCustomLocals(Living arg, ItemInstance arg1, CallbackInfo ci) {
        int itemId = arg1.itemId;
        Object item = itemId < BlockBase.BY_ID.length ? BlockBase.BY_ID[itemId] : ItemBase.byId[itemId];
        method_1862_customLocals_atlas.push(item instanceof CustomAtlasProvider ? ((CustomAtlasProvider) item).getAtlas() : null);
    }

    @Redirect(
            method = "method_1862(Lnet/minecraft/entity/Living;Lnet/minecraft/item/ItemInstance;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/texture/TextureManager;getTextureId(Ljava/lang/String;)I",
                    ordinal = 2
            )
    )
    private int redirectItemAtlasID(TextureManager textureManager, String string, Living arg, ItemInstance arg1) {
        Atlas atlas = method_1862_customLocals_atlas.peek();
        return atlas == null ? textureManager.getTextureId(string) : atlas.getAtlasTextureID(arg.method_917(arg1));
    }

    private final IntStack method_1862_capturedLocals_texturePosition = new IntStack();

    @ModifyVariable(
            method = "method_1862(Lnet/minecraft/entity/Living;Lnet/minecraft/item/ItemInstance;)V",
            index = 4,
            at = @At("STORE")
    )
    private int captureTexturePosition(int texturePosition) {
        method_1862_capturedLocals_texturePosition.push(texturePosition);
        return texturePosition;
    }

    @Unique
    private final Stack<Atlas.Texture> method_1862_customLocals_texture = new Stack<>();

    @ModifyVariable(
            method = "method_1862(Lnet/minecraft/entity/Living;Lnet/minecraft/item/ItemInstance;)V",
            index = 5,
            at = @At("STORE")
    )
    private float modifyStartU(float originalStartU) {
        Atlas atlas = method_1862_customLocals_atlas.peek();
        Atlas.Texture texture = atlas == null ? null : atlas.getTexture(method_1862_capturedLocals_texturePosition.peek());
        method_1862_customLocals_texture.push(texture);
        return texture == null ? originalStartU : texture.getStartU();
    }

    @ModifyVariable(
            method = "method_1862(Lnet/minecraft/entity/Living;Lnet/minecraft/item/ItemInstance;)V",
            index = 6,
            at = @At("STORE")
    )
    private float modifyEndU(float originalEndU) {
        Atlas.Texture texture = method_1862_customLocals_texture.peek();
        return texture == null ? originalEndU : texture.getEndU();
    }

    @ModifyVariable(
            method = "method_1862(Lnet/minecraft/entity/Living;Lnet/minecraft/item/ItemInstance;)V",
            index = 7,
            at = @At("STORE")
    )
    private float modifyStartV(float originalStartV) {
        Atlas.Texture texture = method_1862_customLocals_texture.peek();
        return texture == null ? originalStartV : texture.getStartV();
    }

    @ModifyVariable(
            method = "method_1862(Lnet/minecraft/entity/Living;Lnet/minecraft/item/ItemInstance;)V",
            index = 8,
            at = @At("STORE")
    )
    private float modifyEndV(float originalEndV) {
        Atlas.Texture texture = method_1862_customLocals_texture.peek();
        return texture == null ? originalEndV : texture.getEndV();
    }

    @ModifyConstant(
            method = "method_1862(Lnet/minecraft/entity/Living;Lnet/minecraft/item/ItemInstance;)V",
            constant = @Constant(
                    ordinal = 8,
                    intValue = 16
            )
    )
    private int modifyAmountOfNorthVertexes(int constant) {
        Atlas.Texture texture = method_1862_customLocals_texture.peek();
        return texture == null ? constant : texture.getHeight();
    }

    @ModifyConstant(
            method = "method_1862(Lnet/minecraft/entity/Living;Lnet/minecraft/item/ItemInstance;)V",
            constant = @Constant(
                    ordinal = 0,
                    floatValue = 16
            )
    )
    private float modifyNorthVertexesDenominator(float constant) {
        Atlas.Texture texture = method_1862_customLocals_texture.peek();
        return texture == null ? constant : texture.getHeight();
    }

    @ModifyConstant(
            method = "method_1862(Lnet/minecraft/entity/Living;Lnet/minecraft/item/ItemInstance;)V",
            constant = @Constant(
                    ordinal = 0,
                    floatValue = 0.001953125F
            )
    )
    private float modifyNorthVertexesNudge(float constant) {
        Atlas.Texture texture = method_1862_customLocals_texture.peek();
        return texture == null ? constant : 1F / (texture.getHeight() * texture.getHeight() * 2);
    }

    @ModifyConstant(
            method = "method_1862(Lnet/minecraft/entity/Living;Lnet/minecraft/item/ItemInstance;)V",
            constant = @Constant(
                    ordinal = 9,
                    intValue = 16
            )
    )
    private int modifyAmountOfSouthVertexes(int constant) {
        Atlas.Texture texture = method_1862_customLocals_texture.peek();
        return texture == null ? constant : texture.getHeight();
    }

    @ModifyConstant(
            method = "method_1862(Lnet/minecraft/entity/Living;Lnet/minecraft/item/ItemInstance;)V",
            constant = @Constant(
                    ordinal = 1,
                    floatValue = 16
            )
    )
    private float modifySouthVertexesDenominator(float constant) {
        Atlas.Texture texture = method_1862_customLocals_texture.peek();
        return texture == null ? constant : texture.getHeight();
    }

    @ModifyConstant(
            method = "method_1862(Lnet/minecraft/entity/Living;Lnet/minecraft/item/ItemInstance;)V",
            constant = @Constant(
                    ordinal = 1,
                    floatValue = 0.001953125F
            )
    )
    private float modifySouthVertexesNudge(float constant) {
        Atlas.Texture texture = method_1862_customLocals_texture.peek();
        return texture == null ? constant : 1F / (texture.getHeight() * texture.getHeight() * 2);
    }

    @ModifyConstant(
            method = "method_1862(Lnet/minecraft/entity/Living;Lnet/minecraft/item/ItemInstance;)V",
            constant = @Constant(
                    ordinal = 1,
                    floatValue = 0.0625F
            )
    )
    private float modifySouthVertexesReciprocal(float constant) {
        Atlas.Texture texture = method_1862_customLocals_texture.peek();
        return texture == null ? constant : 1F / texture.getHeight();
    }

    @ModifyConstant(
            method = "method_1862(Lnet/minecraft/entity/Living;Lnet/minecraft/item/ItemInstance;)V",
            constant = @Constant(
                    ordinal = 10,
                    intValue = 16
            )
    )
    private int modifyAmountOfTopVertexes(int constant) {
        Atlas.Texture texture = method_1862_customLocals_texture.peek();
        return texture == null ? constant : texture.getWidth();
    }

    @ModifyConstant(
            method = "method_1862(Lnet/minecraft/entity/Living;Lnet/minecraft/item/ItemInstance;)V",
            constant = @Constant(
                    ordinal = 2,
                    floatValue = 16
            )
    )
    private float modifyTopVertexesDenominator(float constant) {
        Atlas.Texture texture = method_1862_customLocals_texture.peek();
        return texture == null ? constant : texture.getWidth();
    }

    @ModifyConstant(
            method = "method_1862(Lnet/minecraft/entity/Living;Lnet/minecraft/item/ItemInstance;)V",
            constant = @Constant(
                    ordinal = 2,
                    floatValue = 0.001953125F
            )
    )
    private float modifyTopVertexesNudge(float constant) {
        Atlas.Texture texture = method_1862_customLocals_texture.peek();
        return texture == null ? constant : 1F / (texture.getWidth() * texture.getWidth() * 2);
    }

    @ModifyConstant(
            method = "method_1862(Lnet/minecraft/entity/Living;Lnet/minecraft/item/ItemInstance;)V",
            constant = @Constant(
                    ordinal = 2,
                    floatValue = 0.0625F
            )
    )
    private float modifyTopVertexesReciprocal(float constant) {
        Atlas.Texture texture = method_1862_customLocals_texture.peek();
        return texture == null ? constant : 1F / texture.getWidth();
    }

    @ModifyConstant(
            method = "method_1862(Lnet/minecraft/entity/Living;Lnet/minecraft/item/ItemInstance;)V",
            constant = @Constant(
                    ordinal = 11,
                    intValue = 16
            )
    )
    private int modifyAmountOfBottomVertexes(int constant) {
        Atlas.Texture texture = method_1862_customLocals_texture.peek();
        return texture == null ? constant : texture.getWidth();
    }

    @ModifyConstant(
            method = "method_1862(Lnet/minecraft/entity/Living;Lnet/minecraft/item/ItemInstance;)V",
            constant = @Constant(
                    ordinal = 3,
                    floatValue = 16
            )
    )
    private float modifyBottomVertexesDenominator(float constant) {
        Atlas.Texture texture = method_1862_customLocals_texture.peek();
        return texture == null ? constant : texture.getWidth();
    }

    @ModifyConstant(
            method = "method_1862(Lnet/minecraft/entity/Living;Lnet/minecraft/item/ItemInstance;)V",
            constant = @Constant(
                    ordinal = 3,
                    floatValue = 0.001953125F
            )
    )
    private float modifyBottomVertexesNudge(float constant) {
        Atlas.Texture texture = method_1862_customLocals_texture.peek();
        return texture == null ? constant : 1F / (texture.getWidth() * texture.getWidth() * 2);
    }

    @Inject(
            method = "method_1862(Lnet/minecraft/entity/Living;Lnet/minecraft/item/ItemInstance;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/Tessellator;draw()V",
                    shift = At.Shift.BEFORE,
                    ordinal = 5
            )
    )
    private void popTexture(Living arg, ItemInstance arg1, CallbackInfo ci) {
        method_1862_customLocals_texture.pop();
    }

    @Inject(
            method = "method_1862(Lnet/minecraft/entity/Living;Lnet/minecraft/item/ItemInstance;)V",
            at = @At("RETURN")
    )
    private void method_1862_clearCustomLocals(Living arg, ItemInstance arg1, CallbackInfo ci) {
        method_1862_customLocals_atlas.pop();
    }
}
