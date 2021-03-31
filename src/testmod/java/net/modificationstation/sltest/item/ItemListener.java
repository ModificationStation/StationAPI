package net.modificationstation.sltest.item;

import net.minecraft.item.ItemBase;
import net.minecraft.item.tool.ToolMaterial;
import net.modificationstation.sltest.SLTest;
import net.modificationstation.stationapi.api.common.event.EventListener;
import net.modificationstation.stationapi.api.common.event.registry.RegistryEvent;
import net.modificationstation.stationapi.api.common.factory.GeneralFactory;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class ItemListener {

    @EventListener
    public void registerItems(RegistryEvent.Items event) {
        testItem = new ModdedItem(Identifier.of(SLTest.MODID, "test_item")).setTranslationKey(SLTest.MODID, "testItem"); //8475
        testMaterial = GeneralFactory.INSTANCE.newInst(ToolMaterial.class, "testMaterial", 3, Integer.MAX_VALUE, Float.MAX_VALUE, Integer.MAX_VALUE - 2);
        testPickaxe = new ModdedPickaxe(Identifier.of(SLTest.MODID, "test_pickaxe"), testMaterial).setTranslationKey(SLTest.MODID, "testPickaxe"); //8476
        testNBTItem = new NBTItem(Identifier.of(SLTest.MODID, "nbt_item")).setMaxStackSize(1).setTranslationKey(SLTest.MODID, "nbt_item"); //8477
    }

    public static ItemBase testItem;
    public static ToolMaterial testMaterial;
    public static ItemBase testPickaxe;
    public static ItemBase testNBTItem;
}
