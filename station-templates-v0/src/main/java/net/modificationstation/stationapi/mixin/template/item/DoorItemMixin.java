package net.modificationstation.stationapi.mixin.template.item;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.block.Block;
import net.minecraft.item.DoorItem;
import net.modificationstation.stationapi.api.template.item.TemplateDoorItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DoorItem.class)
class DoorItemMixin {
    @ModifyExpressionValue(
            method = "useOnBlock",
            at = {
                    @At(
                            value = "FIELD",
                            target = "Lnet/minecraft/block/Block;DOOR:Lnet/minecraft/block/Block;"
                    ),
                    @At(
                            value = "FIELD",
                            target = "Lnet/minecraft/block/Block;IRON_DOOR:Lnet/minecraft/block/Block;"
                    )
            }
    )
    private Block stationapi_hijackBlock(Block original) {
        return (DoorItem) (Object) this instanceof TemplateDoorItem doorItem ? doorItem.getDoorBlock() : original;
    }
}
