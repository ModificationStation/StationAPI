package net.modificationstation.stationapi.impl.level.chunk;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.io.ListTag;
import net.modificationstation.stationapi.api.packet.Message;
import net.modificationstation.stationapi.api.util.collection.IdList;

import java.util.function.Predicate;

@SuppressWarnings("ClassCanBeRecord")
public class IdListPalette<T> implements Palette<T> {
   private final IdList<T> idList;
   private final T defaultValue;

   public IdListPalette(IdList<T> idList, T defaultValue) {
      this.idList = idList;
      this.defaultValue = defaultValue;
   }

   @Override
   public int getIndex(T object) {
      int i = this.idList.getRawId(object);
      return i == -1 ? 0 : i;
   }

   @Override
   public boolean accepts(Predicate<T> predicate) {
      return true;
   }

   @Override
   public T getByIndex(int index) {
      T object = this.idList.get(index);
      return object == null ? this.defaultValue : object;
   }

   @Override
   @Environment(EnvType.CLIENT)
   public void fromPacket(Message buf) {
   }

   @Override
   public void toPacket(Message buf) {
   }

//   public int getPacketSize() {
//      return PacketByteBuf.getVarIntSizeBytes(0);
//   }

   @Override
   public void fromTag(ListTag tag) {
   }
}
