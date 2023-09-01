package net.modificationstation.stationapi.api.util;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Keyable;
import it.unimi.dsi.fastutil.objects.Object2ReferenceMap;
import it.unimi.dsi.fastutil.objects.Object2ReferenceMaps;
import it.unimi.dsi.fastutil.objects.Object2ReferenceOpenHashMap;
import net.modificationstation.stationapi.api.util.dynamic.Codecs;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface StringIdentifiable {
    int MAPIFY_THRESHOLD = 16;

    String asString();

    /**
     * Creates a codec that serializes an enum implementing this interface either
     * using its ordinals (when compressed) or using its {@link #asString()} method
     * and a given decode function.
     */
    static <E extends Enum<E> & StringIdentifiable> Codec<E> createCodec(Supplier<E[]> enumValues) {
        E[] enums = enumValues.get();
        if (enums.length > MAPIFY_THRESHOLD) {
            //noinspection Convert2MethodRef
            Object2ReferenceMap<String, E> map = Object2ReferenceMaps.unmodifiable(Arrays.stream(enums).collect(Collectors.toMap(StringIdentifiable::asString, Function.identity(), (o, o2) -> { throw new IllegalStateException(String.format("Duplicate key %s", o)); }, () -> new Object2ReferenceOpenHashMap<>())));
            return new Codec<>(enums, id -> id == null ? null : map.get(id));
        }
        return new Codec<>(enums, id -> {
            for (E enum_ : enums) {
                if (!enum_.asString().equals(id)) continue;
                return enum_;
            }
            return null;
        });
    }

    static Keyable toKeyable(final StringIdentifiable[] values) {
        return new Keyable(){

            @Override
            public <T> Stream<T> keys(DynamicOps<T> ops) {
                return Arrays.stream(values).map(StringIdentifiable::asString).map(ops::createString);
            }
        };
    }

    class Codec<E extends Enum<E>>
            implements com.mojang.serialization.Codec<E> {
        private final com.mojang.serialization.Codec<E> base;
        private final Function<String, E> idToIdentifiable;

        public Codec(E[] values, Function<String, E> idToIdentifiable) {
            this.base = Codecs.orCompressed(Codecs.idChecked(identifiable -> ((StringIdentifiable) identifiable).asString(), idToIdentifiable), Codecs.rawIdChecked(Enum::ordinal, ordinal -> ordinal >= 0 && ordinal < values.length ? values[ordinal] : null, -1));
            this.idToIdentifiable = idToIdentifiable;
        }

        @Override
        public <T> DataResult<Pair<E, T>> decode(DynamicOps<T> ops, T input) {
            return this.base.decode(ops, input);
        }

        @Override
        public <T> DataResult<T> encode(E enum_, DynamicOps<T> dynamicOps, T object) {
            return this.base.encode(enum_, dynamicOps, object);
        }

        @Nullable
        public E byId(@Nullable String id) {
            return this.idToIdentifiable.apply(id);
        }
    }
}

