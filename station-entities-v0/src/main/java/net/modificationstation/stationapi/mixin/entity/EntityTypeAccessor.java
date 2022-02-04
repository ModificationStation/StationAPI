package net.modificationstation.stationapi.mixin.entity;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityType.class)
public interface EntityTypeAccessor {

    @Mutable
    @Accessor("field_945")
    void stationapi$setField_945(Class<? extends EntityBase> typeSuperClass);

    @Mutable
    @Accessor("field_946")
    void stationapi$setField_946(int unusedInt);

    @Mutable
    @Accessor("field_947")
    void stationapi$setField_947(Material spawnMaterial);

    @Mutable
    @Accessor("field_948")
    void stationapi$setField_948(boolean unusedBoolean);
}
