package net.modificationstation.stationapi.api.client.model;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.modificationstation.stationapi.api.client.registry.ModelRegistry;
import net.modificationstation.stationapi.api.client.texture.TexturePackDependent;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.resource.ResourceManager;

import static net.modificationstation.stationapi.api.StationAPI.MODID;

public abstract class Model implements TexturePackDependent {

    public final Identifier id;
    public final String modelPath;
    private BakedModel baked;
    protected boolean invalidated;

    public Model(Identifier identifier, String extension) {
        this.id = identifier;
        modelPath = ResourceManager.parsePath(identifier, "/" + MODID + "/models", extension);
        ModelRegistry.INSTANCE.register(identifier, this);
        //noinspection deprecation
        reloadFromTexturePack(((Minecraft) FabricLoader.getInstance().getGameInstance()).texturePackManager.texturePack);
    }

    public final BakedModel getBaked() {
        if (invalidated) {
            invalidated = false;
            baked = bake();
        }
        return baked;
    }

    protected abstract BakedModel bake();
}
