package net.modificationstation.stationapi.api.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Keyable;
import net.modificationstation.stationapi.api.util.dynamic.Codecs;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * An interface, usually implemented by enums, that allows the object to be serialized
 * by codecs. An instance is identified using a string.
 *
 * @apiNote To make an enum serializable with codecs, implement this on the enum class,
 * implement {@link #asString} to return a unique ID, and add a {@code static final}
 * field that holds {@linkplain #createCodec the codec for the enum}.
 */
public interface StringIdentifiable {
    int MAPIFY_THRESHOLD = 16;

    /**
     * {@return the unique string representation of the enum, used for serialization}
     */
    String asString();

    /**
     * Creates a codec that serializes an enum implementing this interface either
     * using its ordinals (when compressed) or using its {@link #asString()} method.
     */
    static <E extends Enum<E> & StringIdentifiable> StringIdentifiable.EnumCodec<E> createCodec(Supplier<E[]> enumValues) {
        return createCodec(enumValues, id -> id);
    }

    /**
     * Creates a codec that serializes an enum implementing this interface either
     * using its ordinals (when compressed) or using its {@link #asString()} method
     * and a given decode function.
     */
    static <E extends Enum<E> & StringIdentifiable> StringIdentifiable.EnumCodec<E> createCodec(
            Supplier<E[]> enumValues, Function<String, String> valueNameTransformer
    ) {
        E[] enums = (E[])enumValues.get();
        Function<String, E> function = createMapper(enums, valueNameTransformer);
        return new StringIdentifiable.EnumCodec<>(enums, function);
    }

    static <T extends StringIdentifiable> Codec<T> createBasicCodec(Supplier<T[]> values) {
        T[] identifiableValues = values.get();
        Function<String, T> function = createMapper(identifiableValues, valueName -> valueName);
        ToIntFunction<T> toIntFunction = Util.lastIndexGetter(Arrays.asList(identifiableValues));
        return new StringIdentifiable.BasicCodec<>(identifiableValues, function, toIntFunction);
    }

    static <T extends StringIdentifiable> Function<String, T> createMapper(T[] values, Function<String, String> valueNameTransformer) {
        if (values.length > 16) {
            Map<String, T> map = Arrays.stream(values)
                    .collect(Collectors.toMap(value -> valueNameTransformer.apply(value.asString()), value -> value));
            return name -> name == null ? null : map.get(name);
        } else {
            return name -> {
                for (T stringIdentifiable : values) {
                    if (valueNameTransformer.apply(stringIdentifiable.asString()).equals(name)) {
                        return stringIdentifiable;
                    }
                }

                return null;
            };
        }
    }

    static Keyable toKeyable(final StringIdentifiable[] values) {
        return new Keyable(){

            @Override
            public <T> Stream<T> keys(DynamicOps<T> ops) {
                return Arrays.stream(values).map(StringIdentifiable::asString).map(ops::createString);
            }
        };
    }

    class BasicCodec<S extends StringIdentifiable> implements Codec<S> {
        private final Codec<S> codec;

        public BasicCodec(S[] values, Function<String, S> idToIdentifiable, ToIntFunction<S> identifiableToOrdinal) {
            this.codec = Codecs.orCompressed(
                    Codec.stringResolver(StringIdentifiable::asString, idToIdentifiable),
                    Codecs.rawIdChecked(identifiableToOrdinal, ordinal -> ordinal >= 0 && ordinal < values.length ? values[ordinal] : null, -1)
            );
        }

        @Override
        public <T> DataResult<com.mojang.datafixers.util.Pair<S, T>> decode(DynamicOps<T> ops, T input) {
            return this.codec.decode(ops, input);
        }

        public <T> DataResult<T> encode(S stringIdentifiable, DynamicOps<T> dynamicOps, T object) {
            return this.codec.encode(stringIdentifiable, dynamicOps, object);
        }
    }

    class EnumCodec<E extends Enum<E> & StringIdentifiable> extends StringIdentifiable.BasicCodec<E> {
        private final Function<String, E> idToIdentifiable;

        public EnumCodec(E[] values, Function<String, E> idToIdentifiable) {
            super(values, idToIdentifiable, Enum::ordinal);
            this.idToIdentifiable = idToIdentifiable;
        }

        @Nullable
        public E byId(@Nullable String id) {
            return (E)this.idToIdentifiable.apply(id);
        }

        public E byId(@Nullable String id, E fallback) {
            return Objects.requireNonNullElse(this.byId(id), fallback);
        }

        public E byId(@Nullable String id, Supplier<? extends E> fallbackSupplier) {
            return Objects.requireNonNullElseGet(this.byId(id), fallbackSupplier);
        }
    }
}

