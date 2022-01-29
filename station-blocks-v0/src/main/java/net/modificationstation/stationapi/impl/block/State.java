package net.modificationstation.stationapi.impl.block;

import com.google.common.collect.ArrayTable;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.Map.*;
import java.util.function.*;
import java.util.stream.*;

public abstract class State<O, S> {
   private static final Function<Entry<Property<?>, Comparable<?>>, String> PROPERTY_MAP_PRINTER = new Function<Entry<Property<?>, Comparable<?>>, String>() {
      public String apply(@Nullable Entry<Property<?>, Comparable<?>> entry) {
         if (entry == null) {
            return "<NULL>";
         } else {
            Property<?> property = entry.getKey();
            return property.getName() + "=" + this.nameValue(property, entry.getValue());
         }
      }

      private <T extends Comparable<T>> String nameValue(Property<T> property, Comparable<?> value) {
         //noinspection unchecked
         return property.name((T) value);
      }
   };
   protected final O owner;
   private final ImmutableMap<Property<?>, Comparable<?>> entries;
   private Table<Property<?>, Comparable<?>, S> withTable;
   protected final MapCodec<S> codec;

   protected State(O owner, ImmutableMap<Property<?>, Comparable<?>> entries, MapCodec<S> codec) {
      this.owner = owner;
      this.entries = entries;
      this.codec = codec;
   }

   public <T extends Comparable<T>> S cycle(Property<T> property) {
      return this.with(property, getNext(property.getValues(), this.get(property)));
   }

   protected static <T> T getNext(Collection<T> values, T value) {
      Iterator<T> iterator = values.iterator();

      do {
         if (!iterator.hasNext()) {
            return iterator.next();
         }
      } while(!iterator.next().equals(value));

      if (iterator.hasNext()) {
         return iterator.next();
      } else {
         return values.iterator().next();
      }
   }

   public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(this.owner);
      if (!this.getEntries().isEmpty()) {
         stringBuilder.append('[');
         stringBuilder.append(this.getEntries().entrySet().stream().map(PROPERTY_MAP_PRINTER).collect(Collectors.joining(",")));
         stringBuilder.append(']');
      }

      return stringBuilder.toString();
   }

   public Collection<Property<?>> getProperties() {
      return Collections.unmodifiableCollection(this.entries.keySet());
   }

   public <T extends Comparable<T>> boolean contains(Property<T> property) {
      return this.entries.containsKey(property);
   }

   public <T extends Comparable<T>> T get(Property<T> property) {
      Comparable<?> comparable = this.entries.get(property);
      if (comparable == null) {
         throw new IllegalArgumentException("Cannot get property " + property + " as it does not exist in " + this.owner);
      } else {
         return property.getType().cast(comparable);
      }
   }

   public <T extends Comparable<T>> Optional<T> method_28500(Property<T> property) {
      Comparable<?> comparable = this.entries.get(property);
      return comparable == null ? Optional.empty() : Optional.of(property.getType().cast(comparable));
   }

   public <T extends Comparable<T>, V extends T> S with(Property<T> property, V value) {
      Comparable<?> comparable = this.entries.get(property);
      if (comparable == null) {
         throw new IllegalArgumentException("Cannot set property " + property + " as it does not exist in " + this.owner);
      } else if (comparable == value) {
         //noinspection unchecked
         return (S) this;
      } else {
         S object = this.withTable.get(property, value);
         if (object == null) {
            throw new IllegalArgumentException("Cannot set property " + property + " to " + value + " on " + this.owner + ", it is not an allowed value");
         } else {
            return object;
         }
      }
   }

   public void createWithTable(Map<Map<Property<?>, Comparable<?>>, S> states) {
      if (this.withTable != null) {
         throw new IllegalStateException();
      } else {
         Table<Property<?>, Comparable<?>, S> table = HashBasedTable.create();

         for (Entry<Property<?>, Comparable<?>> propertyComparableEntry : this.entries.entrySet()) {
            Property<?> property = propertyComparableEntry.getKey();

            for (Comparable<?> value : property.getValues()) {
               if (value != propertyComparableEntry.getValue()) {
                  table.put(property, value, states.get(this.toMapWith(property, value)));
               }
            }
         }

         this.withTable = table.isEmpty() ? table : ArrayTable.create(table);
      }
   }

   private Map<Property<?>, Comparable<?>> toMapWith(Property<?> property, Comparable<?> value) {
      Map<Property<?>, Comparable<?>> map = Maps.newHashMap(this.entries);
      map.put(property, value);
      return map;
   }

   public ImmutableMap<Property<?>, Comparable<?>> getEntries() {
      return this.entries;
   }

   protected static <O, S extends State<O, S>> Codec<S> createCodec(Codec<O> codec, Function<O, S> ownerToStateFunction) {
      return codec.dispatch("Name", (state) -> state.owner, (object) -> {
         S state = ownerToStateFunction.apply(object);
         return state.getEntries().isEmpty() ? Codec.unit(state) : state.codec.fieldOf("Properties").codec();
      });
   }
}
