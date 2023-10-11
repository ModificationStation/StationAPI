package net.modificationstation.stationapi.api.worldgen.surface.condition;

import net.minecraft.block.BlockBase;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.tag.TagKey;

public class TagSurfaceCondition implements SurfaceCondition {
    private final TagKey<BlockBase> tag;

    public TagSurfaceCondition(TagKey<BlockBase> tag) {
        this.tag = tag;
    }

    @Override
    public boolean canApply(Level level, int x, int y, int z, BlockState state) {
        return state.isIn(tag);
    }
}
