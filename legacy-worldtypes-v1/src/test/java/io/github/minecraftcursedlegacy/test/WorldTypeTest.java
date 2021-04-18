package io.github.minecraftcursedlegacy.test;

import io.github.minecraftcursedlegacy.api.worldtype.WorldType;
import net.fabricmc.api.ModInitializer;

public class WorldTypeTest implements ModInitializer {
	@Override
	public void onInitialize() {
		type = new RandomWorldType();
	}

	public static WorldType type;
}
