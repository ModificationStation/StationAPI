package net.modificationstation.stationapi.api.client.render.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenCustomHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.level.BlockView;
import net.minecraft.util.maths.TilePos;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.client.render.model.json.ModelOverrideList;
import net.modificationstation.stationapi.api.client.render.model.json.ModelTransformation;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.level.BlockStateView;
import net.modificationstation.stationapi.api.util.Util;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.*;

@Environment(EnvType.CLIENT)
public class MultipartBakedModel implements BakedModel {
   private final List<Pair<Predicate<BlockState>, BakedModel>> components;
   protected final boolean ambientOcclusion;
   protected final boolean depthGui;
   protected final boolean sideLit;
   protected final Sprite sprite;
   protected final ModelTransformation transformations;
   protected final ModelOverrideList itemPropertyOverrides;
   private final Map<BlockState, BitSet> stateCache = new Object2ObjectOpenCustomHashMap<>(Util.identityHashStrategy());

   public MultipartBakedModel(List<Pair<Predicate<BlockState>, BakedModel>> components) {
      this.components = components;
      BakedModel bakedModel = components.iterator().next().getRight();
      this.ambientOcclusion = bakedModel.useAmbientOcclusion();
      this.depthGui = bakedModel.hasDepth();
      this.sideLit = bakedModel.isSideLit();
      this.sprite = bakedModel.getSprite();
      this.transformations = bakedModel.getTransformation();
      this.itemPropertyOverrides = bakedModel.getOverrides();
   }

   public ImmutableList<BakedQuad> getQuads(@Nullable BlockView blockView, @Nullable TilePos pos, @Nullable Direction face, Random random) {
      if (blockView == null || pos == null) {
         return ImmutableList.of();
      } else {
         BlockState state = ((BlockStateView) blockView).getBlockState(pos.x, pos.y, pos.z);
         BitSet bitSet = this.stateCache.get(state);
         if (bitSet == null) {
            bitSet = new BitSet();

            for(int i = 0; i < this.components.size(); ++i) {
               Pair<Predicate<BlockState>, BakedModel> pair = this.components.get(i);
               if (pair.getLeft().test(state)) {
                  bitSet.set(i);
               }
            }

            this.stateCache.put(state, bitSet);
         }

         ImmutableList.Builder<BakedQuad> list = ImmutableList.builder();
         long l = random.nextLong();

         for(int j = 0; j < bitSet.length(); ++j) {
            if (bitSet.get(j)) {
               list.addAll((this.components.get(j)).getRight().getQuads(blockView, pos, face, new Random(l)));
            }
         }

         return list.build();
      }
   }

   public boolean useAmbientOcclusion() {
      return this.ambientOcclusion;
   }

   public boolean hasDepth() {
      return this.depthGui;
   }

   public boolean isSideLit() {
      return this.sideLit;
   }

   public boolean isBuiltin() {
      return false;
   }

   public Sprite getSprite() {
      return this.sprite;
   }

   public ModelTransformation getTransformation() {
      return this.transformations;
   }

   public ModelOverrideList getOverrides() {
      return this.itemPropertyOverrides;
   }

   @Environment(EnvType.CLIENT)
   public static class Builder {
      private final List<Pair<Predicate<BlockState>, BakedModel>> components = Lists.newArrayList();

      public void addComponent(Predicate<BlockState> predicate, BakedModel model) {
         this.components.add(Pair.of(predicate, model));
      }

      public BakedModel build() {
         return new MultipartBakedModel(this.components);
      }
   }
}
