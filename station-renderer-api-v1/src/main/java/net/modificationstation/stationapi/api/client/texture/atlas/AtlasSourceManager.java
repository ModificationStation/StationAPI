package net.modificationstation.stationapi.api.client.texture.atlas;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.dynamic.Codecs;

import java.util.List;

public class AtlasSourceManager {
    private static final Codecs.IdMapper<Identifier, MapCodec<? extends AtlasSource>> ID_MAPPER = new Codecs.IdMapper<>();

    public static final Codec<AtlasSource> TYPE_CODEC = ID_MAPPER.getCodec(Identifier.CODEC).dispatch(AtlasSource::getCodec, codec -> codec);
    public static Codec<List<AtlasSource>> LIST_CODEC = TYPE_CODEC.listOf().fieldOf("sources").codec();

    private static <T extends AtlasSource> MapCodec<T> register(String id, MapCodec<T> codec) {
        Identifier identifier = Identifier.of(id);
        MapCodec<?> last = ID_MAPPER.putIfAbsent(identifier, codec);
        if (last != null) {
            throw new IllegalStateException("Duplicate registration " + identifier);
        }
        return codec;
    }

    static {
        AtlasSourceManager.register("single", SingleAtlasSource.CODEC);
        AtlasSourceManager.register("directory", DirectoryAtlasSource.CODEC);
        AtlasSourceManager.register("filter", FilterAtlasSource.CODEC);
        AtlasSourceManager.register("unstitch", UnstitchAtlasSource.CODEC);
        AtlasSourceManager.register("paletted_permutations", PalettedPermutationsAtlasSource.CODEC);
    }
}

