package net.modificationstation.stationapi.api.client.colour.block;

import com.google.common.collect.ImmutableSet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockBase;
import net.minecraft.block.material.MaterialColour;
import net.minecraft.client.render.block.FoliageColour;
import net.minecraft.client.render.block.GrassColour;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.minecraft.util.maths.TilePos;
import net.modificationstation.stationapi.api.client.colour.world.BiomeColours;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.util.collection.IdList;
import net.modificationstation.stationapi.impl.block.BlockState;
import net.modificationstation.stationapi.impl.block.Property;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static net.modificationstation.stationapi.impl.block.Properties.META;

@Environment(EnvType.CLIENT)
public class BlockColours {
    private final IdList<BlockColorProvider> providers = new IdList<>(32);
    private final Map<BlockBase, Set<Property<?>>> properties = new HashMap<>();

    public static BlockColours create() {
        BlockColours blockColours = new BlockColours();
        blockColours.registerColourProvider((state, world, pos, tintIndex) -> world == null || pos == null ? GrassColour.get(0.5, 1.0) : BiomeColours.getGrassColour(world, pos), BlockBase.GRASS, BlockBase.TALLGRASS);
        blockColours.registerColourProvider((state, world, pos, tintIndex) -> {
            if (world == null || pos == null)
                return FoliageColour.method_1083();
            else {
                int meta = state.get(META);
                return (meta & 1) == 1 ? FoliageColour.method_1079() : (meta & 2) == 2 ? FoliageColour.method_1082() : BiomeColours.getFoliageColour(world, pos);
            }
        }, BlockBase.LEAVES);
        blockColours.registerColourProperty(META, BlockBase.LEAVES);
        blockColours.registerColourProvider(
                (state, world, pos, tintIndex) -> world == null || pos == null ? -1 : BiomeColours.getWaterColour(world, pos),
                BlockBase.FLOWING_WATER, BlockBase.STILL_WATER
        );
        return blockColours;
    }

    public int getColour(BlockState state, Level world, TilePos pos) {
        BlockColorProvider blockColorProvider = this.providers.get(BlockRegistry.INSTANCE.getRawId(state.getBlock()));
        if (blockColorProvider != null) {
            return blockColorProvider.getColor(state, null, null, 0);
        } else {
            MaterialColour materialColor = state.getTopMaterialColor(world, pos);
            return materialColor != null ? materialColor.colour : -1;
        }
    }

    public int getColour(BlockState state, @Nullable BlockView world, @Nullable TilePos pos, int tint) {
        BlockColorProvider blockColorProvider = this.providers.get(BlockRegistry.INSTANCE.getRawId(state.getBlock()));
        return blockColorProvider == null ? -1 : blockColorProvider.getColor(state, world, pos, tint);
    }

    public void registerColourProvider(BlockColorProvider provider, BlockBase... blocks) {

        for (BlockBase block : blocks) {
            this.providers.set(provider, BlockRegistry.INSTANCE.getRawId(block));
        }

    }

    private void registerColourProperties(Set<Property<?>> properties, BlockBase... blocks) {

        for (BlockBase block : blocks) {
            this.properties.put(block, properties);
        }

    }

    private void registerColourProperty(Property<?> property, BlockBase... blocks) {
        this.registerColourProperties(ImmutableSet.of(property), blocks);
    }

    public Set<Property<?>> getProperties(BlockBase block) {
        return this.properties.getOrDefault(block, ImmutableSet.of());
    }
}
