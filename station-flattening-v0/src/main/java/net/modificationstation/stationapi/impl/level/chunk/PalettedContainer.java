package net.modificationstation.stationapi.impl.level.chunk;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.io.CompoundTag;
import net.minecraft.util.io.ListTag;
import net.modificationstation.stationapi.api.packet.Message;
import net.modificationstation.stationapi.api.util.collection.IdList;
import net.modificationstation.stationapi.api.util.collection.PackedIntegerArray;
import net.modificationstation.stationapi.api.util.exception.CrashException;
import net.modificationstation.stationapi.api.util.exception.CrashReport;
import net.modificationstation.stationapi.api.util.exception.CrashReportSection;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import net.modificationstation.stationapi.impl.nbt.LongArrayCompound;

import java.util.*;
import java.util.concurrent.locks.*;
import java.util.function.*;
import java.util.stream.*;

public class PalettedContainer<T> implements PaletteResizeListener<T> {
   private final Palette<T> fallbackPalette;
   private final PaletteResizeListener<T> noOpPaletteResizeHandler = (newSize, added) -> 0;
   private final IdList<T> idList;
   private final Function<CompoundTag, T> elementDeserializer;
   private final Function<T, CompoundTag> elementSerializer;
   private final T defaultValue;
   protected PackedIntegerArray data;
   private Palette<T> palette;
   private int paletteSize;
   private final ReentrantLock writeLock = new ReentrantLock();

   public void lock() {
      if (this.writeLock.isLocked() && !this.writeLock.isHeldByCurrentThread()) {
         String string = Thread.getAllStackTraces().keySet().stream().filter(Objects::nonNull).map((thread) -> thread.getName() + ": \n\tat " + Arrays.stream(thread.getStackTrace()).map(Object::toString).collect(Collectors.joining("\n\tat "))).collect(Collectors.joining("\n"));
         CrashReport crashReport = new CrashReport("Writing into PalettedContainer from multiple threads", new IllegalStateException());
         CrashReportSection crashReportSection = crashReport.addElement("Thread dumps");
         crashReportSection.add("Thread dumps", string);
         throw new CrashException(crashReport);
      } else {
         this.writeLock.lock();
      }
   }

   public void unlock() {
      this.writeLock.unlock();
   }

   public PalettedContainer(Palette<T> fallbackPalette, IdList<T> idList, Function<CompoundTag, T> elementDeserializer, Function<T, CompoundTag> elementSerializer, T defaultElement) {
      this.fallbackPalette = fallbackPalette;
      this.idList = idList;
      this.elementDeserializer = elementDeserializer;
      this.elementSerializer = elementSerializer;
      this.defaultValue = defaultElement;
      this.setPaletteSize(4);
   }

   private static int toIndex(int x, int y, int z) {
      return (y << 8) | (z << 4) | x;
   }

   private void setPaletteSize(int size) {
      if (size != this.paletteSize) {
         this.paletteSize = size;
         if (this.paletteSize <= 4) {
            this.paletteSize = 4;
            this.palette = new ArrayPalette<>(this.idList, this.paletteSize, this, this.elementDeserializer);
         } else if (this.paletteSize < 9) {
            this.palette = new BiMapPalette<>(this.idList, this.paletteSize, this, this.elementDeserializer, this.elementSerializer);
         } else {
            this.palette = this.fallbackPalette;
            this.paletteSize = MathHelper.log2DeBruijn(this.idList.size());
         }

         this.palette.getIndex(this.defaultValue);
         this.data = new PackedIntegerArray(this.paletteSize, 4096);
      }
   }

   public int onResize(int i, T object) {
      this.lock();
      PackedIntegerArray packedIntegerArray = this.data;
      Palette<T> palette = this.palette;
      this.setPaletteSize(i);

      int j;
      for(j = 0; j < packedIntegerArray.getSize(); ++j) {
         T object2 = palette.getByIndex(packedIntegerArray.get(j));
         if (object2 != null) {
            this.set(j, object2);
         }
      }

      j = this.palette.getIndex(object);
      this.unlock();
      return j;
   }

   public T setSync(int x, int y, int z, T value) {
      this.lock();
      T object = this.setAndGetOldValue(toIndex(x, y, z), value);
      this.unlock();
      return object;
   }

   public T set(int x, int y, int z, T value) {
      return this.setAndGetOldValue(toIndex(x, y, z), value);
   }

   protected T setAndGetOldValue(int index, T value) {
      int i = this.palette.getIndex(value);
      int j = this.data.setAndGetOldValue(index, i);
      T object = this.palette.getByIndex(j);
      return object == null ? this.defaultValue : object;
   }

   protected void set(int i, T object) {
      int j = this.palette.getIndex(object);
      this.data.set(i, j);
   }

   public T get(int x, int y, int z) {
      return this.get(toIndex(x, y, z));
   }

   protected T get(int index) {
      T object = this.palette.getByIndex(this.data.get(index));
      return object == null ? this.defaultValue : object;
   }

   @Environment(EnvType.CLIENT)
   public void fromPacket(Message buf) {
      this.lock();
      int i = buf.longs.length;
      if (this.paletteSize != i) {
         this.setPaletteSize(i);
      }

      this.palette.fromPacket(buf);
      System.arraycopy(buf.longs, 0, data.getStorage(), 0, i);
      this.unlock();
   }

   public void toPacket(Message buf) {
      this.lock();
      buf.longs = new long[paletteSize];
      this.palette.toPacket(buf);
      System.arraycopy(data.getStorage(), 0, buf.longs, 0, paletteSize);
      this.unlock();
   }

   public void read(ListTag paletteTag, long[] data) {
      this.lock();
      int i = Math.max(4, MathHelper.log2DeBruijn(paletteTag.size()));
      if (i != this.paletteSize) {
         this.setPaletteSize(i);
      }

      this.palette.fromTag(paletteTag);
      int j = data.length * 64 / 4096;
      if (this.palette == this.fallbackPalette) {
         Palette<T> palette = new BiMapPalette<>(this.idList, i, this.noOpPaletteResizeHandler, this.elementDeserializer, this.elementSerializer);
         palette.fromTag(paletteTag);
         PackedIntegerArray packedIntegerArray = new PackedIntegerArray(i, 4096, data);

         for(int k = 0; k < 4096; ++k) {
            this.data.set(k, this.fallbackPalette.getIndex(palette.getByIndex(packedIntegerArray.get(k))));
         }
      } else if (j == this.paletteSize) {
         System.arraycopy(data, 0, this.data.getStorage(), 0, data.length);
      } else {
         PackedIntegerArray packedIntegerArray2 = new PackedIntegerArray(j, 4096, data);

         for(int l = 0; l < 4096; ++l) {
            this.data.set(l, packedIntegerArray2.get(l));
         }
      }

      this.unlock();
   }

   public void write(CompoundTag compoundTag, String string, String string2) {
      this.lock();
      BiMapPalette<T> biMapPalette = new BiMapPalette<>(this.idList, this.paletteSize, this.noOpPaletteResizeHandler, this.elementDeserializer, this.elementSerializer);
      T object = this.defaultValue;
      int i = biMapPalette.getIndex(this.defaultValue);
      int[] is = new int[4096];

      for(int j = 0; j < 4096; ++j) {
         T object2 = this.get(j);
         if (object2 != object) {
            object = object2;
            i = biMapPalette.getIndex(object2);
         }

         is[j] = i;
      }

      ListTag listTag = new ListTag();
      biMapPalette.toTag(listTag);
      compoundTag.put(string, listTag);
      int k = Math.max(4, MathHelper.log2DeBruijn(listTag.size()));
      PackedIntegerArray packedIntegerArray = new PackedIntegerArray(k, 4096);

      for(int l = 0; l < is.length; ++l) {
         packedIntegerArray.set(l, is[l]);
      }

      ((LongArrayCompound) compoundTag).put(string2, packedIntegerArray.getStorage());
      this.unlock();
   }

//   public int getPacketSize() {
//      return 1 + this.palette.getPacketSize() + PacketByteBuf.getVarIntSizeBytes(this.data.getSize()) + this.data.getStorage().length * 8;
//   }

   public boolean hasAny(Predicate<T> predicate) {
      return this.palette.accepts(predicate);
   }

   public void count(PalettedContainer.CountConsumer<T> consumer) {
      Int2IntMap int2IntMap = new Int2IntOpenHashMap();
      this.data.forEach((i) -> int2IntMap.put(i, int2IntMap.get(i) + 1));
      int2IntMap.int2IntEntrySet().forEach((entry) -> consumer.accept(this.palette.getByIndex(entry.getIntKey()), entry.getIntValue()));
   }

   @FunctionalInterface
   public interface CountConsumer<T> {
      void accept(T object, int count);
   }
}
