package net.modificationstation.stationapi.impl.util.dynamic;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import org.apache.commons.lang3.mutable.MutableObject;

import java.util.Optional;

public class Codecs {

    public static <A> Codec.ResultFunction<A> orElsePartial(final A object) {
        return new Codec.ResultFunction<>() {

            @Override
            public <T> DataResult<Pair<A, T>> apply(DynamicOps<T> ops, T input, DataResult<Pair<A, T>> result) {
                MutableObject<String> mutableObject = new MutableObject<>();
                Optional<Pair<A, T>> optional = result.resultOrPartial(mutableObject::setValue);
                if (optional.isPresent()) {
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
}

