package net.modificationstation.stationapi.api.client.render.model;

import com.google.common.collect.Streams;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.state.StateManager;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Environment(EnvType.CLIENT)
public class AndMultipartModelSelector implements MultipartModelSelector {
   private final Iterable<? extends MultipartModelSelector> selectors;

   public AndMultipartModelSelector(Iterable<? extends MultipartModelSelector> selectors) {
      this.selectors = selectors;
   }

   public Predicate<BlockState> getPredicate(StateManager<Block, BlockState> stateManager) {
      List<Predicate<BlockState>> list = Streams.stream(this.selectors).map((multipartModelSelector) -> multipartModelSelector.getPredicate(stateManager)).collect(Collectors.toList());
      return (blockState) -> list.stream().allMatch((predicate) -> predicate.test(blockState));
   }
}
