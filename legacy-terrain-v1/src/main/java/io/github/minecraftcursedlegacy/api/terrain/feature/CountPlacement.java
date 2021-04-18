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
import java.util.stream.IntStream;

import net.minecraft.level.Level;
import net.minecraft.util.maths.TilePos;

/**
 * {@linkplain Placement} that iterates the given feature a given number of times. Use in combination with other placements for the best results.
 */
public class CountPlacement extends Placement {
	public CountPlacement(int count) {
		this.count = count;
	}

	private final int count;

	/**
	 * Get the count for the given position and rng.
	 * @param rand the worldgen pseudorandom number generator.
	 * @param x the x position for worldgen.
	 * @param y the y position for worldgen.
	 * @param z the z position for worldgen.
	 * @return the number of times to generate the feature (i.e. how many times to duplicate the position).
	 */
	protected int getCount(Random rand, int x, int y, int z) {
		return this.count;
	}

	@Override
	public Iterable<TilePos> getPositions(Level level, Random rand, int startX, int startY, int startZ) {
		TilePos position = new TilePos(startX, startY, startZ);
		return IntStream.range(0, this.getCount(rand, startX, startY, startZ)).mapToObj(i -> position)::iterator;
	}
}
