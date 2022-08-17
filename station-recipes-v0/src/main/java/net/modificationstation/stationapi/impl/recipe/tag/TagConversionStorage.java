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

// TODO: replace in vanillafix by actually changing recipes
public class TagConversionStorage {

    public static final HashMap<BiTuple<Identifier, Integer>, Identifier> CONVERSION_TABLE = new HashMap<>();

    private static ArrayList<String> spammedNames = new ArrayList<>();

    public static void init() {
//        CONVERSION_TABLE.put(getIdentifier(BlockBase.SUGAR_CANES), Identifier.of("canes/sugar"));
//        CONVERSION_TABLE.put(getIdentifier(ItemBase.paper), Identifier.of("papers"));
//        CONVERSION_TABLE.put(getIdentifier(ItemBase.stick), Identifier.of("sticks/"));
//        CONVERSION_TABLE.put(getIdentifier(BlockBase.LOG), Identifier.of("logs/"));
//        CONVERSION_TABLE.put(getIdentifier(BlockBase.WOOD), Identifier.of("planks/"));
//        CONVERSION_TABLE.put(getIdentifier(ItemBase.diamond), Identifier.of("gems/diamond"));
//        CONVERSION_TABLE.put(getIdentifier(ItemBase.redstoneDust), Identifier.of("dusts/redstone"));
//        CONVERSION_TABLE.put(getIdentifier(ItemBase.book), Identifier.of("books"));
//        CONVERSION_TABLE.put(getIdentifier(ItemBase.snowball), Identifier.of("snowballs"));
//        CONVERSION_TABLE.put(getIdentifier(ItemBase.clay), Identifier.of("clayballs"));
//        CONVERSION_TABLE.put(getIdentifier(ItemBase.brick), Identifier.of("ingots/brick"));
//        CONVERSION_TABLE.put(getIdentifier(ItemBase.glowstoneDust), Identifier.of("dusts/glowstone"));
//        CONVERSION_TABLE.put(getIdentifier(ItemBase.string), Identifier.of("strings"));
//        CONVERSION_TABLE.put(getIdentifier(ItemBase.gunpowder), Identifier.of("gunpowders"));
//        CONVERSION_TABLE.put(getIdentifier(BlockBase.SAND), Identifier.of("sands"));
//        CONVERSION_TABLE.put(getIdentifier(BlockBase.COBBLESTONE), Identifier.of("cobblestones"));
//        CONVERSION_TABLE.put(getIdentifier(BlockBase.STONE), Identifier.of("stones"));
//        CONVERSION_TABLE.put(getIdentifier(BlockBase.SANDSTONE), Identifier.of("sandstones"));
//        CONVERSION_TABLE.put(getIdentifier(ItemBase.ironIngot), Identifier.of("ingots/iron"));
//        CONVERSION_TABLE.put(getIdentifier(ItemBase.milk), Identifier.of("tools/buckets/full/milk"));
//        CONVERSION_TABLE.put(getIdentifier(ItemBase.sugar), Identifier.of("sugars"));
//        CONVERSION_TABLE.put(getIdentifier(ItemBase.wheat), Identifier.of("wheats"));
//        CONVERSION_TABLE.put(getIdentifier(ItemBase.egg), Identifier.of("eggs"));
//        CONVERSION_TABLE.put(getIdentifier(ItemBase.coal), Identifier.of("coals"));
//        CONVERSION_TABLE.put(getIdentifier(ItemBase.coal, 1), Identifier.of("coals/charcoal"));
//        CONVERSION_TABLE.put(getIdentifier(ItemBase.goldIngot), Identifier.of("ingots/gold"));
//        CONVERSION_TABLE.put(getIdentifier(BlockBase.WOODEN_PRESSURE_PLATE), Identifier.of("pressure_plates/wood"));
//        CONVERSION_TABLE.put(getIdentifier(BlockBase.PUMPKIN), Identifier.of("pumpkins"));
//        CONVERSION_TABLE.put(getIdentifier(BlockBase.TORCH), Identifier.of("torches"));
//        CONVERSION_TABLE.put(getIdentifier(BlockBase.CHEST), Identifier.of("chests"));
//        CONVERSION_TABLE.put(getIdentifier(ItemBase.minecart), Identifier.of("minecarts"));
//        CONVERSION_TABLE.put(getIdentifier(BlockBase.FURNACE), Identifier.of("furnaces/"));
//        CONVERSION_TABLE.put(getIdentifier(ItemBase.flint), Identifier.of("flints"));
//        CONVERSION_TABLE.put(getIdentifier(BlockBase.GOLD_BLOCK), Identifier.of("storage_blocks/gold"));
//        CONVERSION_TABLE.put(getIdentifier(ItemBase.apple), Identifier.of("foods/apple"));
//        CONVERSION_TABLE.put(getIdentifier(BlockBase.WOOL), Identifier.of("wools/"));
//        CONVERSION_TABLE.put(getIdentifier(BlockBase.REDSTONE_TORCH_LIT), Identifier.of("redstone_torches/on"));
//        CONVERSION_TABLE.put(getIdentifier(ItemBase.compass), Identifier.of("compasses"));
//        CONVERSION_TABLE.put(getIdentifier(ItemBase.bow), Identifier.of("tools/bows"));
//        CONVERSION_TABLE.put(getIdentifier(ItemBase.slimeball), Identifier.of("slimeballs"));
//        CONVERSION_TABLE.put(getIdentifier(BlockBase.PISTON), Identifier.of("pistons/normal"));
//        CONVERSION_TABLE.put(getIdentifier(BlockBase.IRON_ORE), Identifier.of("ores/iron"));
//        CONVERSION_TABLE.put(getIdentifier(BlockBase.GOLD_ORE), Identifier.of("ores/gold"));
//        CONVERSION_TABLE.put(getIdentifier(BlockBase.DIAMOND_ORE), Identifier.of("ores/diamond"));
//        CONVERSION_TABLE.put(getIdentifier(ItemBase.rawPorkchop), Identifier.of("foods/porkchop/raw"));
//        CONVERSION_TABLE.put(getIdentifier(ItemBase.rawFish), Identifier.of("foods/fish/raw"));
//        CONVERSION_TABLE.put(getIdentifier(ItemBase.clay), Identifier.of("clayballs"));
//        CONVERSION_TABLE.put(getIdentifier(ItemBase.leather), Identifier.of("leathers"));
//        CONVERSION_TABLE.put(getIdentifier(ItemBase.feather), Identifier.of("feathers"));
//        CONVERSION_TABLE.put(getIdentifier(ItemBase.bone), Identifier.of("bones"));
//        CONVERSION_TABLE.put(getIdentifier(ItemBase.bowl), Identifier.of("bowls"));
//        CONVERSION_TABLE.put(getIdentifier(ItemBase.dyePowder, 4), Identifier.of("gems/lapis"));
//        CONVERSION_TABLE.put(getIdentifier(ItemBase.dyePowder, 3), Identifier.of("cocoa"));
//        CONVERSION_TABLE.put(getIdentifier(BlockBase.CACTUS), Identifier.of("cacti"));
//        CONVERSION_TABLE.put(getIdentifier(BlockBase.RED_MUSHROOM), Identifier.of("mushrooms/red"));
//        CONVERSION_TABLE.put(getIdentifier(BlockBase.BROWN_MUSHROOM), Identifier.of("mushrooms/brown"));
//        CONVERSION_TABLE.put(getIdentifier(BlockBase.ROSE), Identifier.of("flowers/rose"));
//        CONVERSION_TABLE.put(getIdentifier(BlockBase.DANDELION), Identifier.of("flowers/dandelion"));
//        CONVERSION_TABLE.put(getIdentifier(BlockBase.IRON_BLOCK), Identifier.of("storage_blocks/iron"));
//        CONVERSION_TABLE.put(getIdentifier(BlockBase.DIAMOND_BLOCK), Identifier.of("storage_blocks/diamond"));
//        CONVERSION_TABLE.put(getIdentifier(BlockBase.LAPIS_LAZULI_BLOCK), Identifier.of("storage_blocks/lapis"));
    }

//    public static void tagify(Object[] objects) {
//        for (int i = 0; i < objects.length; i++) {
//            Object object = objects[i];
//            if (object instanceof ItemInstance item) {
//                Identifier identifier = CONVERSION_TABLE.get(getIdentifier(item.getType(), item.getDamage()));
//                if (identifier != null) {
//                    objects[i] = identifier;
//                }
//                else System.out.println(item.getTranslationKey());
//            }
//            else if (object instanceof SecondaryBlock) {
//                Identifier identifier = CONVERSION_TABLE.get(getIdentifier(BlockBase.BY_ID[((SecondaryBlockAccessor) object).getTileId()]));
//                if (identifier != null) {
//                    objects[i] = identifier;
//                }
//            }
//            else if (object instanceof ItemBase || object instanceof BlockBase) {
//                Identifier identifier = CONVERSION_TABLE.get(getIdentifier(object));
//                if (identifier != null) {
//                    objects[i] = identifier;
//                }
//            }
//        }
//    }

    private static BiTuple<Identifier, Integer> getIdentifier(Object o, int meta) {
        return o instanceof BlockBase block ? BiTuple.of(BlockRegistry.INSTANCE.getId(block), meta) : BiTuple.of(ItemRegistry.INSTANCE.getId((ItemBase) o), meta);
    }

    private static BiTuple<Identifier, Integer> getIdentifier(Object o) {
        return getIdentifier(o, 0);
    }

}
