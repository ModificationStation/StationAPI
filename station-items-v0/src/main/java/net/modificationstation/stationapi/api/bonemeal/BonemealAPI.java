package net.modificationstation.stationapi.api.bonemeal;

import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.block.BlockBase;
import net.minecraft.level.Level;
import net.minecraft.level.structure.Structure;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.tag.TagKey;
import net.modificationstation.stationapi.api.util.collection.WeightedList;
import net.modificationstation.stationapi.api.util.math.Direction;

import java.util.Map;
import java.util.Random;

public class BonemealAPI {
    private static final Map<TagKey<BlockBase>, WeightedList<Structure>> PLACERS_TAG = new Reference2ObjectOpenHashMap<>();
    private static final Map<BlockState, WeightedList<Structure>> PLACERS_BLOCK = new Reference2ObjectOpenHashMap<>();
    private static final WeightedList<Structure> CACHE = new WeightedList<>();

    public static void addPlant(BlockState ground, BlockState plant, int weight) {
        addPlant(ground, new SimpleStateStructure(plant), weight);
    }

    public static void addPlant(BlockState ground, Structure plant, int weight) {
        PLACERS_BLOCK.computeIfAbsent(ground, g -> new WeightedList<>()).add(plant, weight);
    }

    public static void addPlant(TagKey<BlockBase> ground, BlockState plant, int weight) {
        addPlant(ground, new SimpleStateStructure(plant), weight);
    }

    public static void addPlant(TagKey<BlockBase> ground, Structure plant, int weight) {
        PLACERS_TAG.computeIfAbsent(ground, g -> new WeightedList<>()).add(plant, weight);
    }

    public static boolean generate(Level level, int x, int y, int z, BlockState state, int side) {
        updateCache(state);
        if (CACHE.isEmpty()) return false;
        Random random = level.rand;
        Direction offset = Direction.byId(side);
        CACHE.get(random).generate(
                level,
                random,
                x + offset.getOffsetX(),
                y + offset.getOffsetY(),
                z + offset.getOffsetZ()
        );
        for (byte i = 0; i < 127; i++) {
            int px = x + random.nextInt(7) - 3;
            int py = y + random.nextInt(5) - 2;
            int pz = z + random.nextInt(7) - 3;
            state = level.getBlockState(px, py, pz);
            updateCache(state);
            if (CACHE.isEmpty()) continue;
            CACHE.get(random).generate(level, random, px, py + 1, pz);
        }
        return true;
    }

    private static void updateCache(BlockState state) {
        CACHE.clear();
        WeightedList<Structure> structures = PLACERS_BLOCK.get(state);
        if (structures != null) CACHE.addAll(structures);
        state.streamTags().forEach(tag -> {
            WeightedList<Structure> tagStructures = PLACERS_TAG.get(tag);
            if (tagStructures != null) CACHE.addAll(tagStructures);
        });
    }

    private static class SimpleStateStructure extends Structure {
        private final BlockState state;

        private SimpleStateStructure(BlockState state) {
            this.state = state;
        }

        @Override
        public boolean generate(Level level, Random random, int x, int y, int z) {
            BlockState levelState = level.getBlockState(x, y, z);
            if (!levelState.isAir() && !levelState.getMaterial().isLiquid()) return false;
            if (state.getBlock().canPlaceAt(level, x, y, z)) {
                level.setBlockState(x, y, z, state);
                return true;
            }
            return false;
        }
    }

    private static class GrassStructure extends Structure {
        private static final BlockState STATE = BlockBase.TALLGRASS.getDefaultState();

        @Override
        public boolean generate(Level level, Random random, int x, int y, int z) {
            BlockState levelState = level.getBlockState(x, y, z);
            if (!levelState.isAir() && !levelState.getMaterial().isLiquid()) return false;
            if (STATE.getBlock().canPlaceAt(level, x, y, z)) {
                level.setBlockState(x, y, z, STATE);
                level.setTileMeta(x, y, z, 1);
                return true;
            }
            return false;
        }
    }

    static {
        BlockState grass = BlockBase.GRASS.getDefaultState();
        addPlant(grass, new GrassStructure(), 10);
        addPlant(grass, BlockBase.DANDELION.getDefaultState(), 1);
        addPlant(grass, BlockBase.ROSE.getDefaultState(), 1);
    }
}
