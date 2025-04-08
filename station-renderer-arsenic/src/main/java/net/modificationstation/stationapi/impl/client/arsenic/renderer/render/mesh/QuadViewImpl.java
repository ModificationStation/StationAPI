package net.modificationstation.stationapi.impl.client.arsenic.renderer.render.mesh;

import static net.modificationstation.stationapi.impl.client.arsenic.renderer.render.mesh.EncodingFormat.HEADER_BITS;
import static net.modificationstation.stationapi.impl.client.arsenic.renderer.render.mesh.EncodingFormat.HEADER_FACE_NORMAL;
import static net.modificationstation.stationapi.impl.client.arsenic.renderer.render.mesh.EncodingFormat.HEADER_STRIDE;
import static net.modificationstation.stationapi.impl.client.arsenic.renderer.render.mesh.EncodingFormat.HEADER_TAG;
import static net.modificationstation.stationapi.impl.client.arsenic.renderer.render.mesh.EncodingFormat.HEADER_TINT_INDEX;
import static net.modificationstation.stationapi.impl.client.arsenic.renderer.render.mesh.EncodingFormat.QUAD_STRIDE;
import static net.modificationstation.stationapi.impl.client.arsenic.renderer.render.mesh.EncodingFormat.VERTEX_COLOR;
import static net.modificationstation.stationapi.impl.client.arsenic.renderer.render.mesh.EncodingFormat.VERTEX_NORMAL;
import static net.modificationstation.stationapi.impl.client.arsenic.renderer.render.mesh.EncodingFormat.VERTEX_STRIDE;
import static net.modificationstation.stationapi.impl.client.arsenic.renderer.render.mesh.EncodingFormat.VERTEX_U;
import static net.modificationstation.stationapi.impl.client.arsenic.renderer.render.mesh.EncodingFormat.VERTEX_V;
import static net.modificationstation.stationapi.impl.client.arsenic.renderer.render.mesh.EncodingFormat.VERTEX_X;
import static net.modificationstation.stationapi.impl.client.arsenic.renderer.render.mesh.EncodingFormat.VERTEX_Y;
import static net.modificationstation.stationapi.impl.client.arsenic.renderer.render.mesh.EncodingFormat.VERTEX_Z;

import net.modificationstation.stationapi.api.client.render.mesh.QuadView;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.render.helper.ColorHelper;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.render.helper.GeometryHelper;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.render.helper.NormalHelper;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.render.material.RenderMaterialImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3fc;

/**
 * Base class for all quads / quad makers. Handles the ugly bits
 * of maintaining and encoding the quad state.
 */
public class QuadViewImpl implements QuadView {
    @Nullable
    protected Direction nominalFace;
    /** True when face normal, light face, or geometry flags may not match geometry. */
    protected boolean isGeometryInvalid = true;
    protected final Vector3f faceNormal = new Vector3f();

    /** Size and where it comes from will vary in subtypes. But in all cases quad is fully encoded to array. */
    protected int[] data;

    /** Beginning of the quad. Also the header index. */
    protected int baseIndex = 0;

    /**
     * Decodes necessary state from the backing data array.
     * The encoded data must contain valid computed geometry.
     */
    public final void load() {
        isGeometryInvalid = false;
        nominalFace = lightFace();
        NormalHelper.unpackNormal(packedFaceNormal(), faceNormal);
    }

    protected final void computeGeometry() {
        if (isGeometryInvalid) {
            isGeometryInvalid = false;

            NormalHelper.computeFaceNormal(faceNormal, this);
            data[baseIndex + HEADER_FACE_NORMAL] = NormalHelper.packNormal(faceNormal);

            // depends on face normal
            data[baseIndex + HEADER_BITS] = EncodingFormat.lightFace(data[baseIndex + HEADER_BITS], GeometryHelper.lightFace(this));

            // depends on light face
            data[baseIndex + HEADER_BITS] = EncodingFormat.geometryFlags(data[baseIndex + HEADER_BITS], GeometryHelper.computeShapeFlags(this));
        }
    }

    /** gets flags used for lighting - lazily computed via {@link GeometryHelper#computeShapeFlags(QuadView)}. */
    public final int geometryFlags() {
        computeGeometry();
        return EncodingFormat.geometryFlags(data[baseIndex + HEADER_BITS]);
    }

    public final boolean hasShade() {
        return !material().disableDiffuse();
    }

    @Override
    public final float x(int vertexIndex) {
        return Float.intBitsToFloat(data[baseIndex + vertexIndex * VERTEX_STRIDE + VERTEX_X]);
    }

    @Override
    public final float y(int vertexIndex) {
        return Float.intBitsToFloat(data[baseIndex + vertexIndex * VERTEX_STRIDE + VERTEX_Y]);
    }

    @Override
    public final float z(int vertexIndex) {
        return Float.intBitsToFloat(data[baseIndex + vertexIndex * VERTEX_STRIDE + VERTEX_Z]);
    }

    @Override
    public final float posByIndex(int vertexIndex, int coordinateIndex) {
        return Float.intBitsToFloat(data[baseIndex + vertexIndex * VERTEX_STRIDE + VERTEX_X + coordinateIndex]);
    }

    @Override
    public final Vector3f copyPos(int vertexIndex, @Nullable Vector3f target) {
        if (target == null) {
            target = new Vector3f();
        }

        final int index = baseIndex + vertexIndex * VERTEX_STRIDE + VERTEX_X;
        target.set(Float.intBitsToFloat(data[index]), Float.intBitsToFloat(data[index + 1]), Float.intBitsToFloat(data[index + 2]));
        return target;
    }

    @Override
    public final int color(int vertexIndex) {
        return data[baseIndex + vertexIndex * VERTEX_STRIDE + VERTEX_COLOR];
    }

    @Override
    public final float u(int vertexIndex) {
        return Float.intBitsToFloat(data[baseIndex + vertexIndex * VERTEX_STRIDE + VERTEX_U]);
    }

    @Override
    public final float v(int vertexIndex) {
        return Float.intBitsToFloat(data[baseIndex + vertexIndex * VERTEX_STRIDE + VERTEX_V]);
    }

    @Override
    public final Vector2f copyUv(int vertexIndex, @Nullable Vector2f target) {
        if (target == null) {
            target = new Vector2f();
        }

        final int index = baseIndex + vertexIndex * VERTEX_STRIDE + VERTEX_U;
        target.set(Float.intBitsToFloat(data[index]), Float.intBitsToFloat(data[index + 1]));
        return target;
    }

    public final int normalFlags() {
        return EncodingFormat.normalFlags(data[baseIndex + HEADER_BITS]);
    }

    @Override
    public final boolean hasNormal(int vertexIndex) {
        return (normalFlags() & (1 << vertexIndex)) != 0;
    }

    /** True if any vertex normal has been set. */
    public final boolean hasVertexNormals() {
        return normalFlags() != 0;
    }

    /** True if all vertex normals have been set. */
    public final boolean hasAllVertexNormals() {
        return (normalFlags() & 0b1111) == 0b1111;
    }

    protected final int normalIndex(int vertexIndex) {
        return baseIndex + vertexIndex * VERTEX_STRIDE + VERTEX_NORMAL;
    }

    @Override
    public final float normalX(int vertexIndex) {
        return hasNormal(vertexIndex) ? NormalHelper.unpackNormalX(data[normalIndex(vertexIndex)]) : Float.NaN;
    }

    @Override
    public final float normalY(int vertexIndex) {
        return hasNormal(vertexIndex) ? NormalHelper.unpackNormalY(data[normalIndex(vertexIndex)]) : Float.NaN;
    }

    @Override
    public final float normalZ(int vertexIndex) {
        return hasNormal(vertexIndex) ? NormalHelper.unpackNormalZ(data[normalIndex(vertexIndex)]) : Float.NaN;
    }

    @Override
    @Nullable
    public final Vector3f copyNormal(int vertexIndex, @Nullable Vector3f target) {
        if (hasNormal(vertexIndex)) {
            if (target == null) {
                target = new Vector3f();
            }

            final int normal = data[normalIndex(vertexIndex)];
            NormalHelper.unpackNormal(normal, target);
            return target;
        } else {
            return null;
        }
    }

    @Override
    @Nullable
    public final Direction cullFace() {
        return EncodingFormat.cullFace(data[baseIndex + HEADER_BITS]);
    }

    @Override
    @NotNull
    public final Direction lightFace() {
        computeGeometry();
        return EncodingFormat.lightFace(data[baseIndex + HEADER_BITS]);
    }

    @Override
    @Nullable
    public final Direction nominalFace() {
        return nominalFace;
    }

    public final int packedFaceNormal() {
        computeGeometry();
        return data[baseIndex + HEADER_FACE_NORMAL];
    }

    @Override
    public final Vector3fc faceNormal() {
        computeGeometry();
        return faceNormal;
    }

    @Override
    public final RenderMaterialImpl material() {
        return EncodingFormat.material(data[baseIndex + HEADER_BITS]);
    }

    @Override
    public final int tintIndex() {
        return data[baseIndex + HEADER_TINT_INDEX];
    }

    @Override
    public final int tag() {
        return data[baseIndex + HEADER_TAG];
    }

    @Override
    public final void toVanilla(int[] target, int targetIndex) {
        System.arraycopy(data, baseIndex + HEADER_STRIDE, target, targetIndex, QUAD_STRIDE);

        int colorIndex = targetIndex + VERTEX_COLOR - HEADER_STRIDE;

        for (int i = 0; i < 4; i++) {
            target[colorIndex] = ColorHelper.toVanillaColor(target[colorIndex]);
            colorIndex += VANILLA_VERTEX_STRIDE;
        }
    }
}
