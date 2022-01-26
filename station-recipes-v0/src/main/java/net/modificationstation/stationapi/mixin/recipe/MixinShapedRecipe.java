package net.modificationstation.stationapi.mixin.recipe;

import net.minecraft.inventory.Crafting;
import net.minecraft.item.ItemInstance;
import net.minecraft.recipe.ShapedRecipe;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.recipe.StationRecipe;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.tags.TagEntry;
import net.modificationstation.stationapi.api.tags.TagRegistry;
import net.modificationstation.stationapi.impl.recipe.tag.ShapedTagRecipeAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Mixin(ShapedRecipe.class)
public class MixinShapedRecipe implements ShapedTagRecipeAccessor, StationRecipe {

    @Shadow private int width;
    @Shadow private int height;
    @Shadow private ItemInstance output;
    private Object[] taggedIngredients = new Object[]{};
    private static final Random RANDOM = new Random();

    @Override
    public void setTaggedIngredients(Object[] taggedIngredients) {
        this.taggedIngredients = taggedIngredients;
    }

    /**
     * @author I made a huge mistake
     */
    @Overwrite
    private boolean matchesSmall(Crafting arg, int i, int j, boolean flag) {
        for(int var5 = 0; var5 < 3; ++var5) {
            for(int var6 = 0; var6 < 3; ++var6) {
                int var7 = var5 - i;
                int var8 = var6 - j;
                Object var9 = null;
                if (var7 >= 0 && var8 >= 0 && var7 < this.width && var8 < this.height) {
                    if (flag) {
                        var9 = this.taggedIngredients[this.width - var7 - 1 + var8 * this.width];
                    } else {
                        var9 = this.taggedIngredients[var7 + var8 * this.width];
                    }
                }

                ItemInstance var10 = arg.method_974(var5, var6);
                if (var10 != null || var9 != null) {
                    if (var10 == null && var9 != null || var10 != null && var9 == null) {
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
                    else if (!TagRegistry.INSTANCE.tagMatches((Identifier) var9, var10)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    @Override
    public ItemInstance[] getIngredients() {
        List<ItemInstance> itemInstances = new ArrayList<>();
        Arrays.asList(taggedIngredients).forEach(entry -> {
            if (entry instanceof Identifier) {
                List<TagEntry> tagEntries = TagRegistry.INSTANCE.get((Identifier) entry).orElseThrow(() -> new RuntimeException("Identifier ingredient \"" + entry.toString() + "\" has no entry in the tag registry!"));
                itemInstances.add(tagEntries.get(RANDOM.nextInt(tagEntries.size())).displayItem);
            }
            else {
                itemInstances.add((ItemInstance) entry);
            }
        });
        return itemInstances.toArray(new ItemInstance[]{});
    }

    @Override
    public ItemInstance[] getOutputs() {
        return new ItemInstance[]{output};
    }
}
