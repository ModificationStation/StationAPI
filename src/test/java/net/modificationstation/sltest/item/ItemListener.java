package net.modificationstation.sltest.item;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.item.ItemInstance;
import net.minecraft.item.tool.ToolMaterial;
import net.modificationstation.sltest.block.Blocks;
import net.modificationstation.sltest.block.VariationBlock;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.item.tool.ToolMaterialFactory;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.tags.TagRegistry;
import net.modificationstation.stationapi.api.template.item.BlockStateItem;
import net.modificationstation.stationapi.api.template.item.TemplateItemBase;
import net.modificationstation.stationapi.api.template.item.tool.TemplatePickaxe;

import static net.modificationstation.sltest.SLTest.MODID;

public class ItemListener {

    @EventListener
    public void registerItems(ItemRegistryEvent event) {
        testItem = new ModdedItem(MODID.id("test_item")).setTranslationKey(MODID, "testItem"); //8475
        testMaterial = ToolMaterialFactory.create("testMaterial", 3, Integer.MAX_VALUE, Float.MAX_VALUE, Integer.MAX_VALUE - 2);
        testPickaxe = new ModdedPickaxe(MODID.id("test_pickaxe"), testMaterial).setTranslationKey(MODID, "testPickaxe"); //8476
        testNBTItem = new NBTItem(MODID.id("nbt_item")).setTranslationKey(MODID, "nbt_item"); //8477
        testModelItem = new ModelItem(MODID.id("model_item")).setMaxStackSize(1).setTranslationKey(MODID, "idkSomething");
        ironOre = new TemplateItemBase(MODID.id("ironOre")).setTranslationKey(MODID, "ironOre");
        generatedItem = new TemplateItemBase(MODID.id("generated_item")).setTranslationKey(MODID, "generatedItem");
        variationBlockIdle = new BlockStateItem(MODID.id("variation_block_idle"), Blocks.VARIATION_BLOCK.get().getDefaultState()).setTranslationKey(MODID, "variationBlockIdle");
        variationBlockPassive = new BlockStateItem(MODID.id("variation_block_passive"), Blocks.VARIATION_BLOCK.get().getDefaultState().with(VariationBlock.VARIANT, VariationBlock.Variant.PASSIVE)).setTranslationKey(MODID, "variationBlockPassive");
        variationBlockActive = new BlockStateItem(MODID.id("variation_block_active"), Blocks.VARIATION_BLOCK.get().getDefaultState().with(VariationBlock.VARIANT, VariationBlock.Variant.ACTIVE)).setTranslationKey(MODID, "variationBlockActive");
        TagRegistry.INSTANCE.register(Identifier.of("blocks/ores/iron"), new ItemInstance(ironOre), e -> ironOre.id == e.itemId);

        TagRegistry.INSTANCE.register(Identifier.of("items/tools/pickaxes/testpickaxe"), new ItemInstance(testPickaxe), (e) -> e.itemId == testPickaxe.id);
    }

    public static TemplateItemBase testItem;
    public static ToolMaterial testMaterial;
    public static TemplatePickaxe testPickaxe;
    public static TemplateItemBase testNBTItem;
    public static TemplateItemBase testModelItem;
    public static TemplateItemBase ironOre;
    public static TemplateItemBase generatedItem;
    public static TemplateItemBase variationBlockIdle;
    public static TemplateItemBase variationBlockPassive;
    public static TemplateItemBase variationBlockActive;
}
