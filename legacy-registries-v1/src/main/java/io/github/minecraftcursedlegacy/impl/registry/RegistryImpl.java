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

package io.github.minecraftcursedlegacy.impl.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.function.IntFunction;

import io.github.minecraftcursedlegacy.accessor.registry.AccessorEntityRegistry;
import io.github.minecraftcursedlegacy.api.networking.PluginChannel;
import io.github.minecraftcursedlegacy.api.networking.PluginChannelRegistry;
import io.github.minecraftcursedlegacy.api.registry.Id;
import io.github.minecraftcursedlegacy.api.registry.Registry;
import io.github.minecraftcursedlegacy.api.registry.RegistryEntryAddedCallback;
import io.github.minecraftcursedlegacy.impl.Hacks;
import io.github.minecraftcursedlegacy.impl.registry.sync.RegistrySyncChannelS2C;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.item.ItemType;
import net.minecraft.tile.Tile;
import net.minecraft.util.io.CompoundTag;

public class RegistryImpl implements ModInitializer {
	@Override
	public void onInitialize() {
		Hacks.hack = Registry::lockAll;

		PluginChannelRegistry.registerPluginChannel(syncChannel = new RegistrySyncChannelS2C());
	}
	

	public static <I extends ItemType> I addTileItem(Id id, Tile value, IntFunction<I> constructor) {
		return ((ItemTypeRegistry) ITEM_TYPE).addTileItem(id, value, constructor);
	}

	public static <T> Event<RegistryEntryAddedCallback<T>> createEvent(Class<T> clazz) {
		return EventFactory.createArrayBacked(RegistryEntryAddedCallback.class, listeners -> (object, id, rawId) -> {
			for (RegistryEntryAddedCallback<T> listener : listeners) {
				listener.onEntryAdded(object, id, rawId);
			}
		});
	}

	static int nextItemTypeId() {
		while (ItemType.byId[currentItemtypeId] != null) {
			++currentItemtypeId;
		}

		return currentItemtypeId;
	}

	static int nextTileId() {
		while (Tile.BY_ID[currentTileId] != null) {
			++currentTileId;
		}

		return currentTileId;
	}

	public static final Registry<ItemType> ITEM_TYPE;
	public static final Registry<Tile> TILE;
	public static final Registry<EntityType> ENTITY_TYPE;

	private static int currentItemtypeId = Tile.BY_ID.length;
	private static int currentTileId = 1;

	static final Map<Tile, ItemType> T_2_TI = new HashMap<>();

	// Sync Stuff
	public static PluginChannel syncChannel;
	public static CompoundTag registryData; // this is used server side only

	static {
		//noinspection ResultOfMethodCallIgnored
		Tile.BED.hashCode(); // make sure tiles are initialised
		AccessorEntityRegistry.getIdToClassMap(); // make sure entities are initialised
		TILE = new TileRegistry(new Id("api:tile")); // TILES BEFORE ITEMS SO ITEM TYPE REMAPPING HAPPENS LATER!
		ITEM_TYPE = new ItemTypeRegistry(new Id("api:item_type"));
		ENTITY_TYPE = new EntityTypeRegistry(new Id("api:entity_type"));
	}
}
