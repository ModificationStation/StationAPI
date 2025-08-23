package net.modificationstation.stationapi.api.state;

import com.google.common.base.MoreObjects;
import com.google.common.collect.*;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.MapCodec;
import net.modificationstation.stationapi.api.state.property.Property;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StateManager<O, S extends State<O, S>> {
   private static final Pattern VALID_NAME_PATTERN = Pattern.compile("^[a-z0-9_]+$");
   private final O owner;
   private final ImmutableSortedMap<String, Property<?>> properties;
   private final ImmutableList<S> states;

   protected StateManager(Function<O, S> function, O object, StateManager.Factory<O, S> factory, Map<String, Property<?>> propertiesMap) {
      this.owner = object;
      this.properties = ImmutableSortedMap.copyOf(propertiesMap);
      Supplier<S> supplier = () -> function.apply(object);
      MapCodec<S> mapCodec = MapCodec.of(Encoder.empty(), Decoder.unit(supplier));

      Entry<String, Property<?>> entry;
      for(UnmodifiableIterator<Entry<String, Property<?>>> var7 = this.properties.entrySet().iterator(); var7.hasNext(); mapCodec = addFieldToMapCodec(mapCodec, supplier, entry.getKey(), entry.getValue())) {
         entry = var7.next();
      }

      Map<Map<Property<?>, Comparable<?>>, S> map = Maps.newLinkedHashMap();
      List<S> list = Lists.newArrayList();
      Stream<List<Pair<Property<?>, Comparable<?>>>> stream = Stream.of(Collections.emptyList());

      for (Property<?> value : this.properties.values()) {
         stream = stream.flatMap((listx) -> value.getValues().stream().map((comparable) -> {
            List<Pair<Property<?>, Comparable<?>>> list2 = Lists.newArrayList(listx);
            list2.add(Pair.of(value, comparable));
            return list2;
         }));
      }

      MapCodec<S> finalMapCodec = mapCodec;
      stream.forEach((list2) -> {
         ImmutableMap<Property<?>, Comparable<?>> immutableMap = list2.stream().collect(ImmutableMap.toImmutableMap(Pair::getFirst, Pair::getSecond));
         S state = factory.create(object, immutableMap, finalMapCodec);
         map.put(immutableMap, state);
         list.add(state);
      });

      for (S s : list) {
         s.createWithTable(map);
      }

      this.states = ImmutableList.copyOf(list);
   }

   private static <S extends State<?, S>, T extends Comparable<T>> MapCodec<S> addFieldToMapCodec(MapCodec<S> mapCodec, Supplier<S> supplier, String string, Property<T> property) {
      return Codec.mapPair(mapCodec, property.getValueCodec().fieldOf(string).setPartial(() -> property.createValue(supplier.get()))).xmap((pair) -> pair.getFirst().with(property, pair.getSecond().getValue()), (state) -> Pair.of(state, property.createValue(state)));
   }

   public ImmutableList<S> getStates() {
      return this.states;
   }

   public S getDefaultState() {
      return this.states.get(0);
   }

   public O getOwner() {
      return this.owner;
   }

   public Collection<Property<?>> getProperties() {
      return this.properties.values();
   }

   public String toString() {
      return MoreObjects.toStringHelper(this).add("block", this.owner).add("properties", this.properties.values().stream().map(Property::getName).collect(Collectors.toList())).toString();
   }

   @Nullable
   public Property<?> getProperty(String name) {
      return this.properties.get(name);
   }

   public static class Builder<O, S extends State<O, S>> {
      private final O owner;
      private final Map<String, Property<?>> namedProperties = Maps.newHashMap();

      public Builder(O owner) {
         this.owner = owner;
      }

      public StateManager.Builder<O, S> add(Property<?>... properties) {

         for (Property<?> property : properties) {
            this.validate(property);
            this.namedProperties.put(property.getName(), property);
         }

         return this;
      }

      private <T extends Comparable<T>> void validate(Property<T> property) {
         String string = property.getName();
         if (!StateManager.VALID_NAME_PATTERN.matcher(string).matches()) {
            throw new IllegalArgumentException(this.owner + " has invalidly named property: " + string);
         } else {
            Collection<T> collection = property.getValues();
            if (collection.size() <= 1) {
               throw new IllegalArgumentException(this.owner + " attempted use property " + string + " with <= 1 possible values");
            } else {
               Iterator<T> var4 = collection.iterator();

               String string2;
               do {
                  if (!var4.hasNext()) {
                     if (this.namedProperties.containsKey(string)) {
                        throw new IllegalArgumentException(this.owner + " has duplicate property: " + string);
                     }

                     return;
                  }

                  T comparable = var4.next();
                  string2 = property.name(comparable);
               } while(StateManager.VALID_NAME_PATTERN.matcher(string2).matches());

               throw new IllegalArgumentException(this.owner + " has property: " + string + " with invalidly named value: " + string2);
            }
         }
      }

      public StateManager<O, S> build(Function<O, S> ownerToStateFunction, StateManager.Factory<O, S> factory) {
         return new StateManager<>(ownerToStateFunction, this.owner, factory, this.namedProperties);
      }
   }

   public interface Factory<O, S> {
      S create(O owner, ImmutableMap<Property<?>, Comparable<?>> entries, MapCodec<S> mapCodec);
   }
}
