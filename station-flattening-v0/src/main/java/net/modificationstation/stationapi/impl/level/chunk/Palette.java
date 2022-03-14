package net.modificationstation.stationapi.impl.level.chunk;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.io.ListTag;
import net.modificationstation.stationapi.api.packet.Message;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public interface Palette<T> {
   int getIndex(T object);

   boolean accepts(Predicate<T> predicate);

   @Nullable
   T getByIndex(int index);

   @Environment(EnvType.CLIENT)
   void fromPacket(Message buf);

   void toPacket(Message buf);

//   int getPacketSize();

   void fromTag(ListTag tag);
}
