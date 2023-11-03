package net.modificationstation.stationapi.api.nbt;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.PeekingIterator;
import com.google.common.primitives.Bytes;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import net.minecraft.nbt.NbtByte;
import net.minecraft.nbt.NbtByteArray;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtEnd;
import net.minecraft.nbt.NbtFloat;
import net.minecraft.nbt.NbtInt;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtLong;
import net.minecraft.nbt.NbtShort;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.io.*;
import net.modificationstation.stationapi.api.util.Util;
import net.modificationstation.stationapi.mixin.nbt.CompoundTagAccessor;
import net.modificationstation.stationapi.mixin.nbt.ListTagAccessor;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.Nullable;

import java.io.DataInput;
import java.io.DataOutput;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.function.*;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * Used to handle Minecraft NBTs within {@link com.mojang.serialization.Dynamic
 * dynamics} for DataFixerUpper, allowing generalized serialization logic
 * shared across different type of data structures. Use {@link NbtOps#INSTANCE}
 * for the ops singleton.
 * 
 * <p>For instance, dimension data may be stored as JSON in data packs, but
 * they will be transported in packets as NBT. DataFixerUpper allows
 * generalizing the dimension serialization logic to prevent duplicate code,
 * where the NBT ops allow the DataFixerUpper dimension serialization logic
 * to interact with Minecraft NBTs.
 * 
 * @see NbtOps#INSTANCE
 */
public class NbtOps implements DynamicOps<NbtElement> {
    /**
     * An singleton of the NBT dynamic ops.
     * 
     * <p>This ops does not compress maps (replace field name to value pairs
     * with an ordered list of values in serialization). In fact, since
     * Minecraft NBT lists can only contain elements of the same type, this op
     * cannot compress maps.
     */
    public static final NbtOps INSTANCE = new NbtOps();

    private static final NbtElement NBT_END = new NbtEnd();

    protected NbtOps() {
    }

    @Override
    public NbtElement empty() {
        return NBT_END;
    }

    @Override
    public <U> U convertTo(DynamicOps<U> dynamicOps, NbtElement nbtElement) {
        return switch (nbtElement.getType()) {
            case 0 -> dynamicOps.empty();
            case 1 -> dynamicOps.createByte(((NbtByte) nbtElement).value);
            case 2 -> dynamicOps.createShort(((NbtShort) nbtElement).value);
            case 3 -> dynamicOps.createInt(((NbtInt) nbtElement).value);
            case 4 -> dynamicOps.createLong(((NbtLong) nbtElement).value);
            case 5 -> dynamicOps.createFloat(((NbtFloat) nbtElement).value);
            case 6 -> dynamicOps.createDouble(((NbtDouble) nbtElement).value);
            case 7 -> dynamicOps.createByteList(ByteBuffer.wrap(((NbtByteArray) nbtElement).value));
            case 8 -> dynamicOps.createString(((NbtString) nbtElement).value);
            case 9 -> convertList(dynamicOps, nbtElement);
            case 10 -> convertMap(dynamicOps, nbtElement);
            case 11 -> dynamicOps.createIntList(Arrays.stream(((NbtIntArray) nbtElement).data));
            case 12 -> dynamicOps.createLongList(Arrays.stream(((NbtLongArray) nbtElement).data));
            default -> throw new IllegalStateException("Unknown tag type: " + nbtElement);
        };
    }

    @Override
    public DataResult<Number> getNumberValue(NbtElement nbtElement) {
        return switch (nbtElement.getType()) {
            case 1 -> DataResult.success(((NbtByte) nbtElement).value);
            case 2 -> DataResult.success(((NbtShort) nbtElement).value);
            case 3 -> DataResult.success(((NbtInt) nbtElement).value);
            case 4 -> DataResult.success(((NbtLong) nbtElement).value);
            case 5 -> DataResult.success(((NbtFloat) nbtElement).value);
            case 6 -> DataResult.success(((NbtDouble) nbtElement).value);
            default -> DataResult.error(() -> "Not a number");
        };
    }

    @Override
    public NbtElement createNumeric(Number number) {
        return new NbtDouble(number.doubleValue());
    }

    @Override
    public NbtElement createByte(byte b) {
        return new NbtByte(b);
    }

    @Override
    public NbtElement createShort(short s) {
        return new NbtShort(s);
    }

    @Override
    public NbtElement createInt(int i) {
        return new NbtInt(i);
    }

    @Override
    public NbtElement createLong(long l) {
        return new NbtLong(l);
    }

    @Override
    public NbtElement createFloat(float f) {
        return new NbtFloat(f);
    }

    @Override
    public NbtElement createDouble(double d) {
        return new NbtDouble(d);
    }

    @Override
    public NbtElement createBoolean(boolean bl) {
        return new NbtByte((byte) (bl ? 1 : 0));
    }

    @Override
    public DataResult<String> getStringValue(NbtElement nbtElement) {
        return nbtElement instanceof NbtString stringTag ? DataResult.success(stringTag.value) : DataResult.error(() -> "Not a string");
    }

    @Override
    public NbtElement createString(String string) {
        return new NbtString(string);
    }

    private static class ListArrayTag<T extends NbtElement> extends NbtList {

        private final T array;

        private <U extends NbtElement> ListArrayTag(
                final T array,
                final Function<T, Predicate<U>> adderFactory,
                final Function<T, IntFunction<U>> getterFactory,
                final Function<T, IntSupplier> sizeGetterFactory,
                final byte listTypeId
        ) {
            this.array = array;
            final Predicate<U> adder = adderFactory.apply(array);
            final IntFunction<U> getter = getterFactory.apply(array);
            final IntSupplier sizeGetter = sizeGetterFactory.apply(array);
            final ListTagAccessor _super = (ListTagAccessor) this;
            _super.stationapi$setData(new AbstractList<U>() {

                @Override
                public boolean add(U abstractTag) {
                    return adder.test(abstractTag);
                }

                @Override
                public U get(int index) {
                    return getter.apply(index);
                }

                @Override
                public int size() {
                    return sizeGetter.getAsInt();
                }
            });
            _super.stationapi$setListTypeId(listTypeId);
        }

        @Override
        public byte getType() {
            return array.getType();
        }

        @Override
        public String toString() {
            return array.toString();
        }

        @Override
        public void write(DataOutput out) {
            array.write(out);
        }

        @Override
        public void read(DataInput in) {
            array.read(in);
        }

        @Override
        public String getKey() {
            return array.getKey();
        }

        @Override
        public NbtElement setKey(String string) {
            return array.setKey(string);
        }
    }

    private static NbtList createList(byte knownType, byte valueType) {
        if (NbtOps.isTypeEqual(knownType, valueType, (byte) 4)) return new ListArrayTag<>(new NbtLongArray(new long[0]), array -> Predicates.compose(Predicates.alwaysTrue(), tag -> array.data = ArrayUtils.insert(array.data.length, array.data, tag.value)), array -> i -> new NbtLong(array.data[i]), array -> () -> array.data.length, (byte) 4);
        if (NbtOps.isTypeEqual(knownType, valueType, (byte) 1)) return new ListArrayTag<>(new NbtByteArray(new byte[0]), array -> Predicates.compose(Predicates.alwaysTrue(), tag -> array.value = ArrayUtils.insert(array.value.length, array.value, tag.value)), array -> i -> new NbtByte(array.value[i]), array -> () -> array.value.length, (byte) 1);
        if (NbtOps.isTypeEqual(knownType, valueType, (byte) 3)) return new ListArrayTag<>(new NbtIntArray(new int[0]), array -> Predicates.compose(Predicates.alwaysTrue(), tag -> array.data = ArrayUtils.insert(array.data.length, array.data, tag.value)), array -> i -> new NbtInt(array.data[i]), array -> () -> array.data.length, (byte) 3);
        return new NbtList();
    }

    private static boolean isTypeEqual(byte knownType, byte valueType, byte expectedType) {
        return knownType == expectedType && (valueType == expectedType || valueType == 0);
    }

    private static void addAll(NbtList destination, NbtElement source, NbtElement additionalValue) {
        if (source instanceof NbtList abstractNbtList) {
            IntStream.of(0, abstractNbtList.size()).mapToObj(abstractNbtList::get).forEach(destination::add);
        }
        destination.add(additionalValue);
    }

    private static void addAll(NbtList destination, NbtElement source, List<NbtElement> additionalValues) {
        if (source instanceof NbtList abstractNbtList) {
            IntStream.of(0, abstractNbtList.size()).mapToObj(abstractNbtList::get).forEach(destination::add);
        }
        additionalValues.forEach(destination::add);
    }

    @Override
    public DataResult<NbtElement> mergeToList(NbtElement nbtElement, NbtElement nbtElement2) {
        if (!(nbtElement instanceof NbtList) && !(nbtElement instanceof NbtEnd))
            return DataResult.error(() -> "mergeToList called with not a list: " + nbtElement, nbtElement);
        NbtList abstractNbtList = NbtOps.createList(nbtElement instanceof NbtList ? ((ListTagAccessor)nbtElement).stationapi$getListTypeId() : (byte)0, nbtElement2.getType());
        NbtOps.addAll(abstractNbtList, nbtElement, nbtElement2);
        return DataResult.success(abstractNbtList);
    }

    @Override
    public DataResult<NbtElement> mergeToList(NbtElement nbtElement, List<NbtElement> list) {
        if (!(nbtElement instanceof NbtList) && !(nbtElement instanceof NbtEnd))
            return DataResult.error(() -> "mergeToList called with not a list: " + nbtElement, nbtElement);
        NbtList abstractNbtList = NbtOps.createList(nbtElement instanceof NbtList ? ((ListTagAccessor)nbtElement).stationapi$getListTypeId() : (byte)0, list.stream().findFirst().map(NbtElement::getType).orElse((byte)0));
        NbtOps.addAll(abstractNbtList, nbtElement, list);
        return DataResult.success(abstractNbtList);
    }

    @Override
    public DataResult<NbtElement> mergeToMap(NbtElement nbtElement, NbtElement nbtElement2, NbtElement nbtElement3) {
        if (!(nbtElement instanceof NbtCompound) && !(nbtElement instanceof NbtEnd))
            return DataResult.error(() -> "mergeToMap called with not a map: " + nbtElement, nbtElement);
        if (!(nbtElement2 instanceof NbtString stringTag))
            return DataResult.error(() -> "key is not a string: " + nbtElement2, nbtElement);
        NbtCompound nbtCompound = nbtElement instanceof NbtCompound nbtCompound2 ? nbtCompound2.copy() : new NbtCompound();
        nbtCompound.put(stringTag.value, nbtElement3);
        return DataResult.success(nbtCompound);
    }

    @Override
    public DataResult<NbtElement> mergeToMap(NbtElement nbtElement, MapLike<NbtElement> mapLike) {
        if (!(nbtElement instanceof NbtCompound) && !(nbtElement instanceof NbtEnd))
            return DataResult.error(() -> "mergeToMap called with not a map: " + nbtElement, nbtElement);
        NbtCompound nbtCompound = nbtElement instanceof NbtCompound nbtCompound2 ? nbtCompound2.copy() : new NbtCompound();
        List<NbtElement> invalidKeys = new ArrayList<>();
        mapLike.entries().forEach(pair -> {
            NbtElement key = pair.getFirst();
            if (key instanceof NbtString stringTag)
                nbtCompound.put(stringTag.value, pair.getSecond());
            else
                invalidKeys.add(key);
        });
        return invalidKeys.isEmpty() ? DataResult.success(nbtCompound) : DataResult.error(() -> "some keys are not strings: " + invalidKeys, nbtCompound);
    }

    @Override
    public DataResult<Stream<Pair<NbtElement, NbtElement>>> getMapValues(NbtElement nbtElement) {
        if (!(nbtElement instanceof NbtCompound nbtCompound)) return DataResult.error(() -> "Not a map: " + nbtElement);
        return DataResult.success(((CompoundTagAccessor) nbtCompound).stationapi$getData().entrySet().stream().map(entry -> Pair.of(createString(entry.getKey()), entry.getValue())));
    }

    @Override
    public DataResult<Consumer<BiConsumer<NbtElement, NbtElement>>> getMapEntries(NbtElement nbtElement) {
        if (!(nbtElement instanceof NbtCompound nbtCompound)) return DataResult.error(() -> "Not a map: " + nbtElement);
        return DataResult.success(entryConsumer -> ((CompoundTagAccessor) nbtCompound).stationapi$getData().forEach((key, value) -> entryConsumer.accept(this.createString(key), value)));
    }

    @Override
    public DataResult<MapLike<NbtElement>> getMap(NbtElement nbtElement) {
        if (!(nbtElement instanceof NbtCompound nbtCompound)) return DataResult.error(() -> "Not a map: " + nbtElement);
        return DataResult.success(new MapLike<>() {

            @Override
            @Nullable
            public NbtElement get(NbtElement nbtElement) {
                return ((CompoundTagAccessor) nbtCompound).stationapi$getData().get(nbtElement.toString());
            }

            @Override
            @Nullable
            public NbtElement get(String string) {
                return ((CompoundTagAccessor) nbtCompound).stationapi$getData().get(string);
            }

            @Override
            public Stream<Pair<NbtElement, NbtElement>> entries() {
                return ((CompoundTagAccessor) nbtCompound).stationapi$getData().entrySet().stream().map(entry -> Pair.of(createString(entry.getKey()), entry.getValue()));
            }

            public String toString() {
                return "MapLike[" + nbtCompound + "]";
            }
        });
    }

    @Override
    public NbtElement createMap(Stream<Pair<NbtElement, NbtElement>> stream) {
        return Util.make(new NbtCompound(), nbtCompound -> stream.forEach(entry -> nbtCompound.put(entry.getFirst().toString(), entry.getSecond())));
    }

    @Override
    public DataResult<Stream<NbtElement>> getStream(NbtElement nbtElement) {
        return nbtElement instanceof NbtList listTag ? DataResult.success(IntStream.range(0, listTag.size()).mapToObj(listTag::get)) : DataResult.error(() -> "Not a list");
    }

    @Override
    public DataResult<Consumer<Consumer<NbtElement>>> getList(NbtElement nbtElement) {
        if (nbtElement instanceof NbtList abstractNbtList) {
            return DataResult.success(IntStream.range(0, abstractNbtList.size()).mapToObj(abstractNbtList::get)::forEach);
        }
        return DataResult.error(() -> "Not a list: " + nbtElement);
    }

    @Override
    public DataResult<ByteBuffer> getByteBuffer(NbtElement nbtElement) {
        if (nbtElement instanceof NbtByteArray nbtByteArray)
            return DataResult.success(ByteBuffer.wrap(nbtByteArray.value));
        return DynamicOps.super.getByteBuffer(nbtElement);
    }

    @Override
    public NbtElement createByteList(ByteBuffer byteBuffer) {
        return new NbtByteArray(DataFixUtils.toArray(byteBuffer));
    }

    @Override
    public DataResult<IntStream> getIntStream(NbtElement nbtElement) {
        if (nbtElement instanceof NbtIntArray nbtIntArray)
            return DataResult.success(Arrays.stream(nbtIntArray.data));
        return DynamicOps.super.getIntStream(nbtElement);
    }

    @Override
    public NbtElement createIntList(IntStream intStream) {
        return new NbtIntArray(intStream.toArray());
    }

    @Override
    public DataResult<LongStream> getLongStream(NbtElement nbtElement) {
        if (nbtElement instanceof NbtLongArray nbtLongArray)
            return DataResult.success(Arrays.stream(nbtLongArray.data));
        return DynamicOps.super.getLongStream(nbtElement);
    }

    @Override
    public NbtElement createLongList(LongStream longStream) {
        return new NbtLongArray(longStream.toArray());
    }

    @Override
    public NbtElement createList(Stream<NbtElement> stream) {
        PeekingIterator<NbtElement> peekingIterator = Iterators.peekingIterator(stream.iterator());
        if (!peekingIterator.hasNext()) return new NbtList();
        NbtElement nbtElement = peekingIterator.peek();
        if (nbtElement instanceof NbtByte nbtByte) {
            ArrayList<Byte> list = Lists.newArrayList(Iterators.transform(peekingIterator, nbt -> nbtByte.value));
            return new NbtByteArray(Bytes.toArray(list));
        }
        if (nbtElement instanceof NbtInt nbtInt) {
            ArrayList<Integer> list = Lists.newArrayList(Iterators.transform(peekingIterator, nbt -> nbtInt.value));
            return new NbtIntArray(Ints.toArray(list));
        }
        if (nbtElement instanceof NbtLong nbtLong) {
            ArrayList<Long> list = Lists.newArrayList(Iterators.transform(peekingIterator, nbt -> nbtLong.value));
            return new NbtLongArray(Longs.toArray(list));
        }
        NbtList list = new NbtList();
        while (peekingIterator.hasNext()) {
            NbtElement nbtElement2 = peekingIterator.next();
            if (nbtElement2 instanceof NbtEnd) continue;
            list.add(nbtElement2);
        }
        return list;
    }

    @Override
    public NbtElement remove(NbtElement nbtElement, String string) {
        if (nbtElement instanceof NbtCompound nbtCompound) {
            NbtCompound nbtCompound2 = new NbtCompound();
            ((CompoundTagAccessor) nbtCompound).stationapi$getData().entrySet().stream().filter(entry -> !Objects.equals(entry.getKey(), string)).forEach(entry -> nbtCompound2.put(entry.getKey(), entry.getValue()));
            return nbtCompound2;
        }
        return nbtElement;
    }

    public String toString() {
        return "NBT";
    }

    @Override
    public RecordBuilder<NbtElement> mapBuilder() {
        return new MapBuilder();
    }

    class MapBuilder
    extends RecordBuilder.AbstractStringBuilder<NbtElement, NbtCompound> {
        protected MapBuilder() {
            super(NbtOps.this);
        }

        @Override
        protected NbtCompound initBuilder() {
            return new NbtCompound();
        }

        @Override
        protected NbtCompound append(String string, NbtElement nbtElement, NbtCompound nbtCompound) {
            nbtCompound.put(string, nbtElement);
            return nbtCompound;
        }

        @Override
        protected DataResult<NbtElement> build(NbtCompound nbtCompound, NbtElement nbtElement) {
            if (nbtElement == null || nbtElement instanceof NbtEnd) return DataResult.success(nbtCompound);
            if (nbtElement instanceof NbtCompound nbtCompound3) {
                NbtCompound nbtCompound2 = Util.make(new NbtCompound(), compoundTag -> ((CompoundTagAccessor) nbtCompound3).stationapi$getData().forEach(compoundTag::put));
                ((CompoundTagAccessor) nbtCompound).stationapi$getData().forEach(nbtCompound2::put);
                return DataResult.success(nbtCompound2);
            }
            return DataResult.error(() -> "mergeToMap called with not a map: " + nbtElement, nbtElement);
        }
    }
}

