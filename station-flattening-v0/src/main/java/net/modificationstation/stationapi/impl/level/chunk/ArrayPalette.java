package net.modificationstation.stationapi.impl.level.chunk;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.io.CompoundTag;
import net.minecraft.util.io.ListTag;
import net.modificationstation.stationapi.api.packet.Message;
import net.modificationstation.stationapi.api.util.collection.IdList;
import org.jetbrains.annotations.Nullable;

import java.util.function.*;

public class ArrayPalette<T> implements Palette<T> {
   private final IdList<T> idList;
   private final T[] array;
   private final PaletteResizeListener<T> resizeListener;
   private final Function<CompoundTag, T> valueDeserializer;
   private final int indexBits;
   private int size;

   public ArrayPalette(IdList<T> idList, int integer, PaletteResizeListener<T> resizeListener, Function<CompoundTag, T> valueDeserializer) {
      this.idList = idList;
      //noinspection unchecked
      this.array = (T[]) new Object[1 << integer];
      this.indexBits = integer;
      this.resizeListener = resizeListener;
      this.valueDeserializer = valueDeserializer;
   }

   public int getIndex(T object) {
      int j;
      for(j = 0; j < this.size; ++j) {
         if (this.array[j] == object) {
            return j;
         }
      }

      j = this.size;
      if (j < this.array.length) {
         this.array[j] = object;
         ++this.size;
         return j;
      } else {
         return this.resizeListener.onResize(this.indexBits + 1, object);
      }
   }

   public boolean accepts(Predicate<T> predicate) {
      for(int i = 0; i < this.size; ++i) {
         if (predicate.test(this.array[i])) {
            return true;
         }
      }

      return false;
   }

   @Nullable
   public T getByIndex(int index) {
      return index >= 0 && index < this.size ? this.array[index] : null;
   }

   @Environment(EnvType.CLIENT)
   public void fromPacket(Message buf) {
      this.size = buf.ints.length;

      for(int i = 0; i < this.size; ++i) {
         this.array[i] = this.idList.get(buf.ints[i]);
      }

   }

   public void toPacket(Message buf) {
      buf.ints = new int[this.size];

      for(int i = 0; i < this.size; ++i) {
         buf.ints[i] = this.idList.getRawId(this.array[i]);
      }

   }

//   public int getPacketSize() {
//      int i = PacketByteBuf.getVarIntSizeBytes(this.getSize());
//
//      for(int j = 0; j < this.getSize(); ++j) {
//         i += PacketByteBuf.getVarIntSizeBytes(this.idList.getRawId(this.array[j]));
//      }
//
//      return i;
//   }

   public int getSize() {
      return this.size;
   }

   public void fromTag(ListTag tag) {
      for(int i = 0; i < tag.size(); ++i) {
         this.array[i] = this.valueDeserializer.apply((CompoundTag) tag.get(i));
      }

      this.size = tag.size();
   }
}
