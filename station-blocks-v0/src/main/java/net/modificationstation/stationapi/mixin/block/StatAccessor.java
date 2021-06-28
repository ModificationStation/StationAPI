package net.modificationstation.stationapi.mixin.block;

import net.minecraft.stat.Stat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Stat.class)
public interface StatAccessor {

    @Accessor
    void setID(int ID);
}
