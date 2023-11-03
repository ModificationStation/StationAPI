package net.modificationstation.stationapi.mixin.nbt;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;

@Mixin(NbtList.class)
public interface ListTagAccessor {

    @Accessor("data")
    void stationapi$setData(List<? extends NbtElement> data);

    @Accessor("listTypeId")
    byte stationapi$getListTypeId();

    @Accessor("listTypeId")
    void stationapi$setListTypeId(byte listTypeId);

    @Accessor("data")
    List<NbtElement> stationapi$getData();
}
