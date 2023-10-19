package net.modificationstation.stationapi.impl.client.arsenic.renderer.render;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.modificationstation.stationapi.api.client.render.RenderContext;
import net.modificationstation.stationapi.api.client.render.mesh.MutableQuadView;
import net.modificationstation.stationapi.api.util.math.Matrix3f;
import net.modificationstation.stationapi.api.util.math.Matrix4f;

abstract class AbstractRenderContext implements RenderContext {
    private final ObjectArrayList<QuadTransform> transformStack = new ObjectArrayList<>();
    private static final RenderContext.QuadTransform NO_TRANSFORM = (q) -> true;
    protected Matrix4f matrix;
    protected Matrix3f normalMatrix;
    protected int overlay;

    private final QuadTransform stackTransform = (q) -> {
        int i = transformStack.size() - 1;

        while (i >= 0) {
            if (!transformStack.get(i--).transform(q)) {
                return false;
            }
        }

        return true;
    };

    private QuadTransform activeTransform = NO_TRANSFORM;

    protected final boolean transform(MutableQuadView q) {
        return activeTransform.transform(q);
    }

    protected boolean hasTransform() {
        return activeTransform != NO_TRANSFORM;
    }

    @Override
    public void pushTransform(QuadTransform transform) {
        if (transform == null) {
            throw new NullPointerException("Renderer received null QuadTransform.");
        }

        transformStack.push(transform);

        if (transformStack.size() == 1) {
            activeTransform = transform;
        } else if (transformStack.size() == 2) {
            activeTransform = stackTransform;
        }
    }

    @Override
    public void popTransform() {
        transformStack.pop();

        if (transformStack.size() == 0) {
            activeTransform = NO_TRANSFORM;
        } else if (transformStack.size() == 1) {
            activeTransform = transformStack.get(0);
        }
    }
}