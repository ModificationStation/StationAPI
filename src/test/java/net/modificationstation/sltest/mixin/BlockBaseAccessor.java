package net.modificationstation.sltest.mixin;

import net.minecraft.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Block.class)
public interface BlockBaseAccessor {

    @Invoker
    Block invokeSetHardness(float hardness);
}
