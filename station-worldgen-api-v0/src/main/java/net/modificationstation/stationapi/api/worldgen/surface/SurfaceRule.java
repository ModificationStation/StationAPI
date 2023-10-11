package net.modificationstation.stationapi.api.worldgen.surface;

import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.worldgen.surface.condition.SurfaceCondition;

import java.util.ArrayList;
import java.util.List;

public abstract class SurfaceRule {
    private final List<SurfaceCondition> conditions = new ArrayList<>();

    public abstract void apply(Level level, int x, int y, int z);

    public boolean canApply(Level level, int x, int y, int z, BlockState state) {
        for (SurfaceCondition condition : conditions) {
            if (!condition.canApply(level, x, y, z, state)) return false;
        }
        return true;
    }

    public void addCondition(SurfaceCondition condition) {
        conditions.add(condition);
    }
}
