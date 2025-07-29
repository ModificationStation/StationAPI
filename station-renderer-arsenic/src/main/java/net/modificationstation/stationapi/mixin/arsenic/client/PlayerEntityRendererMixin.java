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
class PlayerEntityRendererMixin {
    @Inject(
            method = "renderMore(Lnet/minecraft/entity/player/PlayerEntity;F)V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/item/ItemStack;itemId:I",
                    ordinal = 1
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void stationapi_pushIfJson(PlayerEntity f, float par2, CallbackInfo ci, ItemStack var3, ItemStack var4) {
        if (RendererAccess.INSTANCE.hasRenderer() && !(RendererAccess.INSTANCE.getRenderer().bakedModelRenderer().getItemModels().getModel(var4) instanceof VanillaBakedModel))
            GL11.glPushMatrix();
    }

    @Inject(
            method = "renderMore(Lnet/minecraft/entity/player/PlayerEntity;F)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;)V",
                    ordinal = 1
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void stationapi_popIfJson(PlayerEntity f, float par2, CallbackInfo ci, ItemStack var3, ItemStack var4) {
        if (RendererAccess.INSTANCE.hasRenderer() && !(RendererAccess.INSTANCE.getRenderer().bakedModelRenderer().getItemModels().getModel(var4) instanceof VanillaBakedModel))
            GL11.glPopMatrix();
    }
}
