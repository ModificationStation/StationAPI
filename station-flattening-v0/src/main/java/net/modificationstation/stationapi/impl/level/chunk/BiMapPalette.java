package net.modificationstation.stationapi.impl.level.chunk;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.io.CompoundTag;
import net.minecraft.util.io.ListTag;
import net.modificationstation.stationapi.api.packet.Message;
import net.modificationstation.stationapi.api.util.collection.IdList;
import net.modificationstation.stationapi.api.util.collection.Int2ObjectBiMap;
import org.jetbrains.annotations.Nullable;

import java.util.function.*;

public class BiMapPalette<T> implements Palette<T> {
   private final IdList<T> idList;
   private final Int2ObjectBiMap<T> map;
   private final PaletteResizeListener<T> resizeHandler;
   private final Function<CompoundTag, T> elementDeserializer;
   private final Function<T, CompoundTag> elementSerializer;
   private final int indexBits;

   public BiMapPalette(IdList<T> idList, int indexBits, PaletteResizeListener<T> resizeHandler, Function<CompoundTag, T> elementDeserializer, Function<T, CompoundTag> elementSerializer) {
      this.idList = idList;
      this.indexBits = indexBits;
      this.resizeHandler = resizeHandler;
      this.elementDeserializer = elementDeserializer;
      this.elementSerializer = elementSerializer;
      this.map = new Int2ObjectBiMap<>(1 << indexBits);
   }

   public int getIndex(T object) {
      int i = this.map.getRawId(object);
      if (i == -1) {
         i = this.map.add(object);
         if (i >= 1 << this.indexBits) {
            i = this.resizeHandler.onResize(this.indexBits + 1, object);
         }
      }

      return i;
   }

   public boolean accepts(Predicate<T> predicate) {
      for(int i = 0; i < this.getIndexBits(); ++i) {
         if (predicate.test(this.map.get(i))) {
            return true;
         }
      }

      return false;
   }

   @Nullable
   public T getByIndex(int index) {
      return this.map.get(index);
   }

   @Environment(EnvType.CLIENT)
   public void fromPacket(Message buf) {
      this.map.clear();
      int i = buf.ints.length;

      for(int j = 0; j < i; ++j) {
         this.map.add(this.idList.get(buf.ints[j]));
      }

   }

   public void toPacket(Message buf) {
      int i = this.getIndexBits();
      buf.ints = new int[i];

      for(int j = 0; j < i; ++j) {
         buf.ints[j] = (this.idList.getRawId(this.map.get(j)));
      }

   }

//   public int getPacketSize() {
//      int i = PacketByteBuf.getVarIntSizeBytes(this.getIndexBits());
//
//      for(int j = 0; j < this.getIndexBits(); ++j) {
//         i += PacketByteBuf.getVarIntSizeBytes(this.idList.getRawId(this.map.get(j)));
//      }
//
//      return i;
//   }

   public int getIndexBits() {
      return this.map.size();
   }

   public void fromTag(ListTag tag) {
      this.map.clear();

      for(int i = 0; i < tag.size(); ++i) {
         this.map.add(this.elementDeserializer.apply((CompoundTag) tag.get(i)));
      }

   }

   public void toTag(ListTag tag) {
      for(int i = 0; i < this.getIndexBits(); ++i) {
         tag.add(this.elementSerializer.apply(this.map.get(i)));
      }

   }
}
