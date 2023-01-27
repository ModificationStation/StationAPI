package net.modificationstation.stationapi.api.client.render.model;

import net.minecraft.entity.Living;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.minecraft.util.maths.TilePos;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.client.render.item.ItemModels;
import net.modificationstation.stationapi.api.client.render.model.json.ModelTransformation;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public interface BakedModelRenderer {

    boolean renderBlock(BlockState state, TilePos pos, BlockView world, boolean cull, Random random);

    boolean render(BlockView world, BakedModel model, BlockState state, TilePos pos, boolean cull, Random random, long seed);

    void renderDamage(BlockState state, TilePos pos, BlockView world, float progress);

    ItemModels getItemModels();

    void renderItem(ItemInstance stack, ModelTransformation.Mode renderMode, float brightness, BakedModel model);

    BakedModel getModel(ItemInstance stack, @Nullable Level world, @Nullable Living entity, int seed);

    void renderItem(@Nullable Living entity, ItemInstance item, ModelTransformation.Mode renderMode, @Nullable Level world, float brightness, int seed);

    void renderInGuiWithOverrides(ItemInstance stack, int x, int y);
}
