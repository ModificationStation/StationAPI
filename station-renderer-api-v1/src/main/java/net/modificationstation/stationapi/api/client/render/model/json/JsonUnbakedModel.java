package net.modificationstation.stationapi.api.client.render.model.json;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.*;
import com.mojang.datafixers.util.Either;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import it.unimi.dsi.fastutil.objects.ReferenceSet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.client.render.model.*;
import net.modificationstation.stationapi.api.client.texture.MissingSprite;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.client.texture.SpriteIdentifier;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.JsonHelper;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static net.modificationstation.stationapi.impl.client.texture.StationRenderImpl.LOGGER;

public final class JsonUnbakedModel implements UnbakedModel {

    public static final Identifier BUILTIN_GENERATED = Identifier.of("builtin/generated");

    private static final BakedQuadFactory QUAD_FACTORY = new BakedQuadFactory();
    private static final Gson GSON = (new GsonBuilder()).registerTypeAdapter(JsonUnbakedModel.class, new JsonUnbakedModel.Deserializer()).registerTypeAdapter(ModelElement.class, new ModelElement.Deserializer()).registerTypeAdapter(ModelElementFace.class, new ModelElementFace.Deserializer()).registerTypeAdapter(ModelElementTexture.class, new ModelElementTexture.Deserializer()).registerTypeAdapter(Transformation.class, new Transformation.Deserializer()).registerTypeAdapter(ModelTransformation.class, new ModelTransformation.Deserializer()).registerTypeAdapter(ModelOverride.class, new ModelOverride.Deserializer()).create();
    private final List<ModelElement> elements;
    @Nullable
    private final JsonUnbakedModel.GuiLight guiLight;
    private final boolean ambientOcclusion;
    private final ModelTransformation transformations;
    private final List<ModelOverride> overrides;
    public String id = "";
    private final Map<String, Either<SpriteIdentifier, String>> textureMap;
    @Nullable
    private JsonUnbakedModel parent;
    @Nullable
    private Identifier parentId;

    public static JsonUnbakedModel deserialize(Reader input) {
        return JsonHelper.deserialize(GSON, input, JsonUnbakedModel.class);
    }

    public static JsonUnbakedModel deserialize(String json) {
        return deserialize(new StringReader(json));
    }

    public JsonUnbakedModel(
            @Nullable Identifier parentId,
            List<ModelElement> elements,
            Map<String, Either<SpriteIdentifier, String>> textureMap,
            boolean ambientOcclusion,
            @Nullable JsonUnbakedModel.GuiLight guiLight,
            ModelTransformation transformations,
            List<ModelOverride> overrides
    ) {
        this.elements = elements;
        this.ambientOcclusion = ambientOcclusion;
        this.guiLight = guiLight;
        this.textureMap = textureMap;
        this.parentId = parentId;
        this.transformations = transformations;
        this.overrides = overrides;
    }

    public List<ModelElement> getElements() {
        return this.elements.isEmpty() && this.parent != null ? this.parent.getElements() : this.elements;
    }

    public boolean useAmbientOcclusion() {
        return this.parent != null ? this.parent.useAmbientOcclusion() : this.ambientOcclusion;
    }

    public JsonUnbakedModel.GuiLight getGuiLight() {
        if (this.guiLight != null) return this.guiLight;
        else return this.parent != null ? this.parent.getGuiLight() : GuiLight.SIDE;
    }

    public List<ModelOverride> getOverrides() {
        return this.overrides;
    }

    private ModelOverrideList compileOverrides(Baker baker, JsonUnbakedModel parent) {
        return this.overrides.isEmpty() ? ModelOverrideList.EMPTY : new ModelOverrideList(baker, parent, baker::getOrLoadModel, this.overrides);
    }

    public Collection<Identifier> getModelDependencies() {
        ReferenceSet<Identifier> set = new ReferenceOpenHashSet<>();
        for (ModelOverride modelOverride : this.overrides) set.add(modelOverride.getModelId());
        if (this.parentId != null) set.add(this.parentId);
        return set;
    }

    @Override
    public void setParents(Function<Identifier, UnbakedModel> modelLoader) {
        LinkedHashSet<JsonUnbakedModel> set = new LinkedHashSet<>();
        JsonUnbakedModel jsonUnbakedModel = this;
        while (jsonUnbakedModel.parentId != null && jsonUnbakedModel.parent == null) {
            set.add(jsonUnbakedModel);
            UnbakedModel unbakedModel = modelLoader.apply(jsonUnbakedModel.parentId);
            if (unbakedModel == null)
                LOGGER.warn("No parent '{}' while loading model '{}'", this.parentId, jsonUnbakedModel);
            if (set.contains(unbakedModel)) {
                LOGGER.warn("Found 'parent' loop while loading model '{}' in chain: {} -> {}", jsonUnbakedModel, set.stream().map(Object::toString).collect(Collectors.joining(" -> ")), this.parentId);
                unbakedModel = null;
            }
            if (unbakedModel == null) {
                jsonUnbakedModel.parentId = ModelLoader.MISSING_ID.asIdentifier();
                unbakedModel = modelLoader.apply(jsonUnbakedModel.parentId);
            }
            if (!(unbakedModel instanceof JsonUnbakedModel))
                throw new IllegalStateException("BlockModel parent has to be a block model.");
            jsonUnbakedModel.parent = (JsonUnbakedModel)unbakedModel;
            jsonUnbakedModel = jsonUnbakedModel.parent;
        }
        this.overrides.forEach(override -> {
            UnbakedModel unbakedModel = modelLoader.apply(override.getModelId());
            if (Objects.equals(unbakedModel, this)) return;
            unbakedModel.setParents(modelLoader);
        });
    }

    @Override
    public BakedModel bake(Baker baker, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
        return this.bake(baker, this, textureGetter, rotationContainer, modelId, true);
    }

    public BakedModel bake(Baker baker, JsonUnbakedModel parent, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings settings, Identifier id, boolean hasDepth) {
        Sprite sprite = textureGetter.apply(this.resolveSprite("particle"));
        if (this.getRootModel() == ModelLoader.BLOCK_ENTITY_MARKER)
            return new BuiltinBakedModel(this.getTransformations(), this.compileOverrides(baker, parent), sprite, this.getGuiLight().isSide());
        else if (getRootModel() == ModelLoader.VANILLA_MARKER) return new VanillaBakedModel();
        else {
            BasicBakedModel.Builder builder = new BasicBakedModel.Builder(this, this.compileOverrides(baker, parent), hasDepth).setParticle(sprite);
            for (ModelElement modelElement : this.getElements())
                for (Direction direction : modelElement.faces.keySet()) {
                    ModelElementFace modelElementFace = modelElement.faces.get(direction);
                    Sprite sprite2 = textureGetter.apply(this.resolveSprite(modelElementFace.textureId));
                    if (modelElementFace.cullFace == null)
                        builder.addQuad(createQuad(modelElement, modelElementFace, sprite2, direction, settings, id));
                    else
                        builder.addQuad(Direction.transform(settings.getRotation().copyMatrix(), modelElementFace.cullFace), createQuad(modelElement, modelElementFace, sprite2, direction, settings, id));
                }

            return builder.build();
        }
    }

    private static BakedQuad createQuad(ModelElement element, ModelElementFace elementFace, Sprite sprite, Direction side, ModelBakeSettings settings, Identifier id) {
        return QUAD_FACTORY.bake(element.from, element.to, elementFace, sprite, side, settings, element.rotation, element.shade, id);
    }

    public boolean textureExists(String name) {
        return MissingSprite.getMissingSpriteId() != this.resolveSprite(name).texture;
    }

    public SpriteIdentifier resolveSprite(String spriteName) {
        if (isTextureReference(spriteName)) spriteName = spriteName.substring(1);

        List<String> list = new ArrayList<>();

        while(true) {
            Either<SpriteIdentifier, String> either = this.resolveTexture(spriteName);
            Optional<SpriteIdentifier> optional = either.left();
            if (optional.isPresent()) return optional.get();

            spriteName = either.right().orElseThrow(NullPointerException::new);
            if (list.contains(spriteName)) {
                LOGGER.warn("Unable to resolve texture due to reference chain {}->{} in {}", Joiner.on("->").join(list), spriteName, this.id);
                return SpriteIdentifier.of(Atlases.GAME_ATLAS_TEXTURE, MissingSprite.getMissingSpriteId());
            }

            list.add(spriteName);
        }
    }

    private Either<SpriteIdentifier, String> resolveTexture(String name) {
        for(JsonUnbakedModel JsonModel = this; JsonModel != null; JsonModel = JsonModel.parent) {
            Either<SpriteIdentifier, String> either = JsonModel.textureMap.get(name);
            if (either != null) return either;
        }

        return Either.left(SpriteIdentifier.of(Atlases.GAME_ATLAS_TEXTURE, MissingSprite.getMissingSpriteId()));
    }

    private static boolean isTextureReference(String reference) {
        return reference.charAt(0) == '#';
    }

    public JsonUnbakedModel getRootModel() {
        return this.parent == null ? this : this.parent.getRootModel();
    }

    public ModelTransformation getTransformations() {
        Transformation transformation = this.getTransformation(ModelTransformation.Mode.THIRD_PERSON_LEFT_HAND);
        Transformation transformation2 = this.getTransformation(ModelTransformation.Mode.THIRD_PERSON_RIGHT_HAND);
        Transformation transformation3 = this.getTransformation(ModelTransformation.Mode.FIRST_PERSON_LEFT_HAND);
        Transformation transformation4 = this.getTransformation(ModelTransformation.Mode.FIRST_PERSON_RIGHT_HAND);
        Transformation transformation5 = this.getTransformation(ModelTransformation.Mode.HEAD);
        Transformation transformation6 = this.getTransformation(ModelTransformation.Mode.GUI);
        Transformation transformation7 = this.getTransformation(ModelTransformation.Mode.GROUND);
        Transformation transformation8 = this.getTransformation(ModelTransformation.Mode.FIXED);
        return new ModelTransformation(transformation, transformation2, transformation3, transformation4, transformation5, transformation6, transformation7, transformation8);
    }

    private Transformation getTransformation(ModelTransformation.Mode renderMode) {
        return this.parent != null && !this.transformations.isTransformationDefined(renderMode) ? this.parent.getTransformation(renderMode) : this.transformations.getTransformation(renderMode);
    }

    public String toString() {
        return this.id;
    }

    @Environment(EnvType.CLIENT)
    public enum GuiLight {

        /**
         * The model will be shaded from the front, like a basic item
         */
        FRONT("front"),

        /**
         * The model will be shaded from the side, like a block.
         */
        SIDE("side");

        private final String name;

        GuiLight(String name) {
            this.name = name;
        }

        public static JsonUnbakedModel.GuiLight deserialize(String value) {
            JsonUnbakedModel.GuiLight[] var1 = values();

            for (JsonUnbakedModel.GuiLight guiLight : var1) if (guiLight.name.equals(value)) return guiLight;

            throw new IllegalArgumentException("Invalid gui light: " + value);
        }

        public boolean isSide() {
            return this == SIDE;
        }
    }

    @Environment(EnvType.CLIENT)
    public static class Deserializer implements JsonDeserializer<JsonUnbakedModel> {
        public JsonUnbakedModel deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            List<ModelElement> list = this.deserializeElements(jsonDeserializationContext, jsonObject);
            String string = this.deserializeParent(jsonObject);
            Map<String, Either<SpriteIdentifier, String>> map = this.deserializeTextures(jsonObject);
            boolean bl = this.deserializeAmbientOcclusion(jsonObject);
            ModelTransformation modelTransformation = ModelTransformation.NONE;
            if (jsonObject.has("display")) {
                JsonObject jsonObject2 = JsonHelper.getObject(jsonObject, "display");
                modelTransformation = jsonDeserializationContext.deserialize(jsonObject2, ModelTransformation.class);
            }

            List<ModelOverride> list2 = this.deserializeOverrides(jsonDeserializationContext, jsonObject);
            JsonUnbakedModel.GuiLight guiLight = null;
            if (jsonObject.has("gui_light"))
                guiLight = GuiLight.deserialize(JsonHelper.getString(jsonObject, "gui_light"));

            Identifier identifier = string.isEmpty() ? null : Identifier.of(string);
            return new JsonUnbakedModel(identifier, list, map, bl, guiLight, modelTransformation, list2);
        }

        protected List<ModelOverride> deserializeOverrides(JsonDeserializationContext context, JsonObject object) {
            List<ModelOverride> list = Lists.newArrayList();
            if (object.has("overrides")) {
                JsonArray jsonArray = JsonHelper.getArray(object, "overrides");

                for (JsonElement jsonElement : jsonArray)
                    list.add(context.deserialize(jsonElement, ModelOverride.class));
            }

            return list;
        }

        private Map<String, Either<SpriteIdentifier, String>> deserializeTextures(JsonObject object) {
            Identifier identifier = Atlases.GAME_ATLAS_TEXTURE;
            Map<String, Either<SpriteIdentifier, String>> map = Maps.newHashMap();
            if (object.has("textures")) {
                JsonObject jsonObject = JsonHelper.getObject(object, "textures");

                for (Map.Entry<String, JsonElement> stringJsonElementEntry : jsonObject.entrySet())
                    map.put(stringJsonElementEntry.getKey(), resolveReference(identifier, stringJsonElementEntry.getValue().getAsString()));
            }

            return map;
        }

        private static Either<SpriteIdentifier, String> resolveReference(Identifier id, String name) {
            if (JsonUnbakedModel.isTextureReference(name)) return Either.right(name.substring(1));
            else {
                Identifier identifier = Identifier.tryParse(name);
                if (identifier == null) throw new JsonParseException(name + " is not valid resourceId location");
                else return Either.left(SpriteIdentifier.of(id, identifier));
            }
        }

        private String deserializeParent(JsonObject json) {
            return JsonHelper.getString(json, "parent", "");
        }

        protected boolean deserializeAmbientOcclusion(JsonObject json) {
            return JsonHelper.getBoolean(json, "ambientocclusion", true);
        }

        protected List<ModelElement> deserializeElements(JsonDeserializationContext context, JsonObject json) {
            List<ModelElement> list = Lists.newArrayList();
            if (json.has("elements")) for (JsonElement jsonElement : JsonHelper.getArray(json, "elements"))
                list.add(context.deserialize(jsonElement, ModelElement.class));

            return list;
        }
    }
}
