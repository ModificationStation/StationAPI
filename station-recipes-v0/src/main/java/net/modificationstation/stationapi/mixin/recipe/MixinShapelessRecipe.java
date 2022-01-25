package net.modificationstation.stationapi.mixin.recipe;

import net.minecraft.inventory.Crafting;
import net.minecraft.item.ItemInstance;
import net.minecraft.recipe.ShapelessRecipe;
import net.modificationstation.stationapi.api.recipe.StationRecipe;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.tags.TagRegistry;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Mixin(ShapelessRecipe.class)
public class MixinShapelessRecipe implements StationRecipe {

    @Shadow @Final private List input;

    @Shadow @Final private ItemInstance output;

    /**
     * @author calmilamsy
     */
    @Overwrite
    public boolean canCraft(Crafting arg) {
        ArrayList var2 = new ArrayList(this.input);

        for(int var3 = 0; var3 < 3; ++var3) {
            for(int var4 = 0; var4 < 3; ++var4) {
                ItemInstance var5 = arg.method_974(var4, var3);
                if (var5 != null) {
                    boolean var6 = false;

                    for (Object var8 : var2) {
                        if (var8 instanceof Identifier && TagRegistry.INSTANCE.tagMatches((Identifier) var8, var5)) {
                            var6 = true;
                            var2.remove(var8);
                            break;
                        }
                        if (var8 instanceof ItemInstance && (var5.itemId == ((ItemInstance) var8).itemId && (((ItemInstance) var8).getDamage() == -1 || var5.getDamage() == ((ItemInstance) var8).getDamage()))) {
                            var6 = true;
                            var2.remove(var8);
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
        input.forEach(entry -> {
            if (entry instanceof Identifier) {
                itemInstances.add(TagRegistry.INSTANCE.get((Identifier) entry).get().get(0).displayItem);
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
