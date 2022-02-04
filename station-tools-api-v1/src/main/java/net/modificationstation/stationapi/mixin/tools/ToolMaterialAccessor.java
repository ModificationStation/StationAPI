package net.modificationstation.stationapi.mixin.tools;

import net.minecraft.item.tool.ToolMaterial;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ToolMaterial.class)
public interface ToolMaterialAccessor {

    @Mutable
    @Accessor("miningLevel")
    void stationapi$setMiningLevel(int miningLevel);

    @Mutable
    @Accessor("durability")
    void stationapi$setDurability(int durability);

    @Mutable
    @Accessor("miningSpeed")
    void stationapi$setMiningSpeed(float miningSpeed);

    @Mutable
    @Accessor("attackDamage")
    void stationapi$setAttackDamage(int attackDamage);
}
