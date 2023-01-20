package net.modificationstation.stationapi.impl.client.arsenic.renderer.mesh;

import net.modificationstation.stationapi.api.client.render.mesh.Mesh;
import net.modificationstation.stationapi.api.client.render.mesh.MeshBuilder;
import net.modificationstation.stationapi.api.client.render.mesh.MutableQuadView;
import net.modificationstation.stationapi.api.client.render.mesh.QuadEmitter;

/**
 * Our implementation of {@link MeshBuilder}, used for static mesh creation and baking.
 * Not much to it - mainly it just needs to grow the int[] array as quads are appended
 * and maintain/provide a properly-configured {@link MutableQuadView} instance.
 * All the encoding and other work is handled in the quad base classes.
 * The one interesting bit is in {@link Maker#emit()}.
 */
public class MeshBuilderImpl implements MeshBuilder {
	int[] data = new int[256];
	private final Maker maker = new Maker();
	int index = 0;
	int limit = data.length;

	protected void ensureCapacity(int stride) {
		if (stride > limit - index) {
			limit *= 2;
			final int[] bigger = new int[limit];
			System.arraycopy(data, 0, bigger, 0, index);
			data = bigger;
			maker.data = bigger;
		}
	}

	@Override
	public Mesh build() {
		final int[] packed = new int[index];
		System.arraycopy(data, 0, packed, 0, index);
		index = 0;
		maker.begin(data, index);
		return new MeshImpl(packed);
	}

	@Override
	public QuadEmitter getEmitter() {
		ensureCapacity(EncodingFormat.TOTAL_STRIDE);
		maker.begin(data, index);
		return maker;
	}

	/**
	 * Our base classes are used differently so we define final
	 * encoding steps in subtypes. This will be a static mesh used
	 * at render time so we want to capture all geometry now and
	 * apply non-location-dependent lighting.
	 */
	private class Maker extends MutableQuadViewImpl implements QuadEmitter {
		@Override
		public Maker emit() {
			computeGeometry();
			index += EncodingFormat.TOTAL_STRIDE;
			ensureCapacity(EncodingFormat.TOTAL_STRIDE);
			baseIndex = index;
			clear();
			return this;
		}
	}
}