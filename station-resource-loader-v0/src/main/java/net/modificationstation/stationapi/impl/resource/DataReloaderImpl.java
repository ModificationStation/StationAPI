package net.modificationstation.stationapi.impl.resource;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.event.resource.DataReloadEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.resource.DataManager;
import net.modificationstation.stationapi.api.resource.ResourceReload;
import net.modificationstation.stationapi.api.resource.ResourceType;
import net.modificationstation.stationapi.api.util.Unit;
import net.modificationstation.stationapi.api.util.Util;
import net.modificationstation.stationapi.impl.resource.loader.ModResourcePackCreator;

import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class DataReloaderImpl {

    private static final ResourcePackManager DATA_PACK_MANAGER = new ResourcePackManager(consumer -> consumer.accept(ResourcePackProfile.create("vanilla", "fixText", true, name -> new DefaultResourcePack(), ResourceType.SERVER_DATA, ResourcePackProfile.InsertionPosition.BOTTOM, ResourcePackSource.BUILTIN)), new ModResourcePackCreator(ResourceType.SERVER_DATA));
    private static final Queue<Runnable> TASKS = new ConcurrentLinkedQueue<>();
    private static final CompletableFuture<Unit> COMPLETED_UNIT_FUTURE = CompletableFuture.completedFuture(Unit.INSTANCE);

    @EventListener(numPriority = Integer.MAX_VALUE / 2 + Integer.MAX_VALUE / 4)
    private static void reloadResourceManager(DataReloadEvent event) {
        DATA_PACK_MANAGER.scanPacks();
        ResourceReload resourceReload = DataManager.INSTANCE.reload(
                Util.getMainWorkerExecutor(),
                TASKS::add,
                COMPLETED_UNIT_FUTURE,
                DATA_PACK_MANAGER.createResourcePacks()
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
