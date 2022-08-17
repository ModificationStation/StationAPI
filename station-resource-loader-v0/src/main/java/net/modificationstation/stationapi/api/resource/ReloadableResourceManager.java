package net.modificationstation.stationapi.api.resource;

import net.modificationstation.stationapi.api.util.Unit;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public interface ReloadableResourceManager extends ResourceManager {

    void registerReloader(ResourceReloader reloader);

    ResourceReload reload(Executor prepareExecutor, Executor applyExecutor, CompletableFuture<Unit> initialStage, List<ResourcePack> packs);
}
