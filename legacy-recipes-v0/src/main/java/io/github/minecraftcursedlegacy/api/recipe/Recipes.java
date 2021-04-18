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

package io.github.minecraftcursedlegacy.api.recipe;

import io.github.minecraftcursedlegacy.accessor.recipe.AccessorRecipeRegistry;
import net.minecraft.item.ItemInstance;
import net.minecraft.recipe.DyeRecipes;
import net.minecraft.recipe.RecipeRegistry;

/**
 * Class that acts as an interface for the recipe registry.
 */
public final class Recipes {
	private Recipes() {
		// NO-OP
	}

	/**
	 * Adds a shaped recipe to the game.
	 * For Example, the vanilla cake recipe:<br/>&nbsp;<b>{@code Recipes.addShapedRecipe(new ItemInstance(ItemType.cake, 1), "AAA", "BEB", "CCC", 'A', ItemType.milk, 'B', ItemType.sugar, 'C', ItemType.wheat, 'E', ItemType.egg);}</b>
	 * @param result the item instance the recipe crafts.
	 * @param recipe the recipe, shaped.
	 * @see the code in {@link RecipeRegistry} for more examples on how to use this.
	 */
	public static void addShapedRecipe(ItemInstance result, Object... recipe) {
		((AccessorRecipeRegistry) RecipeRegistry.getInstance()).invokeAddShapedRecipe(result, recipe);
	}

	/**
	 * Adds a shapeless recipe to the game.
	 * For Example, the vanilla bonemeal recipe:<br/>&nbsp<b>{@code arg.addShapelessRecipe(new ItemInstance(ItemType.dyePowder, 3, 15), ItemType.bone);}</b>
	 * @param result the item instance the recipe crafts.
	 * @param ingredients a list of various item instances, item types, and tiles which are the required ingredients of this recipe.
	 * @see the code in {@link DyeRecipes} for more examples on how to use this.
	 */
	public static void addShapelessRecipe(ItemInstance result, Object... ingredients) {
		((AccessorRecipeRegistry) RecipeRegistry.getInstance()).invokeAddShapelessRecipe(result, ingredients);
	}
}
