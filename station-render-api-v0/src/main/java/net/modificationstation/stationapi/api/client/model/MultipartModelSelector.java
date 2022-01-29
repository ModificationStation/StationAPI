package net.modificationstation.stationapi.api.client.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.impl.block.BlockState;
import net.modificationstation.stationapi.impl.block.StateManager;

import java.util.function.*;

@FunctionalInterface
@Environment(EnvType.CLIENT)
public interface MultipartModelSelector {
   MultipartModelSelector TRUE = (stateManager) -> (blockState) -> true;
   MultipartModelSelector FALSE = (stateManager) -> (blockState) -> false;

   Predicate<BlockState> getPredicate(StateManager<BlockBase, BlockState> stateFactory);
}
