package net.modificationstation.stationapi.mixin.nbt;

import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(NbtList.class)
public interface NbtListAccessor {
    @Accessor("value")
    void stationapi$setValue(List<? extends NbtElement> value);

    @Accessor("type")
    byte stationapi$getType();

    @Accessor("type")
    void stationapi$setType(byte type);

    @Accessor("value")
    List<NbtElement> stationapi$getValue();
}
