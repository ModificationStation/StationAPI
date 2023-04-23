package net.modificationstation.stationapi.impl.level.chunk;

import net.minecraft.block.BlockBase;
import net.minecraft.level.LightType;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.States;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class ChunkSection {

    @Nullable
    public static final ChunkSection EMPTY_SECTION = null;
    private final short yOffset;
    private short nonEmptyBlockCount;
    private short randomTickableBlockCount;
    private short nonEmptyFluidCount;
    private final PalettedContainer<BlockState> blockStateContainer;
    private final NibbleArray metadataArray = new NibbleArray(4096);
    private final NibbleArray skyLightArray = new NibbleArray(4096);
    private final NibbleArray blockLightArray = new NibbleArray(4096);

    public ChunkSection(int chunkPos, PalettedContainer<BlockState> blockStateContainer) {
        this.yOffset = (short) ChunkSection.blockCoordFromChunkCoord(chunkPos);
        this.blockStateContainer = blockStateContainer;
        this.calculateCounts();
    }

    public ChunkSection(int chunkPos) {
        this.yOffset = (short) ChunkSection.blockCoordFromChunkCoord(chunkPos);
        this.blockStateContainer = new PalettedContainer<>(BlockBase.STATE_IDS, States.AIR.get(), PalettedContainer.PaletteProvider.BLOCK_STATE);
    }

    public static int blockCoordFromChunkCoord(int chunkPos) {
        return chunkPos << 4;
    }

    public BlockState getBlockState(int x, int y, int z) {
        return this.blockStateContainer.get(x, y, z);
    }

//   public FluidState getFluidState(int x, int y, int z) {
//      return ((BlockState)this.container.get(x, y, z)).getFluidState();
//   }

    public BlockState setBlockState(int x, int y, int z, BlockState state) {
        BlockState blockState = this.blockStateContainer.swap(x, y, z, state);

//      FluidState fluidState = blockState2.getFluidState();
//      FluidState fluidState2 = state.getFluidState();
        if (!blockState.isAir()) {
            --this.nonEmptyBlockCount;
            if (blockState.hasRandomTicks()) {
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

        return blockState;
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
        this.blockStateContainer.count((blockState, i) -> {
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

    public PalettedContainer<BlockState> getBlockStateContainer() {
        return this.blockStateContainer;
    }

    public NibbleArray getMetadataArray() {
        return metadataArray;
    }

//    @Environment(EnvType.CLIENT)
//    public void fromPacket(Message packetByteBuf) {
//        this.nonEmptyBlockCount = packetByteBuf.shorts[0];
//        this.blockStateContainer.fromPacket(packetByteBuf);
//    }
//
//    public void toPacket(Message packetByteBuf) {
//        packetByteBuf.shorts = new short[] { this.nonEmptyBlockCount };
//        this.blockStateContainer.toPacket(packetByteBuf);
//    }
//
//   public int getPacketSize() {
//      return 2 + this.container.getPacketSize();
//   }

    public boolean hasAny(Predicate<BlockState> predicate) {
        return this.blockStateContainer.hasAny(predicate);
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

    private int getIndex(int x, int y, int z) {
        return x << 8 | y << 4 | z;
    }

    public NibbleArray getLightArray(LightType type) {
        return type == LightType.field_2758 ? blockLightArray : skyLightArray;
    }
}
