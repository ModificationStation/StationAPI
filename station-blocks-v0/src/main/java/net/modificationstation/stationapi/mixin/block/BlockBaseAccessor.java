package net.modificationstation.stationapi.mixin.block;

import net.minecraft.block.BlockBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BlockBase.class)
public interface BlockBaseAccessor {

    @Accessor @Mutable
    void setId(int id);
}
