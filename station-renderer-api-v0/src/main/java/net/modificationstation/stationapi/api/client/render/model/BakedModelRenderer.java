package net.modificationstation.stationapi.api.client.render.model;

import net.minecraft.entity.Living;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.minecraft.util.maths.TilePos;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.client.render.VertexConsumer;
import net.modificationstation.stationapi.api.client.render.item.ItemModels;
import net.modificationstation.stationapi.api.client.render.model.json.ModelTransformation;
import net.modificationstation.stationapi.api.util.math.MatrixStack;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public interface BakedModelRenderer {

    boolean renderBlock(BlockState state, TilePos pos, BlockView world, MatrixStack matrices, VertexConsumer vertexConsumer, boolean cull, Random random);

    boolean render(BlockView world, BakedModel model, BlockState state, TilePos pos, MatrixStack matrices, VertexConsumer vertexConsumer, boolean cull, Random random, long seed, int overlay);

    ItemModels getItemModels();

    void renderItem(ItemInstance stack, ModelTransformation.Mode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumer vertexConsumers, int light, int overlay, BakedModel model);

    BakedModel getModel(ItemInstance stack, @Nullable Level world, @Nullable Living entity, int seed);

    void renderItem(ItemInstance stack, ModelTransformation.Mode transformationType, int light, int overlay, MatrixStack matrices, VertexConsumer vertexConsumer, int seed);

    void renderItem(@Nullable Living entity, ItemInstance item, ModelTransformation.Mode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumer vertexConsumer, @Nullable Level world, int light, int overlay, int seed);

    void renderGuiItemIcon(ItemInstance stack, int x, int y);

    void renderInGuiWithOverrides(ItemInstance stack, int x, int y);
}
