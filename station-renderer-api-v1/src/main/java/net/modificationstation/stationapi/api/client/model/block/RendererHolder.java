package net.modificationstation.stationapi.api.client.model.block;

import com.google.common.base.Suppliers;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.modificationstation.stationapi.api.client.render.Renderer;
import net.modificationstation.stationapi.api.client.render.model.BakedModelRenderer;

import java.util.function.Supplier;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class RendererHolder {

    static final Supplier<BakedModelRenderer> BAKED_MODEL_RENDERER = Suppliers.memoize(() -> Renderer.get().bakedModelRenderer());
}
