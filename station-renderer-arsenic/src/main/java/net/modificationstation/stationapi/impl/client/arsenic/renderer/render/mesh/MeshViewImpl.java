package net.modificationstation.stationapi.impl.client.arsenic.renderer.render.mesh;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.modificationstation.stationapi.api.client.render.mesh.MeshView;
import net.modificationstation.stationapi.api.client.render.mesh.QuadEmitter;
import net.modificationstation.stationapi.api.client.render.mesh.QuadView;
import org.jetbrains.annotations.Range;

import java.util.function.Consumer;

public class MeshViewImpl implements MeshView {
    /** Used to satisfy external calls to {@link #forEach(Consumer)}. */
    private static final ThreadLocal<ObjectArrayList<QuadViewImpl>> CURSOR_POOLS = ThreadLocal.withInitial(ObjectArrayList::new);

    int[] data;
    int limit;

    MeshViewImpl() {
    }

    @Override
    @Range(from = 0, to = Integer.MAX_VALUE)
    public int size() {
        return limit / EncodingFormat.TOTAL_STRIDE;
    }

    @Override
    public void forEach(Consumer<? super QuadView> action) {
        ObjectArrayList<QuadViewImpl> pool = CURSOR_POOLS.get();
        QuadViewImpl cursor;

        if (pool.isEmpty()) {
            cursor = new QuadViewImpl();
        } else {
            cursor = pool.pop();
        }

        forEach(action, cursor);

        pool.push(cursor);
    }

    /**
     * The renderer can call this with its own cursor to avoid the performance hit of a
     * thread-local lookup or to use a mutable cursor.
     */
    <C extends QuadViewImpl> void forEach(Consumer<? super C> action, C cursor) {
        final int limit = this.limit;
        int index = 0;
        cursor.data = data;

        while (index < limit) {
            cursor.baseIndex = index;
            cursor.load();
            action.accept(cursor);
            index += EncodingFormat.TOTAL_STRIDE;
        }

        cursor.data = null;
    }

    // TODO: This could be optimized by checking if the emitter is that of a MutableMeshImpl and if
    //  it has no transforms, in which case the entire data array can be copied in bulk.
    @Override
    public void outputTo(QuadEmitter emitter) {
        MutableQuadViewImpl e = (MutableQuadViewImpl) emitter;
        final int[] data = this.data;
        final int limit = this.limit;
        int index = 0;

        while (index < limit) {
            System.arraycopy(data, index, e.data, e.baseIndex, EncodingFormat.TOTAL_STRIDE);
            e.load();
            e.transformAndEmit();
            index += EncodingFormat.TOTAL_STRIDE;
        }

        e.clear();
    }
}