package net.modificationstation.stationapi.mixin.tools;

import net.minecraft.item.ToolMaterial;
import net.modificationstation.stationapi.api.item.tool.StationToolMaterial;
import net.modificationstation.stationapi.api.item.tool.ToolLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ToolMaterial.class)
class ToolMaterialMixin implements StationToolMaterial {
    @Unique
    private ToolLevel stationapi_toolLevel;

    @Override
    @Unique
    public ToolMaterial toolLevel(ToolLevel toolLevel) {
        stationapi_toolLevel = toolLevel;
        return ToolMaterial.class.cast(this);
    }

    @Override
    @Unique
    public ToolLevel getToolLevel() {
        return stationapi_toolLevel;
    }
}
