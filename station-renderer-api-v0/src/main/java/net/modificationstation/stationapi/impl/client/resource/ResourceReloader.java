package net.modificationstation.stationapi.impl.client.resource;

import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Unit;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resource.TexturePack;
import net.modificationstation.stationapi.api.util.Util;
import net.modificationstation.stationapi.api.util.profiler.DummyProfiler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

public class ResourceReloader<S> implements ResourceReloadMonitor {
   protected final TexturePack manager;
   protected final CompletableFuture<Unit> prepareStageFuture = new CompletableFuture<>();
   protected final CompletableFuture<List<S>> applyStageFuture;
   private final Set<ResourceReloadListener> waitingListeners;
   private final int listenerCount;
   private int applyingCount;
   private int appliedCount;
   private final AtomicInteger preparingCount = new AtomicInteger();
   private final AtomicInteger preparedCount = new AtomicInteger();

   public static ResourceReloader<Void> create(TexturePack manager, List<ResourceReloadListener> listeners, Executor prepareExecutor, Executor applyExecutor, CompletableFuture<Unit> initialStage) {
//      return new ResourceReloader<>(prepareExecutor, applyExecutor, manager, listeners, (synchronizer, resourceManager, resourceReloadListener, executor2, executor3) -> resourceReloadListener.reload(synchronizer, resourceManager, DummyProfiler.INSTANCE, DummyProfiler.INSTANCE, prepareExecutor, executor3), initialStage);
      listeners.stream().filter(resourceReloadListener -> resourceReloadListener instanceof SinglePreparationResourceReloadListener).map(resourceReloadListener -> (SinglePreparationResourceReloadListener<?>) resourceReloadListener).forEach(singlePreparationResourceReloadListener -> singlePreparationResourceReloadListener.apply(singlePreparationResourceReloadListener.prepare(manager, DummyProfiler.INSTANCE), manager, DummyProfiler.INSTANCE));
      return null;
   }

   protected ResourceReloader(Executor prepareExecutor, final Executor applyExecutor, TexturePack manager, List<ResourceReloadListener> listeners, ResourceReloader.Factory<S> creator, CompletableFuture<Unit> initialStage) {
      this.manager = manager;
      this.listenerCount = listeners.size();
      this.preparingCount.incrementAndGet();
      initialStage.thenRun(this.preparedCount::incrementAndGet);
      List<CompletableFuture<S>> list = new ArrayList<>();
      CompletableFuture<?> completableFuture = initialStage;
      this.waitingListeners = Sets.newHashSet(listeners);

      CompletableFuture<S> completableFuture3;
      for (ResourceReloadListener resourceReloadListener : listeners) {
         CompletableFuture<?> finalCompletableFuture = completableFuture;
         completableFuture3 = creator.create(new ResourceReloadListener.Synchronizer() {
            public <T> CompletableFuture<T> whenPrepared(T preparedObject) {
               applyExecutor.execute(() -> {
                  ResourceReloader.this.waitingListeners.remove(resourceReloadListener);
                  if (ResourceReloader.this.waitingListeners.isEmpty()) {
                     ResourceReloader.this.prepareStageFuture.complete(Unit.INSTANCE);
                  }

               });
               return ResourceReloader.this.prepareStageFuture.thenCombine(finalCompletableFuture, (unit, object2) -> preparedObject);
            }
         }, manager, resourceReloadListener, (runnable) -> {
            this.preparingCount.incrementAndGet();
            prepareExecutor.execute(() -> {
               runnable.run();
               this.preparedCount.incrementAndGet();
            });
         }, (runnable) -> {
            ++this.applyingCount;
            applyExecutor.execute(() -> {
               runnable.run();
               ++this.appliedCount;
            });
         });
         list.add(completableFuture3);
         completableFuture = completableFuture3;
      }

      this.applyStageFuture = Util.combine(list);
   }

   public CompletableFuture<Unit> whenComplete() {
      return this.applyStageFuture.thenApply((list) -> Unit.INSTANCE);
   }

   @Environment(EnvType.CLIENT)
   public float getProgress() {
      int i = this.listenerCount - this.waitingListeners.size();
      float f = (float)(this.preparedCount.get() * 2 + this.appliedCount * 2 + i);
      float g = (float)(this.preparingCount.get() * 2 + this.applyingCount * 2 + this.listenerCount);
      return f / g;
   }

   @Environment(EnvType.CLIENT)
   public boolean isPrepareStageComplete() {
      return this.prepareStageFuture.isDone();
   }

   @Environment(EnvType.CLIENT)
   public boolean isApplyStageComplete() {
      return this.applyStageFuture.isDone();
   }

   @Environment(EnvType.CLIENT)
   public void throwExceptions() {
      if (this.applyStageFuture.isCompletedExceptionally()) {
         this.applyStageFuture.join();
      }

   }

   public interface Factory<S> {
      CompletableFuture<S> create(ResourceReloadListener.Synchronizer helper, TexturePack manager, ResourceReloadListener listener, Executor prepareExecutor, Executor applyExecutor);
   }
}
