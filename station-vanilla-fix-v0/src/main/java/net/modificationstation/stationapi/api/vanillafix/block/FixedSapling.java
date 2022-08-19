package net.modificationstation.stationapi.api.vanillafix.block;

import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.minecraft.util.maths.TilePos;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.item.Fuel;
import net.modificationstation.stationapi.api.level.BlockStateView;
import net.modificationstation.stationapi.api.level.StationFlatteningLevel;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.state.property.IntProperty;
import net.modificationstation.stationapi.api.state.property.Properties;
import net.modificationstation.stationapi.api.template.block.TemplatePlant;
import net.modificationstation.stationapi.api.vanillafix.block.sapling.SaplingGenerator;

import java.util.Random;

public class FixedSapling extends TemplatePlant implements Fuel {

    public static final IntProperty STAGE = Properties.STAGE;

    private final SaplingGenerator generator;

    public FixedSapling(Identifier identifier, SaplingGenerator generator) {
        super(identifier, 0);
        float size = 0.4f;
        this.setBoundingBox(0.5f - size, 0.0f, 0.5f - size, 0.5f + size, size * 2.0f, 0.5f + size);
        this.generator = generator;
    }

    @Override
    public void onScheduledTick(Level level, int x, int y, int z, Random random) {
        if (level.isServerSide) return;
        super.onScheduledTick(level, x, y, z, random);
        if (level.placeTile(x, y + 1, z) >= 9 && random.nextInt(30) == 0)
            generate(level, new TilePos(x, y, z), ((BlockStateView) level).getBlockState(x, y, z), random);
    }

    public void generate(Level level, TilePos pos, BlockState state, Random random) {
        StationFlatteningLevel view = (StationFlatteningLevel) level;
        if (state.get(STAGE) == 0) view.setBlockStateWithNotify(pos, state.cycle(STAGE));
        else generator.generate(level, level.getCache(), pos, state, random);
    }

    @Override
    public void appendProperties(StateManager.Builder<BlockBase, BlockState> builder) {
        builder.add(STAGE);
    }

    // TODO: replace with fuel tags
    @Override
    public int getFuelTime(ItemInstance itemInstance) {
        return 100;
    }
}
