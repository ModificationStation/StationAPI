package net.modificationstation.stationapi.api.client.render.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.state.StateManager;

import java.util.function.Predicate;

@FunctionalInterface
@Environment(EnvType.CLIENT)
public interface MultipartModelSelector {
   MultipartModelSelector TRUE = (stateManager) -> (blockState) -> true;
   MultipartModelSelector FALSE = (stateManager) -> (blockState) -> false;

   Predicate<BlockState> getPredicate(StateManager<Block, BlockState> stateFactory);
}
