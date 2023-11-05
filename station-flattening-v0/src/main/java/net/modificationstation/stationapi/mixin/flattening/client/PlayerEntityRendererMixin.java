package net.modificationstation.stationapi.mixin.flattening.client;

import net.minecraft.block.Block;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.item.BlockItemForm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerEntityRenderer.class)
class PlayerEntityRendererMixin {
    @Unique
    private BlockItemForm stationapi_blockItemForm;

    @Redirect(
            method = "method_827(Lnet/minecraft/entity/player/PlayerEntity;F)V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/item/Item;id:I"
            )
    )
    private int stationapi_isBlockItemForm(Item instance) {
        if (instance instanceof BlockItemForm blockItemForm){
            stationapi_blockItemForm = blockItemForm;
            return 255;
        }
        return 256;
    }

    @Redirect(
            method = "method_827(Lnet/minecraft/entity/player/PlayerEntity;F)V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/item/ItemStack;itemId:I",
                    ordinal = 1
            )
    )
    private int stationapi_isBlockItemForm(ItemStack instance) {
        if (instance.getItem() instanceof BlockItemForm blockItemForm) {
            stationapi_blockItemForm = blockItemForm;
            return 255;
        }
        return 256;
    }

    @Redirect(
            method = "method_827(Lnet/minecraft/entity/player/PlayerEntity;F)V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/block/Block;BLOCKS:[Lnet/minecraft/block/Block;",
                    args = "array=get"
            )
    )
    private Block stationapi_getBlockItemForm(Block[] array, int index) {
        return stationapi_blockItemForm.getBlock();
    }
}
