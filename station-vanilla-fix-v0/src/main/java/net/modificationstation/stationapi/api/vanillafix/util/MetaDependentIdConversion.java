package net.modificationstation.stationapi.api.vanillafix.util;

import com.mojang.serialization.Dynamic;
import lombok.Getter;
import net.minecraft.nbt.NbtCompound;
import net.modificationstation.stationapi.api.nbt.NbtOps;

import java.util.Arrays;

/**
 * Provides advanced conversion rules which depend on metadata
 * <p>
 * Contains a default tag for unspecified metadata rules and arrays which are null by default to save memory
 */
public class MetaDependentIdConversion {
    private static final int META_COUNT = 16;
    public static final int UNSPECIFIED_META = -1;

    @Getter
    private final Dynamic<?> defaultTag;

    private Dynamic<?>[] metaDependentTags = null;
    private int[] outputMetas = null;

    /**
     * @param defaultTag Tag to be used for unspecified metadata rules
     */
    public MetaDependentIdConversion(NbtCompound defaultTag) {
        this.defaultTag = toDynamic(defaultTag);
    }

    /**
     * Adds a meta rule and initializes arrays if necessary
     * @param meta Metadata of the rule
     * @param metaDependentTag Rule to be added
     * @param outputMeta New metadata of the converted block,
     *                   or {@link #UNSPECIFIED_META} to keep the original metadata
     * @throws IllegalArgumentException if outputMeta is neither {@link #UNSPECIFIED_META}
     *                                  nor a valid metadata value
     */
    public void addMetaDependentTag(int meta, NbtCompound metaDependentTag, int outputMeta) {
        if (outputMeta != UNSPECIFIED_META && (outputMeta < 0 || outputMeta >= META_COUNT)) {
            throw new IllegalArgumentException(
                    "Output metadata " + outputMeta + " is out of range, it must be between 0 and " +
                    (META_COUNT - 1) + ", or " + UNSPECIFIED_META + " to keep the original metadata"
            );
        }
        if (metaDependentTags == null || outputMetas == null) {
            metaDependentTags = new Dynamic[META_COUNT];
            outputMetas = new int[META_COUNT];
            Arrays.fill(outputMetas, UNSPECIFIED_META);
        }
        if (meta < metaDependentTags.length) {
            metaDependentTags[meta] = toDynamic(metaDependentTag);
            outputMetas[meta] = outputMeta;
        }
    }

    /**
     * Provides a conversion rule for a given metadata value
     * @param meta Metadata to check the rule for
     * @return Specific rule or default if unspecified
     */
    public Dynamic<?> getTagForMeta(int meta) {
        if (metaDependentTags == null) {
            return defaultTag;
        }
        Dynamic<?> tag = metaDependentTags[meta];
        if (tag == null) {
            return defaultTag;
        }
        return tag;
    }

    /**
     * Replaces an old metadata value with a new one
     * @param meta Old metadata to be replaced
     * @return New metadata or -1 if not specified
     */
    public int getOutputMeta(int meta) {
        int outputMeta = UNSPECIFIED_META;
        if (outputMetas == null) {
            return UNSPECIFIED_META;
        }
        if (meta < outputMetas.length) {
            outputMeta = outputMetas[meta];
        }
        return outputMeta;
    }

    private Dynamic<?> toDynamic(NbtCompound tag) {
        return new Dynamic<>(NbtOps.INSTANCE, tag);
    }
}
