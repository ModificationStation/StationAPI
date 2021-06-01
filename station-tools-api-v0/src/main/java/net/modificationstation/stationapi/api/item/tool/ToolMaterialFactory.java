package net.modificationstation.stationapi.api.item.tool;

import net.minecraft.item.tool.ToolMaterial;
import net.modificationstation.stationapi.api.factory.EnumFactory;

public class ToolMaterialFactory {

    public static ToolMaterial create(String materialName, int miningLevel, int durability, float miningSpeed, int attackDamage) {
        return EnumFactory.INSTANCE.addEnum(
                ToolMaterial.class,
                materialName,
                new Class[] { int.class, int.class, float.class, int.class },
                new Object[] { miningLevel, durability, miningSpeed, attackDamage }
        );
    }
}
