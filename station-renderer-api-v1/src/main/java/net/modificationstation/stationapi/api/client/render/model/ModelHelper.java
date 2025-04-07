package net.modificationstation.stationapi.api.client.render.model;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.ImmutableList;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.render.mesh.Mesh;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;

/**
 * Collection of utilities for model implementations.
 */
public final class ModelHelper {
    /** @see #faceFromIndex(int) */
    private static final Direction[] FACES = Arrays.copyOf(Direction.values(), 7);

    /** Result from {@link #toFaceIndex(Direction)} for null values. */
    public static final int NULL_FACE_ID = 6;

    private ModelHelper() { }

    /**
     * Convenient way to encode faces that may be null.
     * Null is returned as {@link #NULL_FACE_ID}.
     * Use {@link #faceFromIndex(int)} to retrieve encoded face.
     */
    public static int toFaceIndex(@Nullable Direction face) {
        return face == null ? NULL_FACE_ID : face.getId();
    }

    /**
     * Use to decode a result from {@link #toFaceIndex(Direction)}.
     * Return value will be null if encoded value was null.
     * Can also be used for no-allocation iteration of {@link Direction#values()},
     * optionally including the null face. (Use &lt; or  &lt;= {@link #NULL_FACE_ID}
     * to exclude or include the null value, respectively.)
     */
    @Nullable
    public static Direction faceFromIndex(int faceIndex) {
        return FACES[faceIndex];
    }

    /**
     * Converts a mesh into an array of lists of vanilla baked quads.
     * Useful for creating vanilla baked models when required for compatibility.
     * The array indexes correspond to {@link Direction#getId()} with the
     * addition of {@link #NULL_FACE_ID}.
     *
     * <p>Retrieves sprites from the block texture atlas via {@link SpriteFinder}.
     */
    public static List<BakedQuad>[] toQuadLists(Mesh mesh) {
        SpriteFinder finder = SpriteFinder.get(StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE));

        @SuppressWarnings("unchecked")
        final ImmutableList.Builder<BakedQuad>[] builders = new ImmutableList.Builder[7];

        for (int i = 0; i < 7; i++) {
            builders[i] = ImmutableList.builder();
        }

        if (mesh != null) {
            mesh.forEach(q -> {
                Direction cullFace = q.cullFace();
                builders[cullFace == null ? NULL_FACE_ID : cullFace.getId()].add(q.toBakedQuad(finder.find(q)));
            });
        }

        @SuppressWarnings("unchecked")
        List<BakedQuad>[] result = new List[7];

        for (int i = 0; i < 7; i++) {
            result[i] = builders[i].build();
        }

        return result;
    }
}
