package net.modificationstation.stationapi.api.util.collection;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntMaps;
import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class IdList<T> implements IndexedIterable<T> {
    private int nextId;
    private final Reference2IntMap<T> idMap;
    private final List<T> list;

    public IdList() {
        this(512);
    }

    public IdList(int initialSize) {
        list = Lists.newArrayListWithExpectedSize(initialSize);
        idMap = new Reference2IntOpenHashMap<>(initialSize);
        idMap.defaultReturnValue(-1);
    }

    public void set(T value, int id) {
        this.idMap.put(value, id);

        while(this.list.size() <= id) this.list.add(null);

        this.list.set(id, value);
        if (this.nextId <= id) this.nextId = id + 1;

    }

    public void add(T value) {
        this.set(value, this.nextId);
    }

    @Override
    public int getRawId(T entry) {
        return this.idMap.getInt(entry);
    }

    @Nullable
    @Override
    public final T get(int index) {
        return index >= 0 && index < this.list.size() ? this.list.get(index) : null;
    }

    public Iterator<T> iterator() {
        return Iterators.filter(this.list.iterator(), Objects::nonNull);
    }

    public boolean containsKey(int index) {
        return this.get(index) != null;
    }

    public int size() {
        return this.idMap.size();
    }

    public void clear() {
        nextId = 0;
        idMap.clear();
        list.clear();
    }

    private void removeInner(T o) {
        int value = idMap.removeInt(o);
        list.set(value, null);

        while (nextId > 1 && list.get(nextId - 1) == null) nextId--;
    }

    public void remove(T o) {
        if (idMap.containsKey(o)) removeInner(o);
    }

    public void removeId(int i) {
        List<T> removals = new ArrayList<>();

        for (T o : idMap.keySet()) {
            int j = idMap.getInt(o);

            if (i == j) removals.add(o);
        }

        removals.forEach(this::removeInner);
    }

    public void remapId(int from, int to) {
        remapIds(Int2IntMaps.singleton(from, to));
    }

    public void remapIds(Int2IntMap map) {
        // remap idMap
        idMap.replaceAll((a, b) -> map.get((int) b));

        // remap list
        nextId = 0;
        List<T> oldList = new ArrayList<>(list);
        list.clear();

        for (int k = 0; k < oldList.size(); k++) {
            T o = oldList.get(k);

            if (o != null) {
                int i = map.getOrDefault(k, k);

                while (list.size() <= i) list.add(null);

                list.set(i, o);

                if (nextId <= i) nextId = i + 1;
            }
        }
    }
}
