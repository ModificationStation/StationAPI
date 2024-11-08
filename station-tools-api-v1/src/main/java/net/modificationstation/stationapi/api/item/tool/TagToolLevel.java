package net.modificationstation.stationapi.api.item.tool;

import net.minecraft.block.Block;
import net.modificationstation.stationapi.api.tag.TagKey;

/**
 * This class is a simple implementation of {@link ToolLevel} in a tag-based approach.
 *
 * <p>
 *     It tests if the block state in the context is tagged with the {@link TagKey}
 *     that this tool level was instantiated with. For example, <code>needs_iron_tool</code>.
 * </p>
 */
public class TagToolLevel extends ToolLevel {
    public final TagKey<Block> tag;

    public TagToolLevel(TagKey<Block> tag) {
        this.tag = tag;
    }

    @Override
    protected boolean isSuitable(TestContext context) {
        return context.blockState().isIn(tag);
    }

    @Override
    public String toString() {
        return tag.toString();
    }
}
