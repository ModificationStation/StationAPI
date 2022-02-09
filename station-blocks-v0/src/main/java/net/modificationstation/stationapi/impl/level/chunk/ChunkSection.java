package net.modificationstation.stationapi.impl.level.chunk;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.States;
import net.modificationstation.stationapi.api.packet.Message;
import net.modificationstation.stationapi.impl.block.NbtHelper;
import org.jetbrains.annotations.Nullable;

import java.util.function.*;

public class ChunkSection {
   private static final Palette<BlockState> PALETTE;
   @Nullable
   public static final ChunkSection EMPTY_SECTION = null;
   private final int yOffset;
   private short nonEmptyBlockCount;
   private short randomTickableBlockCount;
   private short nonEmptyFluidCount;
   private final PalettedContainer<BlockState> container;

   public ChunkSection(int yOffset) {
      this(yOffset, (short)0, (short)0, (short)0);
   }

   public ChunkSection(int yOffset, short nonEmptyBlockCount, short randomTickableBlockCount, short nonEmptyFluidCount) {
      this.yOffset = yOffset;
      this.nonEmptyBlockCount = nonEmptyBlockCount;
      this.randomTickableBlockCount = randomTickableBlockCount;
      this.nonEmptyFluidCount = nonEmptyFluidCount;
      this.container = new PalettedContainer<>(PALETTE, States.STATE_IDS, NbtHelper::toBlockState, NbtHelper::fromBlockState, States.AIR.get());
   }

   public BlockState getBlockState(int x, int y, int z) {
      return this.container.get(x, y, z);
   }

//   public FluidState getFluidState(int x, int y, int z) {
//      return ((BlockState)this.container.get(x, y, z)).getFluidState();
//   }

   public void lock() {
      this.container.lock();
   }

   public void unlock() {
      this.container.unlock();
   }

   public BlockState setBlockState(int x, int y, int z, BlockState state) {
      return this.setBlockState(x, y, z, state, true);
   }

   public BlockState setBlockState(int x, int y, int z, BlockState state, boolean lock) {
      BlockState blockState2;
      if (lock) {
         blockState2 = this.container.setSync(x, y, z, state);
      } else {
         blockState2 = this.container.set(x, y, z, state);
      }

//      FluidState fluidState = blockState2.getFluidState();
//      FluidState fluidState2 = state.getFluidState();
      if (!blockState2.isAir()) {
         --this.nonEmptyBlockCount;
         if (blockState2.hasRandomTicks()) {
            --this.randomTickableBlockCount;
         }
      }

//      if (!fluidState.isEmpty()) {
//         --this.nonEmptyFluidCount;
//      }

      if (!state.isAir()) {
         ++this.nonEmptyBlockCount;
         if (state.hasRandomTicks()) {
            ++this.randomTickableBlockCount;
         }
      }

//      if (!fluidState2.isEmpty()) {
//         ++this.nonEmptyFluidCount;
//      }

      return blockState2;
   }

   public boolean isEmpty() {
      return this.nonEmptyBlockCount == 0;
   }

   public static boolean isEmpty(@Nullable ChunkSection section) {
      return section == EMPTY_SECTION || section.isEmpty();
   }

   public boolean hasRandomTicks() {
      return this.hasRandomBlockTicks() || this.hasRandomFluidTicks();
   }

   public boolean hasRandomBlockTicks() {
      return this.randomTickableBlockCount > 0;
   }

   public boolean hasRandomFluidTicks() {
      return this.nonEmptyFluidCount > 0;
   }

   public int getYOffset() {
      return this.yOffset;
   }

   public void calculateCounts() {
      this.nonEmptyBlockCount = 0;
      this.randomTickableBlockCount = 0;
      this.nonEmptyFluidCount = 0;
      this.container.count((blockState, i) -> {
//         FluidState fluidState = blockState.getFluidState();
         if (!blockState.isAir()) {
            this.nonEmptyBlockCount = (short)(this.nonEmptyBlockCount + i);
            if (blockState.hasRandomTicks()) {
               this.randomTickableBlockCount = (short)(this.randomTickableBlockCount + i);
            }
         }

//         if (!fluidState.isEmpty()) {
//            this.nonEmptyBlockCount = (short)(this.nonEmptyBlockCount + i);
//            if (fluidState.hasRandomTicks()) {
//               this.nonEmptyFluidCount = (short)(this.nonEmptyFluidCount + i);
//            }
//         }

      });
   }

   public PalettedContainer<BlockState> getContainer() {
      return this.container;
   }

   @Environment(EnvType.CLIENT)
   public void fromPacket(Message packetByteBuf) {
      this.nonEmptyBlockCount = packetByteBuf.shorts[0];
      this.container.fromPacket(packetByteBuf);
   }

   public void toPacket(Message packetByteBuf) {
      packetByteBuf.shorts = new short[] { this.nonEmptyBlockCount };
      this.container.toPacket(packetByteBuf);
   }

//   public int getPacketSize() {
//      return 2 + this.container.getPacketSize();
//   }

   public boolean hasAny(Predicate<BlockState> predicate) {
      return this.container.hasAny(predicate);
   }

   static {
      PALETTE = new IdListPalette<>(States.STATE_IDS, States.AIR.get());
   }
}
