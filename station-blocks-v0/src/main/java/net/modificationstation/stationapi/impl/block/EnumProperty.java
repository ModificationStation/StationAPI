package net.modificationstation.stationapi.impl.block;

import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.modificationstation.stationapi.api.util.StringIdentifiable;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class EnumProperty<T extends Enum<T> & StringIdentifiable> extends Property<T> {
   private final ImmutableSet<T> values;
   private final Map<String, T> byName = Maps.newHashMap();

   protected EnumProperty(String name, Class<T> type, Collection<T> values) {
      super(name, type);
      this.values = ImmutableSet.copyOf(values);

      for (T value : values) {
         String string = value.asString();
         if (this.byName.containsKey(string)) {
            throw new IllegalArgumentException("Multiple values have the same name '" + string + "'");
         }

         this.byName.put(string, value);
      }

   }

   public Collection<T> getValues() {
      return this.values;
   }

   public Optional<T> parse(String name) {
      return Optional.ofNullable(this.byName.get(name));
   }

   public String name(T enum_) {
      return enum_.asString();
   }

   public boolean equals(Object object) {
      if (this == object) {
         return true;
      } else if (object instanceof EnumProperty<?> enumProperty && super.equals(object)) {
         return this.values.equals(enumProperty.values) && this.byName.equals(enumProperty.byName);
      } else {
         return false;
      }
   }

   public int computeHashCode() {
      int i = super.computeHashCode();
      i = 31 * i + this.values.hashCode();
      i = 31 * i + this.byName.hashCode();
      return i;
   }

   /**
    * Creates an enum property.
    * 
    * @param name the name of this property
    * @param type the type this property contains
    */
   public static <T extends Enum<T> & StringIdentifiable> EnumProperty<T> of(String name, Class<T> type) {
      return of(name, type, Predicates.alwaysTrue());
   }

   /**
    * Creates an enum property.
    * 
    * @param name the name of this property
    * @param type the type this property contains
    * @param filter a filter that specifies if a value is allowed
    */
   public static <T extends Enum<T> & StringIdentifiable> EnumProperty<T> of(String name, Class<T> type, Predicate<T> filter) {
      return of(name, type, Arrays.stream(type.getEnumConstants()).filter(filter).collect(Collectors.toList()));
   }

   public static <T extends Enum<T> & StringIdentifiable> EnumProperty<T> of(String name, Class<T> type, T... values) {
      return of(name, type, Lists.newArrayList(values));
   }

   /**
    * Creates an enum property.
    * 
    * @param name the name of this property
    * @param type the type this property contains
    * @param values the values this property could contain
    */
   public static <T extends Enum<T> & StringIdentifiable> EnumProperty<T> of(String name, Class<T> type, Collection<T> values) {
      return new EnumProperty<>(name, type, values);
   }
}
