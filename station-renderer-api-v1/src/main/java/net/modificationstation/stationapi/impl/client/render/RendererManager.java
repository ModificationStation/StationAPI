package net.modificationstation.stationapi.impl.client.render;

import net.modificationstation.stationapi.api.client.render.Renderer;

public class RendererManager {
    private static Renderer activeRenderer;

    public static void registerRenderer(Renderer renderer) {
        if (renderer == null) {
            throw new NullPointerException("Attempt to register a NULL rendering plug-in.");
        } else if (activeRenderer != null) {
            throw new UnsupportedOperationException("A second rendering plug-in attempted to register. Multiple rendering plug-ins are not supported.");
        } else {
            activeRenderer = renderer;
        }
    }

    public static Renderer getRenderer() {
        if (activeRenderer == null) {
            throw new UnsupportedOperationException("Attempted to retrieve active rendering plug-in before one was registered.");
        }

        return activeRenderer;
    }
}
