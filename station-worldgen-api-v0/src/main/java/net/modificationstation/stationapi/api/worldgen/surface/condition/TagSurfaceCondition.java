package net.modificationstation.stationapi.api.worldgen.surface.condition;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.tag.TagKey;

public class TagSurfaceCondition implements SurfaceCondition {
    private final TagKey<Block> tag;

    public TagSurfaceCondition(TagKey<Block> tag) {
        this.tag = tag;
    }

    @Override
    public boolean canApply(World world, int x, int y, int z, BlockState state) {
        return state.isIn(tag);
    }
}
