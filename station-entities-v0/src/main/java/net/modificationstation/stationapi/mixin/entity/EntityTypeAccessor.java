package net.modificationstation.stationapi.mixin.entity;

import net.minecraft.block.Material;
import net.minecraft.class_238;
import net.minecraft.entity.Entity;
import net.modificationstation.stationapi.api.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(class_238.class)
public interface EntityTypeAccessor {
    @Invoker("<init>")
    static class_238 stationapi_create(String typeName, int id, Class<? extends Entity> typeSuperClass, int unusedInt, Material spawnMaterial, boolean unusedBoolean) {
        return Util.assertMixin();
    }
}
