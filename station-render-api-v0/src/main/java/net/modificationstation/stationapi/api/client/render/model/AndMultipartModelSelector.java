package net.modificationstation.stationapi.api.client.render.model;

import com.google.common.collect.Streams;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.impl.block.BlockState;
import net.modificationstation.stationapi.impl.block.StateManager;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

@Environment(EnvType.CLIENT)
public class AndMultipartModelSelector implements MultipartModelSelector {
   private final Iterable<? extends MultipartModelSelector> selectors;

   public AndMultipartModelSelector(Iterable<? extends MultipartModelSelector> selectors) {
      this.selectors = selectors;
   }

   public Predicate<BlockState> getPredicate(StateManager<BlockBase, BlockState> stateManager) {
      List<Predicate<BlockState>> list = Streams.stream(this.selectors).map((multipartModelSelector) -> multipartModelSelector.getPredicate(stateManager)).collect(Collectors.toList());
      return (blockState) -> list.stream().allMatch((predicate) -> predicate.test(blockState));
   }
}
