package net.modificationstation.stationapi.api.client.resource;

import lombok.Getter;
import net.fabricmc.loader.api.FabricLoader;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.client.Minecraft;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.event.resource.ClientResourceReloaderRegisterEvent;
import net.modificationstation.stationapi.api.client.event.resource.ClientResourcesReloadEvent;
import net.modificationstation.stationapi.api.client.event.resource.TexturePackLoadedEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.resource.*;
import net.modificationstation.stationapi.api.util.Null;
import net.modificationstation.stationapi.api.util.Unit;
import net.modificationstation.stationapi.api.util.Util;
import net.modificationstation.stationapi.api.util.thread.ReentrantThreadExecutor;
import net.modificationstation.stationapi.api.util.thread.ThreadExecutor;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class ClientResources extends ReentrantThreadExecutor<Runnable> {

    @Entrypoint.Instance
    public static final ThreadExecutor<Runnable> APPLY_EXECUTOR = Null.get();
    private static Thread thread;
    public static final CompletableFuture<Unit> COMPLETED_UNIT_FUTURE = CompletableFuture.completedFuture(Unit.INSTANCE);

    @Getter
    private static ReloadableResourceManager resourceManager;

    public ClientResources() {
        super("ClientResources");
    }

    @EventListener(numPriority = Integer.MAX_VALUE / 2 + Integer.MAX_VALUE / 4)
    private static void onTexturePackLoad(TexturePackLoadedEvent.After event) {
        StationAPI.EVENT_BUS.post(ClientResourcesReloadEvent.builder().build());
    }

    @EventListener(numPriority = Integer.MAX_VALUE / 2 + Integer.MAX_VALUE / 4)
    private void reloadResourceManager(ClientResourcesReloadEvent event) {
        if (resourceManager == null)
            resourceManager = StationAPI.EVENT_BUS.post(ClientResourceReloaderRegisterEvent.builder().resourceManager(new ReloadableResourceManagerImpl(ResourceType.CLIENT_RESOURCES)).build()).resourceManager;
        thread = Thread.currentThread();
        //noinspection deprecation
        ResourceReload resourceReload = resourceManager.reload(Util.getMainWorkerExecutor(), APPLY_EXECUTOR, COMPLETED_UNIT_FUTURE, List.of((ResourcePack) ((Minecraft) FabricLoader.getInstance().getGameInstance()).texturePackManager.texturePack));
        while (!resourceReload.isComplete()) runTasks();
        resourceReload.throwException();
    }

    @Override
    protected Runnable createTask(Runnable runnable) {
        return runnable;
    }

    @Override
    protected boolean canExecute(Runnable task) {
        return true;
    }

    @Override
    protected Thread getThread() {
        return thread;
    }
}
