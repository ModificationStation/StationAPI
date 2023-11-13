package net.modificationstation.stationapi.mixin.nbt;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(NbtCompound.class)
public interface NbtCompoundAccessor {
    @Accessor("entries")
    Map<String, NbtElement> stationapi$getEntries();
}
