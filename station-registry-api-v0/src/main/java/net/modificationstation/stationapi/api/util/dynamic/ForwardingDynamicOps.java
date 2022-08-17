/*
 * Decompiled with CFR 0.1.1 (FabricMC 57d88659).
 */
package net.modificationstation.stationapi.api.util.dynamic;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * A dynamic ops that delegates all operations from another one.
 */
public abstract class ForwardingDynamicOps<T>
implements DynamicOps<T> {
    protected final DynamicOps<T> delegate;

    protected ForwardingDynamicOps(DynamicOps<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public T empty() {
        return this.delegate.empty();
    }

    @Override
    public <U> U convertTo(DynamicOps<U> outputOps, T input) {
        return this.delegate.convertTo(outputOps, input);
    }

    @Override
    public DataResult<Number> getNumberValue(T input) {
        return this.delegate.getNumberValue(input);
    }

    @Override
    public T createNumeric(Number number) {
        return this.delegate.createNumeric(number);
    }

    @Override
    public T createByte(byte b) {
        return this.delegate.createByte(b);
    }

    @Override
    public T createShort(short s) {
        return this.delegate.createShort(s);
    }

    @Override
    public T createInt(int i) {
        return this.delegate.createInt(i);
    }

    @Override
    public T createLong(long l) {
        return this.delegate.createLong(l);
    }

    @Override
    public T createFloat(float f) {
        return this.delegate.createFloat(f);
    }

    @Override
    public T createDouble(double d) {
        return this.delegate.createDouble(d);
    }

    @Override
    public DataResult<Boolean> getBooleanValue(T input) {
        return this.delegate.getBooleanValue(input);
    }

    @Override
    public T createBoolean(boolean bl) {
        return this.delegate.createBoolean(bl);
    }

    @Override
    public DataResult<String> getStringValue(T input) {
        return this.delegate.getStringValue(input);
    }

    @Override
    public T createString(String string) {
        return this.delegate.createString(string);
    }

    @Override
    public DataResult<T> mergeToList(T list, T value) {
        return this.delegate.mergeToList(list, value);
    }

    @Override
    public DataResult<T> mergeToList(T list, List<T> values) {
        return this.delegate.mergeToList(list, values);
    }

    @Override
    public DataResult<T> mergeToMap(T map, T key, T value) {
        return this.delegate.mergeToMap(map, key, value);
    }

    @Override
    public DataResult<T> mergeToMap(T map, MapLike<T> values) {
        return this.delegate.mergeToMap(map, values);
    }

    @Override
    public DataResult<Stream<Pair<T, T>>> getMapValues(T input) {
        return this.delegate.getMapValues(input);
    }

    @Override
    public DataResult<Consumer<BiConsumer<T, T>>> getMapEntries(T input) {
        return this.delegate.getMapEntries(input);
    }

    @Override
    public T createMap(Stream<Pair<T, T>> map) {
        return this.delegate.createMap(map);
    }

    @Override
    public DataResult<MapLike<T>> getMap(T input) {
        return this.delegate.getMap(input);
    }

    @Override
    public DataResult<Stream<T>> getStream(T input) {
        return this.delegate.getStream(input);
    }

    @Override
    public DataResult<Consumer<Consumer<T>>> getList(T input) {
        return this.delegate.getList(input);
    }

    @Override
    public T createList(Stream<T> stream) {
        return this.delegate.createList(stream);
    }

    @Override
    public DataResult<ByteBuffer> getByteBuffer(T input) {
        return this.delegate.getByteBuffer(input);
    }

    @Override
    public T createByteList(ByteBuffer buf) {
        return this.delegate.createByteList(buf);
    }

    @Override
    public DataResult<IntStream> getIntStream(T input) {
        return this.delegate.getIntStream(input);
    }

    @Override
    public T createIntList(IntStream stream) {
        return this.delegate.createIntList(stream);
    }

    @Override
    public DataResult<LongStream> getLongStream(T input) {
        return this.delegate.getLongStream(input);
    }

    @Override
    public T createLongList(LongStream stream) {
        return this.delegate.createLongList(stream);
    }

    @Override
    public T remove(T input, String key) {
        return this.delegate.remove(input, key);
    }

    @Override
    public boolean compressMaps() {
        return this.delegate.compressMaps();
    }

    @Override
    public ListBuilder<T> listBuilder() {
        return new ListBuilder.Builder<>(this);
    }

    @Override
    public RecordBuilder<T> mapBuilder() {
        return new RecordBuilder.MapBuilder<>(this);
    }
}

