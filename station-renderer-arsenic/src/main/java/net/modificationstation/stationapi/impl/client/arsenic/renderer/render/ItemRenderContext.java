package net.modificationstation.stationapi.impl.client.arsenic.renderer.render;

import net.modificationstation.stationapi.impl.client.arsenic.renderer.render.mesh.MutableQuadViewImpl;

public class ItemRenderContext extends AbstractRenderContext {
    public static final ThreadLocal<ItemRenderContext> POOL = ThreadLocal.withInitial(ItemRenderContext::new);

    @Override
    protected void bufferQuad(MutableQuadViewImpl quad) {

    }
}
