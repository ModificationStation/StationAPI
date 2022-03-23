package net.modificationstation.stationapi.impl.level.chunk;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.level.LightType;
import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.States;
import net.modificationstation.stationapi.api.packet.Message;
import net.modificationstation.stationapi.impl.nbt.BlockStateHelper;
import net.modificationstation.stationapi.impl.nbt.LongArrayCompound;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class ChunkSection {
   private static final String PALETTE_KEY = "Palette";
   private static final String BLOCKSTATES_KEY = "BlockStates";
   private static final String METADATA_KEY = "Data";
   private static final String SKY_LIGHT_KEY = "SkyLight";
   private static final String BLOCK_LIGHT_KEY = "BlockLight";
   
   private static final Palette<BlockState> PALETTE;
   @Nullable
   public static final ChunkSection EMPTY_SECTION = null;
   private final short yOffset;
   private short nonEmptyBlockCount;
   private short randomTickableBlockCount;
   private short nonEmptyFluidCount;
   private final PalettedContainer<BlockState> container;
   private final NibbleArray metadataArray = new NibbleArray(4096);
   private final NibbleArray skyLightArray = new NibbleArray(4096);
   private final NibbleArray blockLightArray = new NibbleArray(4096);

   public ChunkSection(int yOffset) {
      this((short) yOffset, (short) 0, (short) 0, (short) 0);
   }

   public ChunkSection(short yOffset, short nonEmptyBlockCount, short randomTickableBlockCount, short nonEmptyFluidCount) {
      this.yOffset = yOffset;
      this.nonEmptyBlockCount = nonEmptyBlockCount;
      this.randomTickableBlockCount = randomTickableBlockCount;
      this.nonEmptyFluidCount = nonEmptyFluidCount;
      this.container = new PalettedContainer<>(PALETTE, States.STATE_IDS, BlockStateHelper::toBlockState, BlockStateHelper::fromBlockState, States.AIR.get());
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

   public int getMeta(int index) {
      return metadataArray.getValue(index);
   }
   
   public int getMeta(int x, int y, int z) {
      return getMeta(getIndex(x, y, z));
   }
   
   public void setMeta(int index, int meta) {
      metadataArray.setValue(index, meta);
   }
   
   public void setMeta(int x, int y, int z, int meta) {
      setMeta(getIndex(x, y, z), meta);
   }
   
   public int getLight(LightType type, int index) {
      return getLightArray(type).getValue(index);
   }
   
   public int getLight(LightType type, int x, int y, int z) {
      return getLight(type, getIndex(x, y, z));
   }
   
   public void setLight(LightType type, int index, int light) {
      getLightArray(type).setValue(index, light);
   }
   
   public void setLight(LightType type, int x, int y, int z, int light) {
      setLight(type, getIndex(x, y, z), light);
   }
   
   public CompoundTag toNBT() {
      CompoundTag tag = new CompoundTag();
      tag.put("Y", (byte) (yOffset >> 4));
      getContainer().write(tag, "Palette", "BlockStates");
      tag.put(METADATA_KEY, metadataArray.toTag());
      tag.put(SKY_LIGHT_KEY, skyLightArray.toTag());
      tag.put(BLOCK_LIGHT_KEY, blockLightArray.toTag());
      return tag;
   }
   
   public void fromNBT(CompoundTag chunkTag, CompoundTag sectionTag) {
      if (sectionTag.containsKey(PALETTE_KEY) && sectionTag.containsKey(BLOCKSTATES_KEY)) {
         long[] data = LongArrayCompound.class.cast(sectionTag).getLongArray(BLOCKSTATES_KEY);
         getContainer().read(sectionTag.getListTag(PALETTE_KEY), data);
      }
      metadataArray.copyArray(sectionTag.getByteArray(METADATA_KEY));
      skyLightArray.copyArray(sectionTag.getByteArray(SKY_LIGHT_KEY));
      blockLightArray.copyArray(sectionTag.getByteArray(BLOCK_LIGHT_KEY));
      if (chunkTag.containsKey(METADATA_KEY)) {
         fillArrayPart(chunkTag.getByteArray(METADATA_KEY), metadataArray);
      }
      if (chunkTag.containsKey(SKY_LIGHT_KEY)) {
         fillArrayPart(chunkTag.getByteArray(SKY_LIGHT_KEY), skyLightArray);
      }
      if (chunkTag.containsKey(BLOCK_LIGHT_KEY)) {
         fillArrayPart(chunkTag.getByteArray(BLOCK_LIGHT_KEY), blockLightArray);
      }
   }
   
   // vanilla order x << 11 | z << 7 | y
   private void fillArrayPart(byte[] src, NibbleArray res) {
      int index = 0;
      final short maxY = (short) (yOffset + 16);
      for (byte x = 0; x < 16; x++) {
         for (byte z = 0; z < 16; z++) {
            for (short y = yOffset; y < maxY; y++) {
               int srcIndex = x << 11 | z << 7 | y;
               byte offset = (byte) (srcIndex & 1);
               res.setValue(index++, (src[srcIndex >> 1] >> offset) & 15);
            }
         }
      }
   }
   
   private int getIndex(int x, int y, int z) {
      return x << 8 | y << 4 | z;
   }
   
   private NibbleArray getLightArray(LightType type) {
      return type == LightType.BLOCK ? blockLightArray : skyLightArray;
   }
   
   static {
      PALETTE = new IdListPalette<>(States.STATE_IDS, States.AIR.get());
   }
}
