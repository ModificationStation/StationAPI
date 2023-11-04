package net.modificationstation.stationapi.api.client.render.model.json;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.client.render.model.*;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.client.texture.SpriteIdentifier;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.state.StateManager;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Environment(EnvType.CLIENT)
public class MultipartUnbakedModel implements UnbakedModel {
    private final StateManager<Block, BlockState> stateFactory;
    private final List<MultipartModelComponent> components;

    public MultipartUnbakedModel(StateManager<Block, BlockState> stateFactory, List<MultipartModelComponent> components) {
        this.stateFactory = stateFactory;
        this.components = components;
    }

    public List<MultipartModelComponent> getComponents() {
        return this.components;
    }

    public Set<WeightedUnbakedModel> getModels() {
        Set<WeightedUnbakedModel> set = Sets.newHashSet();

        for (MultipartModelComponent multipartModelComponent : this.components) {
            set.add(multipartModelComponent.getModel());
        }

        return set;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof MultipartUnbakedModel multipartUnbakedModel)) {
            return false;
        } else {
            return Objects.equals(this.stateFactory, multipartUnbakedModel.stateFactory) && Objects.equals(this.components, multipartUnbakedModel.components);
        }
    }

    public int hashCode() {
        return Objects.hash(this.stateFactory, this.components);
    }

    public Collection<Identifier> getModelDependencies() {
        return this.getComponents().stream().flatMap((multipartModelComponent) -> multipartModelComponent.getModel().getModelDependencies().stream()).collect(Collectors.toSet());
    }

    @Override
    public void setParents(Function<Identifier, UnbakedModel> modelLoader) {
        this.getComponents().forEach(component -> component.getModel().setParents(modelLoader));
    }

    @Nullable
    public BakedModel bake(Baker baker, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
        MultipartBakedModel.Builder builder = new MultipartBakedModel.Builder();

        for (MultipartModelComponent multipartModelComponent : this.getComponents()) {
            BakedModel bakedModel = multipartModelComponent.getModel().bake(baker, textureGetter, rotationContainer, modelId);
            if (bakedModel != null) {
                builder.addComponent(multipartModelComponent.getPredicate(this.stateFactory), bakedModel);
            }
        }

        return builder.build();
    }

    @Environment(EnvType.CLIENT)
    public static class Deserializer implements JsonDeserializer<MultipartUnbakedModel> {
        private final ModelVariantMap.DeserializationContext context;

        public Deserializer(ModelVariantMap.DeserializationContext context) {
            this.context = context;
        }

        public MultipartUnbakedModel deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return new MultipartUnbakedModel(this.context.getStateFactory(), this.deserializeComponents(jsonDeserializationContext, jsonElement.getAsJsonArray()));
        }

        private List<MultipartModelComponent> deserializeComponents(JsonDeserializationContext context, JsonArray array) {
            List<MultipartModelComponent> list = Lists.newArrayList();

            for (JsonElement jsonElement : array) {
                list.add(context.deserialize(jsonElement, MultipartModelComponent.class));
            }

            return list;
        }
    }
}
