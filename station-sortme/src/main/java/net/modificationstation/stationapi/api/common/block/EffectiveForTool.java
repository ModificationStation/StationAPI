package net.modificationstation.stationapi.api.common.block;

import net.modificationstation.stationapi.api.common.item.tool.ToolLevel;

public interface EffectiveForTool {

    boolean isEffectiveFor(ToolLevel toolLevel, int meta);
}
