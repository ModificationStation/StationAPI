package net.modificationstation.stationapi.api.util.collection;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import org.jetbrains.annotations.Nullable;

import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class IdList<T> implements IndexedIterable<T> {
   private int nextId;
   private final IdentityHashMap<T, Integer> idMap;
   private final List<T> list;

   public IdList() {
      this(512);
   }

   public IdList(int initialSize) {
      this.list = Lists.newArrayListWithExpectedSize(initialSize);
      this.idMap = new IdentityHashMap<>(initialSize);
   }

   public void set(T value, int id) {
      this.idMap.put(value, id);

      while(this.list.size() <= id) {
         this.list.add(null);
      }

      this.list.set(id, value);
      if (this.nextId <= id) {
         this.nextId = id + 1;
      }

   }

   public void add(T value) {
      this.set(value, this.nextId);
   }

   @Override
   public int getRawId(T entry) {
      Integer integer = this.idMap.get(entry);
      return integer == null ? -1 : integer;
   }

   @Nullable
   @Override
   public final T get(int index) {
      return index >= 0 && index < this.list.size() ? this.list.get(index) : null;
   }

   public Iterator<T> iterator() {
      return Iterators.filter(this.list.iterator(), Objects::nonNull);
   }

   public int size() {
      return this.idMap.size();
   }
}
