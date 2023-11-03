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
public class MixinSheep {

    @Redirect(method = "interact(Lnet/minecraft/entity/player/PlayerBase;)Z", at = @At(value = "FIELD", target = "Lnet/minecraft/item/ItemInstance;itemId:I", opcode = Opcodes.GETFIELD))
    private int hijackSheepShearing(ItemStack itemInstance) {
        return StationAPI.EVENT_BUS.post(
                ShearsOverrideEvent.builder()
                        .itemStack(itemInstance)
                        .overrideShears(false)
                        .build()
        ).overrideShears ? Item.SHEARS.id : itemInstance.itemId;
    }
}
