package net.modificationstation.stationapi.mixin.tools;

import net.minecraft.item.Item;
import net.modificationstation.stationapi.api.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Item.ToolMaterial.class)
public interface ToolMaterialAccessor {
    @Invoker("<init>")
    static Item.ToolMaterial stationapi_create(String materialName, int id, int miningLevel, int itemDurability, float miningSpeed, int attackDamage) {
        return Util.assertMixin();
    }
}
