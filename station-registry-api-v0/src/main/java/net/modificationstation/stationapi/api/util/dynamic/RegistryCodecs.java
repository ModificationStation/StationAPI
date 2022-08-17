package net.modificationstation.stationapi.api.util.dynamic;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.modificationstation.stationapi.api.registry.RegistryEntryList;

import java.util.List;
import java.util.function.Function;

public class RegistryCodecs {
    public static <T> Function<RegistryEntryList<T>, DataResult<RegistryEntryList<T>>> createNonEmptyEntryListChecker() {
        return entries -> {
            if (entries.getStorage().right().filter(List::isEmpty).isPresent()) {
                return DataResult.error("List must have contents");
            }
            return DataResult.success(entries);
        };
    }

    public static <T> Codec<RegistryEntryList<T>> nonEmptyEntryList(Codec<RegistryEntryList<T>> originalCodec) {
        return originalCodec.flatXmap(createNonEmptyEntryListChecker(), createNonEmptyEntryListChecker());
    }
}
