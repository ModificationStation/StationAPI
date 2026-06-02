package net.modificationstation.stationapi.api.item.tool;

import net.minecraft.item.Item;
import net.modificationstation.stationapi.mixin.tools.ToolMaterialAccessor;

public class ToolMaterialFactory {
    private static int nextId = Item.ToolMaterial.values().length;

    public static Item.ToolMaterial create(String materialName, int miningLevel, int durability, float miningSpeed, int attackDamage) {
        return ToolMaterialAccessor.stationapi_create(materialName, nextId++, miningLevel, durability, miningSpeed, attackDamage);
    }
}
