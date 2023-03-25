package net.modificationstation.stationapi.impl.client.resource;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.event.resource.AssetsReloadEvent;
import net.modificationstation.stationapi.api.client.event.resource.TexturePackLoadedEvent;
import net.modificationstation.stationapi.api.client.resource.ReloadableAssetsManager;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.resource.ResourceReload;
import net.modificationstation.stationapi.api.resource.ResourceType;
import net.modificationstation.stationapi.api.util.Unit;
import net.modificationstation.stationapi.api.util.Util;
import net.modificationstation.stationapi.impl.resource.DefaultResourcePack;
import net.modificationstation.stationapi.impl.resource.ResourcePackManager;
import net.modificationstation.stationapi.impl.resource.ResourcePackProfile;
import net.modificationstation.stationapi.impl.resource.ResourcePackSource;
import net.modificationstation.stationapi.impl.resource.loader.ModResourcePackCreator;

import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public final class AssetsReloaderImpl {

    private static final ResourcePackManager RESOURCE_PACK_MANAGER = new ResourcePackManager(consumer -> consumer.accept(ResourcePackProfile.create("vanilla", "fixText", true, name -> new DefaultResourcePack(), ResourceType.CLIENT_RESOURCES, ResourcePackProfile.InsertionPosition.BOTTOM, ResourcePackSource.BUILTIN)), ModResourcePackCreator.CLIENT_RESOURCE_PACK_PROVIDER);
    private static final Queue<Runnable> TASKS = new ConcurrentLinkedQueue<>();
    private static final CompletableFuture<Unit> COMPLETED_UNIT_FUTURE = CompletableFuture.completedFuture(Unit.INSTANCE);

    @EventListener(numPriority = Integer.MAX_VALUE / 2 + Integer.MAX_VALUE / 4)
    private static void reload(TexturePackLoadedEvent.After event) {
        StationAPI.EVENT_BUS.post(AssetsReloadEvent.builder().build());
    }

    @EventListener(numPriority = Integer.MAX_VALUE / 2 + Integer.MAX_VALUE / 4)
    private static void reloadResourceManager(AssetsReloadEvent event) {
        RESOURCE_PACK_MANAGER.scanPacks();
        ResourceReload resourceReload = ReloadableAssetsManager.INSTANCE.reload(
                Util.getMainWorkerExecutor(),
                TASKS::add,
                COMPLETED_UNIT_FUTURE,
                RESOURCE_PACK_MANAGER.createResourcePacks()
        );
        long timestamp = 0;
        while (!resourceReload.isComplete()) {
            if (System.currentTimeMillis() - timestamp > 1000) {
                timestamp = System.currentTimeMillis();
                System.out.println(resourceReload.getProgress() * 100 + "%");
            }
            Runnable task = TASKS.poll();
            if (task != null)
                task.run();
        }
        resourceReload.throwException();
    }
}
