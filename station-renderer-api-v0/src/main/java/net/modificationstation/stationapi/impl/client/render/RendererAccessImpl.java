package net.modificationstation.stationapi.impl.client.render;

import net.modificationstation.stationapi.api.client.render.Renderer;
import net.modificationstation.stationapi.api.client.render.RendererAccess;

public final class RendererAccessImpl implements RendererAccess {

    public static final RendererAccessImpl INSTANCE = new RendererAccessImpl();

    // private constructor
    private RendererAccessImpl() { }

    @Override
    public void registerRenderer(Renderer renderer) {
        if (renderer == null) {
            throw new NullPointerException("Attempt to register a NULL rendering plug-in.");
        } else if (activeRenderer != null) {
            throw new UnsupportedOperationException("A second rendering plug-in attempted to register. Multiple rendering plug-ins are not supported.");
        } else {
            activeRenderer = renderer;
            hasActiveRenderer = true;
        }
    }

    private Renderer activeRenderer = null;

    /** avoids null test every call to {@link #hasRenderer()}. */
    private boolean hasActiveRenderer = false;

    @Override
    public Renderer getRenderer() {
        return activeRenderer;
    }

    @Override
    public boolean hasRenderer() {
        return hasActiveRenderer;
    }
}