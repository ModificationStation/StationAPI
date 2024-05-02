package net.modificationstation.stationapi.api.item.tool;

import net.minecraft.block.Block;
import net.minecraft.item.ToolMaterial;
import net.modificationstation.stationapi.api.tag.TagKey;
import net.modificationstation.stationapi.api.util.Util;

public interface StationToolMaterial {
    default ToolMaterial miningLevelTag(TagKey<Block> tag) {
        return Util.assertImpl();
    }

    default TagKey<Block> getMiningLevelTag() {
        return Util.assertImpl();
    }
}
