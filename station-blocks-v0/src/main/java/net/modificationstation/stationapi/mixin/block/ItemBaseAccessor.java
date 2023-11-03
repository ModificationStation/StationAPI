package net.modificationstation.stationapi.mixin.block;

import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Item.class)
public interface ItemBaseAccessor {

    @Mutable
    @Accessor
    void setId(int id);
}
