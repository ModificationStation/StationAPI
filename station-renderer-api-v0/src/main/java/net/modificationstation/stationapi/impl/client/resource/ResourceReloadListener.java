package net.modificationstation.stationapi.impl.client.resource;

import net.minecraft.client.resource.TexturePack;
import net.modificationstation.stationapi.api.util.profiler.Profiler;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public interface ResourceReloadListener {
   CompletableFuture<Void> reload(ResourceReloadListener.Synchronizer synchronizer, TexturePack manager, Profiler prepareProfiler, Profiler applyProfiler, Executor prepareExecutor, Executor applyExecutor);

   default String getName() {
      return this.getClass().getSimpleName();
   }

   interface Synchronizer {
      <T> CompletableFuture<T> whenPrepared(T preparedObject);
   }
}
