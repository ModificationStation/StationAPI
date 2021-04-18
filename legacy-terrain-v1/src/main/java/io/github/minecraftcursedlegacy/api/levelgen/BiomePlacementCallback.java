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

package io.github.minecraftcursedlegacy.api.levelgen;

import java.util.function.Consumer;

import io.github.minecraftcursedlegacy.api.event.ActionResult;
import io.github.minecraftcursedlegacy.api.terrain.BiomeEvents;
import net.minecraft.level.biome.Biome;

/**
 * Callback for biome placement. Add a hook for this in the {@link net.fabricmc.api.ModInitializer init} stage, as the biomes are calculated in postinit.
 * 
 * <p>Upon return:
 * <ul>
 * <li> SUCCESS succeeds in altering the biome, and sets the latest biome set via the biome setter. If no biome has been set, then the behaviour defaults to FAIL
 * <li> PASS falls back to further event processing. If all events PASS, then the behaviour defaults to SUCCESS.
 * <li> FAIL falls back to vanilla biome placement.
 * </ul>
 * 
 * @deprecated use {@linkplain BiomeEvents.BiomePlacementCallback instead}.
 */
@FunctionalInterface
@Deprecated
public interface BiomePlacementCallback extends BiomeEvents.BiomePlacementCallback {
	@Override
	default ActionResult onClimaticBiomePlace(float temperature, float humidity, Consumer<Biome> biomeSetter) {
		return this.onBiomePlace(temperature, humidity, biomeSetter);
	}

	ActionResult onBiomePlace(float temperature, float humidity, Consumer<Biome> biomeSetter);
}
