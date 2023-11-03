package net.modificationstation.stationapi.api.entity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.block.Material;
import net.minecraft.class_238;
import net.minecraft.entity.Entity;
import net.modificationstation.stationapi.api.factory.EnumFactory;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EntityTypeFactory {

    public static class_238 create(String typeName, Class<? extends Entity> typeSuperClass, int unusedInt, Material spawnMaterial, boolean unusedBoolean) {
        return EnumFactory.addEnum(
                class_238.class, typeName,
                new Class[] { Class.class, int.class, Material.class, boolean.class },
                typeSuperClass, unusedInt, spawnMaterial, unusedBoolean
        );
    }
}
