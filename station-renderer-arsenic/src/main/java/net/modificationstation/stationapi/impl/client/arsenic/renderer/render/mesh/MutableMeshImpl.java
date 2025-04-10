package net.modificationstation.stationapi.impl.client.arsenic.renderer.render.mesh;

import net.modificationstation.stationapi.api.client.render.mesh.Mesh;
import net.modificationstation.stationapi.api.client.render.mesh.MutableMesh;
import net.modificationstation.stationapi.api.client.render.mesh.MutableQuadView;
import net.modificationstation.stationapi.api.client.render.mesh.QuadEmitter;

import java.util.function.Consumer;

/**
 * Our implementation of {@link MutableMesh}, mainly used for optimized mesh creation.
 * Not much to it - mainly it just needs to grow the int[] array as quads are appended
 * and maintain/provide a properly-configured {@link MutableQuadView} instance.
 * All the encoding and other work is handled in the quad base classes.
 * The one interesting bit is in {@link #emitter}.
 */
public class MutableMeshImpl extends MeshViewImpl implements MutableMesh {
    private final MutableQuadViewImpl emitter = new MutableQuadViewImpl() {
        @Override
        protected void emitDirectly() {
            // Necessary because the validity of geometry is not encoded; reading mesh data always
            // uses QuadViewImpl#load(), which assumes valid geometry. Built immutable meshes
            // should also have valid geometry for better performance.
            computeGeometry();
            limit += EncodingFormat.TOTAL_STRIDE;
            ensureCapacity(EncodingFormat.TOTAL_STRIDE);
            baseIndex = limit;
        }
    };

    public MutableMeshImpl() {
        data = new int[8 * EncodingFormat.TOTAL_STRIDE];
        limit = 0;

        ensureCapacity(EncodingFormat.TOTAL_STRIDE);
        emitter.data = data;
        emitter.baseIndex = limit;
        emitter.clear();
    }

    private void ensureCapacity(int stride) {
        if (stride > data.length - limit) {
            final int[] bigger = new int[data.length * 2];
            System.arraycopy(data, 0, bigger, 0, limit);
            data = bigger;
            emitter.data = data;
        }
    }

    @Override
    public QuadEmitter emitter() {
        emitter.clear();
        return emitter;
    }

    @Override
    public void forEachMutable(Consumer<? super MutableQuadView> action) {
        // emitDirectly will not be called by forEach, so just reuse the main emitter.
        forEach(action, emitter);
        emitter.data = data;
        emitter.baseIndex = limit;
    }

    @Override
    public Mesh immutableCopy() {
        final int[] packed = new int[limit];
        System.arraycopy(data, 0, packed, 0, limit);
        return new MeshImpl(packed);
    }

    @Override
    public void clear() {
        limit = 0;
        emitter.baseIndex = limit;
        emitter.clear();
    }
}
