package net.modificationstation.stationapi.mixin.block;

import net.minecraft.item.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Block.class)
public interface BlockAccessor {

    @Accessor
    void setBlockId(int blockId);
}
