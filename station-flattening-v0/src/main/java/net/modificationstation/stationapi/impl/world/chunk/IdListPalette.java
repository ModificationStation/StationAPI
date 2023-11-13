package net.modificationstation.stationapi.impl.world.chunk;

import net.modificationstation.stationapi.api.util.collection.IndexedIterable;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.function.Predicate;

/**
 * A palette that directly stores the raw ID of entries to the palette
 * container storage.
 */
public class IdListPalette<T> implements Palette<T> {
    private final IndexedIterable<T> idList;

    public IdListPalette(IndexedIterable<T> idList) {
        this.idList = idList;
    }

    public static <A> Palette<A> create(int bits, IndexedIterable<A> idList, PaletteResizeListener<A> listener, List<A> list) {
        return new IdListPalette<>(idList);
    }

    @Override
    public int index(T object) {
        int i = this.idList.getRawId(object);
        return i == -1 ? 0 : i;
    }

    @Override
    public boolean hasAny(Predicate<T> predicate) {
        return true;
    }

    @Override
    public T get(int id) {
        T object = this.idList.get(id);
        if (object == null) {
            throw new EntryMissingException(id);
        }
        return object;
    }

    @Override
    public void readPacket(ByteBuffer buf) {
    }

    @Override
    public void writePacket(ByteBuffer buf) {
    }

    @Override
    public int getPacketSize() {
        return 0;
    }

    @Override
    public int getSize() {
        return this.idList.size();
    }

    @Override
    public Palette<T> copy() {
        return this;
    }
}

