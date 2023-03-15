package net.modificationstation.stationapi.api.client.texture.atlas;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.modificationstation.stationapi.api.registry.Identifier;

import java.util.List;

public class AtlasSourceManager {
    private static final BiMap<Identifier, AtlasSourceType> SOURCE_TYPE_BY_ID = HashBiMap.create();
    public static final AtlasSourceType SINGLE = AtlasSourceManager.register("single", SingleAtlasSource.CODEC);
    public static final AtlasSourceType DIRECTORY = AtlasSourceManager.register("directory", DirectoryAtlasSource.CODEC);
    public static final AtlasSourceType FILTER = AtlasSourceManager.register("filter", FilterAtlasSource.CODEC);
    public static final AtlasSourceType UNSTITCH = AtlasSourceManager.register("unstitch", UnstitchAtlasSource.CODEC);
    public static final AtlasSourceType PALETTED_PERMUTATIONS = AtlasSourceManager.register("paletted_permutations", PalettedPermutationsAtlasSource.CODEC);
    public static Codec<AtlasSourceType> CODEC = Identifier.CODEC.flatXmap(id -> {
        AtlasSourceType atlasSourceType = SOURCE_TYPE_BY_ID.get(id);
        return atlasSourceType != null ? DataResult.success(atlasSourceType) : DataResult.error(() -> "Unknown type " + id);
    }, type -> {
        Identifier identifier = SOURCE_TYPE_BY_ID.inverse().get(type);
        return type != null ? DataResult.success(identifier) : DataResult.error(() -> "Unknown type " + identifier);
    });
    public static Codec<AtlasSource> TYPE_CODEC = CODEC.dispatch(AtlasSource::getType, AtlasSourceType::codec);
    public static Codec<List<AtlasSource>> LIST_CODEC = TYPE_CODEC.listOf().fieldOf("sources").codec();

    private static AtlasSourceType register(String id, Codec<? extends AtlasSource> codec) {
        Identifier identifier = Identifier.of(id);
        AtlasSourceType atlasSourceType = new AtlasSourceType(codec);
        AtlasSourceType atlasSourceType2 = SOURCE_TYPE_BY_ID.putIfAbsent(identifier, atlasSourceType);
        if (atlasSourceType2 != null) {
            throw new IllegalStateException("Duplicate registration " + identifier);
        }
        return atlasSourceType;
    }
}

