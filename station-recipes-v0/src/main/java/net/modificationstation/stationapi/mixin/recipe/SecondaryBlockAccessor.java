package net.modificationstation.stationapi.mixin.recipe;

import net.minecraft.item.SecondaryBlockItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SecondaryBlockItem.class)
public interface SecondaryBlockAccessor {

    @Accessor
    int getTileId();
}
