package net.modificationstation.stationapi.impl.client.arsenic.renderer.render.mesh;

import com.google.common.base.Preconditions;
import net.modificationstation.stationapi.api.client.render.VertexFormat;
import net.modificationstation.stationapi.api.client.render.VertexFormatElement;
import net.modificationstation.stationapi.api.client.render.VertexFormats;
import net.modificationstation.stationapi.api.client.render.mesh.QuadView;
import net.modificationstation.stationapi.api.client.render.model.ModelHelper;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.render.helper.GeometryHelper;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.render.material.MaterialViewImpl;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.render.material.RenderMaterialImpl;
import org.jetbrains.annotations.Nullable;

/**
 * Holds all the array offsets and bit-wise encoders/decoders for
 * packing/unpacking quad data in an array of integers.
 * All of this is implementation-specific - that's why it isn't a "helper" class.
 */
public final class EncodingFormat {
    private EncodingFormat() { }

    static final int HEADER_BITS = 0;
    static final int HEADER_FACE_NORMAL = 1;
    static final int HEADER_TINT_INDEX = 2;
    static final int HEADER_TAG = 3;
    public static final int HEADER_STRIDE = 4;

    static final int VERTEX_X;
    static final int VERTEX_Y;
    static final int VERTEX_Z;

    static final int VERTEX_U;
    static final int VERTEX_V;
    static final int VERTEX_COLOR;
    static final int VERTEX_NORMAL;
    public static final int VERTEX_STRIDE;

    public static final int QUAD_STRIDE;
    public static final int QUAD_STRIDE_BYTES;
    public static final int TOTAL_STRIDE;

    static {
        final VertexFormat format = VertexFormats.BLOCK;
        VERTEX_X = HEADER_STRIDE + format.getOffset(VertexFormatElement.POSITION) / 4;
        VERTEX_Y = VERTEX_X + 1;
        VERTEX_Z = VERTEX_Y + 1;

        VERTEX_U = HEADER_STRIDE + format.getOffset(VertexFormatElement.UV0) / 4;
        VERTEX_V = VERTEX_U + 1;
        VERTEX_COLOR = HEADER_STRIDE + format.getOffset(VertexFormatElement.COLOR) / 4;
        VERTEX_NORMAL = HEADER_STRIDE + format.getOffset(VertexFormatElement.NORMAL) / 4;
        VERTEX_STRIDE = format.getVertexSize() / 4;
        QUAD_STRIDE = VERTEX_STRIDE * 4;
        QUAD_STRIDE_BYTES = QUAD_STRIDE * 4;
        TOTAL_STRIDE = HEADER_STRIDE + QUAD_STRIDE;

        Preconditions.checkState(VERTEX_STRIDE == QuadView.VANILLA_VERTEX_STRIDE, "Arsenic vertex stride (%s) mismatched with rendering API (%s)", VERTEX_STRIDE, QuadView.VANILLA_VERTEX_STRIDE);
        Preconditions.checkState(QUAD_STRIDE == QuadView.VANILLA_QUAD_STRIDE, "Arsenic quad stride (%s) mismatched with rendering API (%s)", QUAD_STRIDE, QuadView.VANILLA_QUAD_STRIDE);
    }

    private static final int DIRECTION_COUNT = Direction.values().length;
    private static final int NULLABLE_DIRECTION_COUNT = DIRECTION_COUNT + 1;

    private static final int CULL_BIT_LENGTH = MathHelper.ceilLog2(NULLABLE_DIRECTION_COUNT);
    private static final int LIGHT_BIT_LENGTH = MathHelper.ceilLog2(DIRECTION_COUNT);
    private static final int NORMALS_BIT_LENGTH = 4;
    private static final int GEOMETRY_BIT_LENGTH = GeometryHelper.FLAG_BIT_COUNT;
    private static final int MATERIAL_BIT_LENGTH = MaterialViewImpl.TOTAL_BIT_LENGTH;

    private static final int CULL_BIT_OFFSET = 0;
    private static final int LIGHT_BIT_OFFSET = CULL_BIT_OFFSET + CULL_BIT_LENGTH;
    private static final int NORMALS_BIT_OFFSET = LIGHT_BIT_OFFSET + LIGHT_BIT_LENGTH;
    private static final int GEOMETRY_BIT_OFFSET = NORMALS_BIT_OFFSET + NORMALS_BIT_LENGTH;
    private static final int MATERIAL_BIT_OFFSET = GEOMETRY_BIT_OFFSET + GEOMETRY_BIT_LENGTH;
    private static final int TOTAL_BIT_LENGTH = MATERIAL_BIT_OFFSET + MATERIAL_BIT_LENGTH;

    private static final int CULL_MASK = bitMask(CULL_BIT_LENGTH, CULL_BIT_OFFSET);
    private static final int LIGHT_MASK = bitMask(LIGHT_BIT_LENGTH, LIGHT_BIT_OFFSET);
    private static final int NORMALS_MASK = bitMask(NORMALS_BIT_LENGTH, NORMALS_BIT_OFFSET);
    private static final int GEOMETRY_MASK = bitMask(GEOMETRY_BIT_LENGTH, GEOMETRY_BIT_OFFSET);
    private static final int MATERIAL_MASK = bitMask(MATERIAL_BIT_LENGTH, MATERIAL_BIT_OFFSET);

    static {
        Preconditions.checkArgument(TOTAL_BIT_LENGTH <= 32, "Arsenic header encoding bit count (%s) exceeds integer bit length)", TOTAL_STRIDE);
    }

    public static int bitMask(int bitLength, int bitOffset) {
        return ((1 << bitLength) - 1) << bitOffset;
    }

    @Nullable
    static Direction cullFace(int bits) {
        return ModelHelper.faceFromIndex((bits & CULL_MASK) >>> CULL_BIT_OFFSET);
    }

    static int cullFace(int bits, @Nullable Direction face) {
        return (bits & ~CULL_MASK) | (ModelHelper.toFaceIndex(face) << CULL_BIT_OFFSET);
    }

    static Direction lightFace(int bits) {
        return ModelHelper.faceFromIndex((bits & LIGHT_MASK) >>> LIGHT_BIT_OFFSET);
    }

    static int lightFace(int bits, Direction face) {
        return (bits & ~LIGHT_MASK) | (ModelHelper.toFaceIndex(face) << LIGHT_BIT_OFFSET);
    }

    /** indicate if vertex normal has been set - bits correspond to vertex ordinals. */
    static int normalFlags(int bits) {
        return (bits & NORMALS_MASK) >>> NORMALS_BIT_OFFSET;
    }

    static int normalFlags(int bits, int normalFlags) {
        return (bits & ~NORMALS_MASK) | ((normalFlags << NORMALS_BIT_OFFSET) & NORMALS_MASK);
    }

    static int geometryFlags(int bits) {
        return (bits & GEOMETRY_MASK) >>> GEOMETRY_BIT_OFFSET;
    }

    static int geometryFlags(int bits, int geometryFlags) {
        return (bits & ~GEOMETRY_MASK) | ((geometryFlags << GEOMETRY_BIT_OFFSET) & GEOMETRY_MASK);
    }

    static RenderMaterialImpl material(int bits) {
        return RenderMaterialImpl.byIndex((bits & MATERIAL_MASK) >>> MATERIAL_BIT_OFFSET);
    }

    static int material(int bits, RenderMaterialImpl material) {
        return (bits & ~MATERIAL_MASK) | (material.index() << MATERIAL_BIT_OFFSET);
    }
}
