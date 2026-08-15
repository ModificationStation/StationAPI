package net.modificationstation.sltest.item;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;
import net.modificationstation.sltest.block.Blocks;
import net.modificationstation.sltest.block.VariationBlock;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.item.tool.TagToolLevel;
import net.modificationstation.stationapi.api.item.tool.ToolLevel;
import net.modificationstation.stationapi.api.item.tool.ToolMaterialFactory;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.tag.TagKey;
import net.modificationstation.stationapi.api.template.item.BlockStateItem;
import net.modificationstation.stationapi.api.template.item.TemplateDoorItem;

import static net.modificationstation.sltest.SLTest.NAMESPACE;

public class ItemListener {

    @EventListener
    public void registerItems(ItemRegistryEvent event) {
        ToolLevel moddedNode = new TagToolLevel(TagKey.of(BlockRegistry.KEY, NAMESPACE.id("needs_tool_level_modded")));
        ToolLevel.GRAPH.putEdge(ToolMaterial.STONE.getToolLevel(), moddedNode);
        ToolLevel.GRAPH.putEdge(moddedNode, ToolMaterial.IRON.getToolLevel());
        ToolLevel siblingNode = new TagToolLevel(TagKey.of(BlockRegistry.KEY, NAMESPACE.id("needs_tool_level_sibling"))).equivalentToImmediateSiblings();
        ToolLevel.GRAPH.putEdge(ToolMaterial.STONE.getToolLevel(), siblingNode);
        ToolLevel.GRAPH.putEdge(siblingNode, ToolMaterial.IRON.getToolLevel());
        ToolLevel.GRAPH.removeEdge(ToolMaterial.STONE.getToolLevel(), ToolMaterial.IRON.getToolLevel());

        testItem = new ModdedItem(NAMESPACE.id("test_item")); //8475
        testMaterial = ToolMaterialFactory.create("testMaterial", 3, Integer.MAX_VALUE, Float.MAX_VALUE, Integer.MAX_VALUE - 2).toolLevel(siblingNode);
        testPickaxe = new ModdedPickaxeItem(NAMESPACE.id("test_pickaxe"), testMaterial); //8476
        testNBTItem = new NBTItem(NAMESPACE.id("nbt_item")); //8477
        testModelItem = new ModelItem(NAMESPACE.id("model_item")).setMaxCount(1);
        ironOre = event.register(NAMESPACE.id("iron_ore"), new Item(ItemRegistry.AUTO_ID));
        generatedItem = event.register(NAMESPACE.id("generated_item"), new Item(ItemRegistry.AUTO_ID));
        variationBlockIdle = new BlockStateItem(NAMESPACE.id("variation_block_idle"), Blocks.VARIATION_BLOCK.get().getDefaultState());
        variationBlockPassive = new BlockStateItem(NAMESPACE.id("variation_block_passive"), Blocks.VARIATION_BLOCK.get().getDefaultState().with(VariationBlock.VARIANT, VariationBlock.Variant.PASSIVE));
        variationBlockActive = new BlockStateItem(NAMESPACE.id("variation_block_active"), Blocks.VARIATION_BLOCK.get().getDefaultState().with(VariationBlock.VARIANT, VariationBlock.Variant.ACTIVE));
        testShears = new TestShearsItem(NAMESPACE.id("test_shears"));
        pacifistSword = new PacifistSwordItem(NAMESPACE.id("pacifist_sword"));
        dullPickaxe = new DullPickaxeItem(NAMESPACE.id("dull_pickaxe"));
        fancyDoor = new TemplateDoorItem(NAMESPACE.id("fancy_wood_door"), Material.WOOD, Blocks.FANCY_WOOD_DOOR.get());

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
    public static Item fancyDoor;
}
