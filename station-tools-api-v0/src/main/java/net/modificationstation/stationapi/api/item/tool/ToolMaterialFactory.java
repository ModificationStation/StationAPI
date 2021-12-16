package net.modificationstation.stationapi.api.item.tool;

import net.minecraft.item.tool.ToolMaterial;
import net.modificationstation.stationapi.api.factory.EnumFactory;
import net.modificationstation.stationapi.mixin.tools.ToolMaterialAccessor;

public class ToolMaterialFactory {

    public static ToolMaterial create(String materialName, int miningLevel, int durability, float miningSpeed, int attackDamage) {
        return EnumFactory.addEnum(ToolMaterial.class, materialName, toolMaterial -> initializeMaterial(toolMaterial, miningLevel, durability, miningSpeed, attackDamage));
    }

    private static void initializeMaterial(ToolMaterial toolMaterial, int miningLevel, int durability, float miningSpeed, int attackDamage) {
        ToolMaterialAccessor accessor = ToolMaterialAccessor.class.cast(toolMaterial);
        //noinspection ConstantConditions
        accessor.setMiningLevel(miningLevel);
        accessor.setDurability(durability);
        accessor.setMiningSpeed(miningSpeed);
        accessor.setAttackDamage(attackDamage);
    }
}
