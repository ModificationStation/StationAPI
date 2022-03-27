package net.modificationstation.stationapi.impl.client.resource;

import net.minecraft.client.resource.TexturePack;
import net.modificationstation.stationapi.api.util.profiler.Profiler;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public abstract class SinglePreparationResourceReloadListener<T> implements ResourceReloadListener {
   public final CompletableFuture<Void> reload(ResourceReloadListener.Synchronizer synchronizer, TexturePack manager, Profiler prepareProfiler, Profiler applyProfiler, Executor prepareExecutor, Executor applyExecutor) {
      return CompletableFuture.supplyAsync(() -> this.prepare(manager, prepareProfiler), prepareExecutor).thenCompose(synchronizer::whenPrepared).thenAcceptAsync((object) -> this.apply(object, manager, applyProfiler), applyExecutor);
   }

   protected abstract T prepare(TexturePack manager, Profiler profiler);

   protected abstract void apply(T loader, TexturePack manager, Profiler profiler);
}
