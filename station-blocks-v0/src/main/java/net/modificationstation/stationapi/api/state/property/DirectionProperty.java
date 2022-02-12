package net.modificationstation.stationapi.api.state.property;

import com.google.common.collect.Lists;
import net.modificationstation.stationapi.api.util.math.Direction;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class DirectionProperty extends EnumProperty<Direction> {
   protected DirectionProperty(String name, Collection<Direction> values) {
      super(name, Direction.class, values);
   }

   /**
    * Creates a direction property.
    * 
    * @param name the name of this property
    * @param filter a filter which specifies if a value is allowed
    */
   public static DirectionProperty of(String name, Predicate<Direction> filter) {
      return of(name, Arrays.stream(Direction.values()).filter(filter).collect(Collectors.toList()));
   }

   /**
    * Creates a direction property which only supports specific values
    * 
    * @param name the name of this property
    * @param values the values this property can have
    */
   public static DirectionProperty of(String name, Direction... values) {
      return of(name, Lists.newArrayList(values));
   }

   /**
    * Creates a direction property which only supports specific values
    * 
    * @param name the name of this property
    * @param values the values this property can have
    */
   public static DirectionProperty of(String name, Collection<Direction> values) {
      return new DirectionProperty(name, values);
   }
}
