package net.modificationstation.stationapi.mixin.tags;

import net.minecraft.item.SecondaryBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SecondaryBlock.class)
public interface HahaThisIsMineNow {

    @Accessor
    int getTileId();
}
