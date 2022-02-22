package net.modificationstation.stationapi.api.client.model;

import net.modificationstation.stationapi.api.client.render.model.BakedModel;
import net.modificationstation.stationapi.api.client.texture.TexturePackDependent;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.resource.ResourceManager;

import static net.modificationstation.stationapi.api.StationAPI.MODID;

@Deprecated
public abstract class Model implements TexturePackDependent {

    public final Identifier id;
    public final String modelPath;
    private BakedModel baked;
    protected boolean invalidated;

//    public static <T extends Model> T get(final Identifier identifier, final Function<Identifier, T> initializer) {
//        //noinspection unchecked
//        return (T) ModelRegistry.INSTANCE.computeIfAbsent(identifier, (Function<Identifier, Model>) initializer);
//    }

    protected Model(final Identifier identifier, final String extension) {
        this.id = identifier;
        modelPath = ResourceManager.ASSETS.toPath(identifier, MODID + "/models", extension);
    }

    public final BakedModel getBaked() {
        if (invalidated) {
            baked = bake();
            invalidated = false;
        }
        return baked;
    }

    protected abstract BakedModel bake();
}
