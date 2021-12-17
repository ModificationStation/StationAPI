package net.modificationstation.stationapi.impl.level.dimension;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.minecraft.level.dimension.Nether;
import net.minecraft.level.dimension.Overworld;
import net.minecraft.level.dimension.Skylands;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.mod.InitEvent;
import net.modificationstation.stationapi.api.event.registry.DimensionRegistryEvent;
import net.modificationstation.stationapi.api.lang.I18n;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.DimensionContainer;
import net.modificationstation.stationapi.api.registry.DimensionRegistry;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.util.Null;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;
import static net.modificationstation.stationapi.api.level.dimension.VanillaDimensions.OVERWORLD;
import static net.modificationstation.stationapi.api.level.dimension.VanillaDimensions.SKYLANDS;
import static net.modificationstation.stationapi.api.level.dimension.VanillaDimensions.THE_NETHER;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class DimensionAPI {

    @Entrypoint.ModID
    private static final ModID MODID = Null.get();

    @EventListener(priority = ListenerPriority.HIGH)
    private static void init(InitEvent event) {
        LOGGER.info("Adding dimension API lang folder...");
        I18n.addLangFolder(StationAPI.MODID, "/assets/" + MODID + "/lang");
    }

    @EventListener(priority = ListenerPriority.HIGH)
    private static void registerDimensions(DimensionRegistryEvent event) {
        DimensionRegistry r = event.registry;
        r.register(THE_NETHER, -1, new DimensionContainer<>(Nether::new));
        r.register(OVERWORLD, 0, new DimensionContainer<>(Overworld::new));
        r.register(SKYLANDS, 1, new DimensionContainer<>(Skylands::new));
    }
}
