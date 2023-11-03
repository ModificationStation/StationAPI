package net.modificationstation.sltest.item;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;
import net.modificationstation.sltest.block.Blocks;
import net.modificationstation.sltest.block.VariationBlock;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.item.tool.ToolMaterialFactory;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.template.item.BlockStateItem;

import static net.modificationstation.sltest.SLTest.MODID;

public class ItemListener {

    @EventListener
    public void registerItems(ItemRegistryEvent event) {
        testItem = new ModdedItem(MODID.id("test_item")).setTranslationKey(MODID, "testItem"); //8475
        testMaterial = ToolMaterialFactory.create("testMaterial", 3, Integer.MAX_VALUE, Float.MAX_VALUE, Integer.MAX_VALUE - 2);
        testPickaxe = new ModdedPickaxe(MODID.id("test_pickaxe"), testMaterial).setTranslationKey(MODID, "testPickaxe"); //8476
        testNBTItem = new NBTItem(MODID.id("nbt_item")).setTranslationKey(MODID, "nbt_item"); //8477
        testModelItem = new ModelItem(MODID.id("model_item")).setMaxCount(1).setTranslationKey(MODID, "idkSomething");
        ironOre = Registry.register(event.registry, MODID.id("ironOre"), new Item(ItemRegistry.AUTO_ID)).setTranslationKey(MODID.id("ironOre"));
        generatedItem = Registry.register(event.registry, MODID.id("generated_item"), new Item(ItemRegistry.AUTO_ID)).setTranslationKey(MODID.id("generatedItem"));
        variationBlockIdle = new BlockStateItem(MODID.id("variation_block_idle"), Blocks.VARIATION_BLOCK.get().getDefaultState()).setTranslationKey(MODID, "variationBlockIdle");
        variationBlockPassive = new BlockStateItem(MODID.id("variation_block_passive"), Blocks.VARIATION_BLOCK.get().getDefaultState().with(VariationBlock.VARIANT, VariationBlock.Variant.PASSIVE)).setTranslationKey(MODID, "variationBlockPassive");
        variationBlockActive = new BlockStateItem(MODID.id("variation_block_active"), Blocks.VARIATION_BLOCK.get().getDefaultState().with(VariationBlock.VARIANT, VariationBlock.Variant.ACTIVE)).setTranslationKey(MODID, "variationBlockActive");
        testShears = new TestShears(MODID.id("test_shears")).setTranslationKey(MODID, "test_shears");
        pacifistSword = new PacifistSword(MODID.id("pacifist_sword")).setTranslationKey(MODID, "pacifist_sword");
        dullPickaxe = new DullPickaxe(MODID.id("dull_pickaxe")).setTranslationKey(MODID, "dull_pickaxe");
    }

    public static Item testItem;
    public static ToolMaterial testMaterial;
    public static Item testPickaxe;
    public static Item testNBTItem;
    public static Item testModelItem;
    public static Item ironOre;
    public static Item generatedItem;
    public static Item variationBlockIdle;
    public static Item variationBlockPassive;
    public static Item variationBlockActive;
    public static Item testShears;
    public static Item pacifistSword;
    public static Item dullPickaxe;
}
