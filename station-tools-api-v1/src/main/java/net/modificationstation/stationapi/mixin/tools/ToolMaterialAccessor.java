package net.modificationstation.stationapi.mixin.tools;

import net.minecraft.item.ToolMaterial;
import net.modificationstation.stationapi.api.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ToolMaterial.class)
public interface ToolMaterialAccessor {
    @Invoker("<init>")
    static ToolMaterial stationapi_create(String materialName, int id, int miningLevel, int itemDurability, float miningSpeed, int attackDamage) {
        return Util.assertMixin();
    }
}
