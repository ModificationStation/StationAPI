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

import java.util.Arrays;
import java.util.Random;

import net.minecraft.level.Level;
import net.minecraft.util.maths.TilePos;

/**
 * {@linkplain Placement} that scatters along the x and z of the given region, and offsets the height (typically from 0) based on a given range.
 * Similar to how vanilla places most ores.
 */
public class ScatteredYRangePlacement extends Placement {
	/**
	 * @param minY the minimum y for generation.
	 * @param maxY the maximum y for generation, exclusive. Must be greater than minY.
	 */
	public ScatteredYRangePlacement(int minY, int maxY) {
		this.minY = minY;
		this.rangeY = maxY - minY;
	}

	private final int minY;
	private final int rangeY;

	@Override
	public Iterable<TilePos> getPositions(Level level, Random rand, int startX, int startY, int startZ) {
		int xToGen = startX + rand.nextInt(16) + 8;
		int yToGen = startY + rand.nextInt(this.rangeY) + this.minY;
		int zToGen = startZ + rand.nextInt(16) + 8;
		return Arrays.asList(new TilePos(xToGen, yToGen, zToGen));
	}
}
