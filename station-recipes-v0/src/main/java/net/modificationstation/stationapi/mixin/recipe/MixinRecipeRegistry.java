package net.modificationstation.stationapi.mixin.recipe;

import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.item.SecondaryBlock;
import net.minecraft.recipe.RecipeRegistry;
import net.minecraft.recipe.ToolRecipes;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.recipe.RecipeRegisterEvent;
import net.modificationstation.stationapi.api.recipe.CraftingRegistry;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.impl.recipe.SmeltingRegistryImpl;
import net.modificationstation.stationapi.mixin.tags.HahaThisIsMineNow;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.*;

import static net.modificationstation.stationapi.api.event.recipe.RecipeRegisterEvent.Vanilla.CRAFTING_SHAPED;
import static net.modificationstation.stationapi.api.event.recipe.RecipeRegisterEvent.Vanilla.CRAFTING_SHAPELESS;

@Mixin(RecipeRegistry.class)
public class MixinRecipeRegistry {

    @Mutable
    @Shadow
    @Final
    private static RecipeRegistry INSTANCE;

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/util/Collections;sort(Ljava/util/List;Ljava/util/Comparator;)V"))
    private <T> void afterRecipeRegister(List<T> list, Comparator<? super T> c) {
        StationAPI.EVENT_BUS.post(new RecipeRegisterEvent(CRAFTING_SHAPED.type()));
        StationAPI.EVENT_BUS.post(new RecipeRegisterEvent(CRAFTING_SHAPELESS.type()));
        //noinspection Java8ListSort
        Collections.sort(list, c);
    }

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/recipe/ToolRecipes;register(Lnet/minecraft/recipe/RecipeRegistry;)V"))
    private void initTags(ToolRecipes toolRecipes, RecipeRegistry arg) {
        INSTANCE = (RecipeRegistry) (Object) this;
        SmeltingRegistryImpl.CONVERSION_TABLE.put(getIdentifier(BlockBase.SUGAR_CANES), Identifier.of("blocks/plants/canes/sugar/"));
        SmeltingRegistryImpl.CONVERSION_TABLE.put(getIdentifier(ItemBase.paper), Identifier.of("items/paper/"));
        SmeltingRegistryImpl.CONVERSION_TABLE.put(getIdentifier(ItemBase.stick), Identifier.of("items/stick/wood/"));
        SmeltingRegistryImpl.CONVERSION_TABLE.put(getIdentifier(BlockBase.WOOD), Identifier.of("blocks/logs/wood/"));
        SmeltingRegistryImpl.CONVERSION_TABLE.put(getIdentifier(ItemBase.diamond), Identifier.of("items/minerals/diamond/"));
        SmeltingRegistryImpl.CONVERSION_TABLE.put(getIdentifier(ItemBase.redstoneDust), Identifier.of("items/minerals/redstone/"));
        SmeltingRegistryImpl.CONVERSION_TABLE.put(getIdentifier(ItemBase.book), Identifier.of("items/book/"));
        SmeltingRegistryImpl.CONVERSION_TABLE.put(getIdentifier(ItemBase.snowball), Identifier.of("items/snowball/"));
        SmeltingRegistryImpl.CONVERSION_TABLE.put(getIdentifier(ItemBase.clay), Identifier.of("items/clay/"));
        SmeltingRegistryImpl.CONVERSION_TABLE.put(getIdentifier(ItemBase.brick), Identifier.of("items/brick/"));
        SmeltingRegistryImpl.CONVERSION_TABLE.put(getIdentifier(ItemBase.glowstoneDust), Identifier.of("items/glowstone/"));
        SmeltingRegistryImpl.CONVERSION_TABLE.put(getIdentifier(ItemBase.string), Identifier.of("items/string/"));
        SmeltingRegistryImpl.CONVERSION_TABLE.put(getIdentifier(ItemBase.gunpowder), Identifier.of("items/gunpowder/"));
        SmeltingRegistryImpl.CONVERSION_TABLE.put(getIdentifier(BlockBase.SAND), Identifier.of("blocks/sand/"));
        SmeltingRegistryImpl.CONVERSION_TABLE.put(getIdentifier(BlockBase.COBBLESTONE), Identifier.of("blocks/cobblestone/"));
        SmeltingRegistryImpl.CONVERSION_TABLE.put(getIdentifier(BlockBase.STONE), Identifier.of("blocks/terrain/stone/"));
        SmeltingRegistryImpl.CONVERSION_TABLE.put(getIdentifier(BlockBase.SANDSTONE), Identifier.of("blocks/terrain/sandstone/"));
        SmeltingRegistryImpl.CONVERSION_TABLE.put(getIdentifier(ItemBase.ironIngot), Identifier.of("items/minerals/iron/"));
        SmeltingRegistryImpl.CONVERSION_TABLE.put(getIdentifier(ItemBase.milk), Identifier.of("items/tools/buckets/full/milk/"));
        SmeltingRegistryImpl.CONVERSION_TABLE.put(getIdentifier(ItemBase.sugar), Identifier.of("items/sugar/"));
        SmeltingRegistryImpl.CONVERSION_TABLE.put(getIdentifier(ItemBase.wheat), Identifier.of("items/wheat/"));
        SmeltingRegistryImpl.CONVERSION_TABLE.put(getIdentifier(ItemBase.egg), Identifier.of("items/egg/"));
        SmeltingRegistryImpl.CONVERSION_TABLE.put(getIdentifier(ItemBase.coal), Identifier.of("items/minerals/coal/"));
        SmeltingRegistryImpl.CONVERSION_TABLE.put(getIdentifier(ItemBase.goldIngot), Identifier.of("items/minerals/gold/"));
        SmeltingRegistryImpl.CONVERSION_TABLE.put(getIdentifier(BlockBase.WOODEN_PRESSURE_PLATE), Identifier.of("blocks/redstone/plates/wood/"));
        SmeltingRegistryImpl.CONVERSION_TABLE.put(getIdentifier(BlockBase.PUMPKIN), Identifier.of("blocks/plants/pumpkin/"));
        SmeltingRegistryImpl.CONVERSION_TABLE.put(getIdentifier(BlockBase.TORCH), Identifier.of("blocks/torch/"));
        SmeltingRegistryImpl.CONVERSION_TABLE.put(getIdentifier(BlockBase.CHEST), Identifier.of("blocks/chest/"));
        SmeltingRegistryImpl.CONVERSION_TABLE.put(getIdentifier(ItemBase.minecart), Identifier.of("items/minecarts"));
        SmeltingRegistryImpl.CONVERSION_TABLE.put(getIdentifier(BlockBase.FURNACE), Identifier.of("blocks/furnace/"));
        SmeltingRegistryImpl.CONVERSION_TABLE.put(getIdentifier(ItemBase.flint), Identifier.of("items/flint/"));
        SmeltingRegistryImpl.CONVERSION_TABLE.put(getIdentifier(BlockBase.GOLD_BLOCK), Identifier.of("blocks/minerals/gold/"));
        SmeltingRegistryImpl.CONVERSION_TABLE.put(getIdentifier(ItemBase.apple), Identifier.of("items/foods/apple/"));
        SmeltingRegistryImpl.CONVERSION_TABLE.put(getIdentifier(BlockBase.WOOL), Identifier.of("blocks/wool/"));
        SmeltingRegistryImpl.CONVERSION_TABLE.put(getIdentifier(BlockBase.REDSTONE_TORCH_LIT), Identifier.of("blocks/redstone/torch/"));
        SmeltingRegistryImpl.CONVERSION_TABLE.put(getIdentifier(ItemBase.compass), Identifier.of("items/tools/compass/"));
        SmeltingRegistryImpl.CONVERSION_TABLE.put(getIdentifier(ItemBase.bow), Identifier.of("items/tools/bow/"));
        SmeltingRegistryImpl.CONVERSION_TABLE.put(getIdentifier(ItemBase.slimeball), Identifier.of("items/slime/"));
        SmeltingRegistryImpl.CONVERSION_TABLE.put(getIdentifier(BlockBase.PISTON), Identifier.of("blocks/redstone/pistons"));
        toolRecipes.register(arg);
    }

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/recipe/RecipeRegistry;addShapedRecipe(Lnet/minecraft/item/ItemInstance;[Ljava/lang/Object;)V"))
    private void tagify(RecipeRegistry recipeRegistry, ItemInstance output, Object[] objects) {
        for (int i = 0; i < objects.length; i++) {
            Object o = objects[i];
            System.out.println(o.getClass());
            if (o instanceof ItemInstance) {
                return;
            }
            else if (o instanceof SecondaryBlock) {
                System.out.println(getIdentifier(BlockBase.BY_ID[((HahaThisIsMineNow) o).getTileId()]));
                objects[i] = SmeltingRegistryImpl.CONVERSION_TABLE.get(getIdentifier(BlockBase.BY_ID[((HahaThisIsMineNow) o).getTileId()]));
            }
            else if (o instanceof ItemBase || o instanceof BlockBase) {
                objects[i] = SmeltingRegistryImpl.CONVERSION_TABLE.get(getIdentifier(o));
            }
        }
        CraftingRegistry.addShapedOreDictRecipe(output, objects);
    }

    private static Identifier getIdentifier(Object o) {
        if (o instanceof BlockBase) {
            return BlockRegistry.INSTANCE.getIdentifier((BlockBase) o);
        }
        else {
            return ItemRegistry.INSTANCE.getIdentifier((ItemBase) o);
        }
    }
}
