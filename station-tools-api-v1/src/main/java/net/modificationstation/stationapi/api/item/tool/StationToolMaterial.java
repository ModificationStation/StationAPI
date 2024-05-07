package net.modificationstation.stationapi.api.item.tool;

import net.minecraft.item.ToolMaterial;
import net.modificationstation.stationapi.api.util.Util;

public interface StationToolMaterial {
    default ToolMaterial miningLevelNode(MiningLevelManager.LevelNode levelNode) {
        return Util.assertImpl();
    }

    default MiningLevelManager.LevelNode getMiningLevelNode() {
        return Util.assertImpl();
    }
}
