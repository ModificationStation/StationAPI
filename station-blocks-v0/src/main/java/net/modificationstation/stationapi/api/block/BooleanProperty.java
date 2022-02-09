package net.modificationstation.stationapi.api.block;

import com.google.common.collect.ImmutableSet;

import java.util.*;

public class BooleanProperty extends Property<Boolean> {
   private final ImmutableSet<Boolean> values = ImmutableSet.of(true, false);

   protected BooleanProperty(String name) {
      super(name, Boolean.class);
   }

   public Collection<Boolean> getValues() {
      return this.values;
   }

   public static BooleanProperty of(String name) {
      return new BooleanProperty(name);
   }

   public Optional<Boolean> parse(String name) {
      return !"true".equals(name) && !"false".equals(name) ? Optional.empty() : Optional.of(Boolean.valueOf(name));
   }

   public String name(Boolean boolean_) {
      return boolean_.toString();
   }

   public boolean equals(Object object) {
      if (this == object) {
         return true;
      } else if (object instanceof BooleanProperty booleanProperty && super.equals(object)) {
         return this.values.equals(booleanProperty.values);
      } else {
         return false;
      }
   }

   public int computeHashCode() {
      return 31 * super.computeHashCode() + this.values.hashCode();
   }
}
