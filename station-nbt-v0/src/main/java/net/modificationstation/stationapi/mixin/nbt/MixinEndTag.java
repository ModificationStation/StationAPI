package net.modificationstation.stationapi.mixin.nbt;

import net.minecraft.util.io.EndTag;
import net.modificationstation.stationapi.api.nbt.StationNbtEnd;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EndTag.class)
public class MixinEndTag implements StationNbtEnd {

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj instanceof EndTag;
    }

    @Override
    public EndTag copy() {
        return new EndTag();
    }
}
