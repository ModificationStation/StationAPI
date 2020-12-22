package net.modificationstation.stationloader.mixin.common;

import net.minecraft.entity.animal.Sheep;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationloader.api.common.item.tool.Shear;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Sheep.class)
public class MixinSheep {

    @Redirect(method = "interact(Lnet/minecraft/entity/player/PlayerBase;)Z", at = @At(value = "FIELD", target = "Lnet/minecraft/item/ItemInstance;itemId:I", opcode = Opcodes.GETFIELD))
    private int hijackSheepShearing(ItemInstance itemInstance) {
        return itemInstance.getType() instanceof Shear ? ItemBase.shears.id : itemInstance.itemId;
    }

}
