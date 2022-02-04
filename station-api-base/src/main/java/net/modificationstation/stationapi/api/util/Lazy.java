package net.modificationstation.stationapi.api.util;

import java.util.function.*;

public class Lazy<T> {
   private Supplier<T> supplier;
   private T value;

   public Lazy(Supplier<T> delegate) {
      this.supplier = delegate;
   }

   public T get() {
      Supplier<T> supplier = this.supplier;
      if (supplier != null) {
         this.value = supplier.get();
         this.supplier = null;
      }

      return this.value;
   }
}
