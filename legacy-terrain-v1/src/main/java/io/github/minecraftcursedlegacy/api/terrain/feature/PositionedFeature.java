/*
 * Copyright (c) 2020 The Cursed Legacy Team.
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package io.github.minecraftcursedlegacy.api.terrain.feature;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import io.github.minecraftcursedlegacy.api.terrain.ChunkGenEvents;
import net.minecraft.level.Level;
import net.minecraft.level.structure.Feature;

/**
 * A combination of a {@linkplain Feature} and a {@linkplain Placement} for ease of use in generation.
 */
public final class PositionedFeature extends Feature {
	PositionedFeature(Placement placement, Feature feature) {
		this.placement = placement;
		this.feature = feature;
	}

	private Placement placement;
	private Feature feature;

	/**
	 * Generate this positioned feature in the world, assuming the root positions of x, z are given (i.e. those provided by {@linkplain ChunkGenEvents.Decorate}, chunkX * 16 and chunkZ * 16).
	 * @param level the level in which to generate the feature.
	 * @param rand the worldgen pseudorandom number generator.
	 * @param startX the start x position for generation.
	 * @param startZ the start z position for generation.
	 * @return whether the feature successfully generated at least once.
	 */
	public boolean generate(Level level, Random rand, int startX, int startZ) {
		return this.generate(level, rand, startX, 0, startZ);
	}

	/**
	 * Generate this positioned feature in various positions relative to the given start coordinates.
	 * @return true if at least one feature generated successfully.
	 */
	@Override
	public boolean generate(Level level, Random rand, int x, int y, int z) {
		AtomicBoolean success = new AtomicBoolean(false);

		this.placement.getPositions(level, rand, x, y, z).forEach(pos -> {
			if (this.feature.generate(level, rand, pos.x, pos.y, pos.z)) {
				success.set(true);
			}
		});

		return success.get();
	}

	@Override
	public void setupTreeGeneration(double d, double d1, double d2) {
		this.feature.setupTreeGeneration(d, d1, d2);
	}
}
