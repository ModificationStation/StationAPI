/*
 * Decompiled with CFR 0.0.9 (FabricMC cc05e23f).
 */
package net.modificationstation.stationapi.api.vanillafix.block.sapling;

import net.minecraft.level.Level;
import net.minecraft.level.source.LevelSource;
import net.minecraft.level.structure.Structure;
import net.minecraft.util.maths.TilePos;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.States;
import net.modificationstation.stationapi.api.level.StationFlatteningWorld;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public abstract class SaplingGenerator {

    @Nullable
    protected abstract Structure getTreeStructure(Random random);

    public boolean generate(Level world, LevelSource chunkGenerator, TilePos pos, BlockState state, Random random) {
        Structure structure = getTreeStructure(random);
        if (structure == null) {
            return false;
        }

        StationFlatteningWorld view = (StationFlatteningWorld) world;
        view.setBlockStateWithNotify(pos, States.AIR.get());
        if (structure.generate(world, random, pos.x, pos.y, pos.z)) {
            return true;
        }
        view.setBlockStateWithNotify(pos, state);
        return false;
    }
}

