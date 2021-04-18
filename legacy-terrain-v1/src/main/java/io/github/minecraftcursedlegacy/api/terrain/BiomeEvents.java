package io.github.minecraftcursedlegacy.api.terrain;

import java.util.function.Consumer;

import io.github.minecraftcursedlegacy.api.event.ActionResult;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.level.biome.Biome;

/**
 * Class of events pertaining to biome placement and biomes.
 * @since 1.0.0
 */
public final class BiomeEvents {
	private BiomeEvents() {
	}

	/**
	 * Callback for biome placement based on climate. Add a hook for this in the {@link net.fabricmc.api.ModInitializer init} stage, as the biomes are calculated in postinit.
	 *
	 * <p>Upon return:
	 * <ul>
	 * <li> SUCCESS succeeds in altering the biome, and sets the latest biome set via the biome setter. If no biome has been set, then the behaviour defaults to FAIL
	 * <li> PASS falls back to further event processing. If all events PASS, then the behaviour defaults to SUCCESS.
	 * <li> FAIL falls back to vanilla biome placement.
	 * </ul>
	 */
	@FunctionalInterface
	public interface BiomePlacementCallback {
		Event<BiomePlacementCallback> EVENT = EventFactory.createArrayBacked(BiomePlacementCallback.class,
				(listeners) -> (temperature, rainfall, biomeSetter) -> {
					for (BiomePlacementCallback listener : listeners) {
						ActionResult result = listener.onClimaticBiomePlace(temperature, rainfall, biomeSetter);

						if (result != ActionResult.PASS) {
							return result;
						}
					}

					return ActionResult.SUCCESS;
				});

		ActionResult onClimaticBiomePlace(float temperature, float humidity, Consumer<Biome> biomeSetter);
	}
}
