package net.modificationstation.stationapi.api.state.property;

import com.google.common.collect.ImmutableSet;

import java.util.Collection;
import java.util.Optional;

public class BooleanProperty extends Property<Boolean> {
   private final ImmutableSet<Boolean> values = ImmutableSet.of(true, false);

   protected BooleanProperty(String name) {
      super(name, Boolean.class);
   }

   @Override
   public Collection<Boolean> getValues() {
      return this.values;
   }

   public static BooleanProperty of(String name) {
      return new BooleanProperty(name);
   }

   @Override
   public Optional<Boolean> parse(String name) {
      return "true".equals(name) || "false".equals(name) ? Optional.of(Boolean.valueOf(name)) : Optional.empty();
   }

   @Override
   public String name(Boolean boolean_) {
      return boolean_.toString();
   }

   @Override
   public boolean equals(Object object) {
      return this == object || object instanceof BooleanProperty booleanProperty && super.equals(object) && this.values.equals(booleanProperty.values);
   }

   @Override
   public int computeHashCode() {
      return 31 * super.computeHashCode() + this.values.hashCode();
   }
}
