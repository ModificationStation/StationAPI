package net.modificationstation.stationapi.api.entity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnGroup;
import net.modificationstation.stationapi.mixin.entity.EntityTypeAccessor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EntityTypeFactory {
    private static int nextId = SpawnGroup.values().length;

    public static SpawnGroup create(String typeName, Class<? extends Entity> typeSuperClass, int unusedInt, Material spawnMaterial, boolean unusedBoolean) {
        return EntityTypeAccessor.stationapi_create(typeName, nextId++, typeSuperClass, unusedInt, spawnMaterial, unusedBoolean);
    }
}
