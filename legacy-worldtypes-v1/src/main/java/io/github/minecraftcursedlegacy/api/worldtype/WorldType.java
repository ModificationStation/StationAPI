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

package io.github.minecraftcursedlegacy.api.worldtype;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import io.github.minecraftcursedlegacy.api.registry.Id;
import io.github.minecraftcursedlegacy.api.terrain.ChunkGenerator;
import io.github.minecraftcursedlegacy.impl.worldtype.WorldTypeImpl;
import net.minecraft.level.Level;
import net.minecraft.level.gen.BiomeSource;
import net.minecraft.level.source.LevelSource;
import net.minecraft.level.source.OverworldLevelSource;
import net.minecraft.util.io.CompoundTag;

/**
 * Indicates a world generator type that can be selected in the game.
 * @since 1.0.0
 */
public class WorldType {
	/**
	 * Create a new world type.
	 * @param id the id of the world type.
	 */
	public WorldType(Id id) {
		this(id, false);
	}

	/**
	 * Create a new world type.
	 * @param id the id of the world type.
	 * @param storeAdditionalData whether the world type should store additional nbt data.
	 */
	public WorldType(Id id, boolean storeAdditionalData) {
		this.id = id;
		REVERSE_LOOKUP.put(id, this);
		WorldTypeImpl.add(this);

		this.storeAdditionalData = storeAdditionalData;
		this.translationKey = "generator." + this.id.getNamespace() + "." + this.id.getName();
	}

	private final Id id;
	private final String translationKey;
	private final boolean storeAdditionalData;

	/**
	 * Creates the world type's overworld biome source for the given level.
	 * @param level the level.
	 * @param additionalData if this world type stores additional data, a tag to read and write such data from. Otherwise null.
	 * @return an instance of {@linkplain BiomeSource} or one of its subclassses, for placing biomes in the world.
	 */
	public BiomeSource createBiomeSource(Level level, @Nullable CompoundTag additionalData) {
		return new BiomeSource(level);
	}

	/**
	 * Creates the world type's overworld chunk gemerator for the given level.
	 * @param level the level.
	 * @param additionalData if this world type stores additional data, a tag to read and write such data from. Otherwise null.
	 * @return an instance of {@linkplain LevelSource} or one of its subclassses (modded ones will typically be an instance of {@linkplain ChunkGenerator}), for generating the shape and decorations of the world.
	 */
	public LevelSource createChunkGenerator(Level level, @Nullable CompoundTag additionalData) {
		return new OverworldLevelSource(level, level.getSeed());
	}

	/**
	 * Retrieves the id of this world type.
	 * @return the id of this world type.
	 */
	public Id getId() {
		return this.id;
	}

	/**
	 * Whether this world type stores additional data.
	 * @return a boolean specifying whether the world type stores additional data.
	 */
	public boolean storesAdditionalData() {
		return this.storeAdditionalData;
	}

	/**
	 * Returns the cached translation key for this world type.
	 * @return the translation key for this world type.
	 */
	@Override
	public String toString() {
		return this.translationKey;
	}

	/**
	 * Gets the {@linkplain WorldType world type} for the given id.
	 * @param id the id of the world type.
	 * @return the world type associated with this id, or the default world type otherwise.
	 */
	public static WorldType getById(Id id) {
		return REVERSE_LOOKUP.getOrDefault(id, DEFAULT);
	}

	private static final Map<Id, WorldType> REVERSE_LOOKUP = new HashMap<>();

	/**
	 * The default world type with the id minecraft:default.
	 */
	public static final WorldType DEFAULT = new WorldType(new Id("default"));
}
