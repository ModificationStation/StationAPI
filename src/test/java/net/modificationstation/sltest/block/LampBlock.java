package net.modificationstation.sltest.block;

import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.state.StateManager.Builder;
import net.modificationstation.stationapi.api.state.property.BooleanProperty;
import net.modificationstation.stationapi.api.template.block.TemplateBlockBase;
import net.modificationstation.stationapi.api.world.BlockStateView;

public class LampBlock extends TemplateBlockBase {
    private static final BooleanProperty ACTIVE = BooleanProperty.of("active");

    public LampBlock(Identifier id) {
        super(id, Material.WOOD);
        setLuminance(state -> state.get(ACTIVE) ? 15 : 0);
        setTranslationKey(id);
        setSounds(WOOD_SOUNDS);
        setDefaultState(getDefaultState().with(ACTIVE, false));
    }

    @Override
    public void appendProperties(Builder<BlockBase, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(ACTIVE);
    }

    @Override
    public int getTextureForSide(BlockView view, int x, int y, int z, int side) {
        if (view instanceof BlockStateView stateView) {
            return stateView.getBlockState(x, y, z).get(ACTIVE) ? STILL_LAVA.texture : OBSIDIAN.texture;
        }
        return OBSIDIAN.texture;
    }

    @Override
    public int getTextureForSide(int side) {
        return OBSIDIAN.texture;
    }

    @Override
    public boolean canUse(Level level, int x, int y, int z, PlayerBase player) {
        BlockState state = level.getBlockState(x, y, z);
        state = state.with(ACTIVE, !state.get(ACTIVE));
        level.setBlockState(x, y, z, state);
        return super.canUse(level, x, y, z, player);
    }
}
