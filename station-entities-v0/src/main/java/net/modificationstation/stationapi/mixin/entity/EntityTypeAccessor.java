package net.modificationstation.stationapi.mixin.entity;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityType.class)
public interface EntityTypeAccessor {

    @Accessor @Mutable
    void setField_945(Class<? extends EntityBase> field_945);

    @Accessor @Mutable
    void setField_946(int field_946);

    @Accessor @Mutable
    void setField_947(Material field_947);

    @Accessor @Mutable
    void setField_948(boolean field_948);
}
