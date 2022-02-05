package net.modificationstation.stationapi.impl.block;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.util.*;

public class IntProperty extends Property<Integer> {
   private final ImmutableSet<Integer> values;

   protected IntProperty(String name, int min, int max) {
      super(name, Integer.class);
      if (min < 0) {
         throw new IllegalArgumentException("Min value of " + name + " must be 0 or greater");
      } else if (max <= min) {
         throw new IllegalArgumentException("Max value of " + name + " must be greater than min (" + min + ")");
      } else {
         Set<Integer> set = Sets.newHashSet();

         for(int i = min; i <= max; ++i) {
            set.add(i);
         }

         this.values = ImmutableSet.copyOf(set);
      }
   }

   public Collection<Integer> getValues() {
      return this.values;
   }

   public boolean equals(Object object) {
      if (this == object) {
         return true;
      } else if (object instanceof IntProperty intProperty && super.equals(object)) {
         return this.values.equals(intProperty.values);
      } else {
         return false;
      }
   }

   public int computeHashCode() {
      return 31 * super.computeHashCode() + this.values.hashCode();
   }

   /**
    * Creates an integer property.
    * 
    * <p>{@code min} must be non-negative and {@code max} must be greater than {@code min}.
    * 
    * <p>Note that this method takes O({@code max} - {@code min}) time as it computes all possible values during instantiation.
    * 
    * @param name the name of the property
    * @param min the minimum value the property can take
    * @param max the maximum value the property can take
    */
   public static IntProperty of(String name, int min, int max) {
      return new IntProperty(name, min, max);
   }

   public Optional<Integer> parse(String name) {
      try {
         Integer integer = Integer.valueOf(name);
         return this.values.contains(integer) ? Optional.of(integer) : Optional.empty();
      } catch (NumberFormatException var3) {
         return Optional.empty();
      }
   }

   public String name(Integer integer) {
      return integer.toString();
   }
}
