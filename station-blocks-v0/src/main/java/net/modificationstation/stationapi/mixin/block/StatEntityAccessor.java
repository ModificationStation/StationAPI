package net.modificationstation.stationapi.mixin.block;

import net.minecraft.stat.ItemOrBlockStat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemOrBlockStat.class)
public interface StatEntityAccessor {

    @Mutable
    @Accessor
    void setValue(int value);
}
