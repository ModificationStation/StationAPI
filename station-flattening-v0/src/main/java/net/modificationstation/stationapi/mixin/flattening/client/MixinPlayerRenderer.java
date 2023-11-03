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
public class MixinPlayerRenderer {

    @Unique
    private BlockItemForm stationapi_blockItemForm;

    @Redirect(
            method = "method_342(Lnet/minecraft/entity/player/PlayerBase;F)V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/item/ItemBase;id:I"
            )
    )
    private int isBlockItemForm(Item instance) {
        if (instance instanceof BlockItemForm blockItemForm){
            stationapi_blockItemForm = blockItemForm;
            return 255;
        }
        return 256;
    }

    @Redirect(
            method = "method_342",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/item/ItemInstance;itemId:I",
                    ordinal = 1
            )
    )
    private int isBlockItemForm(ItemStack instance) {
        if (instance.getItem() instanceof BlockItemForm blockItemForm) {
            stationapi_blockItemForm = blockItemForm;
            return 255;
        }
        return 256;
    }

    @Redirect(
            method = "method_342",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/block/BlockBase;BY_ID:[Lnet/minecraft/block/BlockBase;",
                    args = "array=get"
            )
    )
    private Block getBlockItemForm(Block[] array, int index) {
        return stationapi_blockItemForm.getBlock();
    }
}
