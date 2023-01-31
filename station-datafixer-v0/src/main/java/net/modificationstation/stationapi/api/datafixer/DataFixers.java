package net.modificationstation.stationapi.api.datafixer;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFixer;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.datafixer.DataFixerRegisterEvent;
import net.modificationstation.stationapi.api.registry.ModID;

import java.util.Collections;
import java.util.stream.Collectors;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;
import static net.modificationstation.stationapi.api.StationAPI.MODID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DataFixers {

    private static final String DATA_VERSIONS = MODID.id("data_versions").toString();

    private record DataFixerEntry(DataFixer fixer, int currentVersion) {}
    private static final Reference2ReferenceMap<ModID, DataFixerEntry> DATA_FIXERS = new Reference2ReferenceOpenHashMap<>();
    private static boolean init;

    public static void registerFixer(ModID mod, DataFixer fixer, int currentVersion) {
        DATA_FIXERS.put(mod, new DataFixerEntry(fixer, currentVersion));
    }

    public static <T> T update(DSL.TypeReference type, DynamicOps<T> ops, T input) {
        init();
        T dataVersions = ops.get(input, DATA_VERSIONS).result().orElseGet(() -> ops.createMap(Collections.emptyMap()));
        T ret = input;
        for (Reference2ReferenceMap.Entry<ModID, DataFixerEntry> fixerEntry : DATA_FIXERS.reference2ReferenceEntrySet())
            ret = fixerEntry.getValue().fixer.update(type, new Dynamic<>(ops, ret), ops.get(dataVersions, fixerEntry.getKey().toString()).result().map(version -> ops.getNumberValue(version).getOrThrow(false, LOGGER::error)).orElse(0).intValue(), fixerEntry.getValue().currentVersion).getValue();
        return ret;
    }

    public static <T> T addDataVersions(DynamicOps<T> ops, T input) {
        init();
        return ops.set(input, DATA_VERSIONS, ops.createMap(DATA_FIXERS.reference2ReferenceEntrySet().stream().collect(Collectors.toMap(t -> ops.createString(t.getKey().toString()), t -> ops.createInt(t.getValue().currentVersion)))));
    }

    public static <T> boolean requiresUpdating(DynamicOps<T> ops, T input) {
        init();
        T dataVersions = ops.get(input, DATA_VERSIONS).result().orElseGet(() -> ops.createMap(Collections.emptyMap()));
        for (Reference2ReferenceMap.Entry<ModID, DataFixerEntry> fixerEntry : DATA_FIXERS.reference2ReferenceEntrySet())
            if (ops.get(dataVersions, fixerEntry.getKey().toString()).result().map(version -> ops.getNumberValue(version).getOrThrow(false, LOGGER::error)).orElse(0).intValue() < fixerEntry.getValue().currentVersion)
                return true;
        return false;
    }

    private static void init() {
        if (!init) {
            init = true;
            StationAPI.EVENT_BUS.post(DataFixerRegisterEvent.builder().build());
        }
    }
}
