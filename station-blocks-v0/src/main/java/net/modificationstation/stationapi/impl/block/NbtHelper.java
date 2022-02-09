package net.modificationstation.stationapi.impl.block;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.BlockBase;
import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.BlockStateHolder;
import net.modificationstation.stationapi.api.block.Property;
import net.modificationstation.stationapi.api.block.State;
import net.modificationstation.stationapi.api.block.StateManager;
import net.modificationstation.stationapi.api.block.States;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.mixin.block.CompoundTagAccessor;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;

public class NbtHelper {

    public static BlockState toBlockState(CompoundTag tag) {
        if (tag.containsKey("Name")) {
            @NotNull Optional<BlockBase> block = BlockRegistry.INSTANCE.get(Identifier.of(tag.getString("Name")));
            if (block.isPresent()) {
                BlockStateHolder stateHolder = (BlockStateHolder) block.get();
                BlockState blockState = stateHolder.getDefaultState();
                if (tag.containsKey("Properties")) {
                    CompoundTag compoundTag = tag.getCompoundTag("Properties");
                    StateManager<BlockBase, BlockState> stateManager = stateHolder.getStateManager();

                    for (String string : ((CompoundTagAccessor) compoundTag).stationapi$getData().keySet()) {
                        Property<?> property = stateManager.getProperty(string);
                        if (property != null) {
                            blockState = withProperty(blockState, property, string, compoundTag, tag);
                        }
                    }
                }

                return blockState;
            }
        }
        return States.AIR.get();
    }

    private static <S extends State<?, S>, T extends Comparable<T>> S withProperty(S state, Property<T> property, String key, CompoundTag propertiesTag, CompoundTag mainTag) {
        Optional<T> optional = property.parse(propertiesTag.getString(key));
        if (optional.isPresent()) {
            return state.with(property, optional.get());
        } else {
            LOGGER.warn("Unable to read property: {} with value: {} for blockstate: {}", key, propertiesTag.getString(key), mainTag.toString());
            return state;
        }
    }

    public static CompoundTag fromBlockState(BlockState state) {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.put("Name", BlockRegistry.INSTANCE.getIdentifier(state.getBlock()).toString());
        ImmutableMap<Property<?>, Comparable<?>> immutableMap = state.getEntries();
        if (!immutableMap.isEmpty()) {
            CompoundTag compoundTag2 = new CompoundTag();

            for (Map.Entry<Property<?>, Comparable<?>> propertyComparableEntry : immutableMap.entrySet()) {
                Property<?> property = propertyComparableEntry.getKey();
                compoundTag2.put(property.getName(), nameValue(property, propertyComparableEntry.getValue()));
            }

            compoundTag.put("Properties", compoundTag2);
        }

        return compoundTag;
    }

    private static <T extends Comparable<T>> String nameValue(Property<T> property, Comparable<?> value) {
        //noinspection unchecked
        return property.name((T) value);
    }
}
