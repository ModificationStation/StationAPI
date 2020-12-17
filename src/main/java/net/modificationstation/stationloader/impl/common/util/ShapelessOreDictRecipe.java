package net.modificationstation.stationloader.impl.common.util;

import net.minecraft.inventory.Crafting;
import net.minecraft.item.ItemInstance;
import net.minecraft.recipe.ShapelessRecipe;
import net.modificationstation.stationloader.mixin.common.accessor.ShapelessRecipeAccessor;

import java.util.ArrayList;
import java.util.List;

public class ShapelessOreDictRecipe extends ShapelessRecipe {
    public ShapelessOreDictRecipe(ItemInstance output, List<?> inputs) {
        super(output, inputs);
        if (inputs.stream().anyMatch( o -> o instanceof String && !OreDict.INSTANCE.containsEntry((String) o))) {
            throw new RuntimeException("OreDict recipe has no candidates for input!");
        }
    }

    @Override
    public boolean canCraft(Crafting arg) {
        ArrayList<?> var2 = new ArrayList<>(((ShapelessRecipeAccessor) this).getInput());

        for(int var3 = 0; var3 < 3; ++var3) {
            for(int var4 = 0; var4 < 3; ++var4) {
                ItemInstance var5 = arg.method_974(var4, var3);
                if (var5 != null) {
                    boolean var6 = false;

                    for (Object o : var2) {
                        if (o instanceof ItemInstance) {
                            ItemInstance var8 = (ItemInstance) o;
                            if (var5.itemId == var8.itemId && (var8.getDamage() == -1 || var5.getDamage() == var8.getDamage())) {
                                var6 = true;
                                var2.remove(o);
                                break;
                            }
                        }
                        else //noinspection ConstantConditions kill the game if someone manages to nullpointer this.
                            if (OreDict.INSTANCE.getOreDictEntryObjects((String) o).stream().anyMatch(oreDictEntryObject -> oreDictEntryObject.itemInstancePredicate.test(var5))) {
                            var6 = true;
                            var2.remove(o);
                            break;
                        }
                    }

                    if (!var6) {
                        return false;
                    }
                }
            }
        }

        return var2.isEmpty();
    }
}
