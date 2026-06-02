package net.modificationstation.stationapi.mixin.tools;

import net.minecraft.item.Item;
import net.modificationstation.stationapi.api.item.tool.StationToolMaterial;
import net.modificationstation.stationapi.api.item.tool.ToolLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Item.ToolMaterial.class)
class ToolMaterialMixin implements StationToolMaterial {
    @Unique
    private ToolLevel stationapi_toolLevel;

    @Override
    @Unique
    public Item.ToolMaterial toolLevel(ToolLevel toolLevel) {
        stationapi_toolLevel = toolLevel;
        return Item.ToolMaterial.class.cast(this);
    }

    @Override
    @Unique
    public ToolLevel getToolLevel() {
        return stationapi_toolLevel;
    }
}
