package net.modificationstation.stationapi.api.item.tool;

import net.minecraft.item.Item;
import net.modificationstation.stationapi.api.util.Util;

public interface StationToolMaterial {
    default Item.ToolMaterial toolLevel(ToolLevel toolLevel) {
        return Util.assertImpl();
    }

    default ToolLevel getToolLevel() {
        return Util.assertImpl();
    }
}
