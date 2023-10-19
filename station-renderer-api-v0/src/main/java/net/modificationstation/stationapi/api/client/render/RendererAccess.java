package net.modificationstation.stationapi.api.client.render;

import net.modificationstation.stationapi.impl.client.render.RendererAccessImpl;

/**
 * Registration and access for rendering extensions.
 */
public interface RendererAccess {

    RendererAccess INSTANCE = RendererAccessImpl.INSTANCE;

    /**
     * Rendering extension mods must implement {@link Renderer} and
     * call this method during initialization.
     *
     * <p>Only one {@link Renderer} plug-in can be active in any game instance.
     * If a second mod attempts to register this method will throw an UnsupportedOperationException.
     */
    void registerRenderer(Renderer plugin);

    /**
     * Access to the current {@link Renderer} for creating and retrieving model builders
     * and materials. Will return null if no render plug in is active.
     */
    // @Nullable
    Renderer getRenderer();

    /**
     * Performant test for {@link #getRenderer()} != null.
     */
    boolean hasRenderer();
}