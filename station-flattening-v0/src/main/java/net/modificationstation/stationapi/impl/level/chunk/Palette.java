package net.modificationstation.stationapi.impl.level.chunk;

import net.modificationstation.stationapi.api.util.collection.IndexedIterable;

import java.util.List;
import java.util.function.Predicate;

/**
 * A palette maps objects from and to small integer IDs that uses less
 * number of bits to make storage smaller.
 *
 * <p>While the objects palettes handle are already represented by integer
 * IDs, shrinking IDs in cases where only a few appear can further reduce
 * storage space and network traffic volume.
 *
 * @see PalettedContainer
 */
public interface Palette<T> {
   /**
    * {@return the ID of an object in this palette}
    *
    * <p>If the object does not yet exist in this palette, this palette will
    * register the object. If the palette is too small to include this object,
    * a {@linkplain PaletteResizeListener resize listener} will be called and
    * this palette may be discarded.
    *
    * @param object the object to look up
    */
   int index(T object);

   /**
    * {@return {@code true} if any entry in this palette passes the {@code
    * predicate}}
    */
   boolean hasAny(Predicate<T> var1);

   /**
    * {@return the object associated with the given {@code id}}
    *
    * @throws EntryMissingException if this ID does not exist in this palette
    *
    * @param id the ID to look up
    */
   T get(int id);

//   /**
//    * Initializes this palette from the {@code buf}. Clears the preexisting
//    * data in this palette.
//    *
//    * @param buf the packet byte buffer
//    */
//   public void readPacket(PacketByteBuf var1);
//
//   /**
//    * Writes this palette to the {@code buf}.
//    *
//    * @param buf the packet byte buffer
//    */
//   public void writePacket(PacketByteBuf var1);
//
//   /**
//    * {@return the serialized size of this palette in a byte buf, in bytes}
//    */
//   public int getPacketSize();

   /**
    * {@return the size of the palette}
    */
   int getSize();

   Palette<T> copy();

   interface Factory {
      <A> Palette<A> create(int var1, IndexedIterable<A> var2, PaletteResizeListener<A> var3, List<A> var4);
   }
}

