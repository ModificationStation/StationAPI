package net.modificationstation.stationapi.api.item.tool;

import net.minecraft.item.ToolMaterial;
import net.modificationstation.stationapi.api.util.Util;

public interface StationToolMaterial {
    default ToolMaterial toolLevel(ToolLevel toolLevel) {
        return Util.assertImpl();
    }

    default ToolLevel getToolLevel() {
        return Util.assertImpl();
    }
}
