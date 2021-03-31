package net.modificationstation.stationapi.mixin.common.accessor;

import net.minecraft.block.BlockBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BlockBase.class)
public interface BlockBaseAccessor {

    @Accessor
    void setId(int id);
}
