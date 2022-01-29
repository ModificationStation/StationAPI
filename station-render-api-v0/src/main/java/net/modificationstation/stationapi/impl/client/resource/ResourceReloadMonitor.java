package net.modificationstation.stationapi.impl.client.resource;

import com.mojang.datafixers.util.Unit;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.concurrent.*;

public interface ResourceReloadMonitor {
   CompletableFuture<Unit> whenComplete();

   @Environment(EnvType.CLIENT)
   float getProgress();

   @Environment(EnvType.CLIENT)
   boolean isPrepareStageComplete();

   @Environment(EnvType.CLIENT)
   boolean isApplyStageComplete();

   @Environment(EnvType.CLIENT)
   void throwExceptions();
}
