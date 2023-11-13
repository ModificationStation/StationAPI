package net.modificationstation.stationapi.impl.world.chunk;

import net.modificationstation.stationapi.api.util.collection.IndexedIterable;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.function.Predicate;

/**
 * A palette that only holds a unique entry. Useful for void chunks or a
 * single biome.
 */
public class SingularPalette<T> implements Palette<T> {
    private final IndexedIterable<T> idList;
    @Nullable
    private T entry;
    private final PaletteResizeListener<T> listener;

    public SingularPalette(IndexedIterable<T> idList, PaletteResizeListener<T> listener, List<T> entries) {
        this.idList = idList;
        this.listener = listener;
        if (entries.size() > 0) {
            Validate.isTrue(entries.size() <= 1, "Can't initialize SingleValuePalette with %d values.", entries.size());
            this.entry = entries.get(0);
        }
    }

    /**
     * Creates a singular palette. Used as method reference to create factory.
     * 
     * @param bitSize {@code 0}, as this palette has only 2<sup>0</sup>=1 entry
     */
    public static <A> Palette<A> create(int bitSize, IndexedIterable<A> idList, PaletteResizeListener<A> listener, List<A> entries) {
        return new SingularPalette<>(idList, listener, entries);
    }

    @Override
    public int index(T object) {
        if (this.entry == null || this.entry == object) {
            this.entry = object;
            return 0;
        }
        return this.listener.onResize(1, object);
    }

    @Override
    public boolean hasAny(Predicate<T> predicate) {
        if (this.entry == null) {
            throw new IllegalStateException("Use of an uninitialized palette");
        }
        return predicate.test(this.entry);
    }

    @Override
    public T get(int id) {
        if (this.entry == null || id != 0) {
            throw new IllegalStateException("Missing Palette entry for id " + id + ".");
        }
        return this.entry;
    }

    @Override
    public void readPacket(ByteBuffer buf) {
        this.entry = this.idList.getOrThrow(buf.getInt());
    }

    @Override
    public void writePacket(ByteBuffer buf) {
        if (this.entry == null) {
            throw new IllegalStateException("Use of an uninitialized palette");
        }
        buf.putInt(this.idList.getRawId(this.entry));
    }

    @Override
    public int getPacketSize() {
        if (this.entry == null) {
            throw new IllegalStateException("Use of an uninitialized palette");
        }
        return 4;
    }

    @Override
    public int getSize() {
        return 1;
    }

    @Override
    public Palette<T> copy() {
        if (this.entry == null) {
            throw new IllegalStateException("Use of an uninitialized palette");
        }
        return this;
    }
}

