package net.modificationstation.stationapi.mixin.celestial;

import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(NbtCompound.class)
public interface NbtCompoundAccessor {

    @Accessor
    Map getEntries();

    @Accessor
    void setEntries(Map value);
}
