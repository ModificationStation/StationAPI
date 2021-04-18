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
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.IntFunction;

import io.github.minecraftcursedlegacy.accessor.registry.AccessorPlaceableTileItem;
import io.github.minecraftcursedlegacy.accessor.registry.AccessorRecipeRegistry;
import io.github.minecraftcursedlegacy.accessor.registry.AccessorShapedRecipe;
import io.github.minecraftcursedlegacy.accessor.registry.AccessorShapelessRecipe;
import io.github.minecraftcursedlegacy.accessor.registry.AccessorTileItem;
import io.github.minecraftcursedlegacy.api.registry.Id;
import io.github.minecraftcursedlegacy.api.registry.Registry;
import io.github.minecraftcursedlegacy.impl.registry.client.AtlasMapper;
import io.github.minecraftcursedlegacy.impl.registry.sync.RegistryRemapper;
import net.minecraft.item.ItemInstance;
import net.minecraft.item.ItemType;
import net.minecraft.item.PlaceableTileItem;
import net.minecraft.item.TileItem;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeRegistry;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.recipe.SmeltingRecipeRegistry;
import net.minecraft.tile.Tile;
import net.minecraft.util.io.CompoundTag;

class ItemTypeRegistry extends Registry<ItemType> {
	ItemTypeRegistry(Id registryName) {
		super(ItemType.class, registryName, null);

		// add vanilla item types
		for (int i = 0; i < ItemType.byId.length; ++i) {
			ItemType value = ItemType.byId[i];

			if (value instanceof TileItem) {
				RegistryImpl.T_2_TI.put(Tile.BY_ID[((AccessorTileItem) value).getTileId()], (TileItem) value);
			} else if (value instanceof PlaceableTileItem) {
				RegistryImpl.T_2_TI.put(Tile.BY_ID[((AccessorPlaceableTileItem) value).getTileId()], (PlaceableTileItem) value);
			}

			if (value != null) {
				String idPart = value.getTranslationKey();

				if (idPart == null) {
					idPart = "itemtype";
				} else {
					idPart = idPart.substring(5);
				}

				this.byRegistryId.put(new Id(idPart + "_" + i), value);
				this.bySerialisedId.put(i, value);
			}
		}
	}

	private ItemType[] oldItemTypes = new ItemType[ItemType.byId.length];

	@Override
	public <E extends ItemType> E registerValue(Id id, E value) {
		throw new UnsupportedOperationException("Use register(Id, IntFunction<ItemType>) instead, since item types need to use the provided int serialised ids in their constructor!");
	}

	/**
	 * Item Types are weird.
	 */
	@Override
	public <E extends ItemType> E register(Id id, IntFunction<E> valueProvider) {
		return super.register(id, rawSID -> valueProvider.apply(rawSID - Tile.BY_ID.length));
	}

	@Override
	protected int getNextSerialisedId() {
		return RegistryImpl.nextItemTypeId();
	}

	@Override
	protected void beforeRemap() {
		int size = ItemType.byId.length;
		// copy old array for later recipe remapping
		System.arraycopy(ItemType.byId, 0, this.oldItemTypes, 0, size);
		// clear array
		System.arraycopy(new ItemType[size], 0, ItemType.byId, 0, size);
	}

	@Override
	protected void onRemap(ItemType remappedValue, int newSerialisedId) {
		ItemType.byId[newSerialisedId] = remappedValue;
		((IdSetter) remappedValue).setId(newSerialisedId);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void postRemap() {
		// Remap Recipes
		RegistryRemapper.LOGGER.info("Remapping Crafting Recipes.");

		for (Recipe recipe : ((AccessorRecipeRegistry) RecipeRegistry.getInstance()).getRecipes()) {
			if (recipe instanceof ShapedRecipe) {
				// remap recipe ingredients
				ItemInstance[] ingredients = ((AccessorShapedRecipe) recipe).getIngredients();

				for (int i = 0; i < ingredients.length; ++i) {
					ItemInstance instance = ingredients[i];

					if (instance != null) {
						int oldId = instance.itemId;
						int newId = this.oldItemTypes[oldId].id;

						// only remap if necessary
						if (oldId != newId) {
							// set new id
							instance.itemId = newId;
						}
					}
				}

				// recompute output id
				ItemInstance result = ((AccessorShapedRecipe) recipe).getOutput();
				int newId = this.oldItemTypes[result.itemId].id;

				// only remap if necessary
				if (result.itemId != newId) {
					result.itemId = newId;
					((IdSetter) recipe).setId(newId);
				}
			} else if (recipe instanceof ShapelessRecipe) {
				// remap recipe ingredients
				List<ItemInstance> ingredients = ((AccessorShapelessRecipe) recipe).getInput();

				for (ItemInstance instance : ingredients) {
					if (instance != null) {
						int oldId = instance.itemId;
						int newId = this.oldItemTypes[oldId].id;

						// only remap if necessary
						if (oldId != newId) {
							// set new id
							instance.itemId = newId;
						}
					}
				}

				// recompute output id
				ItemInstance result = ((AccessorShapelessRecipe) recipe).getOutput();
				int newId = this.oldItemTypes[result.itemId].id;

				// only remap if necessary
				if (result.itemId != newId) {
					result.itemId = newId;
				}
			}
		}

		RegistryRemapper.LOGGER.info("Remapping Smelting Recipes.");

		SmeltingRecipeRegistry smelting = SmeltingRecipeRegistry.getInstance();
		Map<Integer, ItemInstance> newRecipes = new HashMap<>();

		smelting.getRecipes().forEach((oldInputId, output) -> {
			int newInputId = this.oldItemTypes[(Integer) oldInputId].id;

			ItemInstance result = (ItemInstance) output;
			int newResultId = this.oldItemTypes[result.itemId].id;

			// only remap if necessary
			if (result.itemId != newResultId) {
				result.itemId = newResultId;
			}

			newRecipes.put(newInputId, result);
		});

		((SmeltingRecipeSetter) smelting).setRecipes(newRecipes);

		RegistryRemapper.LOGGER.info("Remapping custom texture atlases.");
		AtlasMapper.onRegistryRemap(this.oldItemTypes);
	}

	@Override
	protected void addNewValues(List<Entry<Id, ItemType>> unmapped, CompoundTag tag) {
		int serialisedItemId = Tile.BY_ID.length;

		for (Entry<Id, ItemType> entry : unmapped) {
			ItemType value = entry.getValue();

			if (value instanceof TileItem || value instanceof PlaceableTileItem) {
				int tileId = ((HasParentId) value).getParentId();

				// readd to registry
				this.bySerialisedId.put(tileId, value);
				// add to tag
				tag.put(entry.getKey().toString(), tileId);
				this.onRemap(value, tileId);
			} else {
				while (this.bySerialisedId.get(serialisedItemId) != null) {
					++serialisedItemId;
				}

				// readd to registry
				this.bySerialisedId.put(serialisedItemId, value);
				// add to tag
				tag.put(entry.getKey().toString(), serialisedItemId);
				this.onRemap(value, serialisedItemId);
			}
		}
	}

	<I extends ItemType> I addTileItem(Id id, Tile tile, IntFunction<I> constructor) {
		I item = constructor.apply(tile.id - Tile.BY_ID.length);
		this.byRegistryId.put(id, item);
		this.bySerialisedId.put(item.id, item);
		RegistryImpl.T_2_TI.put(tile, item);
		return item;
	}
}