package net.modificationstation.stationapi.mixin.block;

import net.minecraft.util.io.AbstractTag;
import net.minecraft.util.io.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.*;

@Mixin(CompoundTag.class)
public interface CompoundTagAccessor {

    @Accessor("data")
    Map<String, ? extends AbstractTag> stationapi$getData();
}
