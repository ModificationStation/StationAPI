package net.modificationstation.stationapi.api.entity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.EntityType;
import net.modificationstation.stationapi.api.factory.EnumFactory;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EntityTypeFactory {

    public static EntityType create(String typeName, Class<? extends EntityBase> typeSuperClass, int unusedInt, Material spawnMaterial, boolean unusedBoolean) {
        return EnumFactory.addEnum(
                EntityType.class, typeName,
                new Class[] { Class.class, int.class, Material.class, boolean.class },
                typeSuperClass, unusedInt, spawnMaterial, unusedBoolean
        );
    }
}
