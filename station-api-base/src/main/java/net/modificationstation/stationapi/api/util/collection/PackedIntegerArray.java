package net.modificationstation.stationapi.api.util.collection;

import net.modificationstation.stationapi.api.util.Util;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Nullable;

import java.util.function.*;

public class PackedIntegerArray {
   private static final int[] field_24078 = new int[]{-1, -1, 0, Integer.MIN_VALUE, 0, 0, 1431655765, 1431655765, 0, Integer.MIN_VALUE, 0, 1, 858993459, 858993459, 0, 715827882, 715827882, 0, 613566756, 613566756, 0, Integer.MIN_VALUE, 0, 2, 477218588, 477218588, 0, 429496729, 429496729, 0, 390451572, 390451572, 0, 357913941, 357913941, 0, 330382099, 330382099, 0, 306783378, 306783378, 0, 286331153, 286331153, 0, Integer.MIN_VALUE, 0, 3, 252645135, 252645135, 0, 238609294, 238609294, 0, 226050910, 226050910, 0, 214748364, 214748364, 0, 204522252, 204522252, 0, 195225786, 195225786, 0, 186737708, 186737708, 0, 178956970, 178956970, 0, 171798691, 171798691, 0, 165191049, 165191049, 0, 159072862, 159072862, 0, 153391689, 153391689, 0, 148102320, 148102320, 0, 143165576, 143165576, 0, 138547332, 138547332, 0, Integer.MIN_VALUE, 0, 4, 130150524, 130150524, 0, 126322567, 126322567, 0, 122713351, 122713351, 0, 119304647, 119304647, 0, 116080197, 116080197, 0, 113025455, 113025455, 0, 110127366, 110127366, 0, 107374182, 107374182, 0, 104755299, 104755299, 0, 102261126, 102261126, 0, 99882960, 99882960, 0, 97612893, 97612893, 0, 95443717, 95443717, 0, 93368854, 93368854, 0, 91382282, 91382282, 0, 89478485, 89478485, 0, 87652393, 87652393, 0, 85899345, 85899345, 0, 84215045, 84215045, 0, 82595524, 82595524, 0, 81037118, 81037118, 0, 79536431, 79536431, 0, 78090314, 78090314, 0, 76695844, 76695844, 0, 75350303, 75350303, 0, 74051160, 74051160, 0, 72796055, 72796055, 0, 71582788, 71582788, 0, 70409299, 70409299, 0, 69273666, 69273666, 0, 68174084, 68174084, 0, Integer.MIN_VALUE, 0, 5};
   private final long[] storage;
   private final int elementBits;
   private final long maxValue;
   private final int size;
   private final int field_24079;
   private final int field_24080;
   private final int field_24081;
   private final int field_24082;

   public PackedIntegerArray(int elementBits, int size) {
      this(elementBits, size, null);
   }

   public PackedIntegerArray(int elementBits, int size, @SuppressWarnings("NullableProblems") @Nullable long[] storage) {
      Validate.inclusiveBetween(1L, 32L, elementBits);
      this.size = size;
      this.elementBits = elementBits;
      this.maxValue = (1L << elementBits) - 1L;
      this.field_24079 = (char)(64 / elementBits);
      int i = 3 * (this.field_24079 - 1);
      this.field_24080 = field_24078[i];
      this.field_24081 = field_24078[i + 1];
      this.field_24082 = field_24078[i + 2];
      int j = (size + this.field_24079 - 1) / this.field_24079;
      if (storage != null) {
         if (storage.length != j) {
            throw Util.throwOrPause(new RuntimeException("Invalid length given for storage, got: " + storage.length + " but expected: " + j));
         }

         this.storage = storage;
      } else {
         this.storage = new long[j];
      }

   }

   private int method_27284(int i) {
      long l = Integer.toUnsignedLong(this.field_24080);
      long m = Integer.toUnsignedLong(this.field_24081);
      return (int)((long)i * l + m >> 32 >> this.field_24082);
   }

   public int setAndGetOldValue(int index, int value) {
      Validate.inclusiveBetween(0L, this.size - 1, index);
      Validate.inclusiveBetween(0L, this.maxValue, value);
      int i = this.method_27284(index);
      long l = this.storage[i];
      int j = (index - i * this.field_24079) * this.elementBits;
      int k = (int)(l >> j & this.maxValue);
      this.storage[i] = l & ~(this.maxValue << j) | ((long)value & this.maxValue) << j;
      return k;
   }

   public void set(int index, int value) {
      Validate.inclusiveBetween(0L, this.size - 1, index);
      Validate.inclusiveBetween(0L, this.maxValue, value);
      int i = this.method_27284(index);
      long l = this.storage[i];
      int j = (index - i * this.field_24079) * this.elementBits;
      this.storage[i] = l & ~(this.maxValue << j) | ((long)value & this.maxValue) << j;
   }

   public int get(int index) {
      Validate.inclusiveBetween(0L, this.size - 1, index);
      int i = this.method_27284(index);
      long l = this.storage[i];
      int j = (index - i * this.field_24079) * this.elementBits;
      return (int)(l >> j & this.maxValue);
   }

   public long[] getStorage() {
      return this.storage;
   }

   public int getSize() {
      return this.size;
   }

   public void forEach(IntConsumer consumer) {
      int i = 0;
      long[] var3 = this.storage;
      int var4 = var3.length;

      //noinspection ForLoopReplaceableByForEach
      for(int var5 = 0; var5 < var4; ++var5) {
         long l = var3[var5];

         for(int j = 0; j < this.field_24079; ++j) {
            consumer.accept((int)(l & this.maxValue));
            l >>= this.elementBits;
            ++i;
            if (i >= this.size) {
               return;
            }
         }
      }

   }
}
