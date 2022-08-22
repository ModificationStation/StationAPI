package net.modificationstation.stationapi.mixin.recipe;

import net.minecraft.inventory.Crafting;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.recipe.ShapelessRecipe;
import net.modificationstation.stationapi.api.item.StationItemStack;
import net.modificationstation.stationapi.api.recipe.StationRecipe;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.registry.RegistryEntryList;
import net.modificationstation.stationapi.api.tag.TagKey;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Mixin(ShapelessRecipe.class)
public class MixinShapelessRecipe implements StationRecipe {

    @SuppressWarnings("rawtypes")
    @Shadow @Final private List input;

    @Shadow @Final private ItemInstance output;

    private static final Random RANDOM = new Random();

    /**
     * @author calmilamsy
     * @reason need to insert a break statement.
     */
    @Overwrite
    public boolean canCraft(Crafting arg) {
        ArrayList<Object> var2 = new ArrayList<Object>(this.input);

        for(int var3 = 0; var3 < 3; ++var3) {
            for(int var4 = 0; var4 < 3; ++var4) {
                ItemInstance var5 = arg.getInventoryItemXY(var4, var3);
                if (var5 != null) {
                    boolean var6 = false;

                    for (Object var8 : var2) {
                        //noinspection unchecked
                        if (var8 instanceof TagKey<?> tag && StationItemStack.class.cast(var5).isIn((TagKey<ItemBase>) tag)) {
                            var6 = true;
                            var2.remove(tag);
                            break;
                        }
                        if (var8 instanceof ItemInstance item && (var5.itemId == item.itemId && (item.getDamage() == -1 || var5.getDamage() == item.getDamage()))) {
                            var6 = true;
                            var2.remove(item);
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

    @Override
    public ItemInstance[] getIngredients() {
        List<ItemInstance> itemInstances = new ArrayList<>();
        //noinspection unchecked
        input.forEach(entry -> {
            if (entry instanceof TagKey<?> tag) {
                //noinspection unchecked
                TagKey<ItemBase> itemTag = (TagKey<ItemBase>) tag;
                RegistryEntryList.Named<ItemBase> tagEntries = ItemRegistry.INSTANCE.getEntryList(itemTag).orElseThrow(() -> new RuntimeException("Identifier ingredient \"" + tag.id() + "\" has no entry in the tag registry!"));
                itemInstances.add(new ItemInstance(tagEntries.getRandom(RANDOM).orElseThrow().value()));
            }
            else {
                itemInstances.add((ItemInstance) entry);
            }
        });
        return itemInstances.toArray(new ItemInstance[0]);
    }

    @Override
    public ItemInstance[] getOutputs() {
        return new ItemInstance[]{output};
    }
}
