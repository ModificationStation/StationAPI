package net.modificationstation.stationapi.impl.client.arsenic.renderer.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.Tessellator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.client.render.mesh.Mesh;
import net.modificationstation.stationapi.api.client.render.mesh.QuadEmitter;
import net.modificationstation.stationapi.api.client.render.model.BakedModel;
import net.modificationstation.stationapi.api.util.math.Matrix3f;
import net.modificationstation.stationapi.api.util.math.Matrix4f;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.aocalc.LightingCalculatorImpl;

import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class BlockRenderContext extends AbstractRenderContext {

    private final BlockRenderInfo blockInfo = new BlockRenderInfo();
    private final LightingCalculatorImpl aoCalc = new LightingCalculatorImpl(3);
    private final MeshConsumer meshConsumer = new MeshConsumer(blockInfo, this::outputBuffer, aoCalc, this::transform);
    private Tessellator bufferBuilder;
    private boolean didOutput;
    private Random random;
    private long seed;
    private final Supplier<Random> randomSupplier = () -> {
        random.setSeed(seed);
        return random;
    };

    @Override
    public Consumer<Mesh> meshConsumer() {
        return meshConsumer;
    }

    @Override
    public Consumer<BakedModel> fallbackConsumer() {
        return null;
    }

    @Override
    public QuadEmitter getEmitter() {
        return meshConsumer.getEmitter();
    }

    private Tessellator outputBuffer() {
        didOutput = true;
        return bufferBuilder;
    }

    public boolean render(BlockView blockView, BakedModel model, BlockState state, BlockPos pos, Random random, long seed) {
        this.bufferBuilder = Tessellator.INSTANCE;
//        this.matrix = matrixStack.peek().getPositionMatrix();
//        this.normalMatrix = matrixStack.peek().getNormalMatrix();
        this.random = random;
        this.seed = seed;

//        this.overlay = overlay;
        this.didOutput = false;
        aoCalc.initialize(state.getBlock(), blockView, pos.x, pos.y, pos.z, Minecraft.method_2148() && model.useAmbientOcclusion());
        blockInfo.setBlockView(blockView);
        blockInfo.prepareForBlock(state, pos, model.useAmbientOcclusion());

        model.emitBlockQuads(blockView, state, pos, randomSupplier, this);

        blockInfo.release();
        this.bufferBuilder = null;
        this.random = null;
        this.seed = seed;

        return didOutput;
    }

    private class MeshConsumer extends AbstractMeshConsumer {

        MeshConsumer(BlockRenderInfo blockInfo, Supplier<Tessellator> bufferFunc, LightingCalculatorImpl aoCalc, QuadTransform transform) {
            super(blockInfo, bufferFunc, aoCalc, transform);
        }

        @Override
        protected Matrix4f matrix() {
            return matrix;
        }

        @Override
        protected Matrix3f normalMatrix() {
            return normalMatrix;
        }

        @Override
        protected int overlay() {
            return overlay;
        }
    }
}
