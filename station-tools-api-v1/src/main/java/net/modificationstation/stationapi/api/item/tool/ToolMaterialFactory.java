package net.modificationstation.stationapi.api.item.tool;

import net.minecraft.item.ToolMaterial;
import net.modificationstation.stationapi.api.factory.EnumFactory;

public class ToolMaterialFactory {
    public static ToolMaterial create(String materialName, int miningLevel, int durability, float miningSpeed, int attackDamage) {
        return EnumFactory.addEnum(
                ToolMaterial.class, materialName,
                new Class[] { int.class, int.class, float.class, int.class },
                miningLevel, durability, miningSpeed, attackDamage
        );
    }
}
