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
public class NbtOps implements DynamicOps<AbstractTag> {
    /**
     * An singleton of the NBT dynamic ops.
     * 
     * <p>This ops does not compress maps (replace field name to value pairs
     * with an ordered list of values in serialization). In fact, since
     * Minecraft NBT lists can only contain elements of the same type, this op
     * cannot compress maps.
     */
    public static final NbtOps INSTANCE = new NbtOps();

    private static final AbstractTag NBT_END = new EndTag();

    protected NbtOps() {
    }

    @Override
    public AbstractTag empty() {
        return NBT_END;
    }

    @Override
    public <U> U convertTo(DynamicOps<U> dynamicOps, AbstractTag nbtElement) {
        return switch (nbtElement.getId()) {
            case 0 -> dynamicOps.empty();
            case 1 -> dynamicOps.createByte(((ByteTag) nbtElement).data);
            case 2 -> dynamicOps.createShort(((ShortTag) nbtElement).data);
            case 3 -> dynamicOps.createInt(((IntTag) nbtElement).data);
            case 4 -> dynamicOps.createLong(((LongTag) nbtElement).data);
            case 5 -> dynamicOps.createFloat(((FloatTag) nbtElement).data);
            case 6 -> dynamicOps.createDouble(((DoubleTag) nbtElement).data);
            case 7 -> dynamicOps.createByteList(ByteBuffer.wrap(((ByteArrayTag) nbtElement).data));
            case 8 -> dynamicOps.createString(((StringTag) nbtElement).data);
            case 9 -> convertList(dynamicOps, nbtElement);
            case 10 -> convertMap(dynamicOps, nbtElement);
            case 11 -> dynamicOps.createIntList(Arrays.stream(((NbtIntArray) nbtElement).data));
            case 12 -> dynamicOps.createLongList(Arrays.stream(((NbtLongArray) nbtElement).data));
            default -> throw new IllegalStateException("Unknown tag type: " + nbtElement);
        };
    }

    @Override
    public DataResult<Number> getNumberValue(AbstractTag nbtElement) {
        return switch (nbtElement.getId()) {
            case 1 -> DataResult.success(((ByteTag) nbtElement).data);
            case 2 -> DataResult.success(((ShortTag) nbtElement).data);
            case 3 -> DataResult.success(((IntTag) nbtElement).data);
            case 4 -> DataResult.success(((LongTag) nbtElement).data);
            case 5 -> DataResult.success(((FloatTag) nbtElement).data);
            case 6 -> DataResult.success(((DoubleTag) nbtElement).data);
            default -> DataResult.error(() -> "Not a number");
        };
    }

    @Override
    public AbstractTag createNumeric(Number number) {
        return new DoubleTag(number.doubleValue());
    }

    @Override
    public AbstractTag createByte(byte b) {
        return new ByteTag(b);
    }

    @Override
    public AbstractTag createShort(short s) {
        return new ShortTag(s);
    }

    @Override
    public AbstractTag createInt(int i) {
        return new IntTag(i);
    }

    @Override
    public AbstractTag createLong(long l) {
        return new LongTag(l);
    }

    @Override
    public AbstractTag createFloat(float f) {
        return new FloatTag(f);
    }

    @Override
    public AbstractTag createDouble(double d) {
        return new DoubleTag(d);
    }

    @Override
    public AbstractTag createBoolean(boolean bl) {
        return new ByteTag((byte) (bl ? 1 : 0));
    }

    @Override
    public DataResult<String> getStringValue(AbstractTag nbtElement) {
        return nbtElement instanceof StringTag stringTag ? DataResult.success(stringTag.data) : DataResult.error(() -> "Not a string");
    }

    @Override
    public AbstractTag createString(String string) {
        return new StringTag(string);
    }

    private static class ListArrayTag<T extends AbstractTag> extends ListTag {

        private final T array;

        private <U extends AbstractTag> ListArrayTag(
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
        public byte getId() {
            return array.getId();
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
        public String getType() {
            return array.getType();
        }

        @Override
        public AbstractTag setType(String string) {
            return array.setType(string);
        }
    }

    private static ListTag createList(byte knownType, byte valueType) {
        if (NbtOps.isTypeEqual(knownType, valueType, (byte) 4)) return new ListArrayTag<>(new NbtLongArray(new long[0]), array -> Predicates.compose(Predicates.alwaysTrue(), tag -> array.data = ArrayUtils.insert(array.data.length, array.data, tag.data)), array -> i -> new LongTag(array.data[i]), array -> () -> array.data.length, (byte) 4);
        if (NbtOps.isTypeEqual(knownType, valueType, (byte) 1)) return new ListArrayTag<>(new ByteArrayTag(new byte[0]), array -> Predicates.compose(Predicates.alwaysTrue(), tag -> array.data = ArrayUtils.insert(array.data.length, array.data, tag.data)), array -> i -> new ByteTag(array.data[i]), array -> () -> array.data.length, (byte) 1);
        if (NbtOps.isTypeEqual(knownType, valueType, (byte) 3)) return new ListArrayTag<>(new NbtIntArray(new int[0]), array -> Predicates.compose(Predicates.alwaysTrue(), tag -> array.data = ArrayUtils.insert(array.data.length, array.data, tag.data)), array -> i -> new IntTag(array.data[i]), array -> () -> array.data.length, (byte) 3);
        return new ListTag();
    }

    private static boolean isTypeEqual(byte knownType, byte valueType, byte expectedType) {
        return knownType == expectedType && (valueType == expectedType || valueType == 0);
    }

    private static void addAll(ListTag destination, AbstractTag source, AbstractTag additionalValue) {
        if (source instanceof ListTag abstractNbtList) {
            IntStream.of(0, abstractNbtList.size()).mapToObj(abstractNbtList::get).forEach(destination::add);
        }
        destination.add(additionalValue);
    }

    private static void addAll(ListTag destination, AbstractTag source, List<AbstractTag> additionalValues) {
        if (source instanceof ListTag abstractNbtList) {
            IntStream.of(0, abstractNbtList.size()).mapToObj(abstractNbtList::get).forEach(destination::add);
        }
        additionalValues.forEach(destination::add);
    }

    @Override
    public DataResult<AbstractTag> mergeToList(AbstractTag nbtElement, AbstractTag nbtElement2) {
        if (!(nbtElement instanceof ListTag) && !(nbtElement instanceof EndTag))
            return DataResult.error(() -> "mergeToList called with not a list: " + nbtElement, nbtElement);
        ListTag abstractNbtList = NbtOps.createList(nbtElement instanceof ListTag ? ((ListTagAccessor)nbtElement).stationapi$getListTypeId() : (byte)0, nbtElement2.getId());
        NbtOps.addAll(abstractNbtList, nbtElement, nbtElement2);
        return DataResult.success(abstractNbtList);
    }

    @Override
    public DataResult<AbstractTag> mergeToList(AbstractTag nbtElement, List<AbstractTag> list) {
        if (!(nbtElement instanceof ListTag) && !(nbtElement instanceof EndTag))
            return DataResult.error(() -> "mergeToList called with not a list: " + nbtElement, nbtElement);
        ListTag abstractNbtList = NbtOps.createList(nbtElement instanceof ListTag ? ((ListTagAccessor)nbtElement).stationapi$getListTypeId() : (byte)0, list.stream().findFirst().map(AbstractTag::getId).orElse((byte)0));
        NbtOps.addAll(abstractNbtList, nbtElement, list);
        return DataResult.success(abstractNbtList);
    }

    @Override
    public DataResult<AbstractTag> mergeToMap(AbstractTag nbtElement, AbstractTag nbtElement2, AbstractTag nbtElement3) {
        if (!(nbtElement instanceof CompoundTag) && !(nbtElement instanceof EndTag))
            return DataResult.error(() -> "mergeToMap called with not a map: " + nbtElement, nbtElement);
        if (!(nbtElement2 instanceof StringTag stringTag))
            return DataResult.error(() -> "key is not a string: " + nbtElement2, nbtElement);
        CompoundTag nbtCompound = nbtElement instanceof CompoundTag nbtCompound2 ? nbtCompound2.copy() : new CompoundTag();
        nbtCompound.put(stringTag.data, nbtElement3);
        return DataResult.success(nbtCompound);
    }

    @Override
    public DataResult<AbstractTag> mergeToMap(AbstractTag nbtElement, MapLike<AbstractTag> mapLike) {
        if (!(nbtElement instanceof CompoundTag) && !(nbtElement instanceof EndTag))
            return DataResult.error(() -> "mergeToMap called with not a map: " + nbtElement, nbtElement);
        CompoundTag nbtCompound = nbtElement instanceof CompoundTag nbtCompound2 ? nbtCompound2.copy() : new CompoundTag();
        List<AbstractTag> invalidKeys = new ArrayList<>();
        mapLike.entries().forEach(pair -> {
            AbstractTag key = pair.getFirst();
            if (key instanceof StringTag stringTag)
                nbtCompound.put(stringTag.data, pair.getSecond());
            else
                invalidKeys.add(key);
        });
        return invalidKeys.isEmpty() ? DataResult.success(nbtCompound) : DataResult.error(() -> "some keys are not strings: " + invalidKeys, nbtCompound);
    }

    @Override
    public DataResult<Stream<Pair<AbstractTag, AbstractTag>>> getMapValues(AbstractTag nbtElement) {
        if (!(nbtElement instanceof CompoundTag nbtCompound)) return DataResult.error(() -> "Not a map: " + nbtElement);
        return DataResult.success(((CompoundTagAccessor) nbtCompound).stationapi$getData().entrySet().stream().map(entry -> Pair.of(createString(entry.getKey()), entry.getValue())));
    }

    @Override
    public DataResult<Consumer<BiConsumer<AbstractTag, AbstractTag>>> getMapEntries(AbstractTag nbtElement) {
        if (!(nbtElement instanceof CompoundTag nbtCompound)) return DataResult.error(() -> "Not a map: " + nbtElement);
        return DataResult.success(entryConsumer -> ((CompoundTagAccessor) nbtCompound).stationapi$getData().forEach((key, value) -> entryConsumer.accept(this.createString(key), value)));
    }

    @Override
    public DataResult<MapLike<AbstractTag>> getMap(AbstractTag nbtElement) {
        if (!(nbtElement instanceof CompoundTag nbtCompound)) return DataResult.error(() -> "Not a map: " + nbtElement);
        return DataResult.success(new MapLike<>() {

            @Override
            @Nullable
            public AbstractTag get(AbstractTag nbtElement) {
                return ((CompoundTagAccessor) nbtCompound).stationapi$getData().get(nbtElement.toString());
            }

            @Override
            @Nullable
            public AbstractTag get(String string) {
                return ((CompoundTagAccessor) nbtCompound).stationapi$getData().get(string);
            }

            @Override
            public Stream<Pair<AbstractTag, AbstractTag>> entries() {
                return ((CompoundTagAccessor) nbtCompound).stationapi$getData().entrySet().stream().map(entry -> Pair.of(createString(entry.getKey()), entry.getValue()));
            }

            public String toString() {
                return "MapLike[" + nbtCompound + "]";
            }
        });
    }

    @Override
    public AbstractTag createMap(Stream<Pair<AbstractTag, AbstractTag>> stream) {
        return Util.make(new CompoundTag(), nbtCompound -> stream.forEach(entry -> nbtCompound.put(entry.getFirst().toString(), entry.getSecond())));
    }

    @Override
    public DataResult<Stream<AbstractTag>> getStream(AbstractTag nbtElement) {
        return nbtElement instanceof ListTag listTag ? DataResult.success(IntStream.range(0, listTag.size()).mapToObj(listTag::get)) : DataResult.error(() -> "Not a list");
    }

    @Override
    public DataResult<Consumer<Consumer<AbstractTag>>> getList(AbstractTag nbtElement) {
        if (nbtElement instanceof ListTag abstractNbtList) {
            return DataResult.success(IntStream.range(0, abstractNbtList.size()).mapToObj(abstractNbtList::get)::forEach);
        }
        return DataResult.error(() -> "Not a list: " + nbtElement);
    }

    @Override
    public DataResult<ByteBuffer> getByteBuffer(AbstractTag nbtElement) {
        if (nbtElement instanceof ByteArrayTag nbtByteArray)
            return DataResult.success(ByteBuffer.wrap(nbtByteArray.data));
        return DynamicOps.super.getByteBuffer(nbtElement);
    }

    @Override
    public AbstractTag createByteList(ByteBuffer byteBuffer) {
        return new ByteArrayTag(DataFixUtils.toArray(byteBuffer));
    }

    @Override
    public DataResult<IntStream> getIntStream(AbstractTag nbtElement) {
        if (nbtElement instanceof NbtIntArray nbtIntArray)
            return DataResult.success(Arrays.stream(nbtIntArray.data));
        return DynamicOps.super.getIntStream(nbtElement);
    }

    @Override
    public AbstractTag createIntList(IntStream intStream) {
        return new NbtIntArray(intStream.toArray());
    }

    @Override
    public DataResult<LongStream> getLongStream(AbstractTag nbtElement) {
        if (nbtElement instanceof NbtLongArray nbtLongArray)
            return DataResult.success(Arrays.stream(nbtLongArray.data));
        return DynamicOps.super.getLongStream(nbtElement);
    }

    @Override
    public AbstractTag createLongList(LongStream longStream) {
        return new NbtLongArray(longStream.toArray());
    }

    @Override
    public AbstractTag createList(Stream<AbstractTag> stream) {
        PeekingIterator<AbstractTag> peekingIterator = Iterators.peekingIterator(stream.iterator());
        if (!peekingIterator.hasNext()) return new ListTag();
        AbstractTag nbtElement = peekingIterator.peek();
        if (nbtElement instanceof ByteTag nbtByte) {
            ArrayList<Byte> list = Lists.newArrayList(Iterators.transform(peekingIterator, nbt -> nbtByte.data));
            return new ByteArrayTag(Bytes.toArray(list));
        }
        if (nbtElement instanceof IntTag nbtInt) {
            ArrayList<Integer> list = Lists.newArrayList(Iterators.transform(peekingIterator, nbt -> nbtInt.data));
            return new NbtIntArray(Ints.toArray(list));
        }
        if (nbtElement instanceof LongTag nbtLong) {
            ArrayList<Long> list = Lists.newArrayList(Iterators.transform(peekingIterator, nbt -> nbtLong.data));
            return new NbtLongArray(Longs.toArray(list));
        }
        ListTag list = new ListTag();
        while (peekingIterator.hasNext()) {
            AbstractTag nbtElement2 = peekingIterator.next();
            if (nbtElement2 instanceof EndTag) continue;
            list.add(nbtElement2);
        }
        return list;
    }

    @Override
    public AbstractTag remove(AbstractTag nbtElement, String string) {
        if (nbtElement instanceof CompoundTag nbtCompound) {
            CompoundTag nbtCompound2 = new CompoundTag();
            ((CompoundTagAccessor) nbtCompound).stationapi$getData().entrySet().stream().filter(entry -> !Objects.equals(entry.getKey(), string)).forEach(entry -> nbtCompound2.put(entry.getKey(), entry.getValue()));
            return nbtCompound2;
        }
        return nbtElement;
    }

    public String toString() {
        return "NBT";
    }

    @Override
    public RecordBuilder<AbstractTag> mapBuilder() {
        return new MapBuilder();
    }

    class MapBuilder
    extends RecordBuilder.AbstractStringBuilder<AbstractTag, CompoundTag> {
        protected MapBuilder() {
            super(NbtOps.this);
        }

        @Override
        protected CompoundTag initBuilder() {
            return new CompoundTag();
        }

        @Override
        protected CompoundTag append(String string, AbstractTag nbtElement, CompoundTag nbtCompound) {
            nbtCompound.put(string, nbtElement);
            return nbtCompound;
        }

        @Override
        protected DataResult<AbstractTag> build(CompoundTag nbtCompound, AbstractTag nbtElement) {
            if (nbtElement == null || nbtElement instanceof EndTag) return DataResult.success(nbtCompound);
            if (nbtElement instanceof CompoundTag nbtCompound3) {
                CompoundTag nbtCompound2 = Util.make(new CompoundTag(), compoundTag -> ((CompoundTagAccessor) nbtCompound3).stationapi$getData().forEach(compoundTag::put));
                ((CompoundTagAccessor) nbtCompound).stationapi$getData().forEach(nbtCompound2::put);
                return DataResult.success(nbtCompound2);
            }
            return DataResult.error(() -> "mergeToMap called with not a map: " + nbtElement, nbtElement);
        }
    }
}

