package net.modificationstation.stationapi.impl.client.arsenic.renderer.render;

import net.minecraft.client.Minecraft;
import net.minecraft.level.BlockView;
import net.minecraft.util.maths.TilePos;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.client.render.mesh.Mesh;
import net.modificationstation.stationapi.api.client.render.mesh.QuadEmitter;
import net.modificationstation.stationapi.api.client.render.model.BakedModel;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.aocalc.LightingCalculatorImpl;

import java.util.*;
import java.util.function.*;

public class BlockRenderContext extends AbstractRenderContext {

    private final BlockRenderInfo blockInfo = new BlockRenderInfo();
    private final LightingCalculatorImpl aoCalc = new LightingCalculatorImpl(3);
    private final MeshConsumer meshConsumer = new MeshConsumer(blockInfo, aoCalc, this::transform);
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

    public boolean render(BlockView blockView, BakedModel model, BlockState state, TilePos pos, Random random, long seed) {
        this.random = random;
        this.seed = seed;
        this.didOutput = false;
        aoCalc.initialize(state.getBlock(), blockView, pos.x, pos.y, pos.z, Minecraft.isSmoothLightingEnabled() && model.useAmbientOcclusion());

        model.emitBlockQuads(blockView, state, pos, randomSupplier, this);

        return didOutput;
    }

    private static class MeshConsumer extends AbstractMeshConsumer {
        MeshConsumer(BlockRenderInfo blockInfo, LightingCalculatorImpl aoCalc, QuadTransform transform) {
            super(blockInfo, aoCalc, transform);
        }
    }
}
