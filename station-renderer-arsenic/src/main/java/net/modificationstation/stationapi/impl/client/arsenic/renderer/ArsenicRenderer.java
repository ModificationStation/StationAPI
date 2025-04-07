package net.modificationstation.stationapi.impl.client.arsenic.renderer;

import com.google.common.base.Suppliers;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.modificationstation.stationapi.api.client.render.Renderer;
import net.modificationstation.stationapi.api.client.render.StateManager;
import net.modificationstation.stationapi.api.client.render.model.BakedModelRenderer;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.render.BakedModelRendererImpl;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.render.StateManagerImpl;

import java.util.function.Supplier;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ArsenicRenderer implements Renderer {

    public static final ArsenicRenderer INSTANCE = new ArsenicRenderer();

    @Override
    public BakedModelRenderer bakedModelRenderer() {
        return bakedModelRenderer.get();
    }

    @Override
    public StateManager stateManager() {
        return stateManager.get();
    }

    private final Supplier<BakedModelRenderer> bakedModelRenderer = Suppliers.memoize(BakedModelRendererImpl::new);
    private final Supplier<StateManager> stateManager = Suppliers.memoize(StateManagerImpl::new);
}
