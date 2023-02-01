package net.modificationstation.stationapi.impl.vanillafix.datafixer;

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
import net.modificationstation.stationapi.api.vanillafix.datafixer.schema.Schema19132;
import net.modificationstation.stationapi.api.vanillafix.datafixer.schema.Schema69420;

import static net.modificationstation.stationapi.api.StationAPI.MODID;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public final class VanillaDataFixerImpl {

    public static final String STATION_ID = MODID.id("id").toString();
    public static int currentVersion = 69420;

    @EventListener(numPriority = Integer.MAX_VALUE / 2 + Integer.MAX_VALUE / 4)
    private static void registerFixer(DataFixerRegisterEvent event) {
        DataFixerBuilder builder = new DataFixerBuilder(currentVersion);
        Schema schema19132 = builder.addSchema(19132, Schema19132::new);
        Schema schema69420 = builder.addSchema(69420, Schema69420::new);
        builder.addFixer(new McRegionToStationFlatteningChunkFix(schema69420));
        builder.addFixer(McRegionToStationFlatteningItemStackFix.create(schema69420, "Vanilla itemstack fix", Schema69420.ITEM_RENAMES));
        DataFixers.registerFixer(MODID, builder.buildOptimized(Util.getBootstrapExecutor()), currentVersion);
//        File regions = new File(Minecraft.getGameDirectory(), "saves/No Sortme Module Copy/region");
//        File region = new File(regions, "r.0.0.mcr");
//        RegionFile regionFile = new RegionFile(region);
//        CompoundTag chunkTag;
//        chunkTag = NBTIO.readTag(regionFile.getChunkDataInputStream(0, 0));
////        CompoundTag playerTag = chunkTag.getCompoundTag("Data").getCompoundTag("Player");
//        CompoundTag newTag = (CompoundTag) DataFixers.update(TypeReferences.CHUNK, NbtOps.INSTANCE, chunkTag);
////        chunkTag.getCompoundTag("Data").put("Player", newTag);
//        File levelNew = new File(regions, "new_chunk.dat");
//        try {
//            NBTIO.writeGzipped(newTag, new FileOutputStream(levelNew));
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }
    }
}
