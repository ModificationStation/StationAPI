package net.modificationstation.stationapi.mixin.tools;

import net.minecraft.item.ToolMaterial;
import net.modificationstation.stationapi.api.item.tool.StationToolMaterial;
import net.modificationstation.stationapi.api.item.tool.ToolLevel;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ToolMaterial.class)
class ToolMaterialMixin implements StationToolMaterial {
    @Shadow @Final private int miningLevel;

    @Shadow @Final private int itemDurability;

    @Shadow @Final private float miningSpeed;

    @Shadow @Final private int attackDamage;

    @Override
    @Unique
    public ToolLevel toolLevel() {
        return ToolLevel.getNumeric(miningLevel);
    }

    @Override
    @Unique
    public int itemDurability() {
        return itemDurability;
    }

    @Override
    @Unique
    public float miningSpeed() {
        return miningSpeed;
    }

    @Override
    @Unique
    public int attackDamage() {
        return attackDamage;
    }
}
