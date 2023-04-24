package net.modificationstation.stationapi.mixin.flattening.client;

import net.minecraft.block.BlockBase;
import net.minecraft.client.render.entity.PlayerRenderer;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.item.BlockItemForm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerRenderer.class)
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
    private int isBlockItemForm(ItemBase instance) {
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
    private int isBlockItemForm(ItemInstance instance) {
        if (instance.getType() instanceof BlockItemForm blockItemForm) {
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
    private BlockBase getBlockItemForm(BlockBase[] array, int index) {
        return stationapi_blockItemForm.getBlock();
    }
}
