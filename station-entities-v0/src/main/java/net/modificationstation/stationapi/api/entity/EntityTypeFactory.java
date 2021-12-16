package net.modificationstation.stationapi.api.entity;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.EntityType;
import net.modificationstation.stationapi.api.factory.EnumFactory;
import net.modificationstation.stationapi.mixin.entity.EntityTypeAccessor;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.*;

public class EntityTypeFactory {

    public static EntityType create(String typeName, Class<? extends EntityBase> typeSuperClass, int unusedInt, Material spawnMaterial, boolean unusedBoolean) {
        return EnumFactory.addEnum(EntityType.class, typeName, entityType -> initializeType(entityType, typeSuperClass, unusedInt, spawnMaterial, unusedBoolean));
    }

    private static void initializeType(EntityType entityType, Class<? extends EntityBase> typeSuperClass, int unusedInt, Material spawnMaterial, boolean unusedBoolean) {
        EntityTypeAccessor accessor = EntityTypeAccessor.class.cast(entityType);
        //noinspection ConstantConditions
        accessor.setField_945(typeSuperClass);
        accessor.setField_946(unusedInt);
        accessor.setField_947(spawnMaterial);
        accessor.setField_948(unusedBoolean);
    }
}
