package net.modificationstation.stationapi.api.client.color.block;

import com.google.common.collect.ImmutableSet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.class_259;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.client.event.color.block.BlockColorsRegisterEvent;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.state.property.Property;
import net.modificationstation.stationapi.api.util.collection.IdList;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Environment(EnvType.CLIENT)
public class BlockColors {
    private final IdList<BlockColorProvider> providers = new IdList<>(32);
    private final Map<Block, Set<Property<?>>> properties = new HashMap<>();

    public static BlockColors create() {
        BlockColors blockColors = new BlockColors();
        StationAPI.EVENT_BUS.post(BlockColorsRegisterEvent.builder().blockColors(blockColors).build());
        return blockColors;
    }

    public int getColor(BlockState state, BlockView world, BlockPos pos) {
        BlockColorProvider blockColorProvider = this.providers.get(BlockRegistry.INSTANCE.getRawId(state.getBlock()));
        if (blockColorProvider != null) {
            return blockColorProvider.getColor(state, null, null, 0);
        } else {
            class_259 materialColor = state.getTopMaterialColor(world, pos);
            return materialColor != null ? materialColor.field_2752 : -1;
        }
    }

    public int getColor(BlockState state, @Nullable BlockView world, @Nullable BlockPos pos, int tint) {
        BlockColorProvider blockColorProvider = this.providers.get(BlockRegistry.INSTANCE.getRawId(state.getBlock()));
        return blockColorProvider == null ? -1 : blockColorProvider.getColor(state, world, pos, tint);
    }

    public void registerColorProvider(BlockColorProvider provider, Block... blocks) {

        for (Block block : blocks) {
            this.providers.set(provider, BlockRegistry.INSTANCE.getRawId(block));
        }

    }

    public void registerColorProperties(Set<Property<?>> properties, Block... blocks) {

        for (Block block : blocks) {
            this.properties.put(block, properties);
        }

    }

    public void registerColourProperty(Property<?> property, Block... blocks) {
        this.registerColorProperties(ImmutableSet.of(property), blocks);
    }

    public Set<Property<?>> getProperties(Block block) {
        return this.properties.getOrDefault(block, ImmutableSet.of());
    }
}
