package net.modificationstation.stationapi.api.client.render.model;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.client.render.item.ItemModels;
import net.modificationstation.stationapi.api.client.render.model.json.ModelTransformation;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public interface BakedModelRenderer {

    boolean renderBlock(BlockState state, BlockPos pos, BlockView world, boolean cull, Random random);

    boolean render(BlockView world, BakedModel model, BlockState state, BlockPos pos, boolean cull, Random random, long seed);

    void renderDamage(BlockState state, BlockPos pos, BlockView world, float progress);

    ItemModels getItemModels();

    void renderItem(ItemStack stack, ModelTransformation.Mode renderMode, float brightness, BakedModel model);

    BakedModel getModel(ItemStack stack, @Nullable World world, @Nullable LivingEntity entity, int seed);

    void renderItem(@Nullable LivingEntity entity, ItemStack item, ModelTransformation.Mode renderMode, @Nullable World world, float brightness, int seed);

    void renderInGuiWithOverrides(ItemStack stack, int x, int y);
}
