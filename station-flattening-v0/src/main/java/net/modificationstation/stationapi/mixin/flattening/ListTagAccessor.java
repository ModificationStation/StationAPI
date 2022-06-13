package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.util.io.AbstractTag;
import net.minecraft.util.io.ListTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(ListTag.class)
public interface ListTagAccessor {

    @Accessor("data")
    void stationapi$setData(List<? extends AbstractTag> data);

    @Accessor("listTypeId")
    byte stationapi$getListTypeId();

    @Accessor("listTypeId")
    void stationapi$setListTypeId(byte listTypeId);
}
