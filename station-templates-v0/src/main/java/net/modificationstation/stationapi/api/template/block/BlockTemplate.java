package net.modificationstation.stationapi.api.template.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvironmentInterface;
import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.BlockToolLogic;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.BlockStateHolder;
import net.modificationstation.stationapi.api.block.StateManager;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.CustomAtlasProvider;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ModID;
import uk.co.benjiweber.expressions.tuple.BiTuple;

import java.util.*;

@EnvironmentInterface(value = EnvType.CLIENT, itf = CustomAtlasProvider.class)
public interface BlockTemplate<T extends BlockBase> extends CustomAtlasProvider, BlockToolLogic, BlockStateHolder {

    @Override
    default T mineableBy(Identifier toolTag, int level) {
        throw new AssertionError("This method was never supposed to be called, as it should have been overriden by a mixin. Something is very broken!");
    }

    @Override
    default List<BiTuple<Identifier, Integer>> getToolTagEffectiveness() {
        throw new AssertionError("This method was never supposed to be called, as it should have been overriden by a mixin. Something is very broken!");
    }

    default T setTranslationKey(ModID modID, String translationKey) {
        //noinspection unchecked
        return (T) ((BlockBase) this).setTranslationKey(Identifier.of(modID, translationKey).toString());
    }

    @Override
    @Environment(EnvType.CLIENT)
    default Atlas getAtlas() {
        throw new AssertionError("This method was never supposed to be called, as it should have been overriden by a mixin. Something is very broken!");
    }

    @Override
    default StateManager<BlockBase, BlockState> getStateManager() {
        throw new AssertionError("This method was never supposed to be called, as it should have been overriden by a mixin. Something is very broken!");
    }

    @Override
    default BlockState getDefaultState() {
        throw new AssertionError("This method was never supposed to be called, as it should have been overriden by a mixin. Something is very broken!");
    }

    @Override
    default void appendProperties(StateManager.Builder<BlockBase, BlockState> builder) {
        throw new AssertionError("This method was never supposed to be called, as it should have been overriden by a mixin. Something is very broken!");
    }

    @Override
    default void setDefaultState(BlockState state) {
        throw new AssertionError("This method was never supposed to be called, as it should have been overriden by a mixin. Something is very broken!");
    }
}
