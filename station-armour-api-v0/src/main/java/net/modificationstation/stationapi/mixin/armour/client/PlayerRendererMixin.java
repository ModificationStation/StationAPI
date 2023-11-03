package net.modificationstation.stationapi.mixin.armour.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.modificationstation.stationapi.api.client.item.ArmourTextureProvider;
import net.modificationstation.stationapi.api.registry.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerEntityRenderer.class)
public class PlayerRendererMixin extends LivingEntityRenderer {
    public PlayerRendererMixin(EntityModel arg, float f) {
        super(arg, f);
    }

    // TODO: refactor. this seems a bit off in some places
    @WrapOperation(
            method = "method_344",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/entity/PlayerRenderer;bindTexture(Ljava/lang/String;)V"
            )
    )
    private void stationapi_onArmorTexture(
            PlayerEntityRenderer renderer, String texture,
            Operation<Void> fallback,
            PlayerEntity player, int i, float f,
            @Local(index = 6) ArmorItem armor
    ) {
        fallback.call(renderer, armor instanceof ArmourTextureProvider provider ? stationapi_getTexturePath(provider.getTexture(armor), i) : texture);
    }

    @Unique
    private String stationapi_getTexturePath(Identifier identifier, int armourIndex) {
        return "assets/" + identifier.modID + "/stationapi/textures/armour/" + identifier.id.replace(".", "/") + (armourIndex == 2 ? "_2" : "_1") + ".png";
    }
}
