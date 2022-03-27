package net.modificationstation.stationapi.api.util.profiler;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.io.File;
import java.util.List;

public interface ProfileResult {
   @Environment(EnvType.CLIENT)
   List<ProfilerTiming> getTimings(String parentPath);

   boolean save(File file);

   long getStartTime();

   int getStartTick();

   long getEndTime();

   int getEndTick();

   default long getTimeSpan() {
      return this.getEndTime() - this.getStartTime();
   }

   default int getTickSpan() {
      return this.getEndTick() - this.getStartTick();
   }

   static String getHumanReadableName(String path) {
      return path.replace('\u001e', '.');
   }
}
