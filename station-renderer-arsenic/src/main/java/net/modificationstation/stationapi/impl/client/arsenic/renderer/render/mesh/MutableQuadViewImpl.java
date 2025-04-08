package net.modificationstation.stationapi.impl.client.arsenic.renderer.render.mesh;

import static  net.modificationstation.stationapi.impl.client.arsenic.renderer.render.mesh.EncodingFormat.HEADER_BITS;
import static  net.modificationstation.stationapi.impl.client.arsenic.renderer.render.mesh.EncodingFormat.HEADER_STRIDE;
import static  net.modificationstation.stationapi.impl.client.arsenic.renderer.render.mesh.EncodingFormat.HEADER_TAG;
import static  net.modificationstation.stationapi.impl.client.arsenic.renderer.render.mesh.EncodingFormat.HEADER_TINT_INDEX;
import static  net.modificationstation.stationapi.impl.client.arsenic.renderer.render.mesh.EncodingFormat.VERTEX_COLOR;
import static  net.modificationstation.stationapi.impl.client.arsenic.renderer.render.mesh.EncodingFormat.VERTEX_NORMAL;
import static  net.modificationstation.stationapi.impl.client.arsenic.renderer.render.mesh.EncodingFormat.VERTEX_STRIDE;
import static  net.modificationstation.stationapi.impl.client.arsenic.renderer.render.mesh.EncodingFormat.VERTEX_U;
import static  net.modificationstation.stationapi.impl.client.arsenic.renderer.render.mesh.EncodingFormat.VERTEX_X;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.modificationstation.stationapi.api.client.render.material.RenderMaterial;
import net.modificationstation.stationapi.api.client.render.mesh.QuadEmitter;
import net.modificationstation.stationapi.api.client.render.mesh.QuadTransform;
import net.modificationstation.stationapi.api.client.render.mesh.QuadView;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.ArsenicRenderer;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.render.helper.ColorHelper;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.render.helper.NormalHelper;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.render.helper.TextureHelper;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.render.material.RenderMaterialImpl;
import org.jetbrains.annotations.Nullable;

/**
 * Almost-concrete implementation of a mutable quad. The only missing part is {@link #emitDirectly()},
 * because that depends on where/how it is used. (Mesh encoding vs. render-time transformation).
 *
 * <p>In many cases an instance of this class is used as an "editor quad". The editor quad's
 * {@link #emitDirectly()} method calls some other internal method that transforms the quad
 * data and then buffers it. Transformations should be the same as they would be in a vanilla
 * render - the editor is serving mainly as a way to access vertex data without magical
 * numbers. It also allows for a consistent interface for those transformations.
 */
public abstract class MutableQuadViewImpl extends QuadViewImpl implements QuadEmitter {
    private static final QuadTransform NO_TRANSFORM = q -> true;

    private static final int[] DEFAULT_QUAD_DATA = new int[EncodingFormat.TOTAL_STRIDE];

    static {
        MutableQuadViewImpl quad = new MutableQuadViewImpl() {
            @Override
            protected void emitDirectly() {
                // This quad won't be emitted. It's only used to configure the default quad data.
            }
        };

        // Start with all zeroes
        quad.data = DEFAULT_QUAD_DATA;
        // Apply non-zero defaults
        quad.color(-1, -1, -1, -1);
        quad.cullFace(null);
        quad.material(ArsenicRenderer.STANDARD_MATERIAL);
        quad.tintIndex(-1);
    }

    private QuadTransform activeTransform = NO_TRANSFORM;
    private final ObjectArrayList<QuadTransform> transformStack = new ObjectArrayList<>();
    private final QuadTransform stackTransform = q -> {
        int i = transformStack.size() - 1;

        while (i >= 0) {
            if (!transformStack.get(i--).transform(q)) {
                return false;
            }
        }

        return true;
    };

    public final void clear() {
        System.arraycopy(DEFAULT_QUAD_DATA, 0, data, baseIndex, EncodingFormat.TOTAL_STRIDE);
        isGeometryInvalid = true;
        nominalFace = null;
    }

    @Override
    public final MutableQuadViewImpl pos(int vertexIndex, float x, float y, float z) {
        final int index = baseIndex + vertexIndex * VERTEX_STRIDE + VERTEX_X;
        data[index] = Float.floatToRawIntBits(x);
        data[index + 1] = Float.floatToRawIntBits(y);
        data[index + 2] = Float.floatToRawIntBits(z);
        isGeometryInvalid = true;
        return this;
    }

    @Override
    public final MutableQuadViewImpl color(int vertexIndex, int color) {
        data[baseIndex + vertexIndex * VERTEX_STRIDE + VERTEX_COLOR] = color;
        return this;
    }

    @Override
    public final MutableQuadViewImpl uv(int vertexIndex, float u, float v) {
        final int i = baseIndex + vertexIndex * VERTEX_STRIDE + VERTEX_U;
        data[i] = Float.floatToRawIntBits(u);
        data[i + 1] = Float.floatToRawIntBits(v);
        return this;
    }

    @Override
    public final MutableQuadViewImpl spriteBake(Sprite sprite, int bakeFlags) {
        TextureHelper.bakeSprite(this, sprite, bakeFlags);
        return this;
    }

    protected final void normalFlags(int flags) {
        data[baseIndex + HEADER_BITS] = EncodingFormat.normalFlags(data[baseIndex + HEADER_BITS], flags);
    }

    @Override
    public final MutableQuadViewImpl normal(int vertexIndex, float x, float y, float z) {
        normalFlags(normalFlags() | (1 << vertexIndex));
        data[baseIndex + vertexIndex * VERTEX_STRIDE + VERTEX_NORMAL] = NormalHelper.packNormal(x, y, z);
        return this;
    }

    /**
     * Internal helper method. Copies face normals to vertex normals lacking one.
     */
    public final void populateMissingNormals() {
        final int normalFlags = this.normalFlags();

        if (normalFlags == 0b1111) return;

        final int packedFaceNormal = packedFaceNormal();

        for (int v = 0; v < 4; v++) {
            if ((normalFlags & (1 << v)) == 0) {
                data[baseIndex + v * VERTEX_STRIDE + VERTEX_NORMAL] = packedFaceNormal;
            }
        }

        normalFlags(0b1111);
    }

    @Override
    public final MutableQuadViewImpl cullFace(@Nullable Direction face) {
        data[baseIndex + HEADER_BITS] = EncodingFormat.cullFace(data[baseIndex + HEADER_BITS], face);
        nominalFace(face);
        return this;
    }

    @Override
    public final MutableQuadViewImpl nominalFace(@Nullable Direction face) {
        nominalFace = face;
        return this;
    }

    @Override
    public final MutableQuadViewImpl material(RenderMaterial material) {
        data[baseIndex + HEADER_BITS] = EncodingFormat.material(data[baseIndex + HEADER_BITS], (RenderMaterialImpl) material);
        return this;
    }

    @Override
    public final MutableQuadViewImpl tintIndex(int tintIndex) {
        data[baseIndex + HEADER_TINT_INDEX] = tintIndex;
        return this;
    }

    @Override
    public final MutableQuadViewImpl tag(int tag) {
        data[baseIndex + HEADER_TAG] = tag;
        return this;
    }

    @Override
    public final MutableQuadViewImpl copyFrom(QuadView quad) {
        final QuadViewImpl q = (QuadViewImpl) quad;
        System.arraycopy(q.data, q.baseIndex, data, baseIndex, EncodingFormat.TOTAL_STRIDE);
        nominalFace = q.nominalFace;
        isGeometryInvalid = q.isGeometryInvalid;

        if (!isGeometryInvalid) {
            faceNormal.set(q.faceNormal);
        }

        return this;
    }

    @Override
    public final MutableQuadViewImpl fromVanilla(int[] quadData, int startIndex) {
        System.arraycopy(quadData, startIndex, data, baseIndex + HEADER_STRIDE, VANILLA_QUAD_STRIDE);
        isGeometryInvalid = true;

        int normalFlags = 0;
        int colorIndex = baseIndex + VERTEX_COLOR;
        int normalIndex = baseIndex + VERTEX_NORMAL;

        for (int i = 0; i < 4; i++) {
            data[colorIndex] = ColorHelper.fromVanillaColor(data[colorIndex]);

            // Set normal flag if normal is not zero, ignoring W component
            if ((data[normalIndex] & 0xFFFFFF) != 0) {
                normalFlags |= 1 << i;
            }

            colorIndex += VERTEX_STRIDE;
            normalIndex += VERTEX_STRIDE;
        }

        normalFlags(normalFlags);
        return this;
    }

    @Override
    public void pushTransform(QuadTransform transform) {
        if (transform == null) {
            throw new NullPointerException("QuadTransform cannot be null!");
        }

        transformStack.push(transform);

        if (transformStack.size() == 1) {
            activeTransform = transform;
        } else if (transformStack.size() == 2) {
            activeTransform = stackTransform;
        }
    }

    @Override
    public void popTransform() {
        transformStack.pop();

        if (transformStack.size() == 0) {
            activeTransform = NO_TRANSFORM;
        } else if (transformStack.size() == 1) {
            activeTransform = transformStack.get(0);
        }
    }

    /**
     * Emit the quad without applying transforms and without clearing the underlying data.
     * Geometry is not guaranteed to be valid when called, but can be computed by calling {@link #computeGeometry()}.
     */
    protected abstract void emitDirectly();

    /**
     * Apply transforms and then if transforms return true, emit the quad without clearing the underlying data.
     */
    public final void transformAndEmit() {
        if (activeTransform.transform(this)) {
            emitDirectly();
        }
    }

    @Override
    public final MutableQuadViewImpl emit() {
        transformAndEmit();
        clear();
        return this;
    }
}
