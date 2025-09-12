package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.block.Block;
import net.minecraft.item.MapItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(MapItem.class)
public class MapItemMixin {
    @ModifyConstant(method = "update", constant = @Constant(intValue = 256))
    private int adjustArray(int constant) {
        return Block.BLOCKS.length;
    }
}
