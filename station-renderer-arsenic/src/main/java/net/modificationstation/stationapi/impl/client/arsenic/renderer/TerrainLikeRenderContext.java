package net.modificationstation.stationapi.impl.client.arsenic.renderer;

import net.minecraft.client.render.Tessellator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.client.render.VertexConsumer;
import net.modificationstation.stationapi.api.client.render.model.BakedModel;
import net.modificationstation.stationapi.api.util.crash.CrashException;
import net.modificationstation.stationapi.api.util.crash.CrashReport;
import net.modificationstation.stationapi.api.util.crash.CrashReportSection;
import net.modificationstation.stationapi.api.util.math.MatrixStack;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.render.AbstractTerrainRenderContext;

import java.util.Random;
import java.util.function.IntFunction;

public class TerrainLikeRenderContext extends AbstractTerrainRenderContext {
    public static final ThreadLocal<TerrainLikeRenderContext> POOL = ThreadLocal.withInitial(TerrainLikeRenderContext::new);

    private final Random random = new Random();

    private IntFunction<VertexConsumer> vertexConsumers = value -> Tessellator.INSTANCE;

    @Override
    protected VertexConsumer getVertexConsumer(int layer) {
        return this.vertexConsumers.apply(layer);
    }

    public void bufferModel(BlockView blockView, BakedModel model, BlockState state, BlockPos pos, MatrixStack matrixStack, IntFunction<VertexConsumer> vertexConsumers, boolean cull, long seed) {
        try {
            Vec3d offset = state.getModelOffset(pos);
            matrixStack.translate(offset.x, offset.y, offset.z);
            matrices = matrixStack.peek();

            this.vertexConsumers = vertexConsumers;

            blockInfo.prepareForWorld(blockView, cull);
            random.setSeed(seed);

            prepare(pos, state);
            inputContext.prepare(false, state, pos, blockView, matrixStack, random);
            model.emitBlockQuads(inputContext, getEmitter());
            inputContext.release();
        } catch (Throwable throwable) {
            CrashReport crashReport = CrashReport.create(throwable, "Tessellating block model - Indigo Renderer");
            CrashReportSection crashReportSection = crashReport.addElement("Block model being tessellated");
            if (state != null) {
                crashReportSection.add("Block", state::toString);
            }

            crashReportSection.add("Block location", (() -> CrashReportSection.createPositionString(blockInfo.blockView, pos)));
            throw new CrashException(crashReport);
        } finally {
            blockInfo.release();
            matrices = null;
            this.vertexConsumers = null;
        }
    }
}
