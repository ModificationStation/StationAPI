package net.modificationstation.stationapi.mixin.nbt;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

@Mixin(NbtCompound.class)
public interface CompoundTagAccessor {

    @Accessor("data")
    Map<String, NbtElement> stationapi$getData();
}
