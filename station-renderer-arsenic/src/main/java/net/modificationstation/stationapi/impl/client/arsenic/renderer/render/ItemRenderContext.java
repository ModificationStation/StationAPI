package net.modificationstation.stationapi.impl.client.arsenic.renderer.render;

import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.client.colour.item.ItemColors;
import net.modificationstation.stationapi.api.client.render.VertexConsumer;
import net.modificationstation.stationapi.api.client.render.material.BlendMode;
import net.modificationstation.stationapi.api.client.render.mesh.Mesh;
import net.modificationstation.stationapi.api.client.render.mesh.QuadEmitter;
import net.modificationstation.stationapi.api.client.render.model.BakedModel;
import net.modificationstation.stationapi.api.client.render.model.BakedQuad;
import net.modificationstation.stationapi.api.client.render.model.ModelHelper;
import net.modificationstation.stationapi.api.client.render.model.json.ModelTransformation;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.util.math.Matrix4f;
import net.modificationstation.stationapi.api.util.math.MatrixStack;
import net.modificationstation.stationapi.api.util.math.Vec3f;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.ArsenicRenderer;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.RenderMaterialImpl;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.helper.ColourHelper;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.mesh.EncodingFormat;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.mesh.MeshImpl;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.mesh.MutableQuadViewImpl;

import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ItemRenderContext extends AbstractRenderContext {

    /** Value vanilla uses for item rendering.  The only sensible choice, of course.  */
    private static final long ITEM_RANDOM_SEED = 42L;

    /** used to accept a method reference from the ItemRenderer. */
    @FunctionalInterface
    public interface VanillaQuadHandler {
        void accept(BakedModel model, ItemInstance stack, int color, int overlay, MatrixStack matrixStack, VertexConsumer buffer);
    }

    private final ItemColors colorMap;
    private final Random random = new Random();
    private final Consumer<BakedModel> fallbackConsumer;
    private final Vec3f normalVec = new Vec3f();

    private MatrixStack matrixStack;
    private Matrix4f matrix;
//    private VertexConsumer vertexConsumerProvider;
    private VertexConsumer modelVertexConsumer;
    private BlendMode quadBlendMode;
    private VertexConsumer quadVertexConsumer;
    private ModelTransformation.Mode transformMode;
    private int lightmap;
    private int overlay;
    private ItemInstance itemStack;
    private VanillaQuadHandler vanillaHandler;

    private final Supplier<Random> randomSupplier = () -> {
        random.setSeed(ITEM_RANDOM_SEED);
        return random;
    };

    private final int[] quadData = new int[EncodingFormat.TOTAL_STRIDE];

    public ItemRenderContext(ItemColors colorMap) {
        this.colorMap = colorMap;
        fallbackConsumer = this::fallbackConsumer;
    }

    public void renderModel(ItemInstance itemStack, ModelTransformation.Mode transformMode, boolean invert, MatrixStack matrixStack, VertexConsumer vertexConsumer, int lightmap, int overlay, BakedModel model, VanillaQuadHandler vanillaHandler) {
        this.lightmap = lightmap;
        this.overlay = overlay;
        this.itemStack = itemStack;
//        this.vertexConsumerProvider = vertexConsumerProvider;
        this.matrixStack = matrixStack;
        this.transformMode = transformMode;
        this.vanillaHandler = vanillaHandler;
        quadBlendMode = BlendMode.DEFAULT;
//        modelVertexConsumer = selectVertexConsumer(RenderLayers.getItemLayer(itemStack, transformMode != ModelTransformation.Mode.GROUND));
        modelVertexConsumer = vertexConsumer;

        matrixStack.push();
        model.getTransformation().getTransformation(transformMode).apply(invert, matrixStack);
        matrixStack.translate(-0.5D, -0.5D, -0.5D);
        matrix = matrixStack.peek().getPositionMatrix();
        normalMatrix = matrixStack.peek().getNormalMatrix();

        model.emitItemQuads(itemStack, randomSupplier, this);

        matrixStack.pop();

        this.matrixStack = null;
        this.itemStack = null;
        this.vanillaHandler = null;
        modelVertexConsumer = null;
    }

    /**
     * Use non-culling translucent material in GUI to match vanilla behavior. If the item
     * is enchanted then also select a dual-output vertex consumer. For models with layered
     * coplanar polygons this means we will render the glint more than once. Indigo doesn't
     * support sprite layers, so this can't be helped in this implementation.
     */
//    private VertexConsumer selectVertexConsumer(RenderLayer layerIn) {
//        final RenderLayer layer = transformMode == ModelTransformation.Mode.GUI ? TexturedRenderLayers.getEntityTranslucentCull() : layerIn;
//        return ItemRenderer.getArmorGlintConsumer(vertexConsumerProvider, layer, true, itemStack.hasGlint());
//    }

    private class Maker extends MutableQuadViewImpl implements QuadEmitter {
        {
            data = quadData;
            clear();
        }

        @Override
        public Maker emit() {
            computeGeometry();
            renderQuad();
            clear();
            return this;
        }
    }

    private final Maker editorQuad = new Maker();

    private final Consumer<Mesh> meshConsumer = (mesh) -> {
        final MeshImpl m = (MeshImpl) mesh;
        final int[] data = m.data();
        final int limit = data.length;
        int index = 0;

        while (index < limit) {
            System.arraycopy(data, index, editorQuad.data(), 0, EncodingFormat.TOTAL_STRIDE);
            editorQuad.load();
            index += EncodingFormat.TOTAL_STRIDE;
            renderQuad();
        }
    };

    private int indexColor() {
        final int colorIndex = editorQuad.colorIndex();
        return colorIndex == -1 ? -1 : (colorMap.getColor(itemStack, colorIndex) | 0xFF000000);
    }

    private void renderQuad() {
        final MutableQuadViewImpl quad = editorQuad;

        if (!transform(editorQuad)) {
            return;
        }

        final RenderMaterialImpl.Value mat = quad.material();
        final int quadColor = mat.disableColorIndex(0) ? -1 : indexColor();
        final int lightmap = mat.emissive(0) ? AbstractQuadRenderer.FULL_BRIGHTNESS : this.lightmap;

        for (int i = 0; i < 4; i++) {
            int c = quad.spriteColour(i, 0);
            c = ColourHelper.multiplyColour(quadColor, c);
            quad.spriteColour(i, 0, ColourHelper.swapRedBlueIfNeeded(c));
            quad.lightmap(i, ColourHelper.maxBrightness(quad.lightmap(i), lightmap));
        }

        AbstractQuadRenderer.bufferQuad(quadVertexConsumer(mat.blendMode(0)), quad, matrix, overlay, normalMatrix, normalVec);
    }

    private VertexConsumer quadVertexConsumer(BlendMode blendMode) {
        return quadVertexConsumer = modelVertexConsumer;
    }

    @Override
    public Consumer<Mesh> meshConsumer() {
        return meshConsumer;
    }

    private void fallbackConsumer(BakedModel model) {
        if (hasTransform()) {
            // if there's a transform in effect, convert to mesh-based quads so that we can apply it
            for (int i = 0; i <= ModelHelper.NULL_FACE_ID; i++) {
                random.setSeed(ITEM_RANDOM_SEED);
                final Direction cullFace = ModelHelper.faceFromIndex(i);
                renderFallbackWithTransform(model.getQuads(null, cullFace, random), cullFace);
            }
        } else {
            for (int i = 0; i <= ModelHelper.NULL_FACE_ID; i++) {
                vanillaHandler.accept(model, itemStack, lightmap, overlay, matrixStack, modelVertexConsumer);
            }
        }
    }

    private void renderFallbackWithTransform(List<BakedQuad> quads, Direction cullFace) {
        if (quads.isEmpty()) {
            return;
        }

        final Maker editorQuad = this.editorQuad;

        for (final BakedQuad q : quads) {
            editorQuad.fromVanilla(q, ArsenicRenderer.MATERIAL_STANDARD, cullFace);
            renderQuad();
        }
    }

    @Override
    public Consumer<BakedModel> fallbackConsumer() {
        return fallbackConsumer;
    }

    @Override
    public QuadEmitter getEmitter() {
        editorQuad.clear();
        return editorQuad;
    }
}
