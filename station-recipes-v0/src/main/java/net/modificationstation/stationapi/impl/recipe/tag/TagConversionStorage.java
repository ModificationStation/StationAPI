package net.modificationstation.stationapi.impl.recipe.tag;

import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.item.SecondaryBlock;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.mixin.recipe.SecondaryBlockAccessor;
import uk.co.benjiweber.expressions.tuple.BiTuple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;

public class TagConversionStorage {

    public static final HashMap<BiTuple<Identifier, Integer>, Identifier> CONVERSION_TABLE = new HashMap<>();

    private static ArrayList<String> spammedNames = new ArrayList<>();

    public static void init() {
        CONVERSION_TABLE.put(getIdentifier(BlockBase.SUGAR_CANES), Identifier.of("blocks/plants/canes/sugar/"));
        CONVERSION_TABLE.put(getIdentifier(ItemBase.paper), Identifier.of("items/paper/"));
        CONVERSION_TABLE.put(getIdentifier(ItemBase.stick), Identifier.of("items/sticks/wood/"));
        CONVERSION_TABLE.put(getIdentifier(BlockBase.LOG), Identifier.of("blocks/logs/wood/"));
        CONVERSION_TABLE.put(getIdentifier(BlockBase.WOOD), Identifier.of("blocks/planks/wood/"));
        CONVERSION_TABLE.put(getIdentifier(ItemBase.diamond), Identifier.of("items/minerals/diamond/"));
        CONVERSION_TABLE.put(getIdentifier(ItemBase.redstoneDust), Identifier.of("items/minerals/redstone/"));
        CONVERSION_TABLE.put(getIdentifier(ItemBase.book), Identifier.of("items/book/"));
        CONVERSION_TABLE.put(getIdentifier(ItemBase.snowball), Identifier.of("items/snowball/"));
        CONVERSION_TABLE.put(getIdentifier(ItemBase.clay), Identifier.of("items/clay/"));
        CONVERSION_TABLE.put(getIdentifier(ItemBase.brick), Identifier.of("items/brick/"));
        CONVERSION_TABLE.put(getIdentifier(ItemBase.glowstoneDust), Identifier.of("items/glowstone/"));
        CONVERSION_TABLE.put(getIdentifier(ItemBase.string), Identifier.of("items/string/"));
        CONVERSION_TABLE.put(getIdentifier(ItemBase.gunpowder), Identifier.of("items/gunpowder/"));
        CONVERSION_TABLE.put(getIdentifier(BlockBase.SAND), Identifier.of("blocks/sand/"));
        CONVERSION_TABLE.put(getIdentifier(BlockBase.COBBLESTONE), Identifier.of("blocks/cobblestone/"));
        CONVERSION_TABLE.put(getIdentifier(BlockBase.STONE), Identifier.of("blocks/terrain/stone/"));
        CONVERSION_TABLE.put(getIdentifier(BlockBase.SANDSTONE), Identifier.of("blocks/terrain/sandstone/"));
        CONVERSION_TABLE.put(getIdentifier(ItemBase.ironIngot), Identifier.of("items/minerals/iron/"));
        CONVERSION_TABLE.put(getIdentifier(ItemBase.milk), Identifier.of("items/tools/buckets/full/milk/"));
        CONVERSION_TABLE.put(getIdentifier(ItemBase.sugar), Identifier.of("items/sugar/"));
        CONVERSION_TABLE.put(getIdentifier(ItemBase.wheat), Identifier.of("items/wheat/"));
        CONVERSION_TABLE.put(getIdentifier(ItemBase.egg), Identifier.of("items/egg/"));
        CONVERSION_TABLE.put(getIdentifier(ItemBase.coal), Identifier.of("items/minerals/coal"));
        CONVERSION_TABLE.put(getIdentifier(ItemBase.coal, 1), Identifier.of("items/minerals/coal/charcoal"));
        CONVERSION_TABLE.put(getIdentifier(ItemBase.goldIngot), Identifier.of("items/minerals/gold/"));
        CONVERSION_TABLE.put(getIdentifier(BlockBase.WOODEN_PRESSURE_PLATE), Identifier.of("blocks/redstone/plates/wood/"));
        CONVERSION_TABLE.put(getIdentifier(BlockBase.PUMPKIN), Identifier.of("blocks/plants/pumpkin/"));
        CONVERSION_TABLE.put(getIdentifier(BlockBase.TORCH), Identifier.of("blocks/torch/"));
        CONVERSION_TABLE.put(getIdentifier(BlockBase.CHEST), Identifier.of("blocks/chest/"));
        CONVERSION_TABLE.put(getIdentifier(ItemBase.minecart), Identifier.of("items/minecarts"));
        CONVERSION_TABLE.put(getIdentifier(BlockBase.FURNACE), Identifier.of("blocks/furnace/"));
        CONVERSION_TABLE.put(getIdentifier(ItemBase.flint), Identifier.of("items/flint/"));
        CONVERSION_TABLE.put(getIdentifier(BlockBase.GOLD_BLOCK), Identifier.of("blocks/minerals/gold/"));
        CONVERSION_TABLE.put(getIdentifier(ItemBase.apple), Identifier.of("items/foods/apple/"));
        CONVERSION_TABLE.put(getIdentifier(BlockBase.WOOL), Identifier.of("blocks/wool/"));
        CONVERSION_TABLE.put(getIdentifier(BlockBase.REDSTONE_TORCH_LIT), Identifier.of("blocks/redstone/torch/"));
        CONVERSION_TABLE.put(getIdentifier(ItemBase.compass), Identifier.of("items/tools/compass/"));
        CONVERSION_TABLE.put(getIdentifier(ItemBase.bow), Identifier.of("items/tools/bow/"));
        CONVERSION_TABLE.put(getIdentifier(ItemBase.slimeball), Identifier.of("items/slime/"));
        CONVERSION_TABLE.put(getIdentifier(BlockBase.PISTON), Identifier.of("blocks/machines/pistons"));
        CONVERSION_TABLE.put(getIdentifier(BlockBase.IRON_ORE), Identifier.of("blocks/ores/iron"));
        CONVERSION_TABLE.put(getIdentifier(BlockBase.GOLD_ORE), Identifier.of("blocks/ores/gold"));
        CONVERSION_TABLE.put(getIdentifier(BlockBase.DIAMOND_ORE), Identifier.of("blocks/ores/diamond"));
        CONVERSION_TABLE.put(getIdentifier(ItemBase.rawPorkchop), Identifier.of("items/foods/porkchop/raw"));
        CONVERSION_TABLE.put(getIdentifier(ItemBase.rawFish), Identifier.of("items/foods/fish/raw"));
        CONVERSION_TABLE.put(getIdentifier(ItemBase.clay), Identifier.of("items/clay"));
        CONVERSION_TABLE.put(getIdentifier(ItemBase.leather), Identifier.of("items/leather"));
        CONVERSION_TABLE.put(getIdentifier(ItemBase.feather), Identifier.of("items/feather"));
        CONVERSION_TABLE.put(getIdentifier(ItemBase.bone), Identifier.of("items/bone"));
        CONVERSION_TABLE.put(getIdentifier(ItemBase.bowl), Identifier.of("items/bowls/wood"));
        CONVERSION_TABLE.put(getIdentifier(ItemBase.dyePowder, 4), Identifier.of("items/minerals/lapis"));
        CONVERSION_TABLE.put(getIdentifier(ItemBase.dyePowder, 3), Identifier.of("items/cocoa"));
        CONVERSION_TABLE.put(getIdentifier(BlockBase.CACTUS), Identifier.of("blocks/plants/cactus"));
        CONVERSION_TABLE.put(getIdentifier(BlockBase.RED_MUSHROOM), Identifier.of("blocks/plants/mushrooms/red"));
        CONVERSION_TABLE.put(getIdentifier(BlockBase.BROWN_MUSHROOM), Identifier.of("blocks/plants/mushrooms/brown"));
        CONVERSION_TABLE.put(getIdentifier(BlockBase.ROSE), Identifier.of("blocks/plants/flowers/rose"));
        CONVERSION_TABLE.put(getIdentifier(BlockBase.DANDELION), Identifier.of("blocks/plants/flowers/dandelion"));
        CONVERSION_TABLE.put(getIdentifier(BlockBase.IRON_BLOCK), Identifier.of("blocks/minerals/iron"));
        CONVERSION_TABLE.put(getIdentifier(BlockBase.DIAMOND_BLOCK), Identifier.of("blocks/minerals/diamond"));
        CONVERSION_TABLE.put(getIdentifier(BlockBase.LAPIS_LAZULI_BLOCK), Identifier.of("blocks/minerals/lapis"));
        CONVERSION_TABLE.put(getIdentifier(BlockBase.LOG), Identifier.of("blocks/logs/wood/"));
    }

    public static void tagify(Object[] objects) {
        for (int i = 0; i < objects.length; i++) {
            Object object = objects[i];
            if (object instanceof ItemInstance) {
                Identifier identifier = CONVERSION_TABLE.get(getIdentifier(((ItemInstance) object).getType(), ((ItemInstance) object).getDamage()));
                if (identifier != null) {
                    objects[i] = identifier;
                }
                else System.out.println(((ItemInstance) object).getTranslationKey());
            }
            else if (object instanceof SecondaryBlock) {
                Identifier identifier = CONVERSION_TABLE.get(getIdentifier(BlockBase.BY_ID[((SecondaryBlockAccessor) object).getTileId()]));
                if (identifier != null) {
                    objects[i] = identifier;
                }
            }
            else if (object instanceof ItemBase || object instanceof BlockBase) {
                Identifier identifier = CONVERSION_TABLE.get(getIdentifier(object));
                if (identifier != null) {
                    objects[i] = identifier;
                }
            }
        }
    }

    private static BiTuple<Identifier, Integer> getIdentifier(Object o, int meta) {
        if (o instanceof BlockBase) {
            return BiTuple.of(BlockRegistry.INSTANCE.getIdentifier((BlockBase) o), meta);
        }
        else {
            return BiTuple.of(ItemRegistry.INSTANCE.getIdentifier((ItemBase) o), meta);
        }
    }

    private static BiTuple<Identifier, Integer> getIdentifier(Object o) {
        return getIdentifier(o, 0);
    }

}
