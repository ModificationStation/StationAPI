package net.modificationstation.stationapi.api.item.tool;

import net.minecraft.block.Block;
import net.modificationstation.stationapi.api.tag.TagKey;

public class TagToolLevel extends ToolLevel {
    public final TagKey<Block> tag;

    public TagToolLevel(TagKey<Block> tag) {
        this.tag = tag;
    }

    @Override
    protected boolean isSuitable(TestContext context) {
        return context.blockState().isIn(tag);
    }
}
