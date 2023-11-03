package net.modificationstation.stationapi.api.client.model.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.modificationstation.stationapi.api.block.BlockState;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public interface BlockModelPredicateProvider {

    float call(BlockState state, @Nullable BlockView world, @Nullable BlockPos pos, int seed);
}
