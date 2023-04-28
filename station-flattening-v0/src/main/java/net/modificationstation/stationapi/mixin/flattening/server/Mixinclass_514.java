package net.modificationstation.stationapi.mixin.flattening.server;

import it.unimi.dsi.fastutil.shorts.ShortSet;
import net.minecraft.packet.AbstractPacket;
import net.minecraft.server.ServerPlayerView;
import net.minecraft.tileentity.TileEntityBase;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ServerPlayerView.class_514.class)
public abstract class Mixinclass_514 {

    @Shadow
    @Final
    ServerPlayerView field_2136;

    @Shadow
    private int field_2142;

    @Shadow
    private int field_2143;

    @Shadow
    private int field_2144;

    @Shadow
    private int field_2145;

    @Shadow
    private int field_2146;

    @Shadow
    private int field_2147;

    @Shadow
    private int field_2148;

    @Shadow
    private short[] field_2141;

    @Shadow
    private int field_2138;
    @Shadow
    private int field_2139;

    @Shadow
    public abstract void method_1755(AbstractPacket arg);

    @Shadow
    protected abstract void method_1756(TileEntityBase arg);

    private final ShortSet[] blockUpdatesBySection = new ShortSet[field_2136.method_1741().countVerticalSections()];

    /**
     * @author mine_diver
     * @reason early version
     */
    @Overwrite
    public void method_1753(int x, int y, int z) {
//        if (this.field_2142 == 0) {
//            //noinspection DataFlowIssue
//            ((ServerPlayerViewAccessor) field_2136).getField_2131().add((ServerPlayerView.class_514) (Object) this);
//            this.field_2143 = x;
//            this.field_2145 = y;
//            this.field_2147 = z;
//        }
//        short position = ChunkSectionPos.packLocal(x, y, z);
//
//        int sectionY = field_2136.method_1741().getSectionIndex(y);
//        if (this.blockUpdatesBySection[sectionY] == null)
//            this.blockUpdatesBySection[sectionY] = new ShortOpenHashSet();
//        ShortSet blockUpdates = this.blockUpdatesBySection[sectionY];
//        if (blockUpdates.contains(position)) return;
//
//        blockUpdates.add(position);
//        field_2142++;
    }

    /**
     * @author mine_diver
     * @reason early version
     */
    @Overwrite
    public void method_1752() {
//        ServerLevel world = field_2136.method_1741();
//        if (this.field_2142 != 0) {
//            int x;
//            int y;
//            int z;
//            if (this.field_2142 == 1) {
//                x = this.field_2138 * 16 + this.field_2143;
//                y = this.field_2145;
//                z = this.field_2139 * 16 + this.field_2147;
//                this.method_1755(new StationFlatteningBlockChangeS2CPacket(x, y, z, world));
//                if (BlockBase.HAS_TILE_ENTITY[world.getTileId(x, y, z)])
//                    this.method_1756(world.getTileEntity(x, y, z));
//            } else if (this.field_2142 == 10) {
//                this.field_2145 = this.field_2145 / 2 * 2;
//                this.field_2146 = (this.field_2146 / 2 + 1) * 2;
//                x = this.field_2143 + this.field_2138 * 16;
//                y = this.field_2145;
//                z = this.field_2147 + this.field_2139 * 16;
//                int sizeX = this.field_2144 - this.field_2143 + 1;
//                int sizeY = this.field_2146 - this.field_2145 + 2;
//                int sizeZ = this.field_2148 - this.field_2147 + 1;
//                this.method_1755(new MapChunk0x33S2CPacket(x, y, z, sizeX, sizeY, sizeZ, world));
//                //noinspection unchecked
//                List<TileEntityBase> tileEntities = world.method_330(x, y, z, x + sizeX, y + sizeY, z + sizeZ);
//                for (int var9 = 0; var9 < tileEntities.size(); ++var9) this.method_1756(tileEntities.get(var9));
//            } else {
//                if (!(world.getChunk(field_2138, field_2139) instanceof StationFlatteningChunkImpl chunk)) throw new RuntimeException();
//                for (int i = 0; i < blockUpdatesBySection.length; i++) {
//                    int sectionY = world.sectionIndexToCoord(i);
//                    ShortSet blockUpdates = blockUpdatesBySection[i];
//                    if (blockUpdates == null || blockUpdates.isEmpty()) continue;
//                    this.method_1755(new StationFlatteningMultiBlockChangeS2CPacket(
//                            ChunkSectionPos.from(field_2138, sectionY, field_2139),
//                            blockUpdatesBySection[i],
//                            chunk.sections[i]
//                    ));
//                }
//                this.method_1755(new MultiBlockChange0x34S2CPacket(this.field_2138, this.field_2139, this.field_2141, this.field_2142, world));
//                for (int i = 0; i < this.field_2142; i++) {
//                    x = this.field_2138 * 16 + (this.field_2142 >> 12 & 15);
//                    y = this.field_2142 & 255;
//                    z = this.field_2139 * 16 + (this.field_2142 >> 8 & 15);
//                    if (BlockBase.HAS_TILE_ENTITY[world.getTileId(x, y, z)]) {
//                        System.out.println("Sending!");
//                        this.method_1756(world.getTileEntity(x, y, z));
//                    }
//                }
//            }
//
//            this.field_2142 = 0;
//        }
    }
}
