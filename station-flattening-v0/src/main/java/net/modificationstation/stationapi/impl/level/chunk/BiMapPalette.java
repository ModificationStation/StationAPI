package net.modificationstation.stationapi.impl.level.chunk;

import net.modificationstation.stationapi.api.util.collection.IndexedIterable;
import net.modificationstation.stationapi.api.util.collection.Int2ObjectBiMap;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * A palette backed by a bidirectional hash table.
 */
public class BiMapPalette<T>
implements Palette<T> {
    private final IndexedIterable<T> idList;
    private final Int2ObjectBiMap<T> map;
    private final PaletteResizeListener<T> listener;
    private final int indexBits;

    public BiMapPalette(IndexedIterable<T> idList, int bits, PaletteResizeListener<T> listener, List<T> entries) {
        this(idList, bits, listener);
        entries.forEach(this.map::add);
    }

    public BiMapPalette(IndexedIterable<T> idList, int indexBits, PaletteResizeListener<T> listener) {
        this(idList, indexBits, listener, Int2ObjectBiMap.create(1 << indexBits));
    }

    private BiMapPalette(IndexedIterable<T> indexedIterable, int i, PaletteResizeListener<T> paletteResizeListener, Int2ObjectBiMap<T> int2ObjectBiMap) {
        this.idList = indexedIterable;
        this.indexBits = i;
        this.listener = paletteResizeListener;
        this.map = int2ObjectBiMap;
    }

    public static <A> Palette<A> create(int bits, IndexedIterable<A> idList, PaletteResizeListener<A> listener, List<A> entries) {
        return new BiMapPalette<>(idList, bits, listener, entries);
    }

    @Override
    public int index(T object) {
        int i = this.map.getRawId(object);
        if (i == -1 && (i = this.map.add(object)) >= 1 << this.indexBits) {
            i = this.listener.onResize(this.indexBits + 1, object);
        }
        return i;
    }

    @Override
    public boolean hasAny(Predicate<T> predicate) {
        for (int i = 0; i < this.getSize(); ++i) {
            if (!predicate.test(this.map.get(i))) continue;
            return true;
        }
        return false;
    }

    @Override
    public T get(int id) {
        T object = this.map.get(id);
        if (object == null) {
            throw new EntryMissingException(id);
        }
        return object;
    }

//    @Override
//    public void readPacket(PacketByteBuf buf) {
//        this.map.clear();
//        int i = buf.readVarInt();
//        for (int j = 0; j < i; ++j) {
//            this.map.add(this.idList.getOrThrow(buf.readVarInt()));
//        }
//    }
//
//    @Override
//    public void writePacket(PacketByteBuf buf) {
//        int i = this.getSize();
//        buf.writeVarInt(i);
//        for (int j = 0; j < i; ++j) {
//            buf.writeVarInt(this.idList.getRawId(this.map.get(j)));
//        }
//    }
//
//    @Override
//    public int getPacketSize() {
//        int i = PacketByteBuf.getVarIntLength(this.getSize());
//        for (int j = 0; j < this.getSize(); ++j) {
//            i += PacketByteBuf.getVarIntLength(this.idList.getRawId(this.map.get(j)));
//        }
//        return i;
//    }

    public List<T> getElements() {
        List<T> arrayList = new ArrayList<>();
        this.map.iterator().forEachRemaining(arrayList::add);
        return arrayList;
    }

    @Override
    public int getSize() {
        return this.map.size();
    }

    @Override
    public Palette<T> copy() {
        return new BiMapPalette<>(this.idList, this.indexBits, this.listener, this.map.copy());
    }
}

