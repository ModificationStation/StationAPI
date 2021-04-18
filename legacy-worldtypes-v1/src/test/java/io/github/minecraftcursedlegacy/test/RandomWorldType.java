package io.github.minecraftcursedlegacy.test;

import io.github.minecraftcursedlegacy.api.registry.Id;
import io.github.minecraftcursedlegacy.api.registry.Translations;
import io.github.minecraftcursedlegacy.api.worldtype.WorldType;
import net.minecraft.level.Level;
import net.minecraft.level.source.LevelSource;
import net.minecraft.util.io.CompoundTag;

public class RandomWorldType extends WorldType {
	public RandomWorldType() {
		super(new Id("modid", "random"));
		Translations.addTranslation(this.toString(), "Test");
	}

	@Override
	public LevelSource createChunkGenerator(Level level, CompoundTag additionalData) {
		return new RandomChunkGenerator(level, level.getSeed());
	}
}
