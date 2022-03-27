package net.modificationstation.sltest.block;

import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Living;
import net.minecraft.level.Level;
import net.minecraft.util.maths.MathHelper;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.level.BlockStateView;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.template.block.TemplateBlockBase;
import net.modificationstation.stationapi.api.util.math.Direction;

import static net.modificationstation.stationapi.api.state.property.Properties.HORIZONTAL_FACING;

public class ModdedModelBlock extends TemplateBlockBase {

    private static final Direction[] DIRECTIONS = new Direction[] { Direction.NORTH, Direction.WEST, Direction.SOUTH, Direction.WEST };

    protected ModdedModelBlock(Identifier id, Material material) {
        super(id, material);
    }

    @Override
    public boolean isFullOpaque() {
        return false;
    }

    @Override
    public void afterPlaced(Level level, int x, int y, int z, Living living) {
        ((BlockStateView) level).setBlockState(x, y, z, getDefaultState().with(HORIZONTAL_FACING, DIRECTIONS[MathHelper.floor((double)(living.yaw * 4.0F / 360.0F) + 0.5D) & 3]));
    }

    @Override
    public void appendProperties(StateManager.Builder<BlockBase, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(HORIZONTAL_FACING);
    }
}
