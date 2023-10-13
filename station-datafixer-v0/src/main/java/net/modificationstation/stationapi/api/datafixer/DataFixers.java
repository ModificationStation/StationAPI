package net.modificationstation.stationapi.api.datafixer;

import com.google.common.base.Suppliers;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFixer;
import com.mojang.serialization.Dynamic;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.datafixer.DataFixerRegisterEvent;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.util.Util;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static net.modificationstation.stationapi.api.StationAPI.MODID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DataFixers {

    public static final String DATA_VERSIONS = MODID.id("data_versions").toString();

    private record DataFixerEntry(Supplier<DataFixer> fixer, int currentVersion) {}
    private static final Reference2ReferenceMap<ModID, DataFixerEntry> DATA_FIXERS = new Reference2ReferenceOpenHashMap<>();
    private static boolean init;

    public static void registerFixer(ModID mod, Function<Executor, DataFixer> dataFixer, int currentVersion) {
        registerFixer(mod, () -> dataFixer.apply(Util.getBootstrapExecutor()), currentVersion);
    }

    public static void registerFixer(ModID mod, Supplier<DataFixer> dataFixer, int currentVersion) {
        DATA_FIXERS.put(mod, new DataFixerEntry(Suppliers.memoize(dataFixer::get), currentVersion));
    }

    public static <T> Dynamic<T> update(DSL.TypeReference type, Dynamic<T> dynamic) {
        init();
        Dynamic<T> dataVersions = getDataVersions(dynamic);
        Dynamic<T> ret = dynamic;
        for (Reference2ReferenceMap.Entry<ModID, DataFixerEntry> entry : DATA_FIXERS.reference2ReferenceEntrySet()) {
            DataFixerEntry fixerEntry = entry.getValue();
            int version = dataVersions.get(entry.getKey().toString()).asInt(0);
            int currentVersion = fixerEntry.currentVersion;
            // implemented this redundant check to avoid unnecessarily building the datafixer
            if (version < currentVersion)
                ret = fixerEntry.fixer.get().update(type, ret, version, currentVersion);
        }
        return ret;
    }

    public static <T> Dynamic<T> getDataVersions(Dynamic<T> dynamic) {
        return dynamic.get(DATA_VERSIONS).result().orElseGet(() -> dynamic.createMap(Collections.emptyMap()));
    }

    public static <T> Dynamic<T> addDataVersions(Dynamic<T> dynamic) {
        init();
        return dynamic.set(DATA_VERSIONS, dynamic.createMap(DATA_FIXERS.reference2ReferenceEntrySet().stream().collect(Collectors.toMap(t -> dynamic.createString(t.getKey().toString()), t -> dynamic.createInt(t.getValue().currentVersion)))));
    }

    public static <T> boolean requiresUpdating(Dynamic<T> dynamic) {
        init();
        Dynamic<T> dataVersions = getDataVersions(dynamic);
        for (Reference2ReferenceMap.Entry<ModID, DataFixerEntry> fixerEntry : DATA_FIXERS.reference2ReferenceEntrySet())
            if (dataVersions.get(fixerEntry.getKey().toString()).asInt(0) < fixerEntry.getValue().currentVersion)
                return true;
        return false;
    }

    public static <T> Set<UpdateData> getUpdateList(Dynamic<T> dynamic) {
        init();

        HashSet<UpdateData> set = new HashSet<>();
        Dynamic<T> dataVersions = getDataVersions(dynamic);

        for (Reference2ReferenceMap.Entry<ModID, DataFixerEntry> fixerEntry : DATA_FIXERS.reference2ReferenceEntrySet()) {
            int dataVersion = dataVersions.get(fixerEntry.getKey().toString()).asInt(0);
            int currentVersion = fixerEntry.getValue().currentVersion;

            if (dataVersion < currentVersion)
                set.add(new UpdateData(fixerEntry.getKey(), dataVersion, currentVersion));
        }

        return set;
    }

    public record UpdateData(ModID modID, int saveVersion, int currentVersion) {}

    private static void init() {
        if (!init) {
            init = true;
            StationAPI.EVENT_BUS.post(DataFixerRegisterEvent.builder().build());
        }
    }
}
