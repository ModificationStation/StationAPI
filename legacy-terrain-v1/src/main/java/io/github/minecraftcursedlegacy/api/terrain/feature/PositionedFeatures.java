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

import net.minecraft.level.structure.Feature;

/**
 * Class with utility methods for utilising {@linkplain PositionedFeature positioned features}.
 */
public final class PositionedFeatures {
	private PositionedFeatures() {
		// NO-OP
	}

	/**
	 * Create a positioned feature with a count-scattered-heightmap distribution using the generation settings provided.
	 * @param feature the feature to generate.
	 * @param count the number of times to generate the feature.
	 * @return the created positioned feature instance.
	 */
	public static final PositionedFeature withCountScatteredHeightmap(Feature feature, int count) {
		// Generation has the outermost layer run first in generating the positions,
		// then passes those positions to the next feature in.
		// Therefore, we start with a count placement and use it to iterate scattered heightmap multiple times.
		return new CountPlacement(count).apply(Placement.SCATTERED_HEIGHTMAP.apply(feature));
	}

	/**
	 * Create a positioned feature with a count-scattered-yrange distribution using the generation settings provided.
	 * @param feature the feature to generate.
	 * @param count the number of times to generate the feature.
	 * @param minY the minimum y at which to generate the feature.
	 * @param maxY the maximum y, exclusive, at which to generate feature.
	 * @return the created positioned feature instance.
	 */
	public static final PositionedFeature withCountScatteredYRange(Feature feature, int count, int minY, int maxY) {
		return new CountPlacement(count).apply(new ScatteredYRangePlacement(minY, maxY).apply(feature));
	}
}
