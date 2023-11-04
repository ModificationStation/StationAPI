package net.modificationstation.stationapi.api.client.render.model;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.BlockStateHolder;
import net.modificationstation.stationapi.api.client.color.block.BlockColors;
import net.modificationstation.stationapi.api.client.event.render.model.LoadUnbakedModelEvent;
import net.modificationstation.stationapi.api.client.event.render.model.PreLoadUnbakedModelEvent;
import net.modificationstation.stationapi.api.client.render.block.BlockModels;
import net.modificationstation.stationapi.api.client.render.model.json.JsonUnbakedModel;
import net.modificationstation.stationapi.api.client.render.model.json.ModelVariantMap;
import net.modificationstation.stationapi.api.client.render.model.json.MultipartModelComponent;
import net.modificationstation.stationapi.api.client.render.model.json.MultipartUnbakedModel;
import net.modificationstation.stationapi.api.client.texture.MissingSprite;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.client.texture.SpriteIdentifier;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.resource.ResourceFinder;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.state.property.Property;
import net.modificationstation.stationapi.api.util.Util;
import net.modificationstation.stationapi.api.util.math.AffineTransformation;
import net.modificationstation.stationapi.api.util.profiler.Profiler;
import org.jetbrains.annotations.Nullable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;
import static net.modificationstation.stationapi.impl.client.texture.StationRenderImpl.LOGGER;

@Environment(EnvType.CLIENT)
public class ModelLoader {

    public static final List<Identifier> BLOCK_DESTRUCTION_STAGES = IntStream.range(0, 10).mapToObj(stage -> Identifier.of("block/destroy_stage_" + stage)).collect(Collectors.toList());
    public static final List<Identifier> BLOCK_DESTRUCTION_STAGE_TEXTURES = BLOCK_DESTRUCTION_STAGES.stream().map(id -> Identifier.of(NAMESPACE + "/textures/" + id.path + ".png")).collect(Collectors.toList());
    public static final ModelIdentifier MISSING_ID;
    public static final ResourceFinder BLOCK_STATES_FINDER = ResourceFinder.json(NAMESPACE + "/blockstates");
    public static final ResourceFinder MODELS_FINDER = ResourceFinder.json(NAMESPACE + "/models");
    @VisibleForTesting
    public static final String MISSING_DEFINITION;
    private static final Map<String, String> BUILTIN_MODEL_DEFINITIONS;
    private static final Splitter COMMA_SPLITTER;
    private static final Splitter KEY_VALUE_SPLITTER;
    public static final JsonUnbakedModel GENERATION_MARKER;
    public static final JsonUnbakedModel BLOCK_ENTITY_MARKER;
    public static final JsonUnbakedModel VANILLA_MARKER;
    private static final ItemModelGenerator ITEM_MODEL_GENERATOR;
    private static final Map<Identifier, StateManager<Block, BlockState>> STATIC_DEFINITIONS;
    private final BlockColors blockColors;
    private final Map<Identifier, JsonUnbakedModel> jsonUnbakedModels;
    private final Map<Identifier, List<SourceTrackedData>> blockStates;
    private final Set<Identifier> modelsToLoad = Sets.newIdentityHashSet();
    private final ModelVariantMap.DeserializationContext variantMapDeserializationContext = new ModelVariantMap.DeserializationContext();
    private final Map<Identifier, UnbakedModel> unbakedModels = new IdentityHashMap<>();
    private final Map<BakedModelCacheKey, BakedModel> bakedModelCache = new HashMap<>();
    private final Map<Identifier, UnbakedModel> modelsToBake = new IdentityHashMap<>();
    private final Map<Identifier, BakedModel> bakedModels = new IdentityHashMap<>();
    private int nextStateId = 1;
    private final Object2IntMap<BlockState> stateLookup = Util.make(new Object2IntOpenHashMap<>(), (object2IntOpenHashMap) -> object2IntOpenHashMap.defaultReturnValue(-1));

    public ModelLoader(BlockColors blockColors, Profiler profiler, Map<Identifier, JsonUnbakedModel> jsonUnbakedModels, Map<Identifier, List<SourceTrackedData>> blockStates) {
        this.blockColors = blockColors;
        this.jsonUnbakedModels = jsonUnbakedModels;
        this.blockStates = blockStates;
        profiler.push("missing_model");
        try {
            this.unbakedModels.put(MISSING_ID.asIdentifier(), this.loadModelFromJson(MISSING_ID.asIdentifier()));
            this.addModel(MISSING_ID.asIdentifier());
        } catch (IOException iOException) {
            LOGGER.error("Error loading missing model, should never happen :(", iOException);
            throw new RuntimeException(iOException);
        }
        profiler.swap("static_definitions");
        STATIC_DEFINITIONS.forEach((id, stateManager) -> stateManager.getStates().forEach(state -> this.addModel(BlockModels.getModelId(id, state).asIdentifier())));
        profiler.swap("blocks");
        for (Block block : BlockRegistry.INSTANCE)
            block.getStateManager().getStates().forEach(state -> this.addModel(BlockModels.getModelId(state).asIdentifier()));
        profiler.swap("items");
        for (Identifier identifier : ItemRegistry.INSTANCE.getIds())
            this.addModel(ModelIdentifier.of(identifier, "inventory").asIdentifier());
        profiler.swap("special");
        this.modelsToBake.values().forEach(model -> model.setParents(this::getOrLoadModel));
        profiler.pop();
    }

    public void bake(BiFunction<Identifier, SpriteIdentifier, Sprite> spriteLoader) {
        this.modelsToBake.keySet().forEach(modelId -> {
            BakedModel bakedModel = null;
            try {
                bakedModel = new BakerImpl(spriteLoader, modelId).bake(modelId, ModelBakeRotation.X0_Y0);
            } catch (Exception exception) {
                LOGGER.warn("Unable to bake model: '{}': {}", modelId, exception);
            }
            if (bakedModel != null) this.bakedModels.put(modelId, bakedModel);
        });
    }

    private static Predicate<BlockState> stateKeyToPredicate(StateManager<Block, BlockState> stateFactory, String key) {
        Map<Property<?>, Comparable<?>> map = new HashMap<>();
        Iterator<String> var3 = COMMA_SPLITTER.split(key).iterator();

        while (true) {
            Iterator<String> iterator;
            do {
                if (!var3.hasNext()) {
                    Block block = stateFactory.getOwner();
                    return (blockState) -> {
                        if (blockState != null && block == blockState.getBlock()) {
                            Iterator<Entry<Property<?>, Comparable<?>>> var4 = map.entrySet().iterator();

                            Entry<Property<?>, Comparable<?>> entry;
                            do {
                                if (!var4.hasNext()) return true;

                                entry = var4.next();
                            } while (Objects.equals(blockState.get(entry.getKey()), entry.getValue()));

                        }
                        return false;
                    };
                }

                String string = var3.next();
                iterator = KEY_VALUE_SPLITTER.split(string).iterator();
            } while (!iterator.hasNext());

            String string2 = iterator.next();
            Property<?> property = stateFactory.getProperty(string2);
            if (property != null && iterator.hasNext()) {
                String string3 = iterator.next();
                Comparable<?> comparable = getPropertyValue(property, string3);
                if (comparable == null)
                    throw new RuntimeException("Unknown value: '" + string3 + "' for blockstate property: '" + string2 + "' " + property.getValues());

                map.put(property, comparable);
            } else if (!string2.isEmpty()) throw new RuntimeException("Unknown blockstate property: '" + string2 + "'");
        }
    }

    @Nullable
    static <T extends Comparable<T>> T getPropertyValue(Property<T> property, String string) {
        return property.parse(string).orElse(null);
    }

    public UnbakedModel getOrLoadModel(Identifier id) {
        if (this.unbakedModels.containsKey(id)) return this.unbakedModels.get(id);
        else if (this.modelsToLoad.contains(id))
            throw new IllegalStateException("Circular reference while loading " + id);
        else {
            this.modelsToLoad.add(id);
            UnbakedModel unbakedModel = this.unbakedModels.get(MISSING_ID.asIdentifier());

            while(!this.modelsToLoad.isEmpty()) {
                Identifier identifier = this.modelsToLoad.iterator().next();
                Identifier processedId = identifier;
                int atId = processedId.path.indexOf('@');
                if (atId > -1) processedId = Identifier.of(processedId.namespace, processedId.path.substring(atId + 1));

                try {
                    if (!this.unbakedModels.containsKey(processedId)) this.loadModel(identifier);
                } catch (ModelLoader.ModelLoaderException var9) {
                    LOGGER.warn(var9.getMessage());
                    this.unbakedModels.put(processedId, unbakedModel);
                } catch (NullPointerException | FileNotFoundException e) {
                    unbakedModels.put(processedId, VANILLA_MARKER);
                } catch (Exception var10) {
                    LOGGER.warn("Unable to load model: '{}' referenced from: {}: {}", processedId, id, var10);
                    this.unbakedModels.put(processedId, unbakedModel);
                } finally {
                    this.modelsToLoad.remove(identifier);
                }
            }

            return this.unbakedModels.getOrDefault(id, unbakedModel);
        }
    }

    private void loadModel(Identifier id) throws Exception {
        if (id.path.startsWith("dependency@")) {
            id = Identifier.of(id.namespace, id.path.substring(11));
            this.putModel(id, this.loadModelFromResource(id));
            return;
        }
        ModelIdentifier modelIdentifier = ModelIdentifier.of(id.toString());
        if (Objects.equals(modelIdentifier.variant, "inventory")) {
            Identifier identifier = id.withPrefixedPath("item/");
            UnbakedModel unbakedModel = this.loadModelFromResource(identifier);
            this.putModel(modelIdentifier.asIdentifier(), unbakedModel);
            this.unbakedModels.put(identifier, unbakedModel);
        } else {
            StateManager<Block, BlockState> stateManager = /*Optional.ofNullable(STATIC_DEFINITIONS.get(identifier)).orElseGet(() -> */((BlockStateHolder) Objects.requireNonNull(BlockRegistry.INSTANCE.get(modelIdentifier.id))).getStateManager()/*)*/;
            variantMapDeserializationContext.setStateFactory(stateManager);
            ImmutableList<Property<?>> list = ImmutableList.copyOf(this.blockColors.getProperties(stateManager.getOwner()));
            ImmutableList<BlockState> immutableList = stateManager.getStates();
            Map<ModelIdentifier, BlockState> map = new IdentityHashMap<>();
            immutableList.forEach(state -> map.put(BlockModels.getModelId(modelIdentifier.id, state), state));
            Map<BlockState, Pair<UnbakedModel, Supplier<ModelDefinition>>> map2 = new HashMap<>();
            Identifier identifier2 = BLOCK_STATES_FINDER.toResourcePath(modelIdentifier.id);
            UnbakedModel unbakedModel = VANILLA_MARKER;
            ModelDefinition modelDefinition2 = new ModelDefinition(ImmutableList.of(unbakedModel), ImmutableList.of());
            Pair<UnbakedModel, Supplier<ModelDefinition>> pair = Pair.of(unbakedModel, () -> modelDefinition2);
            try {
                List<Pair<String, ModelVariantMap>> list2 = this.blockStates.getOrDefault(identifier2, List.of()).stream().map((blockState) -> {
                    try {
                        return Pair.of(blockState.source, ModelVariantMap.fromJson(variantMapDeserializationContext, blockState.data));
                    } catch (Exception var4) {
                        throw new ModelLoaderException(String.format(Locale.ROOT, "Exception loading blockstate definition: '%s' in texturepack: '%s': %s", identifier2, blockState.source, var4.getMessage()));
                    }
                }).toList();
                for (Pair<String, ModelVariantMap> pair2 : list2) {
                    MultipartUnbakedModel multipartUnbakedModel;
                    ModelVariantMap modelVariantMap = pair2.getSecond();
                    Map<BlockState, Pair<UnbakedModel, Supplier<ModelDefinition>>> map4 = new IdentityHashMap<>();
                    if (modelVariantMap.hasMultipartModel()) {
                        multipartUnbakedModel = modelVariantMap.getMultipartModel();
                        immutableList.forEach(blockState -> map4.put(blockState, Pair.of(multipartUnbakedModel, () -> ModelDefinition.create(blockState, multipartUnbakedModel, list))));
                    } else multipartUnbakedModel = null;
                    modelVariantMap.getVariantMap().forEach((string, weightedUnbakedModel) -> {
                        try {
                            immutableList.stream().filter(ModelLoader.stateKeyToPredicate(stateManager, string)).forEach(blockState -> {
                                Pair<UnbakedModel, Supplier<ModelDefinition>> pair3 = map4.put(blockState, Pair.of(weightedUnbakedModel, () -> ModelDefinition.create(blockState, weightedUnbakedModel, list)));
                                if (pair3 != null && pair3.getFirst() != multipartUnbakedModel) {
                                    map4.put(blockState, pair);
                                    throw new RuntimeException("Overlapping definition with: " + modelVariantMap.getVariantMap().entrySet().stream().filter(entry -> entry.getValue() == pair3.getFirst()).findFirst().orElseThrow(NullPointerException::new).getKey());
                                }
                            });
                        } catch (Exception exception) {
                            LOGGER.warn("Exception loading blockstate definition: '{}' in texturepack: '{}' for variant: '{}': {}", identifier2, pair2.getFirst(), string, exception.getMessage());
                        }
                    });
                    map2.putAll(map4);
                }
            } catch (ModelLoaderException modelLoaderException) {
                throw modelLoaderException;
            } catch (Exception exception) {
                throw new ModelLoaderException(String.format("Exception loading blockstate definition: '%s': %s", identifier2, exception));
            } finally {
                Map<ModelDefinition, Set<BlockState>> map6 = new HashMap<>();
                map.forEach((id1, blockState) -> {
                    Pair<UnbakedModel, Supplier<ModelDefinition>> pair2 = map2.get(blockState);
                    // Vanilla blocks don't have JSON models, and we don't want to enforce them
                    // LOGGER.warn("Exception loading blockstate definition: '{}' missing model for variant: '{}'", identifier2, id1);
                    if (pair2 == null) pair2 = pair;
                    this.putModel(id1.asIdentifier(), pair2.getFirst());
                    try {
                        ModelDefinition modelDefinition3 = pair2.getSecond().get();
                        map6.computeIfAbsent(modelDefinition3, modelDefinition -> Sets.newIdentityHashSet()).add(blockState);
                    } catch (Exception exception) {
                        LOGGER.warn("Exception evaluating model definition: '{}'", id1, exception);
                    }
                });
                map6.forEach((modelDefinition, set) -> {
//                    Iterator<BlockState> iterator = set.iterator();
//                    while (iterator.hasNext()) {
//                        BlockState blockState = iterator.next();
//                        if (blockState.getRenderType() == BlockRenderType.MODEL) continue;
//                        iterator.remove();
//                        this.stateLookup.put(blockState, 0);
//                    }
                    if (set.size() > 1) this.addStates(set);
                });
            }
        }
    }

    private void putModel(Identifier id, UnbakedModel unbakedModel) {
        this.unbakedModels.put(id, unbakedModel);
        this.modelsToLoad.addAll(unbakedModel.getModelDependencies().stream().map(identifier -> identifier.withPrefixedPath("dependency@")).toList());
    }

    private void addModel(Identifier modelId) {
        UnbakedModel unbakedModel = this.getOrLoadModel(modelId);
        this.unbakedModels.put(modelId, unbakedModel);
        this.modelsToBake.put(modelId, unbakedModel);
    }

    private void addStates(Iterable<BlockState> states) {
        int i = this.nextStateId++;
        states.forEach((blockState) -> this.stateLookup.put(blockState, i));
    }

    private UnbakedModel loadModelFromResource(Identifier id) throws IOException {
        return StationAPI.EVENT_BUS.post(
                LoadUnbakedModelEvent.builder()
                        .identifier(id)
                        .modelLoader(this)
                        .model(
                                StationAPI.EVENT_BUS.post(
                                        PreLoadUnbakedModelEvent.builder()
                                                .identifier(id)
                                                .modelLoader(this)
                                                .loader(this::loadModelFromJson)
                                                .build()
                                ).loader.apply(id)
                        )
                        .build()
        ).model;
    }

    private JsonUnbakedModel loadModelFromJson(Identifier id) throws IOException {
        ModelIdentifier modelIdentifier = ModelIdentifier.of(id.toString());
        String path = modelIdentifier.id.path;
        return switch (path) {
            case "builtin/generated" -> GENERATION_MARKER;
            case "builtin/entity" -> BLOCK_ENTITY_MARKER;
            default -> {
                if (path.startsWith("builtin/")) {
                    String string2 = path.substring("builtin/".length());
                    String string3 = BUILTIN_MODEL_DEFINITIONS.get(string2);
                    if (string3 == null) throw new FileNotFoundException(id.toString());
                    Reader reader = new StringReader(string3);
                    JsonUnbakedModel jsonUnbakedModel = JsonUnbakedModel.deserialize(reader);
                    jsonUnbakedModel.id = id.toString();
                    yield jsonUnbakedModel;
                } else {
                    Identifier identifier = MODELS_FINDER.toResourcePath(modelIdentifier.id);
                    JsonUnbakedModel jsonUnbakedModel2 = this.jsonUnbakedModels.get(identifier);
                    if (jsonUnbakedModel2 == null) throw new FileNotFoundException(identifier.toString());
                    jsonUnbakedModel2.id = id.toString();
                    yield jsonUnbakedModel2;
                }
            }
        };
    }

    public Map<Identifier, BakedModel> getBakedModelMap() {
        return this.bakedModels;
    }

    public Object2IntMap<BlockState> getStateLookup() {
        return this.stateLookup;
    }

    static {
        MISSING_ID = ModelIdentifier.of("builtin/missing", "missing");
        MISSING_DEFINITION = ("{    'textures': {       'particle': '" + MissingSprite.getMissingSpriteId().path + "',       'missingno': '" + MissingSprite.getMissingSpriteId().path + "'    },    'elements': [         {  'from': [ 0, 0, 0 ],            'to': [ 16, 16, 16 ],            'faces': {                'down':  { 'uv': [ 0, 0, 16, 16 ], 'cullface': 'down',  'texture': '#missingno' },                'up':    { 'uv': [ 0, 0, 16, 16 ], 'cullface': 'up',    'texture': '#missingno' },                'north': { 'uv': [ 0, 0, 16, 16 ], 'cullface': 'north', 'texture': '#missingno' },                'south': { 'uv': [ 0, 0, 16, 16 ], 'cullface': 'south', 'texture': '#missingno' },                'west':  { 'uv': [ 0, 0, 16, 16 ], 'cullface': 'west',  'texture': '#missingno' },                'east':  { 'uv': [ 0, 0, 16, 16 ], 'cullface': 'east',  'texture': '#missingno' }            }        }    ]}").replace('\'', '"');
        BUILTIN_MODEL_DEFINITIONS = new HashMap<>(ImmutableMap.of("missing", MISSING_DEFINITION));
        COMMA_SPLITTER = Splitter.on(',');
        KEY_VALUE_SPLITTER = Splitter.on('=').limit(2);
        GENERATION_MARKER = Util.make(JsonUnbakedModel.deserialize("{\"gui_light\": \"front\"}"), (jsonUnbakedModel) -> jsonUnbakedModel.id = "generation marker");
        BLOCK_ENTITY_MARKER = Util.make(JsonUnbakedModel.deserialize("{\"gui_light\": \"side\"}"), (jsonUnbakedModel) -> jsonUnbakedModel.id = "block entity marker");
        VANILLA_MARKER = Util.make(JsonUnbakedModel.deserialize("{}"), (jsonUnbakedModel) -> jsonUnbakedModel.id = "vanilla marker");
        ITEM_MODEL_GENERATOR = new ItemModelGenerator();
        STATIC_DEFINITIONS = ImmutableMap.of();
    }

    @Environment(EnvType.CLIENT)
    static class ModelDefinition {
        private final List<UnbakedModel> components;
        private final List<Object> values;

        public ModelDefinition(List<UnbakedModel> components, ImmutableList<Object> values) {
            this.components = components;
            this.values = values;
        }

        public boolean equals(Object o) {
            if (this == o) return true;
            else if (!(o instanceof ModelDefinition modelDefinition)) return false;
            else
                return Objects.equals(this.components, modelDefinition.components) && Objects.equals(this.values, modelDefinition.values);
        }

        public int hashCode() {
            return 31 * this.components.hashCode() + this.values.hashCode();
        }

        public static ModelLoader.ModelDefinition create(BlockState state, MultipartUnbakedModel rawModel, Collection<Property<?>> properties) {
            StateManager<Block, BlockState> stateManager = ((BlockStateHolder) state.getBlock()).getStateManager();
            List<UnbakedModel> list = rawModel.getComponents().stream().filter((multipartModelComponent) -> multipartModelComponent.getPredicate(stateManager).test(state)).map(MultipartModelComponent::getModel).collect(ImmutableList.toImmutableList());
            ImmutableList<Object> list2 = getStateValues(state, properties);
            return new ModelLoader.ModelDefinition(list, list2);
        }

        public static ModelLoader.ModelDefinition create(BlockState state, UnbakedModel rawModel, Collection<Property<?>> properties) {
            ImmutableList<Object> list = getStateValues(state, properties);
            return new ModelLoader.ModelDefinition(ImmutableList.of(rawModel), list);
        }

        private static ImmutableList<Object> getStateValues(BlockState state, Collection<Property<?>> properties) {
            return properties.stream().map(state::get).collect(ImmutableList.toImmutableList());
        }
    }

    public record SourceTrackedData(String source, JsonElement data) {}

    class BakerImpl implements Baker {
        private final Function<SpriteIdentifier, Sprite> textureGetter;

        BakerImpl(BiFunction<Identifier, SpriteIdentifier, Sprite> spriteLoader, Identifier modelId) {
            textureGetter = spriteId -> spriteLoader.apply(modelId, spriteId);
        }

        @Override
        public UnbakedModel getOrLoadModel(Identifier id) {
            return ModelLoader.this.getOrLoadModel(id);
        }

        @Override
        public BakedModel bake(Identifier id, ModelBakeSettings settings) {
            BakedModelCacheKey bakedModelCacheKey = new BakedModelCacheKey(id, settings.getRotation(), settings.isUvLocked());
            BakedModel bakedModel = ModelLoader.this.bakedModelCache.get(bakedModelCacheKey);
            if (bakedModel != null) return bakedModel;
            UnbakedModel unbakedModel = this.getOrLoadModel(id);
            if (unbakedModel instanceof JsonUnbakedModel jsonUnbakedModel && jsonUnbakedModel.getRootModel() == GENERATION_MARKER)
                return ITEM_MODEL_GENERATOR.create(this.textureGetter, jsonUnbakedModel).bake(this, jsonUnbakedModel, this.textureGetter, settings, id, false);
            BakedModel bakedModel2 = unbakedModel.bake(this, this.textureGetter, settings, id);
            ModelLoader.this.bakedModelCache.put(bakedModelCacheKey, bakedModel2);
            return bakedModel2;
        }
    }

    record BakedModelCacheKey(Identifier id, AffineTransformation transformation, boolean isUvLocked) {}

    @Environment(EnvType.CLIENT)
    static class ModelLoaderException extends RuntimeException {
        public ModelLoaderException(String message) {
            super(message);
        }
    }
}
