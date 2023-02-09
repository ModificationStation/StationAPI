package net.modificationstation.stationapi.impl.vanillafix.datafixer;

import com.google.common.base.Suppliers;
import com.mojang.datafixers.DataFixer;
import com.mojang.datafixers.DataFixerBuilder;
import com.mojang.datafixers.schemas.Schema;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.datafixer.DataFixers;
import net.modificationstation.stationapi.api.event.datafixer.DataFixerRegisterEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.util.Util;
import net.modificationstation.stationapi.api.vanillafix.datafixer.fix.McRegionToStationFlatteningChunkFix;
import net.modificationstation.stationapi.api.vanillafix.datafixer.fix.McRegionToStationFlatteningItemStackFix;
import net.modificationstation.stationapi.api.vanillafix.datafixer.schema.SchemaMcRegion;
import net.modificationstation.stationapi.api.vanillafix.datafixer.schema.SchemaStationFlattening;

import java.util.function.Supplier;

import static net.modificationstation.stationapi.api.StationAPI.MODID;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public final class VanillaDataFixerImpl {

    public static final String STATION_ID = MODID.id("id").toString();
    public static final int CURRENT_VERSION = 69420;
    private static final int HIGHEST_VERSION = Integer.MAX_VALUE / 10;
    public static final int VANILLA_VERSION = HIGHEST_VERSION - 19132;
    public static final Supplier<DataFixer> DATA_BREAKER = Suppliers.memoize(() -> Util.make(() -> {
        final DataFixerBuilder builder = new DataFixerBuilder(1);
        builder.addSchema(HIGHEST_VERSION - 69420, SchemaStationFlattening::new);
        builder.addSchema(VANILLA_VERSION, SchemaMcRegion::new);
        return builder.buildOptimized(Util.getBootstrapExecutor());
    }));

    @EventListener(numPriority = Integer.MAX_VALUE / 2 + Integer.MAX_VALUE / 4)
    private static void registerFixer(DataFixerRegisterEvent event) {
        DataFixers.registerFixer(MODID, executor -> {
            DataFixerBuilder builder = new DataFixerBuilder(CURRENT_VERSION);
            Schema schema19132 = builder.addSchema(19132, SchemaMcRegion::new);
            Schema schema69420 = builder.addSchema(69420, SchemaStationFlattening::new);
            builder.addFixer(McRegionToStationFlatteningChunkFix.create(schema69420, "Vanilla chunk fix", SchemaStationFlattening::lookupState));
            builder.addFixer(McRegionToStationFlatteningItemStackFix.create(schema69420, "Vanilla itemstack fix", SchemaStationFlattening::lookupItem));
            return builder.buildOptimized(executor);
        }, CURRENT_VERSION);
    }
}
