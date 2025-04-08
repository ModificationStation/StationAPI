package net.modificationstation.stationapi.impl.client.arsenic.renderer.render;

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
import net.modificationstation.stationapi.impl.util.math.ChunkSectionPos;
import org.lwjgl.opengl.GL11;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Random;
import java.util.function.IntFunction;

/**
 * Used during section block buffering to invoke {@link BakedModel#emitBlockQuads}.
 */
public class TerrainRenderContext extends AbstractTerrainRenderContext {
    public static final ThreadLocal<TerrainRenderContext> POOL = ThreadLocal.withInitial(TerrainRenderContext::new);

    private MatrixStack matrixStack = new MatrixStack();
    private Random random;
    private IntFunction<VertexConsumer> bufferFunc;

    @Override
    protected VertexConsumer getVertexConsumer(int layer) {
        return bufferFunc.apply(layer);
    }

    public void prepare(BlockView blockView, Random random, IntFunction<VertexConsumer> bufferFunc) {
        blockInfo.prepareForWorld(blockView, true);
        inputContext.prepare(false, blockInfo.blockState, blockInfo.blockPos, blockView, matrixStack, random);

        this.random = random;
        this.bufferFunc = bufferFunc;

        // Retrieve OpenGL's model matrix and set it to the MatrixStack
        FloatBuffer openglMatrix = ByteBuffer.allocateDirect(Float.BYTES * 16).asFloatBuffer();
        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, openglMatrix);

        // Convert the OpenGL matrix to Matrix4f and apply it to the MatrixStack
        matrixStack.peek().getPositionMatrix().set(openglMatrix);
    }

    public void release() {
        matrices = null;
        random = null;
        bufferFunc = null;

        blockInfo.release();
        inputContext.release();
    }

    /** Called from section builder hook. */
    public void bufferModel(BakedModel model, BlockState blockState, BlockPos blockPos) {
        matrixStack.push();

        try {
            matrixStack.translate(ChunkSectionPos.getLocalCoord(blockPos.getX()), ChunkSectionPos.getLocalCoord(blockPos.getY()), ChunkSectionPos.getLocalCoord(blockPos.getZ()));
            if (blockState.hasModelOffset()) {
                Vec3d offset = blockState.getModelOffset(blockPos);
                matrixStack.translate(offset.x, offset.y, offset.z);
            }
            matrices = matrixStack.peek();

            random.setSeed(blockState.getRenderingSeed(blockPos));

            prepare(blockPos, blockState);
            model.emitBlockQuads(inputContext, getEmitter());
        } catch (Throwable throwable) {
            CrashReport crashReport = CrashReport.create(throwable, "Tessellating block in world - Indigo Renderer");
            CrashReportSection crashReportSection = crashReport.addElement("Block being tessellated");
            if (blockState != null) {
                crashReportSection.add("Block", blockState::toString);
            }

            crashReportSection.add("Block location", (() -> CrashReportSection.createPositionString(blockInfo.blockView, blockPos)));
            throw new CrashException(crashReport);
        } finally {
            matrixStack.pop();
        }
    }
}
