package net.modificationstation.stationapi.api.util.profiler;

import it.unimi.dsi.fastutil.objects.Object2LongMap;

/**
 * Profiling information on a specific profiler location.
 */
public interface ProfileLocationInfo {
   /**
    * Returns the total time spent visiting the profiler location.
    */
   long getTotalTime();

   /**
    * Returns the number of times the profiler location has been visited.
    */
   long getVisitCount();

   /**
    * Returns a marker to count map indicating the times each marker has been
    * visited in the profiler location.
    */
   Object2LongMap<String> getCounts();
}
