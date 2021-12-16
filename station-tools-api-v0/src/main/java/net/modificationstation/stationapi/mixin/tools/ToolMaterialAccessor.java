package net.modificationstation.stationapi.mixin.tools;

import net.minecraft.item.tool.ToolMaterial;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ToolMaterial.class)
public interface ToolMaterialAccessor {

    @Accessor @Mutable
    void setMiningLevel(int miningLevel);

    @Accessor @Mutable
    void setDurability(int durability);

    @Accessor @Mutable
    void setMiningSpeed(float miningSpeed);

    @Accessor @Mutable
    void setAttackDamage(int attackDamage);
}
