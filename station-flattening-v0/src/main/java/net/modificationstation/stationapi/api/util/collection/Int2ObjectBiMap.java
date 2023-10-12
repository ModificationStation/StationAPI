package net.modificationstation.stationapi.api.util.collection;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterators;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Iterator;

public class Int2ObjectBiMap<K> implements IndexedIterable<K> {
    private static final int ABSENT = -1;
    private static final Object EMPTY = null;
    private static final float LOAD_FACTOR = 0.8f;
    private K[] values;
    private int[] ids;
    private K[] idToValues;
    private int nextId;
    private int size;

    private Int2ObjectBiMap(int size) {
       //noinspection unchecked
       this.values = (K[]) new Object[size];
        this.ids = new int[size];
       //noinspection unchecked
       this.idToValues = (K[]) new Object[size];
    }

    private Int2ObjectBiMap(K[] objects, int[] is, K[] objects2, int i, int j) {
        this.values = objects;
        this.ids = is;
        this.idToValues = objects2;
        this.nextId = i;
        this.size = j;
    }

    public static <A> Int2ObjectBiMap<A> create(int expectedSize) {
        return new Int2ObjectBiMap<>((int)((float)expectedSize / LOAD_FACTOR));
    }

    @Override
    public int getRawId(@Nullable K value) {
        return this.getIdFromIndex(this.findIndex(value, this.getIdealIndex(value)));
    }

    @Override
    @Nullable
    public K get(int index) {
        if (index < 0 || index >= this.idToValues.length) {
            return null;
        }
        return this.idToValues[index];
    }

    private int getIdFromIndex(int index) {
        if (index == ABSENT) {
            return ABSENT;
        }
        return this.ids[index];
    }

    public boolean contains(K value) {
        return this.getRawId(value) != ABSENT;
    }

    public boolean containsKey(int index) {
        return this.get(index) != null;
    }

    public int add(K value) {
        int i = this.nextId();
        this.put(value, i);
        return i;
    }

    private int nextId() {
        while (this.nextId < this.idToValues.length && this.idToValues[this.nextId] != null) {
            ++this.nextId;
        }
        return this.nextId;
    }

    private void resize(int newSize) {
        K[] objects = this.values;
        int[] is = this.ids;
        Int2ObjectBiMap<K> int2ObjectBiMap = new Int2ObjectBiMap<>(newSize);
        for (int i = 0; i < objects.length; ++i) {
            if (objects[i] == null) continue;
            int2ObjectBiMap.put(objects[i], is[i]);
        }
        this.values = int2ObjectBiMap.values;
        this.ids = int2ObjectBiMap.ids;
        this.idToValues = int2ObjectBiMap.idToValues;
        this.nextId = int2ObjectBiMap.nextId;
        this.size = int2ObjectBiMap.size;
    }

    public void put(K value, int id) {
        int j;
        int i = Math.max(id, this.size + 1);
        if ((float)i >= (float)this.values.length * LOAD_FACTOR) {
           j = this.values.length << 1;
           while (j < id) {
              j <<= 1;
           }
           this.resize(j);
        }
        j = this.findFree(this.getIdealIndex(value));
        this.values[j] = value;
        this.ids[j] = id;
        this.idToValues[id] = value;
        ++this.size;
        if (id == this.nextId) {
            ++this.nextId;
        }
    }

    private int getIdealIndex(@Nullable K value) {
        return (MathHelper.idealHash(System.identityHashCode(value)) & Integer.MAX_VALUE) % this.values.length;
    }

    private int findIndex(@Nullable K value, int id) {
        int i;
        for (i = id; i < this.values.length; ++i) {
            if (this.values[i] == value) {
                return i;
            }
            if (this.values[i] != EMPTY) continue;
            return ABSENT;
        }
        for (i = 0; i < id; ++i) {
            if (this.values[i] == value) {
                return i;
            }
            if (this.values[i] != EMPTY) continue;
            return ABSENT;
        }
        return ABSENT;
    }

    private int findFree(int size) {
        int i;
        for (i = size; i < this.values.length; ++i) {
            if (this.values[i] != EMPTY) continue;
            return i;
        }
        for (i = 0; i < size; ++i) {
            if (this.values[i] != EMPTY) continue;
            return i;
        }
        throw new RuntimeException("Overflowed :(");
    }

    @Override
    public Iterator<K> iterator() {
        return Iterators.filter(Iterators.forArray(this.idToValues), Predicates.notNull());
    }

    public void clear() {
        Arrays.fill(this.values, null);
        Arrays.fill(this.idToValues, null);
        this.nextId = 0;
        this.size = 0;
    }

    @Override
    public int size() {
        return this.size;
    }

    public Int2ObjectBiMap<K> copy() {
        return new Int2ObjectBiMap<>(this.values.clone(), this.ids.clone(), this.idToValues.clone(), this.nextId, this.size);
    }
}

