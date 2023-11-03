package net.modificationstation.stationapi.mixin.arsenic.client;

import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.client.render.RendererAccess;
import net.modificationstation.stationapi.api.client.render.model.VanillaBakedModel;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(PlayerEntityRenderer.class)
public class MixinPlayerRenderer {

    @Inject(
            method = "method_342(Lnet/minecraft/entity/player/PlayerBase;F)V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/item/ItemInstance;itemId:I",
                    ordinal = 1
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void pushIfJson(PlayerEntity f, float par2, CallbackInfo ci, ItemStack var3, ItemStack var4) {
        if (RendererAccess.INSTANCE.hasRenderer() && !(RendererAccess.INSTANCE.getRenderer().bakedModelRenderer().getItemModels().getModel(var4) instanceof VanillaBakedModel))
            GL11.glPushMatrix();
    }

    @Inject(
            method = "method_342(Lnet/minecraft/entity/player/PlayerBase;F)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/class_556;method_1862(Lnet/minecraft/entity/Living;Lnet/minecraft/item/ItemInstance;)V",
                    ordinal = 1
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void popIfJson(PlayerEntity f, float par2, CallbackInfo ci, ItemStack var3, ItemStack var4) {
        if (RendererAccess.INSTANCE.hasRenderer() && !(RendererAccess.INSTANCE.getRenderer().bakedModelRenderer().getItemModels().getModel(var4) instanceof VanillaBakedModel))
            GL11.glPopMatrix();
    }
}
