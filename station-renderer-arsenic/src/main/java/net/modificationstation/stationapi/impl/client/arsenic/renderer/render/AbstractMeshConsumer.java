package net.modificationstation.stationapi.impl.client.arsenic.renderer.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.Tessellator;
import net.modificationstation.stationapi.api.client.render.RenderContext;
import net.modificationstation.stationapi.api.client.render.mesh.Mesh;
import net.modificationstation.stationapi.api.client.render.mesh.QuadEmitter;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.ArsenicRenderer;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.RenderMaterialImpl;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.aocalc.LightingCalculatorImpl;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.mesh.EncodingFormat;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.mesh.MeshImpl;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.mesh.MutableQuadViewImpl;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Consumer for pre-baked meshes.  Works by copying the mesh data to a
 * "editor" quad held in the instance, where all transformations are applied before buffering.
 */
public abstract class AbstractMeshConsumer extends AbstractQuadRenderer implements Consumer<Mesh> {
    protected AbstractMeshConsumer(BlockRenderInfo blockInfo, Supplier<Tessellator> bufferFunc, LightingCalculatorImpl aoCalc, RenderContext.QuadTransform transform) {
        super(blockInfo, bufferFunc, aoCalc, transform);
    }

    /**
     * Where we handle all pre-buffer coloring, lighting, transformation, etc.
     * Reused for all mesh quads. Fixed baking array sized to hold largest possible mesh quad.
     */
    private class Maker extends MutableQuadViewImpl implements QuadEmitter {
        {
            data = new int[EncodingFormat.TOTAL_STRIDE];
            material(ArsenicRenderer.MATERIAL_STANDARD);
        }

        // only used via RenderContext.getEmitter()
        @Override
        public Maker emit() {
            computeGeometry();
            renderQuad(this);
            clear();
            return this;
        }
    }

    private final Maker editorQuad = new Maker();

    @Override
    public void accept(Mesh mesh) {
        final MeshImpl m = (MeshImpl) mesh;
        final int[] data = m.data();
        final int limit = data.length;
        int index = 0;

        while (index < limit) {
            System.arraycopy(data, index, editorQuad.data(), 0, EncodingFormat.TOTAL_STRIDE);
            editorQuad.load();
            index += EncodingFormat.TOTAL_STRIDE;
            renderQuad(editorQuad);
        }
    }

    public QuadEmitter getEmitter() {
        editorQuad.clear();
        return editorQuad;
    }

    private void renderQuad(MutableQuadViewImpl q) {
        if (!transform.transform(editorQuad)) {
            return;
        }

        if (!blockInfo.shouldDrawQuad(q)) {
            return;
        }

        if (!blockInfo.shouldDrawFace(q.cullFace())) {
            return;
        }

        final RenderMaterialImpl.Value mat = q.material();

        if (!mat.disableAo(0) && Minecraft.isSmoothLightingEnabled()) {
            // needs to happen before offsets are applied
            aoCalc.calculateForQuad(q);
        }

        tesselateQuad(q, mat, 0);
    }

    /**
     * Determines color index and render layer, then routes to appropriate
     * tesselate routine based on material properties.
     */
    private void tesselateQuad(MutableQuadViewImpl quad, RenderMaterialImpl.Value mat, int textureIndex) {
        final int colorIndex = mat.disableColorIndex(textureIndex) ? -1 : quad.colorIndex();

        if (blockInfo.defaultAo && !mat.disableAo(textureIndex)) {
            if (mat.emissive(textureIndex)) {
                tessellateSmoothEmissive(quad, colorIndex);
            } else {
                tessellateSmooth(quad, colorIndex);
            }
        } else {
            if (mat.emissive(textureIndex)) {
                tessellateFlatEmissive(quad, colorIndex);
            } else {
                tessellateFlat(quad, colorIndex);
            }
        }
    }
}