package net.modificationstation.stationapi.impl.level.chunk;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import net.modificationstation.stationapi.api.world.chunk.CompactingPackedIntegerArray;
import net.modificationstation.stationapi.api.util.Util;
import net.modificationstation.stationapi.api.util.collection.EmptyPaletteStorage;
import net.modificationstation.stationapi.api.util.collection.IndexedIterable;
import net.modificationstation.stationapi.api.util.collection.PackedIntegerArray;
import net.modificationstation.stationapi.api.util.collection.PaletteStorage;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import net.modificationstation.stationapi.impl.util.dynamic.Codecs;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;
import java.util.stream.LongStream;

/**
 * A paletted container stores objects in 3D voxels as small integer indices,
 * governed by "palettes" that map between these objects and indices.
 *
 * @see Palette
 */
public class PalettedContainer<T>
        implements PaletteResizeListener<T> {

    private static final ThreadLocal<short[]> CACHED_ARRAY_4096 = ThreadLocal.withInitial(() -> new short[4096]);
    private static final ThreadLocal<short[]> CACHED_ARRAY_64 = ThreadLocal.withInitial(() -> new short[64]);

    private final PaletteResizeListener<T> dummyListener = (newSize, added) -> 0;
    private final IndexedIterable<T> idList;
    private volatile Data<T> data;
    private final PaletteProvider paletteProvider;

    /**
     * Creates a codec for a paletted container with a specific palette provider.
     *
     * @return the created codec
     *
     * @param entryCodec the codec for each entry in the palette
     * @param provider the palette provider that controls how the data are serialized and what
     * types of palette are used for what entry bit sizes
     * @param idList the id list to map between objects and full integer IDs
     */
    public static <T> Codec<PalettedContainer<T>> createCodec(IndexedIterable<T> idList, Codec<T> entryCodec, PaletteProvider provider, T object) {
        return RecordCodecBuilder.<Serialized<T>>create(instance -> instance.group(entryCodec.mapResult(Codecs.orElsePartial(object)).listOf().fieldOf("palette").forGetter(Serialized::paletteEntries), Codec.LONG_STREAM.optionalFieldOf("data").forGetter(Serialized::storage)).apply(instance, Serialized::new)).comapFlatMap(serialized -> PalettedContainer.read(idList, provider, serialized), container -> container.write(idList, provider));
    }

    public PalettedContainer(IndexedIterable<T> idList, PaletteProvider paletteProvider, DataProvider<T> dataProvider, PaletteStorage storage, List<T> paletteEntries) {
        this.idList = idList;
        this.paletteProvider = paletteProvider;
        this.data = new Data<>(dataProvider, storage, dataProvider.factory().create(dataProvider.bits(), idList, this, paletteEntries));
    }

    private PalettedContainer(IndexedIterable<T> idList, PaletteProvider paletteProvider, Data<T> data) {
        this.idList = idList;
        this.paletteProvider = paletteProvider;
        this.data = data;
    }

    public PalettedContainer(IndexedIterable<T> idList, T object, PaletteProvider paletteProvider) {
        this.paletteProvider = paletteProvider;
        this.idList = idList;
        this.data = this.getCompatibleData(null, 0);
        this.data.palette.index(object);
    }

    /**
     * {@return a compatible data object for the given entry {@code bits} size}
     * This may return a new data object or return {@code previousData} if it
     * can be reused.
     *
     * @param bits the number of bits each entry uses
     * @param previousData the previous data, may be reused if suitable
     */
    private Data<T> getCompatibleData(@Nullable Data<T> previousData, int bits) {
        DataProvider<T> dataProvider = this.paletteProvider.createDataProvider(this.idList, bits);
        if (previousData != null && dataProvider.equals(previousData.configuration())) return previousData;
        return dataProvider.createData(this.idList, this, this.paletteProvider.getContainerSize());
    }

    @Override
    public int onResize(int i, T object) {
        Data<T> data = this.data;
        Data<T> data2 = this.getCompatibleData(data, i);
        data2.importFrom(data.palette, data.storage);
        this.data = data2;
        return data2.palette.index(object);
    }

    public T swap(int x, int y, int z, T value) {
        return this.swap(this.paletteProvider.computeIndex(x, y, z), value);
    }

    private T swap(int index, T value) {
        int i = this.data.palette.index(value);
        int j = this.data.storage.swap(index, i);
        return this.data.palette.get(j);
    }

    public void set(int x, int y, int z, T value) {
        this.set(this.paletteProvider.computeIndex(x, y, z), value);
    }

    private void set(int index, T value) {
        int i = this.data.palette.index(value);
        this.data.storage.set(index, i);
    }

    public T get(int x, int y, int z) {
        return this.get(this.paletteProvider.computeIndex(x, y, z));
    }

    protected T get(int index) {
        Data<T> data = this.data;
        return data.palette.get(data.storage.get(index));
    }

    public void method_39793(Consumer<T> consumer) {
        Palette<T> palette = this.data.palette();
        IntArraySet intSet = new IntArraySet();
        this.data.storage.forEach(intSet::add);
        intSet.forEach(id -> consumer.accept(palette.get(id)));
    }

//    /**
//     * Reads data from the packet byte buffer into this container. Previous data
//     * in this container is discarded.
//     *
//     * @param buf the packet byte buffer
//     */
//    public void readPacket(PacketByteBuf buf) {
//        this.lock();
//        try {
//            byte i = buf.readByte();
//            Data<T> data = this.getCompatibleData(this.data, i);
//            data.palette.readPacket(buf);
//            buf.readLongArray(data.storage.getData());
//            this.data = data;
//        }
//        finally {
//            this.unlock();
//        }
//    }

//    /**
//     * Writes this container to the packet byte buffer.
//     *
//     * @param buf the packet byte buffer
//     */
//    public void writePacket(PacketByteBuf buf) {
//        this.lock();
//        try {
//            this.data.writePacket(buf);
//        }
//        finally {
//            this.unlock();
//        }
//    }

    private static <T> DataResult<PalettedContainer<T>> read(IndexedIterable<T> idList, PaletteProvider provider, Serialized<T> serialized) {
        PaletteStorage paletteStorage;
        List<T> list = serialized.paletteEntries();
        int i2 = provider.getContainerSize();
        int j = provider.getBits(idList, list.size());
        DataProvider<T> dataProvider = provider.createDataProvider(idList, j);
        if (j == 0) paletteStorage = new EmptyPaletteStorage(i2);
        else {
            Optional<LongStream> optional = serialized.storage();
            if (optional.isEmpty()) return DataResult.error("Missing values for non-zero storage");
            long[] ls = optional.get().toArray();
            try {
                if (dataProvider.factory() == PaletteProvider.ID_LIST) {
                    BiMapPalette<T> palette = new BiMapPalette<>(idList, j, (i, object) -> 0, list);
                    PackedIntegerArray packedIntegerArray = new PackedIntegerArray(j, i2, ls);
                    int[] is = new int[i2];
                    packedIntegerArray.method_39892(is);
                    PalettedContainer.method_39894(is, i -> idList.getRawId(palette.get(i)));
                    paletteStorage = new PackedIntegerArray(dataProvider.bits(), i2, is);
                } else paletteStorage = new PackedIntegerArray(dataProvider.bits(), i2, ls);
            }
            catch (PackedIntegerArray.InvalidLengthException palette) {
                return DataResult.error("Failed to read PalettedContainer: " + palette.getMessage());
            }
        }
        return DataResult.success(new PalettedContainer<>(idList, provider, dataProvider, paletteStorage, list));
    }

    private Serialized<T> write(IndexedIterable<T> idList, PaletteProvider provider) {

        // The palette that will be serialized
        LithiumHashPalette<T> hashPalette = null;
        Optional<LongStream> data = Optional.empty();
        List<T> elements = null;
        final Palette<T> palette = this.data.palette();
        final PaletteStorage storage = this.data.storage();
        // If the palette only contains one entry, don't attempt to repack it.
        if (storage instanceof EmptyPaletteStorage || palette.getSize() == 1) elements = List.of(palette.get(0));
        else if (palette instanceof LithiumHashPalette<T> lithiumHashPalette) hashPalette = lithiumHashPalette;
        if (elements == null) {
            LithiumHashPalette<T> compactedPalette = new LithiumHashPalette<>(idList, storage.getElementBits(), this.dummyListener);
            int size = provider.getContainerSize();
            short[] array = switch (size) {
                case 64 -> CACHED_ARRAY_64.get();
                case 4096 -> CACHED_ARRAY_4096.get();
                default -> new short[size];
            };

            ((CompactingPackedIntegerArray) storage).compact(this.data.palette(), compactedPalette, array);

            // If the palette didn't change during compaction, do a simple copy of the data array
            if (hashPalette != null && hashPalette.getSize() == compactedPalette.getSize() && storage.getElementBits() == provider.getBits(idList, hashPalette.getSize())) { // paletteSize can de-sync from palette - see https://github.com/CaffeineMC/lithium-fabric/issues/279
                data = Optional.of(Arrays.stream(storage.getData().clone()));
                elements = hashPalette.getElements();
            } else {
                int bits = provider.getBits(idList, compactedPalette.getSize());
                if (bits != 0) {
                    // Re-pack the integer array as the palette has changed size
                    PackedIntegerArray copy = new PackedIntegerArray(bits, array.length);
                    for (int i = 0; i < array.length; ++i) copy.set(i, array[i]);

                    // We don't need to clone the data array as we are the sole owner of it
                    data = Optional.of(Arrays.stream(copy.getData()));
                }

                elements = compactedPalette.getElements();
            }
        }
        return new PalettedContainer.Serialized<>(elements, data);
    }

    private static void method_39894(int[] is, IntUnaryOperator intUnaryOperator) {
        int i = -1;
        int j = -1;
        for (int k = 0; k < is.length; ++k) {
            int l = is[k];
            if (l != i) {
                i = l;
                j = intUnaryOperator.applyAsInt(l);
            }
            is[k] = j;
        }
    }

//    public int getPacketSize() {
//        return this.data.getPacketSize();
//    }

    /**
     * {@return {@code true} if any object in this container's palette matches
     * this predicate}
     */
    public boolean hasAny(Predicate<T> predicate) {
        return this.data.palette.hasAny(predicate);
    }

    public PalettedContainer<T> copy() {
        return new PalettedContainer<>(this.idList, this.paletteProvider, new Data<>(this.data.configuration(), this.data.storage().copy(), this.data.palette().copy()));
    }

    public void count(Counter<T> counter) {

        int len = this.data.palette().getSize();

        // Do not allocate huge arrays if we're using a large palette
        if (len <= 4096) {
            short[] counts = new short[len];

            this.data.storage().forEach(i -> counts[i]++);

            for (int i = 0; i < counts.length; i++) {
                T obj = this.data.palette().get(i);

                if (obj != null) {
                    counter.accept(obj, counts[i]);
                }
            }
        } else {
            Int2IntOpenHashMap int2IntOpenHashMap = new Int2IntOpenHashMap();
            this.data.storage.forEach(key -> int2IntOpenHashMap.addTo(key, 1));
            int2IntOpenHashMap.int2IntEntrySet().forEach(entry -> counter.accept(this.data.palette.get(entry.getIntKey()), entry.getIntValue()));
        }

    }

    public static abstract class PaletteProvider {
        public static final Palette.Factory SINGULAR = SingularPalette::create;
        public static final Palette.Factory ARRAY = ArrayPalette::create;
        public static final Palette.Factory BI_MAP = BiMapPalette::create;
        static final Palette.Factory ID_LIST = IdListPalette::create;
        private static final Palette.Factory HASH = LithiumHashPalette::create;
        private static final PalettedContainer.DataProvider<?>[] BLOCKSTATE_DATA_PROVIDERS = Util.make(() -> {
            PalettedContainer.DataProvider<?> arrayDataProvider4bit = new PalettedContainer.DataProvider<>(ARRAY, 4);
            PalettedContainer.DataProvider<?> hashDataProvider4bit = new PalettedContainer.DataProvider<>(HASH, 4);
            return new PalettedContainer.DataProvider<?>[]{
                    new PalettedContainer.DataProvider<>(SINGULAR, 0),
                    // Bits 1-4 must all pass 4 bits as parameter, otherwise chunk sections will corrupt.
                    arrayDataProvider4bit,
                    arrayDataProvider4bit,
                    hashDataProvider4bit,
                    hashDataProvider4bit,
                    new PalettedContainer.DataProvider<>(HASH, 5),
                    new PalettedContainer.DataProvider<>(HASH, 6),
                    new PalettedContainer.DataProvider<>(HASH, 7),
                    new PalettedContainer.DataProvider<>(HASH, 8)
            };
        });
        public static final PaletteProvider BLOCK_STATE = new PalettedContainer.PaletteProvider(4) {

            @Override
            public <A> PalettedContainer.DataProvider<A> createDataProvider(IndexedIterable<A> idList, int bits) {
                //noinspection unchecked
                if (bits >= 0 && bits < BLOCKSTATE_DATA_PROVIDERS.length)
                    return (DataProvider<A>) BLOCKSTATE_DATA_PROVIDERS[bits];
                return new PalettedContainer.DataProvider<>(ID_LIST, MathHelper.ceilLog2(idList.size()));
            }
        };
        private final int edgeBits;

        PaletteProvider(int edgeBits) {
            this.edgeBits = edgeBits;
        }

        public int getContainerSize() {
            return 1 << this.edgeBits * 3;
        }

        public int computeIndex(int x, int y, int z) {
            return (y << this.edgeBits | z) << this.edgeBits | x;
        }

        public abstract <A> DataProvider<A> createDataProvider(IndexedIterable<A> var1, int var2);

        <A> int getBits(IndexedIterable<A> idList, int size) {
            int i = MathHelper.ceilLog2(size);
            DataProvider<A> dataProvider = this.createDataProvider(idList, i);
            return dataProvider.factory() == ID_LIST ? i : dataProvider.bits();
        }
    }

    public record Data<T>(DataProvider<T> configuration, PaletteStorage storage, Palette<T> palette) {
        public void importFrom(Palette<T> palette, PaletteStorage storage) {
            for (int i = 0; i < storage.size(); ++i) {
                T object = palette.get(storage.get(i));
                this.storage.set(i, this.palette.index(object));
            }
        }

//        public int getPacketSize() {
//            return 1 + this.palette.getPacketSize() + PacketByteBuf.getVarIntLength(this.storage.getSize()) + this.storage.getData().length * 8;
//        }
//
//        public void writePacket(PacketByteBuf buf) {
//            buf.writeByte(this.storage.getElementBits());
//            this.palette.writePacket(buf);
//            buf.writeLongArray(this.storage.getData());
//        }
    }

    public record DataProvider<T>(Palette.Factory factory, int bits) {
        public Data<T> createData(IndexedIterable<T> idList, PaletteResizeListener<T> listener, int size) {
            PaletteStorage paletteStorage = this.bits == 0 ? new EmptyPaletteStorage(size) : new PackedIntegerArray(this.bits, size);
            Palette<T> palette = this.factory.create(this.bits, idList, listener, List.of());
            return new Data<>(this, paletteStorage, palette);
        }
    }

    record Serialized<T>(List<T> paletteEntries, Optional<LongStream> storage) {
    }

    @FunctionalInterface
    public interface Counter<T> {
        void accept(T var1, int var2);
    }
}

