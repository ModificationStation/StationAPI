package net.modificationstation.stationapi.impl.client.arsenic.renderer;

import com.google.common.base.Preconditions;
import net.modificationstation.stationapi.api.client.render.material.BlendMode;
import net.modificationstation.stationapi.api.client.render.material.MaterialFinder;
import net.modificationstation.stationapi.api.client.render.material.RenderMaterial;
import net.modificationstation.stationapi.api.util.math.MathHelper;

/**
 * Default implementation of the standard render materials.
 * The underlying representation is simply an int with bit-wise
 * packing of the various material properties. This offers
 * easy/fast interning via int/object hashmap.
 */
public abstract class RenderMaterialImpl {
	private static final BlendMode[] BLEND_MODES = BlendMode.values();

	private static final int BLEND_MODE_MASK = MathHelper.smallestEncompassingPowerOfTwo(BlendMode.values().length) - 1;
	private static final int COLOR_DISABLE_FLAG = BLEND_MODE_MASK + 1;
	private static final int EMISSIVE_FLAG = COLOR_DISABLE_FLAG << 1;
	private static final int DIFFUSE_FLAG = EMISSIVE_FLAG << 1;
	private static final int AO_FLAG = DIFFUSE_FLAG << 1;
	public static final int VALUE_COUNT = (AO_FLAG << 1);

	private static final Value[] VALUES = new Value[VALUE_COUNT];

	static {
		for (int i = 0; i < VALUE_COUNT; i++) {
			VALUES[i] = new Value(i);
		}
	}

	public static RenderMaterialImpl.Value byIndex(int index) {
		return VALUES[index];
	}

	protected int bits;

	public BlendMode blendMode(int textureIndex) {
		return BLEND_MODES[bits & BLEND_MODE_MASK];
	}

	public boolean disableColorIndex(int textureIndex) {
		return (bits & COLOR_DISABLE_FLAG) != 0;
	}

	public int spriteDepth() {
		return 1;
	}

	public boolean emissive(int textureIndex) {
		return (bits & EMISSIVE_FLAG) != 0;
	}

	public boolean disableDiffuse(int textureIndex) {
		return (bits & DIFFUSE_FLAG) != 0;
	}

	public boolean disableAo(int textureIndex) {
		return (bits & AO_FLAG) != 0;
	}

	public static class Value extends RenderMaterialImpl implements RenderMaterial {
		private Value(int bits) {
			this.bits = bits;
		}

		public int index() {
			return bits;
		}
	}

	public static class Finder extends RenderMaterialImpl implements MaterialFinder {
		@Override
		public RenderMaterial find() {
			return VALUES[bits];
		}

		@Override
		public MaterialFinder clear() {
			bits = 0;
			return this;
		}

		@Override
		public MaterialFinder blendMode(int textureIndex, BlendMode blendMode) {
			if (blendMode == null) {
				blendMode = BlendMode.DEFAULT;
			}

			bits = (bits & ~BLEND_MODE_MASK) | blendMode.ordinal();
			return this;
		}

		@Override
		public MaterialFinder disableColorIndex(int textureIndex, boolean disable) {
			bits = disable ? (bits | COLOR_DISABLE_FLAG) : (bits & ~COLOR_DISABLE_FLAG);
			return this;
		}

		@Override
		public MaterialFinder spriteDepth(int depth) {
			Preconditions.checkArgument(depth == 1, "Unsupported sprite depth: %s", depth);

			return this;
		}

		@Override
		public MaterialFinder emissive(int textureIndex, boolean isEmissive) {
			bits = isEmissive ? (bits | EMISSIVE_FLAG) : (bits & ~EMISSIVE_FLAG);
			return this;
		}

		@Override
		public MaterialFinder disableDiffuse(int textureIndex, boolean disable) {
			bits = disable ? (bits | DIFFUSE_FLAG) : (bits & ~DIFFUSE_FLAG);
			return this;
		}

		@Override
		public MaterialFinder disableAo(int textureIndex, boolean disable) {
			bits = disable ? (bits | AO_FLAG) : (bits & ~AO_FLAG);
			return this;
		}
	}
}