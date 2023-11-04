package net.modificationstation.stationapi.api.client.render.model;

import net.modificationstation.stationapi.api.util.Identifier;
import org.jetbrains.annotations.Nullable;

public interface Baker {
    UnbakedModel getOrLoadModel(Identifier id);

    @Nullable
    BakedModel bake(Identifier id, ModelBakeSettings settings);
}
