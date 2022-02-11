package net.modificationstation.stationapi.mixin.block;

import net.minecraft.item.Block;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(Block.class)
public class MixinBlock {

    @ModifyConstant(
            method = "<init>(I)V",
            constant = @Constant(intValue = 256)
    )
    private int getBlocksSize(int constant) {
        return BlockRegistry.INSTANCE.getSize();
    }
}
