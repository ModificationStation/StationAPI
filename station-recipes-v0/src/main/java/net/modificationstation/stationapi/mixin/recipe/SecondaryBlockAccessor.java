package net.modificationstation.stationapi.mixin.recipe;

import net.minecraft.item.SecondaryBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SecondaryBlock.class)
public interface SecondaryBlockAccessor {

    @Accessor
    int getTileId();
}
