package net.modificationstation.stationapi.api.client.colour.block;

import com.google.common.collect.ImmutableSet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockBase;
import net.minecraft.block.material.MaterialColour;
import net.minecraft.level.BlockView;
import net.minecraft.util.maths.TilePos;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.client.event.colour.block.BlockColorsRegisterEvent;
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
    private final Map<BlockBase, Set<Property<?>>> properties = new HashMap<>();

    public static BlockColors create() {
        BlockColors blockColors = new BlockColors();
        StationAPI.EVENT_BUS.post(BlockColorsRegisterEvent.builder().blockColors(blockColors).build());
        return blockColors;
    }

    public int getColor(BlockState state, BlockView world, TilePos pos) {
        BlockColorProvider blockColorProvider = this.providers.get(BlockRegistry.INSTANCE.getRawId(state.getBlock()));
        if (blockColorProvider != null) {
            return blockColorProvider.getColor(state, null, null, 0);
        } else {
            MaterialColour materialColor = state.getTopMaterialColor(world, pos);
            return materialColor != null ? materialColor.colour : -1;
        }
    }

    public int getColor(BlockState state, @Nullable BlockView world, @Nullable TilePos pos, int tint) {
        BlockColorProvider blockColorProvider = this.providers.get(BlockRegistry.INSTANCE.getRawId(state.getBlock()));
        return blockColorProvider == null ? -1 : blockColorProvider.getColor(state, world, pos, tint);
    }

    public void registerColorProvider(BlockColorProvider provider, BlockBase... blocks) {

        for (BlockBase block : blocks) {
            this.providers.set(provider, BlockRegistry.INSTANCE.getRawId(block));
        }

    }

    public void registerColorProperties(Set<Property<?>> properties, BlockBase... blocks) {

        for (BlockBase block : blocks) {
            this.properties.put(block, properties);
        }

    }

    public void registerColourProperty(Property<?> property, BlockBase... blocks) {
        this.registerColorProperties(ImmutableSet.of(property), blocks);
    }

    public Set<Property<?>> getProperties(BlockBase block) {
        return this.properties.getOrDefault(block, ImmutableSet.of());
    }
}
