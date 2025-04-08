package net.modificationstation.stationapi.api.client.render.mesh;

import net.modificationstation.stationapi.api.client.render.Renderer;
import net.modificationstation.stationapi.api.client.render.material.MaterialFinder;
import net.modificationstation.stationapi.api.client.render.material.RenderMaterial;
import net.modificationstation.stationapi.api.client.render.model.BakedQuad;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public interface MutableQuadView extends QuadView {
    /**
     * Causes texture to appear with no rotation.
     * Pass in bakeFlags parameter to {@link #spriteBake(Sprite, int)}.
     */
    int BAKE_ROTATE_NONE = 0;

    /**
     * Causes texture to appear rotated 90 deg. clockwise relative to nominal face.
     * Pass in bakeFlags parameter to {@link #spriteBake(Sprite, int)}.
     */
    int BAKE_ROTATE_90 = 1;

    /**
     * Causes texture to appear rotated 180 deg. relative to nominal face.
     * Pass in bakeFlags parameter to {@link #spriteBake(Sprite, int)}.
     */
    int BAKE_ROTATE_180 = 2;

    /**
     * Causes texture to appear rotated 270 deg. clockwise relative to nominal face.
     * Pass in bakeFlags parameter to {@link #spriteBake(Sprite, int)}.
     */
    int BAKE_ROTATE_270 = 3;

    /**
     * When enabled, texture coordinate are assigned based on vertex position.
     * Any existing UV coordinates will be replaced.
     * Pass in bakeFlags parameter to {@link #spriteBake(Sprite, int)}.
     *
     * <p>UV lock always derives texture coordinates based on nominal face, even
     * when the quad is not co-planar with that face, and the result is
     * the same as if the quad were projected onto the nominal face, which
     * is usually the desired result.
     */
    int BAKE_LOCK_UV = 4;

    /**
     * When set, U texture coordinates for the given sprite are
     * flipped as part of baking. Can be useful for some randomization
     * and texture mapping scenarios. Results are different from what
     * can be obtained via rotation and both can be applied.
     * Pass in bakeFlags parameter to {@link #spriteBake(Sprite, int)}.
     */
    int BAKE_FLIP_U = 8;

    /**
     * Same as {@link #BAKE_FLIP_U} but for V coordinate.
     */
    int BAKE_FLIP_V = 16;

    /**
     * UV coordinates by default are assumed to be 0-16 scale for consistency
     * with conventional Minecraft model format. This is scaled to 0-1 during
     * baking before interpolation. Model loaders that already have 0-1 coordinates
     * can avoid wasteful multiplication/division by passing 0-1 coordinates directly.
     * Pass in bakeFlags parameter to {@link #spriteBake(Sprite, int)}.
     */
    int BAKE_NORMALIZED = 32;

    /**
     * Sets the geometric vertex position for the given vertex,
     * relative to block origin, (0,0,0). Minecraft rendering is designed
     * for models that fit within a single block space and is recommended
     * that coordinates remain in the 0-1 range, with multi-block meshes
     * split into multiple per-block models.
     */
    MutableQuadView pos(int vertexIndex, float x, float y, float z);

    /**
     * Same as {@link #pos(int, float, float, float)} but accepts vector type.
     */
    default MutableQuadView pos(int vertexIndex, Vector3f pos) {
        return pos(vertexIndex, pos.x, pos.y, pos.z);
    }

    /**
     * Same as {@link #pos(int, float, float, float)} but accepts vector type.
     */
    default MutableQuadView pos(int vertexIndex, Vector3fc pos) {
        return pos(vertexIndex, pos.x(), pos.y(), pos.z());
    }

    /**
     * Set vertex color in ARGB format (0xAARRGGBB).
     */
    MutableQuadView color(int vertexIndex, int color);

    /**
     * Convenience: set vertex color for all vertices at once.
     */
    default MutableQuadView color(int c0, int c1, int c2, int c3) {
        color(0, c0);
        color(1, c1);
        color(2, c2);
        color(3, c3);
        return this;
    }

    /**
     * Set texture coordinates.
     */
    MutableQuadView uv(int vertexIndex, float u, float v);

    /**
     * Set texture coordinates.
     *
     * <p>Only use this function if you already have a {@link Vector2f}.
     * Otherwise, see {@link MutableQuadView#uv(int, float, float)}.
     */
    default MutableQuadView uv(int vertexIndex, Vector2f uv) {
        return uv(vertexIndex, uv.x, uv.y);
    }

    /**
     * Set texture coordinates.
     *
     * <p>Only use this function if you already have a {@link Vector2fc}.
     * Otherwise, see {@link MutableQuadView#uv(int, float, float)}.
     */
    default MutableQuadView uv(int vertexIndex, Vector2fc uv) {
        return uv(vertexIndex, uv.x(), uv.y());
    }

    /**
     * Assigns sprite atlas u,v coordinates to this quad for the given sprite.
     * Can handle UV locking, rotation, interpolation, etc. Control this behavior
     * by passing additive combinations of the BAKE_ flags defined in this interface.
     */
    MutableQuadView spriteBake(Sprite sprite, int bakeFlags);

    /**
     * Adds a vertex normal. Models that have per-vertex
     * normals should include them to get correct lighting when it matters.
     * Computed face normal is used when no vertex normal is provided.
     *
     * <p>{@link Renderer} implementations should honor vertex normals for
     * diffuse lighting - modifying vertex color(s) or packing normals in the vertex
     * buffer as appropriate for the rendering method/vertex format in effect.
     */
    MutableQuadView normal(int vertexIndex, float x, float y, float z);

    /**
     * Same as {@link #normal(int, float, float, float)} but accepts vector type.
     */
    default MutableQuadView normal(int vertexIndex, Vector3f normal) {
        return normal(vertexIndex, normal.x, normal.y, normal.z);
    }

    /**
     * Same as {@link #normal(int, float, float, float)} but accepts vector type.
     */
    default MutableQuadView normal(int vertexIndex, Vector3fc normal) {
        return normal(vertexIndex, normal.x(), normal.y(), normal.z());
    }

    /**
     * If non-null, quad is coplanar with a block face which, if known, simplifies
     * or shortcuts geometric analysis that might otherwise be needed.
     * Set to null if quad is not coplanar or if this is not known.
     * Also controls face culling during block rendering.
     *
     * <p>Null by default.
     *
     * <p>When called with a non-null value, also sets {@link #nominalFace(Direction)}
     * to the same value.
     *
     * <p>This is different from the value reported by {@link BakedQuad#face()}. That value
     * is computed based on face geometry and must be non-null in vanilla quads.
     * That computed value is returned by {@link #lightFace()}.
     */
    MutableQuadView cullFace(@Nullable Direction face);

    /**
     * Provides a hint to renderer about the facing of this quad. Not required,
     * but if provided can shortcut some geometric analysis if the quad is parallel to a block face.
     * Should be the expected value of {@link #lightFace()}. Value will be confirmed
     * and if invalid the correct light face will be calculated.
     *
     * <p>Null by default, and set automatically by {@link #cullFace()}.
     *
     * <p>Models may also find this useful as the face for texture UV locking and rotation semantics.
     *
     * <p>Note: This value is not persisted independently when the quad is encoded.
     * When reading encoded quads, this value will always be the same as {@link #lightFace()}.
     */
    MutableQuadView nominalFace(@Nullable Direction face);

    /**
     * Assigns a different material to this quad. Useful for transformation of
     * existing meshes because lighting and texture blending are controlled by material.
     */
    MutableQuadView material(RenderMaterial material);

    /**
     * Value functions identically to {@link BakedQuad#tintIndex()} and is
     * used by renderer in same way. Default value is -1.
     */
    MutableQuadView tintIndex(int tintIndex);

    /**
     * Encodes an integer tag with this quad that can later be retrieved via
     * {@link QuadView#tag()}. Useful for models that want to perform conditional
     * transformation or filtering on static meshes.
     */
    MutableQuadView tag(int tag);

    /**
     * Copies all quad properties from the given {@link QuadView} to this quad.
     *
     * <p>Calling this method does not emit the quad.
     */
    MutableQuadView copyFrom(QuadView quad);

    /**
     * Enables bulk vertex data transfer using the standard Minecraft vertex formats.
     * Only the {@link BakedQuad#vertexData() quad vertex data} is copied.
     * This method should be performant whenever caller's vertex representation makes it feasible.
     *
     *
     * <p>Calling this method does not emit the quad.
     */
    MutableQuadView fromVanilla(int[] quadData, int startIndex);
}
