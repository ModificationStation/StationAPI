package net.modificationstation.stationapi.api.client.render.model;

import com.google.common.collect.Streams;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.state.StateManager;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Environment(EnvType.CLIENT)
public class OrMultipartModelSelector implements MultipartModelSelector {
   private final Iterable<? extends MultipartModelSelector> selectors;

   public OrMultipartModelSelector(Iterable<? extends MultipartModelSelector> selectors) {
      this.selectors = selectors;
   }

   public Predicate<BlockState> getPredicate(StateManager<BlockBase, BlockState> stateManager) {
      List<Predicate<BlockState>> list = Streams.stream(this.selectors).map((multipartModelSelector) -> multipartModelSelector.getPredicate(stateManager)).collect(Collectors.toList());
      return (blockState) -> list.stream().anyMatch((predicate) -> predicate.test(blockState));
   }
}
