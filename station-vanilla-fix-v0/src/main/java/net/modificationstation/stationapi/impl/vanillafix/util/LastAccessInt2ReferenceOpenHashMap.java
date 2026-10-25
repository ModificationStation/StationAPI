package net.modificationstation.stationapi.impl.vanillafix.util;

import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.ints.Int2ReferenceOpenHashMap;

import java.util.Map;

/**
 * An {@link Int2ReferenceOpenHashMap} that remembers the slot of the last successful lookup and checks it first.
 *
 * <p>Meant for chunk maps: lighting calls {@code isChunkLoaded} and then {@code getChunk} for nearly every block
 * it touches, mostly for the same chunk.
 *
 * <p>The remembered slot is never invalidated on writes. It's validated against the live arrays instead: since keys
 * are unique, a slot that holds the key and a non-null value is that key's entry. This stays correct however the map
 * is modified, because fastutil nulls the value of every slot it empties (removals, key shifts, iterator removals,
 * {@link #clear()}), keeps the {@code 0} key's entry at slot {@code n} where {@code key[n]} is always {@code 0},
 * and replaces the arrays only in {@link #rehash(int)}, which resets the slot so it stays in bounds.
 * Entries with {@code null} values never hit the remembered slot and take the regular lookup.
 */
public final class LastAccessInt2ReferenceOpenHashMap<V> extends Int2ReferenceOpenHashMap<V> {
    /**
     * Slot of the last successful lookup. Always in bounds: {@code 0} is a valid slot in every table size,
     * and transient fields come back as {@code 0} after deserialization too.
     */
    private transient int lastPos;

    public LastAccessInt2ReferenceOpenHashMap() {
        super();
    }

    public LastAccessInt2ReferenceOpenHashMap(Map<? extends Integer, ? extends V> m) {
        super(m);
    }

    @Override
    public V get(int k) {
        final int pos = lastPos;
        final V v;
        if (key[pos] == k && (v = value[pos]) != null)
            return v;
        final int found = find(k);
        return found < 0 ? defRetValue : value[found];
    }

    @Override
    public boolean containsKey(int k) {
        final int pos = lastPos;
        return key[pos] == k && value[pos] != null || find(k) >= 0;
    }

    @Override
    protected void rehash(int newN) {
        lastPos = 0;
        super.rehash(newN);
    }

    /**
     * Same probing as {@link Int2ReferenceOpenHashMap#get(int)}, but remembers the slot.
     * Kept out of {@link #get(int)} and {@link #containsKey(int)} so they stay small enough to inline.
     *
     * @return the key's slot, or {@code -1} if it's absent.
     */
    private int find(int k) {
        if (k == 0) {
            if (!containsNullKey)
                return -1;
            return lastPos = n;
        }
        final int[] key = this.key;
        int pos = HashCommon.mix(k) & mask;
        int curr;
        while ((curr = key[pos]) != k) {
            if (curr == 0)
                return -1;
            pos = pos + 1 & mask;
        }
        return lastPos = pos;
    }
}
