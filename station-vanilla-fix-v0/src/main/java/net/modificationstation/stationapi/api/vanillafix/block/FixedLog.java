package net.modificationstation.stationapi.api.vanillafix.block;

import net.minecraft.block.material.Material;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.level.StationFlatteningLevel;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.state.property.Properties;
import net.modificationstation.stationapi.api.template.block.TemplateBlockBase;

import java.util.List;

public class FixedLog extends TemplateBlockBase {

    public FixedLog(Identifier identifier) {
        super(identifier, Material.WOOD);
    }

    @Override
    public List<ItemInstance> getDropList(Level level, int x, int y, int z, BlockState state, int meta) {
        return List.of(new ItemInstance(this));
    }

    @Override
    public void onBlockRemoved(Level level, int x, int y, int z) {
        int radius = 4;
        int safeRadius = radius + 1;
        if (level.method_155(x - safeRadius, y - safeRadius, z - safeRadius, x + safeRadius, y + safeRadius, z + safeRadius))
            for (int xOff = -radius; xOff <= radius; ++xOff) for (int yOff = -radius; yOff <= radius; ++yOff) for (int zOff = -radius; zOff <= radius; ++zOff) {
                BlockState state = ((StationFlatteningLevel) level).getBlockState(x + xOff, y + yOff, z + zOff);
                if (!(state.getBlock() instanceof FixedLeaves) || !state.get(Properties.PERSISTENT)) continue;
                ((StationFlatteningLevel) level).setBlockState(x + xOff, y + yOff, z + zOff, state.with(Properties.PERSISTENT, false));
            }
    }
}
