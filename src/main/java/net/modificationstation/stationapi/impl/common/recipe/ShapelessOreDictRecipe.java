package net.modificationstation.stationapi.impl.common.recipe;

import net.minecraft.inventory.Crafting;
import net.minecraft.item.ItemInstance;
import net.minecraft.recipe.Recipe;
import net.modificationstation.stationapi.api.common.util.OreDict;

import java.util.ArrayList;
import java.util.List;

public class ShapelessOreDictRecipe implements Recipe {

    private final ItemInstance output;
    private final List<?> input;

    public ShapelessOreDictRecipe(ItemInstance output, List<?> inputs) {
        for (Object o : inputs) {
            if (!(o instanceof ItemInstance) && !(o instanceof String)) {
                throw new RuntimeException("Invalid shapeless OreDict recipe! Expected " + String.class.getName() + ", " + ItemInstance.class.getName() + ". Got: \"" + o.getClass().getName() + "\"");
            }
            if (o instanceof String && !OreDict.INSTANCE.containsEntry((String) o)) {
                throw new RuntimeException("OreDict recipe has no candidates for input \"" + o + "\"!");
            }
        }
        this.output = output;
        this.input = inputs;
    }

    @Override
    public ItemInstance getOutput() {
        return this.output;
    }

    @Override
    public ItemInstance craft(Crafting arg) {
        return this.output.copy();
    }

    @Override
    public int getIngredientCount() {
        return this.input.size();
    }

    @Override
    public boolean canCraft(Crafting arg) {
        ArrayList<Object> ingredients = new ArrayList<>(input);

        for (int var3 = 0; var3 < 3; ++var3) {
            for (int var4 = 0; var4 < 3; ++var4) {
                ItemInstance itemInCrafting = arg.method_974(var4, var3);
                if (itemInCrafting != null) {
                    boolean var6 = false;

                    for (Object o : ingredients) {
                        if (o instanceof ItemInstance) {
                            ItemInstance var8 = (ItemInstance) o;
                            if (itemInCrafting.itemId == var8.itemId && (var8.getDamage() == -1 || itemInCrafting.getDamage() == var8.getDamage())) {
                                var6 = true;
                                ingredients.remove(o);
                                break;
                            }
                        } else {
                            if (OreDict.INSTANCE.matches((String) o, itemInCrafting)) {
                                var6 = true;
                                ingredients.remove(o);
                                break;
                            }
                        }
                    }

                    if (!var6) {
                        return false;
                    }
                }
            }
        }

        return ingredients.isEmpty();
    }
}
