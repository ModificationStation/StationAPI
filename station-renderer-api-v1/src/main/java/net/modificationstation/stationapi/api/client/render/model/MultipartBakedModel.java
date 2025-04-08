package net.modificationstation.stationapi.api.client.render.model;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenCustomHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.client.render.mesh.QuadEmitter;
import net.modificationstation.stationapi.api.client.render.model.json.ModelOverrideList;
import net.modificationstation.stationapi.api.client.render.model.json.ModelTransformation;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.util.Util;
import org.apache.commons.lang3.tuple.Pair;

import java.util.BitSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Predicate;

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

    @Override
    public void emitBlockQuads(BlockInputContext input, QuadEmitter output) {
        BlockState state = input.blockState();
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

        Random random = input.random();
        long seed = random.nextLong();
        random.setSeed(seed);

        for(int i = 0; i < bitSet.length(); ++i) {
            if (bitSet.get(i)) {
                this.components.get(i).getRight().emitBlockQuads(input, output);
            }
        }
    }

    @Override
    public void emitItemQuads(ItemInputContext input, QuadEmitter output) {}

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
