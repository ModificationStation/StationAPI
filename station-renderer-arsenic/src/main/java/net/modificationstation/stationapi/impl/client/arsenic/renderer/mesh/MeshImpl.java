package net.modificationstation.stationapi.impl.client.arsenic.renderer.mesh;

import net.modificationstation.stationapi.api.client.render.mesh.Mesh;
import net.modificationstation.stationapi.api.client.render.mesh.QuadView;

import java.util.function.Consumer;

/**
 * Implementation of {@link Mesh}.
 * The way we encode meshes makes it very simple.
 */
public class MeshImpl implements Mesh {
	/** Used to satisfy external calls to {@link #forEach(Consumer)}. */
	ThreadLocal<QuadViewImpl> POOL = ThreadLocal.withInitial(QuadViewImpl::new);

	final int[] data;

	MeshImpl(int[] data) {
		this.data = data;
	}

	public int[] data() {
		return data;
	}

	@Override
	public void forEach(Consumer<QuadView> consumer) {
		forEach(consumer, POOL.get());
	}

	/**
	 * The renderer will call this with it's own cursor
	 * to avoid the performance hit of a thread-local lookup.
	 * Also means renderer can hold final references to quad buffers.
	 */
	void forEach(Consumer<QuadView> consumer, QuadViewImpl cursor) {
		final int limit = data.length;
		int index = 0;

		while (index < limit) {
			cursor.load(data, index);
			consumer.accept(cursor);
			index += EncodingFormat.TOTAL_STRIDE;
		}
	}
}