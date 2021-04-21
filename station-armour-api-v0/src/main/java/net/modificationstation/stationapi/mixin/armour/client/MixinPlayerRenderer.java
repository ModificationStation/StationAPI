package net.modificationstation.stationapi.mixin.armour.client;

import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerRenderer;
import net.minecraft.client.render.entity.model.EntityModelBase;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.item.armour.Armour;
import net.modificationstation.stationapi.api.client.item.ArmorTextureProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(PlayerRenderer.class)
public class MixinPlayerRenderer extends LivingEntityRenderer {

    public MixinPlayerRenderer(EntityModelBase arg, float f) {
        super(arg, f);
    }

    @Inject(method = "render(Lnet/minecraft/entity/player/PlayerBase;IF)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/PlayerRenderer;bindTexture(Ljava/lang/String;)V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    private void onArmorTexture(PlayerBase arg, int i, float f, CallbackInfoReturnable<Boolean> cir, ItemInstance var4, ItemBase var5, Armour var6) {
        if (var6 instanceof ArmorTextureProvider) {
            ArmorTextureProvider var7 = (ArmorTextureProvider) var6;
            bindTexture(i == 2 ? var7.getChestplateModelTexture(var4) : var7.getOtherModelTexture(var4));
        }
    }
}
