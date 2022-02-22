package net.modificationstation.stationapi.api.client.render.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Splitter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.state.property.Property;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

@Environment(EnvType.CLIENT)
public class SimpleMultipartModelSelector implements MultipartModelSelector {
   private static final Splitter VALUE_SPLITTER = Splitter.on('|').omitEmptyStrings();
   private final String key;
   private final String valueString;

   public SimpleMultipartModelSelector(String key, String valueString) {
      this.key = key;
      this.valueString = valueString;
   }

   public Predicate<BlockState> getPredicate(StateManager<BlockBase, BlockState> stateManager) {
      Property<?> property = stateManager.getProperty(this.key);
      if (property == null) {
         throw new RuntimeException(String.format("Unknown property '%s' on '%s'", this.key, stateManager.getOwner().toString()));
      } else {
         String string = this.valueString;
         boolean bl = !string.isEmpty() && string.charAt(0) == '!';
         if (bl) {
            string = string.substring(1);
         }

         List<String> list = VALUE_SPLITTER.splitToList(string);
         if (list.isEmpty()) {
            throw new RuntimeException(String.format("Empty value '%s' for property '%s' on '%s'", this.valueString, this.key, stateManager.getOwner().toString()));
         } else {
            Predicate<BlockState> predicate2;
            if (list.size() == 1) {
               predicate2 = this.createPredicate(stateManager, property, string);
            } else {
               List<Predicate<BlockState>> list2 = list.stream().map((stringx) -> this.createPredicate(stateManager, property, stringx)).collect(Collectors.toList());
               predicate2 = (blockState) -> list2.stream().anyMatch((predicate) -> predicate.test(blockState));
            }

            return bl ? predicate2.negate() : predicate2;
         }
      }
   }

   private Predicate<BlockState> createPredicate(StateManager<BlockBase, BlockState> stateFactory, Property<?> property, String valueString) {
      Optional<?> optional = property.parse(valueString);
      if (optional.isEmpty()) {
         throw new RuntimeException(String.format("Unknown value '%s' for property '%s' on '%s' in '%s'", valueString, this.key, stateFactory.getOwner().toString(), this.valueString));
      } else {
         return (blockState) -> blockState.get(property).equals(optional.get());
      }
   }

   public String toString() {
      return MoreObjects.toStringHelper(this).add("key", this.key).add("value", this.valueString).toString();
   }
}
