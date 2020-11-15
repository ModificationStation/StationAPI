package net.modificationstation.stationloader.api.common.block;

import net.modificationstation.stationloader.api.common.item.tool.ToolLevel;

public interface EffectiveForTool {

    boolean isEffectiveFor(ToolLevel toolLevel, int meta);
}
