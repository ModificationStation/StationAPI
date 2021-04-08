package net.modificationstation.stationapi.impl.recipe.oredict;

import net.minecraft.block.BlockBase;
import net.minecraft.inventory.Crafting;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.recipe.Recipe;
import net.modificationstation.stationapi.api.oredict.OreDict;

import java.util.*;

public class ShapedOreDictRecipe implements Recipe {
    public final int outputId;
    private final int width;
    private final int height;
    private final Object[] ingredients;
    private final ItemInstance output;

    public ShapedOreDictRecipe(ItemInstance output, List<?> objects) {
        StringBuilder joinedRecipeString = new StringBuilder();
        int arrayIndex = 0;
        int recipeStringLength = 0;
        int recipeStringHeight = 0;
        if (objects.get(arrayIndex) instanceof String[]) {
            String[] recipeStrings = (String[]) objects.get(arrayIndex);

            for (String recipeString : recipeStrings) {
                ++recipeStringHeight;
                recipeStringLength = recipeString.length();
                joinedRecipeString.append(recipeString);
            }
        } else {
            while(objects.get(arrayIndex) instanceof String) {
                String recipeString = (String)objects.get(arrayIndex++);
                ++recipeStringHeight;
                recipeStringLength = recipeString.length();
                joinedRecipeString.append(recipeString);
                System.out.println(recipeString);
            }
        }

        HashMap<Character, Object> characterToIngredient;

        for(characterToIngredient = new HashMap<>(); arrayIndex < objects.size(); arrayIndex += 2) {
            Character recipeCharacter = (Character)objects.get(arrayIndex);
            Object ingredientToAdd = null;
            if (objects.get(arrayIndex + 1) instanceof ItemBase) {
                ingredientToAdd = new ItemInstance((ItemBase)objects.get(arrayIndex + 1));
            } else if (objects.get(arrayIndex + 1) instanceof BlockBase) {
                ingredientToAdd = new ItemInstance((BlockBase)objects.get(arrayIndex + 1), 1, -1);
            } else if (objects.get(arrayIndex + 1) instanceof ItemInstance || objects.get(arrayIndex + 1) instanceof String) {
                ingredientToAdd = objects.get(arrayIndex + 1);
            }

            characterToIngredient.put(recipeCharacter, ingredientToAdd);
            System.out.println(characterToIngredient);
        }

        Object[] ingredients = new Object[recipeStringLength * recipeStringHeight];

        for(int characterPosition = 0; characterPosition < recipeStringLength * recipeStringHeight; ++characterPosition) {
            char character = joinedRecipeString.charAt(characterPosition);
            if (characterToIngredient.containsKey(character)) {
                Object ingredient = characterToIngredient.get(character);
                if (ingredient instanceof ItemInstance)
                    ingredients[characterPosition] = ((ItemInstance) ingredient).copy();
                else if (ingredient instanceof String) {
                    ingredients[characterPosition] = ingredient;
                }
            } else {
                ingredients[characterPosition] = null;
            }
        }

        this.outputId = output.itemId;
        this.width = recipeStringLength;
        this.height = recipeStringHeight;
        this.ingredients = ingredients;
        System.out.println(Arrays.toString(this.ingredients));
        this.output = output;
    }

    @Override
    public ItemInstance getOutput() {
        return this.output;
    }

    @Override
    public boolean canCraft(Crafting arg) {
        for (int var2 = 0; var2 <= 3 - this.width; ++var2) {
            for (int var3 = 0; var3 <= 3 - this.height; ++var3) {
                if (this.matches(arg, var2, var3, true)) {
                    return true;
                }

                if (this.matches(arg, var2, var3, false)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean matches(Crafting arg, int i, int j, boolean isSmall) {
        for (int var5 = 0; var5 < 3; ++var5) {
            for (int var6 = 0; var6 < 3; ++var6) {
                int var7 = var5 - i;
                int var8 = var6 - j;
                Object var9 = null;
                if (var7 >= 0 && var8 >= 0 && var7 < this.width && var8 < this.height) {
                    if (isSmall) {
                        var9 = this.ingredients[this.width - var7 - 1 + var8 * this.width];
                    } else {
                        var9 = this.ingredients[var7 + var8 * this.width];
                    }
                }

                ItemInstance var10 = arg.method_974(var5, var6);
                if (var10 == null || var9 == null) {
                    return false;
                }

                if (var9 instanceof ItemInstance) {
                    if (((ItemInstance) var9).itemId != var10.itemId) {
                        return false;
                    }

                    if (((ItemInstance) var9).getDamage() != -1 && ((ItemInstance) var9).getDamage() != var10.getDamage()) {
                        return false;
                    }
                }
                else {
                    String var11 = (String) var9;
                    System.out.println(var11);
                    if (!OreDict.INSTANCE.matches(var11, var10)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    @Override
    public ItemInstance craft(Crafting arg) {
        return new ItemInstance(this.output.itemId, this.output.count, this.output.getDamage());
    }

    @Override
    public int getIngredientCount() {
        return this.width * this.height;
    }
}
