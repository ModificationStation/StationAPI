package net.modificationstation.stationapi.api.util.dynamic;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.util.Util;
import org.apache.commons.lang3.mutable.MutableObject;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.*;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Stream;

/**
 * A few extensions for {@link Codec} or {@link DynamicOps}.
 * 
 * <p>It has a few methods to create checkers for {@code Codec.flatXmap} to add
 * extra value validation to encoding and decoding. See the implementation of
 * {@link #nonEmptyList(Codec)}.
 */
public class Codecs {
    public static final Codec<UUID> UUID = DynamicSerializableUuid.CODEC;
    public static final Codec<Integer> NONNEGATIVE_INT = Codecs.rangedInt(0, Integer.MAX_VALUE, v -> "Value must be non-negative: " + v);
    public static final Codec<Integer> POSITIVE_INT = Codecs.rangedInt(1, Integer.MAX_VALUE, v -> "Value must be positive: " + v);
    public static final Codec<Float> POSITIVE_FLOAT = Codecs.rangedFloat(0.0f, Float.MAX_VALUE, v -> "Value must be positive: " + v);
    public static final Codec<Pattern> REGULAR_EXPRESSION = Codec.STRING.comapFlatMap(pattern -> {
        try {
            return DataResult.success(Pattern.compile(pattern));
        }
        catch (PatternSyntaxException patternSyntaxException) {
            return DataResult.error(() -> "Invalid regex pattern '" + pattern + "': " + patternSyntaxException.getMessage());
        }
    }, Pattern::pattern);
    public static final Codec<Instant> INSTANT = Codecs.instant(DateTimeFormatter.ISO_INSTANT);
    public static final Codec<byte[]> BASE_64 = Codec.STRING.comapFlatMap(encoded -> {
        try {
            return DataResult.success(Base64.getDecoder().decode(encoded));
        }
        catch (IllegalArgumentException illegalArgumentException) {
            return DataResult.error(() -> "Malformed base64 string");
        }
    }, data -> Base64.getEncoder().encodeToString(data));
    public static final Codec<TagEntryId> TAG_ENTRY_ID = Codec.STRING.comapFlatMap(tagEntry -> tagEntry.startsWith("#") ? Identifier.validate(tagEntry.substring(1)).map(id -> new TagEntryId(id, true)) : Identifier.validate(tagEntry).map(id -> new TagEntryId(id, false)), TagEntryId::asString);
    public static final Function<Optional<Long>, OptionalLong> OPTIONAL_OF_LONG_TO_OPTIONAL_LONG = optional -> optional.map(OptionalLong::of).orElseGet(OptionalLong::empty);
    public static final Function<OptionalLong, Optional<Long>> OPTIONAL_LONG_TO_OPTIONAL_OF_LONG = optionalLong -> optionalLong.isPresent() ? Optional.of(optionalLong.getAsLong()) : Optional.empty();

    /**
     * Returns an exclusive-or codec for {@link Either} instances.
     * 
     * <p>This returned codec fails if both the {@code first} and {@code second} codecs can
     * decode the input, while DFU's {@link com.mojang.serialization.codecs.EitherCodec}
     * will always take the first decoded result when it is available.
     * 
     * <p>Otherwise, this behaves the same as the either codec.
     * 
     * @param <F> the first type
     * @param <S> the second type
     * @return the xor codec for the two codecs
     * @see Codec#either(Codec, Codec)
     * @see com.mojang.serialization.codecs.EitherCodec
     * 
     * @param first the first codec
     * @param second the second codec
     */
    public static <F, S> Codec<com.mojang.datafixers.util.Either<F, S>> xor(Codec<F> first, Codec<S> second) {
        return new Xor<>(first, second);
    }

    public static <P, I> Codec<I> createCodecForPairObject(Codec<P> codec, String leftFieldName, String rightFieldName, BiFunction<P, P, DataResult<I>> combineFunction, Function<I, P> leftFunction, Function<I, P> rightFunction) {
        Codec<I> codec2 = Codec.list(codec).comapFlatMap(list2 -> Util.toArray(list2, 2).flatMap(list -> combineFunction.apply(list.get(0), list.get(1))), pair -> ImmutableList.of(leftFunction.apply(pair), rightFunction.apply(pair)));
        Codec<I> codec3 = RecordCodecBuilder.<Pair<P, P>>create(instance -> instance.group(codec.fieldOf(leftFieldName).forGetter(Pair::getFirst), codec.fieldOf(rightFieldName).forGetter(Pair::getSecond)).apply(instance, Pair::of)).comapFlatMap(pair -> combineFunction.apply(pair.getFirst(), pair.getSecond()), pair -> Pair.of(leftFunction.apply(pair), rightFunction.apply(pair)));
        Codec<I> codec4 = new Either<>(codec2, codec3).xmap(either -> either.map(object -> object, object -> object), com.mojang.datafixers.util.Either::left);
        return Codec.either(codec, codec4).comapFlatMap(either -> either.map(object -> combineFunction.apply(object, object), DataResult::success), pair -> {
            P object = leftFunction.apply(pair);
            if (Objects.equals(object, rightFunction.apply(pair))) {
                return com.mojang.datafixers.util.Either.left(object);
            }
            return com.mojang.datafixers.util.Either.right(pair);
        });
    }

    public static <A> Codec.ResultFunction<A> orElsePartial(final A object) {
        return new Codec.ResultFunction<>() {

            @Override
            public <T> DataResult<Pair<A, T>> apply(DynamicOps<T> ops, T input, DataResult<Pair<A, T>> result) {
                MutableObject<String> mutableObject = new MutableObject<>();
                if (result.resultOrPartial(mutableObject::setValue).isPresent()) {
                    return result;
                }
                return DataResult.error(() -> "(" + mutableObject.getValue() + " -> using default)", Pair.of(object, input));
            }

            @Override
            public <T> DataResult<T> coApply(DynamicOps<T> ops, A input, DataResult<T> result) {
                return result;
            }

            public String toString() {
                return "OrElsePartial[" + object + "]";
            }
        };
    }

    public static <E> Codec<E> rawIdChecked(ToIntFunction<E> elementToRawId, IntFunction<E> rawIdToElement, int errorRawId) {
        return Codec.INT.flatXmap(rawId -> Optional.ofNullable(rawIdToElement.apply(rawId)).map(DataResult::success).orElseGet(() -> DataResult.error(() -> "Unknown element id: " + rawId)), element -> {
            int j = elementToRawId.applyAsInt(element);
            return j == errorRawId ? DataResult.error(() -> "Element with unknown id: " + element) : DataResult.success(j);
        });
    }

    public static <E> Codec<E> idChecked(Function<E, String> elementToId, Function<String, E> idToElement) {
        return Codec.STRING.flatXmap(id -> Optional.ofNullable(idToElement.apply(id)).map(DataResult::success).orElseGet(() -> DataResult.error(() -> "Unknown element name:" + id)), element -> Optional.ofNullable(elementToId.apply(element)).map(DataResult::success).orElseGet(() -> DataResult.error(() -> "Element with unknown name: " + element)));
    }

    public static <E> Codec<E> orCompressed(final Codec<E> uncompressedCodec, final Codec<E> compressedCodec) {
        return new Codec<>() {

            @Override
            public <T> DataResult<T> encode(E input, DynamicOps<T> ops, T prefix) {
                if (ops.compressMaps()) {
                    return compressedCodec.encode(input, ops, prefix);
                }
                return uncompressedCodec.encode(input, ops, prefix);
            }

            @Override
            public <T> DataResult<Pair<E, T>> decode(DynamicOps<T> ops, T input) {
                if (ops.compressMaps()) {
                    return compressedCodec.decode(ops, input);
                }
                return uncompressedCodec.decode(ops, input);
            }

            public String toString() {
                return uncompressedCodec + " orCompressed " + compressedCodec;
            }
        };
    }

    public static <E> Codec<E> withLifecycle(Codec<E> originalCodec, final Function<E, Lifecycle> entryLifecycleGetter, final Function<E, Lifecycle> lifecycleGetter) {
        return originalCodec.mapResult(new Codec.ResultFunction<>() {

            @Override
            public <T> DataResult<Pair<E, T>> apply(DynamicOps<T> ops, T input, DataResult<Pair<E, T>> result) {
                return result.result().map(pair -> result.setLifecycle(entryLifecycleGetter.apply(pair.getFirst()))).orElse(result);
            }

            @Override
            public <T> DataResult<T> coApply(DynamicOps<T> ops, E input, DataResult<T> result) {
                return result.setLifecycle(lifecycleGetter.apply(input));
            }

            public String toString() {
                return "WithLifecycle[" + entryLifecycleGetter + " " + lifecycleGetter + "]";
            }
        });
    }

    public static <T> Codec<T> validate(Codec<T> codec, Function<T, DataResult<T>> validator) {
        return codec.flatXmap(validator, validator);
    }

    private static <N extends Number & Comparable<N>> Function<N, DataResult<N>> createIntRangeChecker(N min, N max, Function<N, String> messageFactory) {
        return value -> {
            if (value.compareTo(min) >= 0 && value.compareTo(max) <= 0) {
                return DataResult.success(value);
            }
            return DataResult.error(() -> messageFactory.apply(value));
        };
    }

    private static Codec<Integer> rangedInt(int min, int max, Function<Integer, String> messageFactory) {
        Function<Integer, DataResult<Integer>> function = Codecs.createIntRangeChecker(min, max, messageFactory);
        return Codec.INT.flatXmap(function, function);
    }

    private static <N extends Number & Comparable<N>> Function<N, DataResult<N>> createFloatRangeChecker(N min, N max, Function<N, String> messageFactory) {
        return value -> {
            if (value.compareTo(min) > 0 && value.compareTo(max) <= 0) {
                return DataResult.success(value);
            }
            return DataResult.error(() -> messageFactory.apply(value));
        };
    }

    private static Codec<Float> rangedFloat(float min, float max, Function<Float, String> messageFactory) {
        Function<Float, DataResult<Float>> function = Codecs.createFloatRangeChecker(min, max, messageFactory);
        return Codec.FLOAT.flatXmap(function, function);
    }

    public static <T> Function<List<T>, DataResult<List<T>>> createNonEmptyListChecker() {
        return list -> {
            if (list.isEmpty()) {
                return DataResult.error(() -> "List must have contents");
            }
            return DataResult.success(list);
        };
    }

    public static <T> Codec<List<T>> nonEmptyList(Codec<List<T>> originalCodec) {
        return originalCodec.flatXmap(Codecs.createNonEmptyListChecker(), Codecs.createNonEmptyListChecker());
    }

    public static <A> Codec<A> createLazy(Supplier<Codec<A>> supplier) {
        return new Lazy<>(supplier);
    }

    public static <E> MapCodec<E> createContextRetrievalCodec(Function<DynamicOps<?>, DataResult<E>> retriever) {
        class ContextRetrievalCodec
        extends MapCodec<E> {
            @Override
            public <T> RecordBuilder<T> encode(E input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
                return prefix;
            }

            @Override
            public <T> DataResult<E> decode(DynamicOps<T> ops, MapLike<T> input) {
                return retriever.apply(ops);
            }

            public String toString() {
                return "ContextRetrievalCodec[" + retriever + "]";
            }

            @Override
            public <T> Stream<T> keys(DynamicOps<T> ops) {
                return Stream.empty();
            }
        }
        return new ContextRetrievalCodec();
    }

    public static <E, L extends Collection<E>, T> Function<L, DataResult<L>> createEqualTypeChecker(Function<E, T> typeGetter) {
        return collection -> {
            Iterator<E> iterator = collection.iterator();
            if (iterator.hasNext()) {
                T object = typeGetter.apply(iterator.next());
                while (iterator.hasNext()) {
                    E object2 = iterator.next();
                    T object3 = typeGetter.apply(object2);
                    if (object3 == object) continue;
                    return DataResult.error(() -> "Mixed type list: element " + object2 + " had type " + object3 + ", but list is of type " + object);
                }
            }
            return DataResult.success(collection, Lifecycle.stable());
        };
    }

    public static <A> Codec<A> exceptionCatching(final Codec<A> codec) {
        return Codec.of(codec, new Decoder<>() {

            @Override
            public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> ops, T input) {
                try {
                    return codec.decode(ops, input);
                } catch (Exception exception) {
                    return DataResult.error(() -> "Cauch exception decoding " + input + ": " + exception.getMessage());
                }
            }
        });
    }

    public static Codec<Instant> instant(DateTimeFormatter formatter) {
        return Codec.STRING.comapFlatMap(dateTimeString -> {
            try {
                return DataResult.success(Instant.from(formatter.parse(dateTimeString)));
            }
            catch (Exception exception) {
                return DataResult.error(exception::getMessage);
            }
        }, formatter::format);
    }

    public static MapCodec<OptionalLong> optionalLong(MapCodec<Optional<Long>> codec) {
        return codec.xmap(OPTIONAL_OF_LONG_TO_OPTIONAL_LONG, OPTIONAL_LONG_TO_OPTIONAL_OF_LONG);
    }

    static final class Xor<F, S>
    implements Codec<com.mojang.datafixers.util.Either<F, S>> {
        private final Codec<F> first;
        private final Codec<S> second;

        public Xor(Codec<F> first, Codec<S> second) {
            this.first = first;
            this.second = second;
        }

        @Override
        public <T> DataResult<Pair<com.mojang.datafixers.util.Either<F, S>, T>> decode(DynamicOps<T> ops, T input) {
            DataResult<Pair<com.mojang.datafixers.util.Either<F, S>, T>> dataResult = this.first.decode(ops, input).map(pair -> pair.mapFirst(com.mojang.datafixers.util.Either::left));
            DataResult<Pair<com.mojang.datafixers.util.Either<F, S>, T>> dataResult2 = this.second.decode(ops, input).map(pair -> pair.mapFirst(com.mojang.datafixers.util.Either::right));
            Optional<Pair<com.mojang.datafixers.util.Either<F, S>, T>> optional = dataResult.result();
            Optional<Pair<com.mojang.datafixers.util.Either<F, S>, T>> optional2 = dataResult2.result();
            if (optional.isPresent() && optional2.isPresent()) {
                return DataResult.error(() -> "Both alternatives read successfully, can not pick the correct one; first: " + optional.get() + " second: " + optional2.get(), optional.get());
            }
            return optional.isPresent() ? dataResult : dataResult2;
        }

        @Override
        public <T> DataResult<T> encode(com.mojang.datafixers.util.Either<F, S> either, DynamicOps<T> dynamicOps, T object) {
            return either.map(left -> this.first.encode(left, dynamicOps, object), right -> this.second.encode(right, dynamicOps, object));
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            Xor<?, ?> xor = (Xor<?, ?>) o;
            return Objects.equals(this.first, xor.first) && Objects.equals(this.second, xor.second);
        }

        public int hashCode() {
            return Objects.hash(this.first, this.second);
        }

        public String toString() {
            return "XorCodec[" + this.first + ", " + this.second + "]";
        }
    }

    static final class Either<F, S>
    implements Codec<com.mojang.datafixers.util.Either<F, S>> {
        private final Codec<F> first;
        private final Codec<S> second;

        public Either(Codec<F> first, Codec<S> second) {
            this.first = first;
            this.second = second;
        }

        @Override
        public <T> DataResult<Pair<com.mojang.datafixers.util.Either<F, S>, T>> decode(DynamicOps<T> ops, T input) {
            DataResult<Pair<com.mojang.datafixers.util.Either<F, S>, T>> dataResult = this.first.decode(ops, input).map(pair -> pair.mapFirst(com.mojang.datafixers.util.Either::left));
            if (dataResult.error().isEmpty()) {
                return dataResult;
            }
            DataResult<Pair<com.mojang.datafixers.util.Either<F, S>, T>> dataResult2 = this.second.decode(ops, input).map(pair -> pair.mapFirst(com.mojang.datafixers.util.Either::right));
            if (dataResult2.error().isEmpty()) {
                return dataResult2;
            }
            return dataResult.apply2((pair, pair2) -> pair2, dataResult2);
        }

        @Override
        public <T> DataResult<T> encode(com.mojang.datafixers.util.Either<F, S> either, DynamicOps<T> dynamicOps, T object) {
            return either.map(left -> this.first.encode(left, dynamicOps, object), right -> this.second.encode(right, dynamicOps, object));
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            Either<?, ?> either = (Either<?, ?>) o;
            return Objects.equals(this.first, either.first) && Objects.equals(this.second, either.second);
        }

        public int hashCode() {
            return Objects.hash(this.first, this.second);
        }

        public String toString() {
            return "EitherCodec[" + this.first + ", " + this.second + "]";
        }
    }

    record Lazy<A>(Supplier<Codec<A>> delegate) implements Codec<A> {
        Lazy {
            delegate = Suppliers.memoize(delegate::get);
        }

        @Override
        public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> ops, T input) {
            return this.delegate.get().decode(ops, input);
        }

        @Override
        public <T> DataResult<T> encode(A input, DynamicOps<T> ops, T prefix) {
            return this.delegate.get().encode(input, ops, prefix);
        }
    }

    public record TagEntryId(Identifier id, boolean tag) {
        @Override
        public String toString() {
            return this.asString();
        }

        private String asString() {
            return this.tag ? "#" + this.id : this.id.toString();
        }
    }
}

