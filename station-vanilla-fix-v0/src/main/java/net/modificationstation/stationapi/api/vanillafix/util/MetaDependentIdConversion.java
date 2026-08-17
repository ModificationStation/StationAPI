package net.modificationstation.stationapi.api.vanillafix.util;

import com.mojang.serialization.Dynamic;
import lombok.Getter;
import net.minecraft.nbt.NbtCompound;
import net.modificationstation.stationapi.api.nbt.NbtOps;

/**
 * Provides advanced conversion rules which depend on metadata
 * <p>
 * Contains a default tag for unspecified metadata rules
 */
public class MetaDependentIdConversion {
    @Getter
    private final Dynamic<?> defaultTag;
    private final Dynamic<?>[] metaDependentTags = new Dynamic[16];

    /**
     * @param defaultTag Tag to be used for unspecified metadata rules
     */
    public MetaDependentIdConversion(NbtCompound defaultTag) {
        this.defaultTag = toDynamic(defaultTag);
    }

    /**
     * Adds a meta rule
     * @param meta Metadata of the rule
     * @param metaDependentTag Rule to be added
     */
    public void addMetaDependentTag(int meta, NbtCompound metaDependentTag) {
        if (meta < metaDependentTags.length) {
            metaDependentTags[meta] = toDynamic(metaDependentTag);
        }
    }

    /**
     * Adds the same meta rule across an entire interval
     * @param interval Interval to determine metadata values of the rule
     * @param intervalTag Rule to be added
     */
    public void addMetaDependentTagInterval(MetaIntervalToTag interval, NbtCompound intervalTag) {
        for (int meta = interval.start(); meta < interval.end(); meta++) {
            addMetaDependentTag(meta, intervalTag);
        }
    }

    /**
     * Provides a conversion rule for a given metadata value
     * @param meta Metadata to check the rule for
     * @return Specific rule or default if unspecified
     */
    public Dynamic<?> getTagForMeta(int meta) {
        Dynamic<?> tag = metaDependentTags[meta];
        if (tag == null) {
            return defaultTag;
        }
        return tag;
    }

    private Dynamic<?> toDynamic(NbtCompound tag) {
        return new Dynamic<>(NbtOps.INSTANCE, tag);
    }
}
