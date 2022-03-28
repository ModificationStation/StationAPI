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
import net.modificationstation.stationapi.api.client.event.colour.block.BlockColoursRegisterEvent;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.state.property.Property;
import net.modificationstation.stationapi.api.util.collection.IdList;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Environment(EnvType.CLIENT)
public class BlockColours {
    private final IdList<BlockColourProvider> providers = new IdList<>(32);
    private final Map<BlockBase, Set<Property<?>>> properties = new HashMap<>();

    public static BlockColours create() {
        BlockColours blockColours = new BlockColours();
        StationAPI.EVENT_BUS.post(BlockColoursRegisterEvent.maker().blockColours(blockColours).make());
        return blockColours;
    }

    public int getColour(BlockState state, BlockView world, TilePos pos) {
        BlockColourProvider blockColorProvider = this.providers.get(BlockRegistry.INSTANCE.getRawId(state.getBlock()));
        if (blockColorProvider != null) {
            return blockColorProvider.getColour(state, null, null, 0);
        } else {
            MaterialColour materialColor = state.getTopMaterialColor(world, pos);
            return materialColor != null ? materialColor.colour : -1;
        }
    }

    public int getColour(BlockState state, @Nullable BlockView world, @Nullable TilePos pos, int tint) {
        BlockColourProvider blockColourProvider = this.providers.get(BlockRegistry.INSTANCE.getRawId(state.getBlock()));
        return blockColourProvider == null ? -1 : blockColourProvider.getColour(state, world, pos, tint);
    }

    public void registerColourProvider(BlockColourProvider provider, BlockBase... blocks) {

        for (BlockBase block : blocks) {
            this.providers.set(provider, BlockRegistry.INSTANCE.getRawId(block));
        }

    }

    public void registerColourProperties(Set<Property<?>> properties, BlockBase... blocks) {

        for (BlockBase block : blocks) {
            this.properties.put(block, properties);
        }

    }

    public void registerColourProperty(Property<?> property, BlockBase... blocks) {
        this.registerColourProperties(ImmutableSet.of(property), blocks);
    }

    public Set<Property<?>> getProperties(BlockBase block) {
        return this.properties.getOrDefault(block, ImmutableSet.of());
    }
}
