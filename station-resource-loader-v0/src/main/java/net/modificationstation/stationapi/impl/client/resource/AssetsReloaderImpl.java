package net.modificationstation.stationapi.impl.client.resource;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.event.resource.AssetsReloadEvent;
import net.modificationstation.stationapi.api.client.event.resource.TexturePackLoadedEvent;
import net.modificationstation.stationapi.api.client.resource.ReloadScreenManager;
import net.modificationstation.stationapi.api.client.resource.ReloadableAssetsManager;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.resource.CompositeResourceReload;
import net.modificationstation.stationapi.api.util.Unit;
import net.modificationstation.stationapi.api.util.Util;
import net.modificationstation.stationapi.impl.resource.DefaultResourcePackProvider;
import net.modificationstation.stationapi.impl.resource.ResourcePackManager;
import net.modificationstation.stationapi.impl.resource.TexturePackProvider;
import net.modificationstation.stationapi.impl.resource.loader.ModResourcePackCreator;

import java.util.concurrent.CompletableFuture;

import static cyclops.control.Option.none;
import static cyclops.control.Option.some;
import static net.modificationstation.stationapi.api.StationAPI.MODID;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public final class AssetsReloaderImpl {
    public static final ResourcePackManager RESOURCE_PACK_MANAGER = new ResourcePackManager(
            new DefaultResourcePackProvider(),
            ModResourcePackCreator.CLIENT_RESOURCE_PACK_PROVIDER,
            new TexturePackProvider()
    );
    public static final CompletableFuture<Unit> COMPLETED_UNIT_FUTURE = CompletableFuture.completedFuture(Unit.INSTANCE);

    @EventListener
    private static void reload(TexturePackLoadedEvent.After event) {
        StationAPI.EVENT_BUS.post(AssetsReloadEvent.builder().build());
    }

    @EventListener
    private static void reloadResourceManager(final AssetsReloadEvent event) {
        RESOURCE_PACK_MANAGER.scanPacks();
        ReloadScreenManager.getCurrentReload()
                .onEmpty(ReloadScreenManager::open);
        ReloadScreenManager.getCurrentReload()
                .flatMap(reload1 -> reload1 instanceof CompositeResourceReload composite ? some(composite) : none())
                .peek(manager -> manager.scheduleReload(
                        MODID.id("assets"),
                        () -> ReloadableAssetsManager.INSTANCE.reload(
                                Util.getMainWorkerExecutor(),
                                ReloadScreenManager.getApplicationExecutor(),
                                AssetsReloaderImpl.COMPLETED_UNIT_FUTURE,
                                ReloadScreenManager::pushLocation,
                                RESOURCE_PACK_MANAGER.createResourcePacks()
                        )
                ));
    }
}
