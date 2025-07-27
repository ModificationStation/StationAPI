package net.modificationstation.stationapi.mixin.entity;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnGroup;
import net.modificationstation.stationapi.api.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(SpawnGroup.class)
public interface EntityTypeAccessor {
    @Invoker("<init>")
    static SpawnGroup stationapi_create(String typeName, int id, Class<? extends Entity> typeSuperClass, int unusedInt, Material spawnMaterial, boolean unusedBoolean) {
        return Util.assertMixin();
    }
}
