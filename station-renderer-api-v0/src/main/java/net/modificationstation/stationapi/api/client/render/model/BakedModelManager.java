package net.modificationstation.stationapi.api.client.render.model;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ObjectArrays;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import lombok.val;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.texture.TextureManager;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.StationBlock;
import net.modificationstation.stationapi.api.block.StationFlatteningBlock;
import net.modificationstation.stationapi.api.client.color.block.BlockColors;
import net.modificationstation.stationapi.api.client.render.block.BlockModels;
import net.modificationstation.stationapi.api.client.render.model.json.JsonUnbakedModel;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.client.texture.SpriteAtlasTexture;
import net.modificationstation.stationapi.api.client.texture.SpriteIdentifier;
import net.modificationstation.stationapi.api.client.texture.StationTextureManager;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.resource.IdentifiableResourceReloadListener;
import net.modificationstation.stationapi.api.resource.Resource;
import net.modificationstation.stationapi.api.resource.ResourceManager;
import net.modificationstation.stationapi.api.resource.ResourceReloader;
import net.modificationstation.stationapi.api.util.JsonHelper;
import net.modificationstation.stationapi.api.util.Util;
import net.modificationstation.stationapi.api.util.profiler.Profiler;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import static net.modificationstation.stationapi.api.StationAPI.MODID;
import static net.modificationstation.stationapi.api.registry.ModID.MINECRAFT;
import static net.modificationstation.stationapi.impl.client.texture.StationRenderImpl.LOGGER;

@Environment(EnvType.CLIENT)
public class BakedModelManager implements IdentifiableResourceReloadListener, AutoCloseable {

    public static final Identifier MODELS = MODID.id("models");

    private Map<Identifier, BakedModel> models;
    @SuppressWarnings("deprecation")
    private final SpriteAtlasManager atlasManager = new SpriteAtlasManager(Util.make(new Reference2ReferenceOpenHashMap<>(), m -> m.put(Atlases.GAME_ATLAS_TEXTURE, MINECRAFT.id("game"))), StationTextureManager.get(((Minecraft) FabricLoader.getInstance().getGameInstance()).textureManager));
    private final BlockModels blockModelCache;
    private final TextureManager textureManager;
    private final BlockColors colorMap;
    private BakedModel missingModel;
    private Object2IntMap<BlockState> stateLookup;

    public BakedModelManager(TextureManager textureManager, BlockColors colorMap) {
        this.textureManager = textureManager;
        this.colorMap = colorMap;
        this.blockModelCache = new BlockModels(this);
    }

    public BakedModel getModel(ModelIdentifier id) {
        return this.models.getOrDefault(id.asIdentifier(), this.missingModel);
    }

    public BakedModel getMissingModel() {
        return this.missingModel;
    }

    public BlockModels getBlockModels() {
        return this.blockModelCache;
    }

    public SpriteAtlasTexture getAtlas(Identifier identifier) {
        return Objects.requireNonNull(this.atlasManager).getAtlas(identifier);
    }

    public void close() {
        this.atlasManager.close();
    }

    @Override
    public final CompletableFuture<Void> reload(
            ResourceReloader.Synchronizer synchronizer,
            ResourceManager manager,
            Profiler prepareProfiler,
            Profiler applyProfiler,
            Executor prepareExecutor,
            Executor applyExecutor
    ) {
        prepareProfiler.startTick();
        val modelLoader = BakedModelManager.reloadModels(manager, prepareExecutor).thenCombineAsync(
                BakedModelManager.reloadBlockStates(manager, prepareExecutor),
                (jsonUnbakedModels, blockStates) -> new ModelLoader(colorMap, prepareProfiler, jsonUnbakedModels, blockStates),
                prepareExecutor
        );
        val atlases = atlasManager.reload(manager, prepareExecutor);
        return CompletableFuture
                .allOf(ObjectArrays.concat(atlases.values().toArray(CompletableFuture[]::new), modelLoader))
                .thenApplyAsync(void_ -> bake(
                        prepareProfiler,
                        atlases.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().join())),
                        modelLoader.join()
                ), prepareExecutor)
                .thenCompose(result -> result.readyForUpload.thenApply(void_ -> result))
                .thenCompose(synchronizer::whenPrepared)
                .thenAcceptAsync(result -> upload(result, applyProfiler), applyExecutor);
    }

    private static CompletableFuture<Map<Identifier, JsonUnbakedModel>> reloadModels(ResourceManager resourceManager, Executor executor) {
        return CompletableFuture.supplyAsync(() -> ModelLoader.MODELS_FINDER.findResources(resourceManager), executor).thenCompose(models2 -> {
            ArrayList<CompletableFuture<Pair<Identifier, JsonUnbakedModel>>> list = new ArrayList<>(models2.size());
            for (Map.Entry<Identifier, Resource> entry : models2.entrySet())
                list.add(CompletableFuture.supplyAsync(() -> {
                    try (Reader reader = entry.getValue().getReader()) {
                        return Pair.of(entry.getKey(), JsonUnbakedModel.deserialize(reader));
                    } catch (Exception var6) {
                        LOGGER.error("Failed to load model {}", entry.getKey(), var6);
                        return null;
                    }
                }, executor));
            return Util.combineSafe(list).thenApply(models -> models.stream().filter(Objects::nonNull).collect(Collectors.toUnmodifiableMap(Pair::getFirst, Pair::getSecond)));
        });
    }

    private static CompletableFuture<Map<Identifier, List<ModelLoader.SourceTrackedData>>> reloadBlockStates(ResourceManager resourceManager, Executor executor) {
        return CompletableFuture.supplyAsync(() -> ModelLoader.BLOCK_STATES_FINDER.findAllResources(resourceManager), executor).thenCompose(blockStates2 -> {
            List<CompletableFuture<Pair<Identifier, List<ModelLoader.SourceTrackedData>>>> list = new ArrayList<>(blockStates2.size());
            for (Map.Entry<Identifier, List<Resource>> entry : blockStates2.entrySet())
                list.add(CompletableFuture.supplyAsync(() -> {
                    List<Resource> resources = entry.getValue();
                    List<ModelLoader.SourceTrackedData> list2 = new ArrayList<>(resources.size());
                    for (Resource resource : resources)
                        try (BufferedReader reader = resource.getReader()) {
                            list2.add(new ModelLoader.SourceTrackedData("Default", JsonHelper.deserialize(reader)));
                        } catch (Exception exception) {
                            LOGGER.error("Failed to load blockstate {} from pack {}", entry.getKey(), "Default", exception);
                        }
                    return Pair.of(entry.getKey(), list2);
                }, executor));
            return Util.combineSafe(list).thenApply(blockStates -> blockStates.stream().filter(Objects::nonNull).collect(Collectors.toUnmodifiableMap(Pair::getFirst, Pair::getSecond)));
        });
    }

    private BakingResult bake(Profiler profiler, Map<Identifier, SpriteAtlasManager.AtlasPreparation> preparations, ModelLoader modelLoader) {
        profiler.push("load");
        profiler.swap("baking");
        HashMultimap<Identifier, SpriteIdentifier> multimap = HashMultimap.create();
        modelLoader.bake((id, spriteId) -> {
            SpriteAtlasManager.AtlasPreparation atlasPreparation = preparations.get(spriteId.atlas);
            Sprite sprite = atlasPreparation.getSprite(spriteId.texture);
            if (sprite != null) return sprite;
            multimap.put(id, spriteId);
            return atlasPreparation.getMissingSprite();
        });
        multimap.asMap().forEach((modelId, spriteIds) -> LOGGER.warn("Missing textures in model {}:\n{}", modelId, spriteIds.stream().sorted(SpriteIdentifier.COMPARATOR).map(spriteIdentifier -> "    " + spriteIdentifier.atlas + ":" + spriteIdentifier.texture).collect(Collectors.joining("\n"))));
        profiler.swap("dispatch");
        Map<Identifier, BakedModel> map = modelLoader.getBakedModelMap();
        BakedModel bakedModel = map.get(ModelLoader.MISSING_ID.asIdentifier());
        Map<BlockState, BakedModel> map2 = new IdentityHashMap<>();
        for (BlockBase block : BlockRegistry.INSTANCE)
            ((StationFlatteningBlock) block).getStateManager().getStates().forEach(state -> {
                Identifier identifier = ((StationBlock) state.getBlock()).getRegistryEntry().registryKey().getValue();
                BakedModel bakedModel2 = map.getOrDefault(BlockModels.getModelId(identifier, state).asIdentifier(), bakedModel);
                map2.put(state, bakedModel2);
            });
        CompletableFuture<Void> completableFuture = CompletableFuture.allOf(preparations.values().stream().map(SpriteAtlasManager.AtlasPreparation::whenComplete).toArray(CompletableFuture[]::new));
        profiler.pop();
        profiler.endTick();
        return new BakingResult(modelLoader, bakedModel, map2, preparations, completableFuture);
    }

    private void upload(BakingResult bakingResult, Profiler profiler) {
        profiler.startTick();
        profiler.push("upload");
        bakingResult.atlasPreparations.values().forEach(SpriteAtlasManager.AtlasPreparation::upload);
        ModelLoader modelLoader = bakingResult.modelLoader;
        this.models = modelLoader.getBakedModelMap();
        this.stateLookup = modelLoader.getStateLookup();
        this.missingModel = bakingResult.missingModel;
        profiler.swap("cache");
        this.blockModelCache.setModels(bakingResult.modelCache);
        profiler.pop();
        profiler.endTick();
    }

    record BakingResult(ModelLoader modelLoader, BakedModel missingModel, Map<BlockState, BakedModel> modelCache, Map<Identifier, SpriteAtlasManager.AtlasPreparation> atlasPreparations, CompletableFuture<Void> readyForUpload) {}

    @Override
    public Identifier getId() {
        return MODELS;
    }
}
