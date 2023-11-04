package net.modificationstation.sltest.block;

import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.state.StateManager.Builder;
import net.modificationstation.stationapi.api.state.property.BooleanProperty;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.world.BlockStateView;

public class LampBlock extends TemplateBlock {
    private static final BooleanProperty ACTIVE = BooleanProperty.of("active");

    public LampBlock(Identifier id) {
        super(id, Material.WOOD);
        setLuminance(state -> state.get(ACTIVE) ? 15 : 0);
        setTranslationKey(id);
        setSoundGroup(WOOD_SOUND_GROUP);
        setDefaultState(getDefaultState().with(ACTIVE, false));
    }

    @Override
    public void appendProperties(Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(ACTIVE);
    }

    @Override
    public int getTextureId(BlockView view, int x, int y, int z, int side) {
        if (view instanceof BlockStateView stateView) {
            return stateView.getBlockState(x, y, z).get(ACTIVE) ? LAVA.textureId : OBSIDIAN.textureId;
        }
        return OBSIDIAN.textureId;
    }

    @Override
    public int getTexture(int side) {
        return OBSIDIAN.textureId;
    }

    @Override
    public boolean onUse(World level, int x, int y, int z, PlayerEntity player) {
        BlockState state = level.getBlockState(x, y, z);
        state = state.with(ACTIVE, !state.get(ACTIVE));
        level.setBlockState(x, y, z, state);
        return super.onUse(level, x, y, z, player);
    }
}
