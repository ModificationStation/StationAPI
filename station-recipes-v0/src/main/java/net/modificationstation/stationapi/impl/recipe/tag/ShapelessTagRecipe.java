package net.modificationstation.stationapi.impl.recipe.tag;

import net.minecraft.block.BlockBase;
import net.minecraft.inventory.Crafting;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.recipe.Recipe;
import net.modificationstation.stationapi.api.recipe.StationRecipe;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.tags.TagRegistry;

import java.util.*;

public class ShapelessTagRecipe implements Recipe, StationRecipe {

    private final ItemInstance output;
    private final List<Object> input;

    public ShapelessTagRecipe(ItemInstance output, List<Object> inputs) {
        for (int i = 0; i <= inputs.size()-1; i++) {
            Object o = inputs.get(i);
            if (o instanceof BlockBase) {
                inputs.set(i, new ItemInstance((BlockBase) o));
            }
            else if (o instanceof ItemBase) {
                inputs.set(i, new ItemInstance((ItemBase) o));
            }
            else if (!(o instanceof ItemInstance) && !(o instanceof Identifier)) {
                throw new RuntimeException("Invalid shapeless OreDict recipe! Expected " + String.class.getName() + ", " + ItemInstance.class.getName() + ". Got: \"" + o.getClass().getName() + "\"");
            }
            if (o instanceof Identifier && !TagRegistry.INSTANCE.get((Identifier) o).isPresent()) {
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
                            if (TagRegistry.INSTANCE.tagMatches((Identifier) o, itemInCrafting)) {
                                System.out.println("Matches!");
                                var6 = true;
                                ingredients.remove(o);
                                break;
                            }
                            System.out.println("Nope!");
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

    @Override
    public ItemInstance[] getIngredients() {
        ItemInstance[] items = new ItemInstance[9];
        for (int i = 0; i < 8; i++) {
            Object ingredient = input.get(i);
            if (ingredient instanceof ItemInstance) {
                items[i] = (ItemInstance) ingredient;
            }
            else if (ingredient instanceof Identifier) {
                items[i] = TagRegistry.INSTANCE.get((Identifier) ingredient).orElseThrow(NullPointerException::new).get(0).displayItem.copy();
            }
        }
        return items;
    }

    @Override
    public ItemInstance[] getOutputs() {
        return new ItemInstance[]{output.copy()};
    }
}
