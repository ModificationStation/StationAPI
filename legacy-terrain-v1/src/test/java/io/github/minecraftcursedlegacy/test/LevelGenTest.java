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

package io.github.minecraftcursedlegacy.test;

import io.github.minecraftcursedlegacy.api.event.ActionResult;
import io.github.minecraftcursedlegacy.api.terrain.BiomeEvents.BiomePlacementCallback;
import io.github.minecraftcursedlegacy.api.terrain.ChunkGenEvents;
import io.github.minecraftcursedlegacy.api.terrain.feature.PositionedFeature;
import io.github.minecraftcursedlegacy.api.terrain.feature.PositionedFeatures;
import net.fabricmc.api.ModInitializer;
import net.minecraft.level.biome.Biome;

public class LevelGenTest implements ModInitializer {
	@Override
	public void onInitialize() {
		System.out.println("Hello, Fabric LevelGen World!");

		testBiome = new TestBiome("Test");

		BiomePlacementCallback.EVENT.register((temperature, humidity, biomesetter) -> {
			float t = temperature;

			while (t > 0.1f) {
				t -= 0.1f;
			}

			if (t > 0.06f) {
				biomesetter.accept(testBiome);
				return ActionResult.SUCCESS;
			}

			return ActionResult.PASS;
		});

		oreRock = PositionedFeatures.withCountScatteredHeightmap(new OreRock(), 3);

		ChunkGenEvents.Decorate.OVERWORLD.register((level, biome, rand, x, z) -> {
			if (biome == testBiome) {
				oreRock.generate(level, rand, x, z);
			}
		});
	}

	public static Biome testBiome;
	private static PositionedFeature oreRock;
}
