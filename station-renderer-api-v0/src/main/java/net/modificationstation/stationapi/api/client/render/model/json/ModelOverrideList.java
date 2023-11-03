package net.modificationstation.stationapi.api.client.render.model.json;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.client.model.block.BlockModelPredicateProvider;
import net.modificationstation.stationapi.api.client.model.item.ItemModelPredicateProvider;
import net.modificationstation.stationapi.api.client.registry.BlockModelPredicateProviderRegistry;
import net.modificationstation.stationapi.api.client.registry.ItemModelPredicateProviderRegistry;
import net.modificationstation.stationapi.api.client.render.model.BakedModel;
import net.modificationstation.stationapi.api.client.render.model.Baker;
import net.modificationstation.stationapi.api.client.render.model.ModelBakeRotation;
import net.modificationstation.stationapi.api.client.render.model.UnbakedModel;
import net.modificationstation.stationapi.api.registry.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class ModelOverrideList {
    public static final ModelOverrideList EMPTY = new ModelOverrideList();
    private final BakedOverride[] overrides;
    private final Identifier[] conditionTypes;

    private ModelOverrideList() {
        this.overrides = new BakedOverride[0];
        this.conditionTypes = new Identifier[0];
    }

    public ModelOverrideList(Baker baker, JsonUnbakedModel parent, Function<Identifier, UnbakedModel> unbakedModelGetter, List<ModelOverride> overrides) {
        this.conditionTypes = overrides.stream().flatMap(ModelOverride::streamConditions).map(ModelOverride.Condition::getType).distinct().toArray(Identifier[]::new);
        Object2IntOpenHashMap<Identifier> object2IntMap = new Object2IntOpenHashMap<>();
        for (int i = 0; i < this.conditionTypes.length; ++i) {
            object2IntMap.put(this.conditionTypes[i], i);
        }
        ArrayList<BakedOverride> list = Lists.newArrayList();
        for (int j = overrides.size() - 1; j >= 0; --j) {
            ModelOverride modelOverride = overrides.get(j);
            BakedModel bakedModel = this.bakeOverridingModel(baker, parent, unbakedModelGetter, modelOverride);
            InlinedCondition[] inlinedConditions = modelOverride.streamConditions().map(condition -> {
                int i = object2IntMap.getInt(condition.getType());
                return new InlinedCondition(i, condition.getThreshold());
            }).toArray(InlinedCondition[]::new);
            list.add(new BakedOverride(inlinedConditions, bakedModel));
        }
        this.overrides = list.toArray(new BakedOverride[0]);
    }

    @Nullable
    private BakedModel bakeOverridingModel(Baker baker, JsonUnbakedModel parent, Function<Identifier, UnbakedModel> unbakedModelGetter, ModelOverride override) {
        UnbakedModel unbakedModel = unbakedModelGetter.apply(override.getModelId());
        if (Objects.equals(unbakedModel, parent)) {
            return null;
        }
        return baker.bake(override.getModelId(), ModelBakeRotation.X0_Y0);
    }

    @Nullable
    public BakedModel apply(BakedModel model, BlockState state, @Nullable BlockView world, @Nullable BlockPos pos, int seed) {
        if (this.overrides.length != 0) {
            Block block = state.getBlock();
            int i = this.conditionTypes.length;
            float[] fs = new float[i];
            for (int j = 0; j < i; ++j) {
                Identifier identifier = this.conditionTypes[j];
                BlockModelPredicateProvider modelPredicateProvider = BlockModelPredicateProviderRegistry.INSTANCE.get(block, identifier);
                fs[j] = modelPredicateProvider != null ? modelPredicateProvider.call(state, world, pos, seed) : Float.NEGATIVE_INFINITY;
            }
            for (BakedOverride bakedOverride : this.overrides) {
                if (!bakedOverride.test(fs)) continue;
                BakedModel bakedModel = bakedOverride.model;
                if (bakedModel == null) {
                    return model;
                }
                return bakedModel;
            }
        }
        return model;
    }

    @Nullable
    public BakedModel apply(BakedModel model, ItemStack stack, @Nullable BlockView world, @Nullable LivingEntity entity, int seed) {
        if (this.overrides.length != 0) {
            Item item = stack.getItem();
            int i = this.conditionTypes.length;
            float[] fs = new float[i];
            for (int j = 0; j < i; ++j) {
                Identifier identifier = this.conditionTypes[j];
                ItemModelPredicateProvider modelPredicateProvider = ItemModelPredicateProviderRegistry.INSTANCE.get(item, identifier);
                fs[j] = modelPredicateProvider != null ? modelPredicateProvider.call(stack, world, entity, seed) : Float.NEGATIVE_INFINITY;
            }
            for (BakedOverride bakedOverride : this.overrides) {
                if (!bakedOverride.test(fs)) continue;
                BakedModel bakedModel = bakedOverride.model;
                if (bakedModel == null) {
                    return model;
                }
                return bakedModel;
            }
        }
        return model;
    }

    @SuppressWarnings("ClassCanBeRecord")
    @Environment(EnvType.CLIENT)
    static class BakedOverride {
        private final InlinedCondition[] conditions;
        @Nullable
        final BakedModel model;

        BakedOverride(InlinedCondition[] conditions, @Nullable BakedModel model) {
            this.conditions = conditions;
            this.model = model;
        }

        boolean test(float[] values) {
            for (InlinedCondition inlinedCondition : this.conditions) {
                float f = values[inlinedCondition.index];
                if (!(f < inlinedCondition.threshold)) continue;
                return false;
            }
            return true;
        }
    }

    @Environment(EnvType.CLIENT)
    record InlinedCondition(int index, float threshold) { }
}
