package net.modificationstation.stationapi.api.item.tool;

import net.minecraft.block.BlockBase;
import net.minecraft.item.tool.ToolMaterial;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.tag.TagKey;
import net.modificationstation.stationapi.api.util.Util;

public interface StationToolMaterial {

    default ToolMaterial requiredBlockTag(Identifier tag) {
        return Util.assertImpl();
    }

    default TagKey<BlockBase> getRequiredBlockTag() {
        return Util.assertImpl();
    }
}
