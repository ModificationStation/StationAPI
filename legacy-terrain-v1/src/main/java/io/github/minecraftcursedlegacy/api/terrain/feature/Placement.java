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
import java.util.function.Function;

import net.minecraft.level.Level;
import net.minecraft.level.structure.Feature;
import net.minecraft.util.maths.TilePos;

/**
 * Provides the locations for a {@linkplain Feature feature} to generate in the world, when used in a {@linkplain PositionedFeature}.
 */
public abstract class Placement implements Function<Feature, PositionedFeature> {
	/**
	 * Retrieve the set of positions at which the feature should be generated.
	 * @param level the level in whch to generate.
	 * @param rand the pseudo random number generator for worldgen.
	 * @param startX the start X position for offseting positions.
	 * @param startY the start Y position for offseting positions.
	 * @param startZ the start Z position for offseting positions.
	 * @return an Iterable of positions to generate the feature at.
	 */
	public abstract Iterable<TilePos> getPositions(Level level, Random rand, int startX, int startY, int startZ);

	/**
	 * Create a decorated feature from this placement and the provided feature.
	 * @param feature the feature to place via this placement.
	 * @return the {@linkplain PositionedFeature} created via the combination of this placement and the feature.
	 */
	@Override
	public final PositionedFeature apply(Feature feature) {
		return new PositionedFeature(this, feature);
	}

	public static final Placement SCATTERED_HEIGHTMAP = new ScatteredHeightmapPlacement();
}
