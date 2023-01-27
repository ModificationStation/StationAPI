package net.modificationstation.stationapi.api.client.render.model;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.texture.TextureManager;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.client.color.block.BlockColors;
import net.modificationstation.stationapi.api.client.render.block.BlockModels;
import net.modificationstation.stationapi.api.client.texture.SpriteAtlasTexture;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.resource.ResourceManager;
import net.modificationstation.stationapi.api.resource.SinglePreparationResourceReloader;
import net.modificationstation.stationapi.api.util.profiler.Profiler;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;

@Environment(EnvType.CLIENT)
public class BakedModelManager extends SinglePreparationResourceReloader<ModelLoader> implements AutoCloseable {
    private Map<Identifier, BakedModel> models;
    @Nullable
    private SpriteAtlasManager atlasManager;
    private final BlockModels blockModelCache;
    private final TextureManager textureManager;
    private final BlockColors colourMap;
    private BakedModel missingModel;
    private Object2IntMap<BlockState> stateLookup;

    public BakedModelManager(TextureManager textureManager, BlockColors colourMap) {
        this.textureManager = textureManager;
        this.colourMap = colourMap;
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

    @Override
    public ModelLoader prepare(ResourceManager resourceManager, Profiler profiler) {
        profiler.startTick();
        ModelLoader modelLoader = new ModelLoader(resourceManager, this.colourMap, profiler);
        profiler.endTick();
        return modelLoader;
    }

    @Override
    public void apply(ModelLoader modelLoader, ResourceManager resourceManager, Profiler profiler) {
        profiler.startTick();
        profiler.push("upload");
        if (this.atlasManager != null) {
            this.atlasManager.close();
        }

        this.atlasManager = modelLoader.upload(this.textureManager, profiler);
        this.models = modelLoader.getBakedModelMap();
        this.stateLookup = modelLoader.getStateLookup();
        this.missingModel = this.models.get(ModelLoader.MISSING.asIdentifier());
        profiler.swap("cache");
        this.blockModelCache.reload();
        profiler.pop();
        profiler.endTick();
    }

    public boolean shouldRerender(BlockState from, BlockState to) {
        if (from == to) {
            return false;
        } else {
            int i = this.stateLookup.getInt(from);
            if (i != -1) {
                int j = this.stateLookup.getInt(to);
                return i != j;
//            if (i == j) {
//               FluidState fluidState = from.getFluidState();
//               FluidState fluidState2 = to.getFluidState();
//               return fluidState != fluidState2;
//            }
            }

            return true;
        }
    }

    public SpriteAtlasTexture getAtlas(Identifier identifier) {
        return Objects.requireNonNull(this.atlasManager).getAtlas(identifier);
    }

    public void close() {
        if (this.atlasManager != null) {
            this.atlasManager.close();
        }

    }
}
