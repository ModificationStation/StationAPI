package net.modificationstation.stationapi.api.vanillafix.datadamager.damage;

import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.serialization.Dynamic;
import net.minecraft.class_257;
import net.minecraft.class_56;
import net.modificationstation.stationapi.api.datafixer.TypeReferences;
import net.modificationstation.stationapi.api.util.collection.PackedIntegerArray;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import net.modificationstation.stationapi.api.vanillafix.datafixer.schema.StationFlatteningItemStackSchema;
import net.modificationstation.stationapi.impl.level.chunk.NibbleArray;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static net.modificationstation.stationapi.impl.level.FlattenedWorldManager.SECTIONS;

public class StationFlatteningToMcRegionChunkDamage extends DataFix {

    private final static int CHUNK_SIZE = 16 * 128 * 16;
    private final static byte[] DEFAULT_BLOCK_LIGHT = new byte[CHUNK_SIZE >> 1];
    private final static byte[] DEFAULT_SKY_LIGHT = new byte[CHUNK_SIZE >> 1];
    static {
        Arrays.fill(DEFAULT_BLOCK_LIGHT, (byte) (class_56.field_2758.field_2759 << 4 | class_56.field_2758.field_2759));
        Arrays.fill(DEFAULT_SKY_LIGHT, (byte) (class_56.field_2757.field_2759 << 4 | class_56.field_2757.field_2759));
    }

    private final String name;

    public StationFlatteningToMcRegionChunkDamage(Schema outputSchema, String name) {
        super(outputSchema, true);
        this.name = name;
    }

    private Dynamic<?> damageChunk(Dynamic<?> dynamic) {
        Optional<? extends Dynamic<?>> optional = dynamic.get("Level").result();
        if (optional.isPresent() && optional.get().get(SECTIONS).asListOpt(Function.identity()).result().isPresent()) {
            return dynamic.set("Level", new Level(optional.get()).transform());
        }
        return dynamic;
    }

    @Override
    protected TypeRewriteRule makeRule() {
        Type<?> type = this.getInputSchema().getType(TypeReferences.CHUNK);
        Type<?> type2 = this.getOutputSchema().getType(TypeReferences.CHUNK);
        return this.writeFixAndRead(name, type, type2, this::damageChunk);
    }

    static final class Level {
        private final Dynamic<?> level;
        private final List<Section> sections;
        private final ByteBuffer height_map;

        public Level(Dynamic<?> dynamic) {
            level = dynamic;
            sections = dynamic.get(SECTIONS).asList(Section::new);
            height_map = dynamic.get("height_map").asByteBuffer();
        }

        public Dynamic<?> transform() {
            Dynamic<?> self = this.level;

            class_257 blockLight = new class_257(Arrays.copyOf(DEFAULT_BLOCK_LIGHT, CHUNK_SIZE >> 1));
            byte[] blocks = new byte[CHUNK_SIZE];
            class_257 data = new class_257(CHUNK_SIZE);
            class_257 skyLight = new class_257(Arrays.copyOf(DEFAULT_SKY_LIGHT, CHUNK_SIZE >> 1));
            for (Section section : sections) {
                int yOff = section.y << 4;
                for (int i = 0; i < 4096; i++) {
                    int x = i >> 8;
                    int y = i >> 4 & 0b1111;
                    int z = i & 0b1111;
                    int worldY = yOff + y;
                    blockLight.method_1704(x, worldY, z, section.block_light.getValue(i));
                    blocks[x << 11 | z << 7 | worldY] = (byte) StationFlatteningItemStackSchema.lookupOldBlockId(section.palette.get(section.statesData.get((y << 4 | z) << 4 | x)));
                    data.method_1704(x, worldY, z, section.data.getValue(i));
                    skyLight.method_1704(x, worldY, z, section.sky_light.getValue(i));
                }
            }

            byte[] heightMap = new byte[256];
            for (int i = 0; i < heightMap.length; i++) heightMap[i] = this.height_map.get(i << 1);

            return self
                    .set("BlockLight", self.createByteList(ByteBuffer.wrap(blockLight.field_2103)))
                    .set("Blocks", self.createByteList(ByteBuffer.wrap(blocks)))
                    .set("Data", self.createByteList(ByteBuffer.wrap(data.field_2103)))
                    .set("HeightMap", self.createByteList(ByteBuffer.wrap(heightMap)))
                    .set("SkyLight", self.createByteList(ByteBuffer.wrap(skyLight.field_2103)))
                    .remove(SECTIONS)
                    .remove("height_map");
        }
    }

    static final class Section {

        private final Dynamic<?> section;
        private final int y;
        private final NibbleArray block_light = new NibbleArray(4096);
        private final List<? extends Dynamic<?>> palette;
        private final PackedIntegerArray statesData;
        private final NibbleArray data = new NibbleArray(4096);
        private final NibbleArray sky_light = new NibbleArray(4096);

        public Section(Dynamic<?> section) {
            this.section = section;
            y = section.get("y").asInt(0);
            block_light.copyArray(DataFixUtils.toArray(section.get("block_light").asByteBuffer()));
            Map<String, ? extends Dynamic<?>> blockStates = section.get("block_states").asMap(dynamic -> dynamic.asString(""), Function.identity());
            palette = blockStates.get("palette").asList(Function.identity());
            long[] states = blockStates.containsKey("data") ? blockStates.get("data").asLongStream().toArray() : null;
            int elementBits = Math.max(4, MathHelper.ceilLog2(palette.size()));
            int elementsPerLong = 64 / elementBits;
            statesData = new PackedIntegerArray(elementBits, states == null ? 4096 : states.length * elementsPerLong, states);
            data.copyArray(DataFixUtils.toArray(section.get("data").asByteBuffer()));
            sky_light.copyArray(DataFixUtils.toArray(section.get("sky_light").asByteBuffer()));
        }
    }
}
