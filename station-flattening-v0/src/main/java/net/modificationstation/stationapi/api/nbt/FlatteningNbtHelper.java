package net.modificationstation.stationapi.api.nbt;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.nbt.NbtCompound;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.States;
import net.modificationstation.stationapi.api.registry.*;
import net.modificationstation.stationapi.api.state.State;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.state.property.Property;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.mixin.nbt.CompoundTagAccessor;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;

public class FlatteningNbtHelper {
    /**
     * {@return the block state from the {@code nbt}}
     *
     * <p>This returns the default state for {@link States#AIR}
     * if the block name is not present.
     *
     * @see #fromBlockState(BlockState)
     */
    public static BlockState toBlockState(RegistryEntryLookup<Block> blockLookup, NbtCompound nbt) {
        if (!nbt.contains("Name")) return States.AIR.get();
        Identifier identifier = Identifier.of(nbt.getString("Name"));
        Optional<RegistryEntry.Reference<Block>> optional = blockLookup.getOptional(RegistryKey.of(BlockRegistry.KEY, identifier));
        if (optional.isEmpty()) {
            return States.AIR.get();
        }
        Block block = optional.get().value();
        BlockState blockState = block.getDefaultState();
        if (nbt.contains("Properties")) {
            NbtCompound nbtCompound = nbt.getCompound("Properties");
            StateManager<Block, BlockState> stateManager = block.getStateManager();
            for (String string : ((CompoundTagAccessor) nbtCompound).stationapi$getData().keySet()) {
                Property<?> property = stateManager.getProperty(string);
                if (property == null) continue;
                blockState = withProperty(blockState, property, string, nbtCompound, nbt);
            }
        }
        return blockState;
    }

    private static <S extends State<?, S>, T extends Comparable<T>> S withProperty(S state, Property<T> property, String key, NbtCompound properties, NbtCompound root) {
        Optional<T> optional = property.parse(properties.getString(key));
        if (optional.isPresent()) return state.with(property, optional.get());
        LOGGER.warn("Unable to read property: {} with value: {} for blockstate: {}", key, properties.getString(key), root.toString());
        return state;
    }

    /**
     * {@return the serialized block state}
     *
     * @see #toBlockState(RegistryEntryLookup, NbtCompound)
     */
    public static NbtCompound fromBlockState(BlockState state) {
        NbtCompound nbtCompound = new NbtCompound();
        nbtCompound.putString("Name", Objects.requireNonNull(BlockRegistry.INSTANCE.getId(state.getBlock())).toString());
        ImmutableMap<Property<?>, Comparable<?>> immutableMap = state.getEntries();
        if (!immutableMap.isEmpty()) {
            NbtCompound nbtCompound2 = new NbtCompound();
            for (Map.Entry<Property<?>, Comparable<?>> entry : immutableMap.entrySet()) {
                Property<?> property = entry.getKey();
                nbtCompound2.putString(property.getName(), nameValue(property, entry.getValue()));
            }
            nbtCompound.put("Properties", nbtCompound2);
        }
        return nbtCompound;
    }

    private static <T extends Comparable<T>> String nameValue(Property<T> property, Comparable<?> value) {
        //noinspection unchecked
        return property.name((T) value);
    }
}
