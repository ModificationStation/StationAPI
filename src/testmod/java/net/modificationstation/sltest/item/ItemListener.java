package net.modificationstation.sltest.item;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.item.tool.ToolMaterial;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.item.tool.ToolMaterialFactory;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.tags.TagRegistry;
import net.modificationstation.stationapi.api.template.item.TemplateItemBase;
import net.modificationstation.stationapi.api.template.item.tool.TemplatePickaxe;

import static net.modificationstation.sltest.SLTest.MODID;
import static net.modificationstation.stationapi.api.registry.Identifier.of;

public class ItemListener {

    @EventListener
    public void registerItems(ItemRegistryEvent event) {
        testItem = new ModdedItem(of(MODID, "test_item")).setTranslationKey(MODID, "testItem"); //8475
        testMaterial = ToolMaterialFactory.create("testMaterial", 3, Integer.MAX_VALUE, Float.MAX_VALUE, Integer.MAX_VALUE - 2);
        testPickaxe = new ModdedPickaxe(of(MODID, "test_pickaxe"), testMaterial).setTranslationKey(MODID, "testPickaxe"); //8476
        testNBTItem = new NBTItem(of(MODID, "nbt_item")).setMaxStackSize(1).setTranslationKey(MODID, "nbt_item"); //8477
        testModelItem = new ModelItem(of(MODID, "model_item")).setMaxStackSize(1).setTranslationKey(MODID, "idkSomething");

        TagRegistry.INSTANCE.register(Identifier.of("items/tools/pickaxes/testpickaxe"), (e) -> e.itemId == testPickaxe.id);
    }

    public static TemplateItemBase testItem;
    public static ToolMaterial testMaterial;
    public static TemplatePickaxe testPickaxe;
    public static TemplateItemBase testNBTItem;
    public static TemplateItemBase testModelItem;
}
