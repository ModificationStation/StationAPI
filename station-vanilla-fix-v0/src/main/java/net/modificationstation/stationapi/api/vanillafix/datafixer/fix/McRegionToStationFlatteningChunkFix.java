package net.modificationstation.stationapi.api.vanillafix.datafixer.fix;

import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.serialization.Dynamic;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import it.unimi.dsi.fastutil.objects.ReferenceList;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import it.unimi.dsi.fastutil.objects.ReferenceSet;
import net.minecraft.class_257;
import net.modificationstation.stationapi.api.datafixer.TypeReferences;
import net.modificationstation.stationapi.api.util.collection.Int2ObjectBiMap;
import net.modificationstation.stationapi.api.util.collection.PackedIntegerArray;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import net.modificationstation.stationapi.api.vanillafix.datafixer.schema.StationFlatteningItemStackSchema;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static net.modificationstation.stationapi.impl.world.FlattenedWorldManager.SECTIONS;

public class McRegionToStationFlatteningChunkFix extends DataFix {
    private final String name;

    public McRegionToStationFlatteningChunkFix(Schema outputSchema, String name) {
        super(outputSchema, true);
        this.name = name;
    }

    public static int addTo(Int2ObjectBiMap<Dynamic<?>> int2ObjectBiMap, Dynamic<?> dynamic) {
        int i = int2ObjectBiMap.getRawId(dynamic);
        if (i == -1) {
            i = int2ObjectBiMap.add(dynamic);
        }
        return i;
    }

    private Dynamic<?> fixChunk(Dynamic<?> dynamic) {
        Optional<? extends Dynamic<?>> optional = dynamic.get("Level").result();
        if (optional.isPresent() && optional.get().get("Blocks").asByteBufferOpt().result().isPresent()) {
            return dynamic.set("Level", new Level(optional.get()).transform());
        }
        return dynamic;
    }

    @Override
    protected TypeRewriteRule makeRule() {
        Type<?> type = this.getInputSchema().getType(TypeReferences.CHUNK);
        Type<?> type2 = this.getOutputSchema().getType(TypeReferences.CHUNK);
        return this.writeFixAndRead(name, type, type2, this::fixChunk);
    }

    static final class Level {
        private final Dynamic<?> level;
        private final class_257 block_light;
        private final ByteBuffer blocks;
        private final class_257 data;
        private final ByteBuffer height_map;
        private final class_257 sky_light;

        public Level(Dynamic<?> dynamic) {
            level = dynamic;
            block_light = new class_257(DataFixUtils.toArray(dynamic.get("BlockLight").asByteBuffer()));
            blocks = dynamic.get("Blocks").asByteBuffer();
            data = new class_257(DataFixUtils.toArray(dynamic.get("Data").asByteBuffer()));
            height_map = dynamic.get("HeightMap").asByteBuffer();
            sky_light = new class_257(DataFixUtils.toArray(dynamic.get("SkyLight").asByteBuffer()));
        }

        public Dynamic<?> transform() {
            Dynamic<?> self = this.level;

            // create sections with blocks
            Section[] sections = new Section[8];
            for (int i = 0; i < 32768; i++) {
                int worldY = i & 0b1111111;
                int sectionY = worldY >> 4;
                int y = (worldY & 0b1111);
                int z = i >> 7 & 0b1111;
                int x = i >> 11;
                int block = Byte.toUnsignedInt(blocks.get(i));
                int data = this.data.method_1703(x, worldY, z);
                if (block > 0 || data > 0) {
                    if (sections[sectionY] == null)
                        sections[sectionY] = new Section(self.createMap(Map.of(self.createString("y"), self.createByte((byte) sectionY))));
                    Section section = sections[sectionY];
                    section.setBlock(x, y, z, StationFlatteningItemStackSchema.lookupState(block)); // do not convert just yet. we need same references for faster key comparison
                    section.setData(x, y, z, data);
                }
            }

            // add lighting in created sections
            for (Section section : sections) {
                if (section != null) {
                    int sectionY = section.y;
                    int sectionWorldY = sectionY << 4;
                    for (int i = 0; i < 4096; i++) {
                        int x = i >> 8;
                        int y = i >> 4 & 0b1111;
                        int z = i & 0b1111;
                        int worldY = sectionWorldY | y;
                        section.setSkyLight(x, y, z, sky_light.method_1703(x, worldY, z));
                        section.setBlockLight(x, y, z, block_light.method_1703(x, worldY, z));
                    }
                }
            }

            // expand height map
            byte[] height_map = new byte[512];
            for (int i = 0; i < height_map.length >> 1; i++) height_map[i << 1] = this.height_map.get(i);

            return self
                    .set(SECTIONS, self.createList(Arrays.stream(sections).filter(Objects::nonNull).map(Section::transform)))
                    .set("height_map", self.createByteList(ByteBuffer.wrap(height_map)))
                    .remove("BlockLight")
                    .remove("Blocks")
                    .remove("Data")
                    .remove("HeightMap")
                    .remove("SkyLight");
        }
    }

    static class ChunkNibbleArray {
        public final byte[] data;

        public ChunkNibbleArray(int capacity) {
            data = new byte[capacity >> 1];
        }

        public void setValue(int index, int value) {
            int index2 = index >> 1;
            short internal = (short) (data[index2] & 255);
            internal = (index & 1) == 0 ? (short) (value | internal & 0xF0) : (short) (value << 4 | internal & 15);
            data[index2] = (byte) internal;
        }
    }

    static final class Section {

        private final Int2ObjectBiMap<Dynamic<?>> paletteMap = Int2ObjectBiMap.create(32);
        private final ReferenceList<Dynamic<?>> paletteData = new ReferenceArrayList<>();
        private final Dynamic<?> section;
        private final int y;
        private final ReferenceSet<Dynamic<?>> seenStates = new ReferenceOpenHashSet<>();
        private final ChunkNibbleArray block_light = new ChunkNibbleArray(4096);
        private final int[] states = new int[4096];
        private final ChunkNibbleArray data = new ChunkNibbleArray(4096);
        private final ChunkNibbleArray sky_light = new ChunkNibbleArray(4096);

        public Section(Dynamic<?> section) {
            this.section = section;
            y = section.get("y").asInt(0);
            Dynamic<?> air = StationFlatteningItemStackSchema.lookupState(0); // same applies
            seenStates.add(air);
            paletteData.add(air);
            paletteMap.add(air);
        }

        public void setSkyLight(int x, int y, int z, int skyLight) {
            this.sky_light.setValue(x << 8 | y << 4 | z, skyLight);
        }

        public void setBlock(int x, int y, int z, Dynamic<?> state) {
            if (seenStates.add(state)) paletteData.add(state);
            states[(y << 4 | z) << 4 | x] = addTo(paletteMap, state);
        }

        public void setData(int x, int y, int z, int data) {
            this.data.setValue(x << 8 | y << 4 | z, data);
        }

        public void setBlockLight(int x, int y, int z, int blockLight) {
            this.block_light.setValue(x << 8 | y << 4 | z, blockLight);
        }

        public Dynamic<?> transform() {
            Dynamic<?> self = this.section;
            Dynamic<?> palette = self.createList(paletteData.stream().map(dynamic -> dynamic.convert(self.getOps()))); // instead, convert when used
            PackedIntegerArray array = new PackedIntegerArray(Math.max(4, MathHelper.ceilLog2(paletteData.size())), states.length);
            for (int i = 0; i < states.length; i++) array.set(i, states[i]);
            Dynamic<?> data = self.createLongList(Arrays.stream(array.getData()));
            return self
                    .set("block_states", self.createMap(Map.of(
                            self.createString("palette"), palette,
                            self.createString("data"), data
                    )))
                    .set("block_light", self.createByteList(ByteBuffer.wrap(this.block_light.data)))
                    .set("data", self.createByteList(ByteBuffer.wrap(this.data.data)))
                    .set("sky_light", self.createByteList(ByteBuffer.wrap(this.sky_light.data)));
        }
    }
}
