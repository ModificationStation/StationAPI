package net.modificationstation.stationapi.api.util.profiler;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.function.*;

public interface Profiler {
   void startTick();

   void endTick();

   void push(String location);

   void push(Supplier<String> locationGetter);

   void pop();

   void swap(String location);

   @Environment(EnvType.CLIENT)
   void swap(Supplier<String> locationGetter);

   /**
    * Increment the visit count for a marker.
    * 
    * <p>This is useful to keep track of number of calls made to performance-
    * wise expensive methods.
    * 
    * @param marker a unique marker
    */
   void visit(String marker);

   /**
    * Increment the visit count for a marker.
    * 
    * <p>This is useful to keep track of number of calls made to performance-
    * wise expensive methods.
    * 
    * <p>This method is preferred if getting the marker is costly; the
    * supplier won't be called if the profiler is disabled.
    * 
    * @param markerGetter the getter for a unique marker
    */
   void visit(Supplier<String> markerGetter);

   static Profiler union(final Profiler profiler, final Profiler profiler2) {
      if (profiler == DummyProfiler.INSTANCE) {
         return profiler2;
      } else {
         return profiler2 == DummyProfiler.INSTANCE ? profiler : new Profiler() {
            public void startTick() {
               profiler.startTick();
               profiler2.startTick();
            }

            public void endTick() {
               profiler.endTick();
               profiler2.endTick();
            }

            public void push(String location) {
               profiler.push(location);
               profiler2.push(location);
            }

            public void push(Supplier<String> locationGetter) {
               profiler.push(locationGetter);
               profiler2.push(locationGetter);
            }

            public void pop() {
               profiler.pop();
               profiler2.pop();
            }

            public void swap(String location) {
               profiler.swap(location);
               profiler2.swap(location);
            }

            @Environment(EnvType.CLIENT)
            public void swap(Supplier<String> locationGetter) {
               profiler.swap(locationGetter);
               profiler2.swap(locationGetter);
            }

            public void visit(String marker) {
               profiler.visit(marker);
               profiler2.visit(marker);
            }

            public void visit(Supplier<String> markerGetter) {
               profiler.visit(markerGetter);
               profiler2.visit(markerGetter);
            }
         };
      }
   }
}
