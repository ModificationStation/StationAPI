package net.modificationstation.stationapi.mixin.recipe;

import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.recipe.*;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.recipe.RecipeRegisterEvent;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.tag.TagKey;
import net.modificationstation.stationapi.impl.recipe.tag.ShapedTagRecipeAccessor;
import net.modificationstation.stationapi.impl.recipe.tag.TagConversionStorage;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.*;

import static net.modificationstation.stationapi.api.event.recipe.RecipeRegisterEvent.Vanilla.CRAFTING_SHAPED;
import static net.modificationstation.stationapi.api.event.recipe.RecipeRegisterEvent.Vanilla.CRAFTING_SHAPELESS;

@Mixin(RecipeRegistry.class)
public class MixinRecipeRegistry {

    @Unique
    private Object[] capturedRecipe;
    @Unique
    private int capturedItemIndex;

    @Mutable
    @Shadow
    @Final
    private static RecipeRegistry INSTANCE;

    @Shadow private List<Recipe> recipes;

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/util/Collections;sort(Ljava/util/List;Ljava/util/Comparator;)V"))
    private <T> void afterRecipeRegister(List<T> list, Comparator<? super T> c) {
        StationAPI.EVENT_BUS.post(RecipeRegisterEvent.builder().recipeId(CRAFTING_SHAPED.type()).build());
        StationAPI.EVENT_BUS.post(RecipeRegisterEvent.builder().recipeId(CRAFTING_SHAPELESS.type()).build());
        //noinspection Java8ListSort
        Collections.sort(list, c);
    }

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/recipe/ToolRecipes;register(Lnet/minecraft/recipe/RecipeRegistry;)V"))
    private void initTags(ToolRecipes toolRecipes, RecipeRegistry arg) {
        INSTANCE = (RecipeRegistry) (Object) this;
        TagConversionStorage.init();
        toolRecipes.register(arg);
    }

    /**
     * @author calmilamsy
     * @reason insane jank
     */
    @SuppressWarnings("OverwriteModifiers")
    @Overwrite
    public void addShapelessRecipe(ItemInstance output, Object... objects) {
        List<Object> var3 = new ArrayList<>();

        for (Object var7 : objects) {
            if (var7 instanceof ItemInstance item) {
                var3.add(item.copy());
            } else if (var7 instanceof ItemBase item) {
                var3.add(new ItemInstance(item));
            } else if (var7 instanceof BlockBase block) {
                var3.add(new ItemInstance(block));
            } else if (var7 instanceof Identifier id) {
                TagKey<ItemBase> tag = TagKey.of(ItemRegistry.INSTANCE.getKey(), id);
//                if (!ItemRegistry.INSTANCE.containsTag(tag)) {
//                    throw new RuntimeException("Identifier ingredient \"" + id + "\" has no entry in the tag registry!");
//                }
                var3.add(tag);
            } else {
                throw new RuntimeException("Invalid shapeless recipe ingredient of type " + var7.getClass().getName() + "!");
            }
        }

        this.recipes.add(new ShapelessRecipe(output, var3));
    }

    @Inject(method = "addShapedRecipe", at = @At(value = "HEAD"))
    private void hijackShapedRecipes(ItemInstance output, Object[] objects, CallbackInfo ci) {
//        TagConversionStorage.tagify(objects);
        capturedRecipe = objects;
    }

    @ModifyVariable(method = "addShapedRecipe", at = @At(value = "LOAD", ordinal = 6), index = 4)
    private int myLocalsNow(int var4) {
        capturedItemIndex = var4;
        return var4;
    }

    @Redirect(method = "addShapedRecipe", at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"))
    private Object hijackShapedRecipes2(Map<Object, Object> hashMap, Object key, Object value) {
        if (capturedRecipe[capturedItemIndex + 1] instanceof Identifier id) {
            return hashMap.put(key, id);
        }
        else {
            return hashMap.put(key, value);
        }
    }

    @Inject(method = "addShapedRecipe", at = @At(value = "CONSTANT", args = "intValue=0", ordinal = 4, shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void hijackShapedRecipes3(ItemInstance output, Object[] objects, CallbackInfo ci, String a, int itemIndex, int c, int d, HashMap<Object, Object> itemMap) {
        ShapedRecipe recipe = new ShapedRecipe(c, d, null, output);
        Object[] var14 = new Object[c * d];

        for(int var16 = 0; var16 < c * d; ++var16) {
            char var10 = a.charAt(var16);
            if (itemMap.containsKey(var10)) {
                Object item = itemMap.get(var10);
                if (item instanceof ItemInstance inst) {
                    var14[var16] = inst.copy();
                }
                else if (item instanceof Identifier id) {
                    TagKey<ItemBase> tag = TagKey.of(ItemRegistry.INSTANCE.getKey(), id);
//                    if (!ItemRegistry.INSTANCE.containsTag(tag)) {
//                        throw new RuntimeException("Identifier ingredient \"" + id + "\" has no entry in the tag registry!");
//                    }
                    var14[var16] = tag;
                }
            } else {
                var14[var16] = null;
            }
        }
        ((ShapedTagRecipeAccessor) recipe).setTaggedIngredients(var14);
        recipes.add(recipe);
        ci.cancel();
    }
}
