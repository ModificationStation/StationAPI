package net.modificationstation.stationapi.mixin.tools;

import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.impl.item.ShearsOverrideEvent;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SheepEntity.class)
class SheepEntityMixin {
    @Redirect(
            method = "method_1323",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/item/ItemStack;itemId:I",
                    opcode = Opcodes.GETFIELD
            )
    )
    private int stationapi_hijackSheepShearing(ItemStack stack) {
        return StationAPI.EVENT_BUS.post(
                ShearsOverrideEvent.builder()
                        .itemStack(stack)
                        .overrideShears(false)
                        .build()
        ).overrideShears ? Item.SHEARS.id : stack.itemId;
    }
}
