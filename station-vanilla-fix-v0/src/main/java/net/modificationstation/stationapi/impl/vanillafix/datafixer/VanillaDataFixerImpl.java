package net.modificationstation.stationapi.impl.vanillafix.datafixer;

import com.google.common.base.Suppliers;
import com.mojang.datafixers.DataFixer;
import com.mojang.datafixers.DataFixerBuilder;
import com.mojang.datafixers.schemas.Schema;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.datafixer.DataFixers;
import net.modificationstation.stationapi.api.datafixer.TypeReferences;
import net.modificationstation.stationapi.api.event.datafixer.DataFixerRegisterEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.util.Util;
import net.modificationstation.stationapi.api.vanillafix.datadamager.damage.StationFlatteningToMcRegionChunkDamage;
import net.modificationstation.stationapi.api.vanillafix.datadamager.damage.StationFlatteningToMcRegionItemStackDamage;
import net.modificationstation.stationapi.api.vanillafix.datadamager.schema.McRegionChunkDamagerSchema;
import net.modificationstation.stationapi.api.vanillafix.datadamager.schema.McRegionItemStackDamagerSchema;
import net.modificationstation.stationapi.api.vanillafix.datadamager.schema.StationFlatteningDamagerSchemaB1_7_3;
import net.modificationstation.stationapi.api.vanillafix.datafixer.fix.McRegionToStationFlatteningChunkFix;
import net.modificationstation.stationapi.api.vanillafix.datafixer.fix.McRegionToStationFlatteningItemStackFix;
import net.modificationstation.stationapi.api.vanillafix.datafixer.schema.McRegionSchemaB1_7_3;
import net.modificationstation.stationapi.api.vanillafix.datafixer.schema.StationFlatteningChunkSchema;
import net.modificationstation.stationapi.api.vanillafix.datafixer.schema.StationFlatteningItemStackSchema;

import java.util.Set;
import java.util.function.Supplier;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public final class VanillaDataFixerImpl {
    public static final String STATION_ID = NAMESPACE.id("id").toString();
    public static final int CURRENT_VERSION = 69421;
    public static final int HIGHEST_VERSION = Integer.MAX_VALUE / 10;
    public static final int VANILLA_VERSION = HIGHEST_VERSION - 19132;
    public static final Supplier<DataFixer> DATA_DAMAGER = Suppliers.memoize(() -> {
        final DataFixerBuilder builder = new DataFixerBuilder(VANILLA_VERSION);
        Schema schema69421 = builder.addSchema(HIGHEST_VERSION - 69421, StationFlatteningDamagerSchemaB1_7_3::new);
        Schema schema69420 = builder.addSchema(HIGHEST_VERSION - 69420, McRegionChunkDamagerSchema::new);
        builder.addFixer(new StationFlatteningToMcRegionChunkDamage(schema69420, "StationFlatteningToMcRegionChunkDamage"));
        Schema schema19132 = builder.addSchema(VANILLA_VERSION, McRegionItemStackDamagerSchema::new);
        builder.addFixer(new StationFlatteningToMcRegionItemStackDamage(schema19132, "StationFlatteningToMcRegionItemStackDamage"));
        return builder.buildOptimized(Set.of(TypeReferences.LEVEL), Util.getBootstrapExecutor());
    });

    @EventListener
    private static void registerFixer(DataFixerRegisterEvent event) {
        DataFixers.registerFixer(NAMESPACE, executor -> {
            DataFixerBuilder builder = new DataFixerBuilder(CURRENT_VERSION);
            Schema schema19132 = builder.addSchema(19132, McRegionSchemaB1_7_3::new);
            Schema schema69420 = builder.addSchema(69420, StationFlatteningItemStackSchema::new);
            builder.addFixer(new McRegionToStationFlatteningItemStackFix(schema69420, "McRegionToStationFlatteningItemStackFix"));
            Schema schema69421 = builder.addSchema(69421, StationFlatteningChunkSchema::new);
            builder.addFixer(new McRegionToStationFlatteningChunkFix(schema69421, "McRegionToStationFlatteningChunkFix"));
            return builder.buildOptimized(Set.of(TypeReferences.LEVEL), executor);
        }, CURRENT_VERSION);
    }
}
