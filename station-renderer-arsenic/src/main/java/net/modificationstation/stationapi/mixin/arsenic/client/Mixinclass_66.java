package net.modificationstation.stationapi.mixin.arsenic.client;

import net.minecraft.block.BlockBase;
import net.minecraft.class_66;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.entity.TileEntityRenderDispatcher;
import net.minecraft.level.Level;
import net.minecraft.level.WorldPopulationRegion;
import net.minecraft.level.chunk.Chunk;
import net.minecraft.tileentity.TileEntityBase;
import net.minecraft.util.maths.Box;
import net.minecraft.util.maths.TilePos;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.client.gl.VertexBuffer;
import net.modificationstation.stationapi.api.client.render.*;
import net.modificationstation.stationapi.api.client.render.chunk.BlockBufferBuilderStorage;
import net.modificationstation.stationapi.api.client.render.model.BakedModelRenderer;
import net.modificationstation.stationapi.api.level.BlockStateView;
import net.modificationstation.stationapi.api.util.math.MatrixStack;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.render.ChunkBuilderVBO;
import net.modificationstation.stationapi.mixin.arsenic.TilePosAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.*;
import java.util.stream.Collectors;

@Mixin(class_66.class)
public abstract class Mixinclass_66 implements ChunkBuilderVBO {

    @Shadow public boolean field_249;

    @Shadow public int field_231;

    @Shadow public int field_232;

    @Shadow public int field_233;

    @Shadow public int field_234;

    @Shadow public int field_235;

    @Shadow public int field_236;

    @Shadow public boolean[] field_244;

    @Shadow public List<TileEntityBase> field_224;

    @Shadow public Level level;

    @Shadow private int field_225;

    @Shadow protected abstract void method_306();

    @Shadow private static Tessellator tesselator;

    @Shadow private List<TileEntityBase> field_228;

    @Shadow public boolean field_223;

    @Shadow private boolean field_227;

    @Shadow public abstract void method_301();

    @Shadow public int field_245;
    @Shadow public int field_246;
    @Shadow public int field_247;
    @Shadow public int field_240;
    @Shadow public int field_241;
    @Shadow public int field_242;
    @Shadow public int field_237;
    @Shadow public int field_238;
    @Shadow public int field_239;
    @Shadow public Box field_250;

    @Shadow public abstract void method_305();

    private static final BufferBuilderStorage bufferStorage = new BufferBuilderStorage();
    private static final TilePos pos = new TilePos(0, 0, 0);
    private static final TilePosAccessor posA = (TilePosAccessor) pos;
    private static final MatrixStack matrices = new MatrixStack();
    private static final Random random = new Random();
//    private final Queue<Runnable> uploadQueue = new ConcurrentLinkedQueue<>();

    private Map<RenderLayer, VertexBuffer> buffers;

    /**
     * @reason early version
     * @author mine_diver
     */
    @Overwrite
    public void method_298(int i, int j, int k) {
        if (i != this.field_231 || j != this.field_232 || k != this.field_233) {
            this.method_301();
            this.field_231 = i;
            this.field_232 = j;
            this.field_233 = k;
            this.field_245 = i + this.field_234 / 2;
            this.field_246 = j + this.field_235 / 2;
            this.field_247 = k + this.field_236 / 2;
            this.field_240 = i & 1023;
            this.field_241 = j;
            this.field_242 = k & 1023;
            this.field_237 = i - this.field_240;
            this.field_238 = 0;
            this.field_239 = k - this.field_242;
            float var4 = 6.0F;
            this.field_250 = Box.create((float)i - var4, (float)j - var4, (float)k - var4, (float)(i + this.field_234) + var4, (float)(j + this.field_235) + var4, (float)(k + this.field_236) + var4);
//            GL11.glNewList(this.field_225 + 2, 4864);
//            ItemRenderer.method_2024(Box.createButWasteMemory((float)this.field_240 - var4, (float)this.field_241 - var4, (float)this.field_242 - var4, (float)(this.field_240 + this.field_234) + var4, (float)(this.field_241 + this.field_235) + var4, (float)(this.field_242 + this.field_236) + var4));
//            GL11.glEndList();
            this.buffers = RenderLayer.getBlockLayers().stream().collect(Collectors.toMap(renderLayer -> renderLayer, renderLayer -> new VertexBuffer(RenderRegion.VBO_POOL)));
            this.method_305();
        }
    }

    /**
     * @reason early version
     * @author mine_diver
     */
    @Overwrite
    public void method_296() {
        if (!this.field_249) {
            return;
        }
        ++class_66.field_230;
        int n = this.field_231;
        int n2 = this.field_232;
        int n3 = this.field_233;
        int n4 = this.field_231 + this.field_234;
        int n5 = this.field_232 + this.field_235;
        int n6 = this.field_233 + this.field_236;
        for (int i = 0; i < 2; ++i) {
            this.field_244[i] = true;
        }
        Chunk.field_953 = false;
        HashSet<TileEntityBase> hashSet = new HashSet<>(this.field_224);
        this.field_224.clear();
        int n7 = 1;
        WorldPopulationRegion worldPopulationRegion = new WorldPopulationRegion(this.level, n - n7, n2 - n7, n3 - n7, n4 + n7, n5 + n7, n6 + n7);
//        BlockRenderer blockRenderer = new BlockRenderer(worldPopulationRegion);
        BakedModelRenderer bakedModelRenderer = Objects.requireNonNull(RendererAccess.INSTANCE.getRenderer()).bakedModelRenderer();
        BlockBufferBuilderStorage blockBuffers = bufferStorage.getBlockBufferBuilders();
//        List<CompletableFuture<Void>> layers = new ArrayList<>();
        for (int i = 0; i < 2; ++i) {
            boolean bl = false;
            boolean bl2 = false;
            boolean bl3 = false;
            RenderLayer renderLayer = i == 0 ? RenderLayer.getSolid() : RenderLayer.getTranslucent();
            BufferBuilder builder = blockBuffers.get(renderLayer);
            for (int j = n2; j < n5; ++j) {
                for (int k = n3; k < n6; ++k) {
                    for (int i2 = n; i2 < n4; ++i2) {
                        TileEntityBase tileEntityBase;
                        int n9 = worldPopulationRegion.getTileId(i2, j, k);
                        if (n9 <= 0) continue;
                        if (!bl3) {
                            bl3 = true;

//                            GL11.glNewList(this.field_225 + i, 4864);
//                            GL11.glPushMatrix();
//                            this.method_306();
//                            float f = 1.000001f;
//                            GL11.glTranslatef((float)(-this.field_236) / 2.0f, (float)(-this.field_235) / 2.0f, (float)(-this.field_236) / 2.0f);
//                            GL11.glScalef(f, f, f);
//                            GL11.glTranslatef((float)this.field_236 / 2.0f, (float)this.field_235 / 2.0f, (float)this.field_236 / 2.0f);
//                            tesselator.start();
//                            tesselator.setOffset(-this.field_231, -this.field_232, -this.field_233);

                            matrices.push();
                            matrices.translate(this.field_240, this.field_241, this.field_242);
                            matrices.translate((float)(-this.field_236) / 2.0f, (float)(-this.field_235) / 2.0f, (float)(-this.field_236) / 2.0f);
                            float f = 1.000001f;
                            matrices.scale(f, f, f);
                            matrices.translate((float)this.field_236 / 2.0f, (float)this.field_235 / 2.0f, (float)this.field_236 / 2.0f);
                            builder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR_NORMAL);
                            matrices.translate(-this.field_231, -this.field_232, -this.field_233);
                        }
                        if (i == 0 && BlockBase.HAS_TILE_ENTITY[n9] && TileEntityRenderDispatcher.INSTANCE.hasCustomRenderer(tileEntityBase = worldPopulationRegion.getTileEntity(i2, j, k))) {
                            this.field_224.add(tileEntityBase);
                        }
                        if (BlockBase.BY_ID[n9].getRenderPass() != i) {
                            bl = true;
                        } else {
                            BlockState blockState = ((BlockStateView) level).getBlockState(i2, j, k);
                            posA.stationapi$setX(i2);
                            posA.stationapi$setY(j);
                            posA.stationapi$setZ(k);
                            bl2 |= bakedModelRenderer.renderBlock(blockState, pos, level, matrices, builder, true, random);
                        }
                    }
                }
            }
            if (bl3) {
                builder.end();
                buffers.get(renderLayer).upload(builder);
                matrices.pop();
//                layers.add(scheduleUpload(blockBuffers.get(renderLayer), buffers.get(renderLayer)));

//                tesselator.draw();
//                GL11.glPopMatrix();
//                GL11.glEndList();
//                tesselator.setOffset(0.0, 0.0, 0.0);
            } else {
                bl2 = false;
            }
            if (bl2) {
                this.field_244[i] = false;
            }
            if (!bl) break;
        }
        HashSet<TileEntityBase> hashSet2 = new HashSet<>(this.field_224);
        hashSet2.removeAll(hashSet);
        this.field_228.addAll(hashSet2);
        this.field_224.forEach(hashSet::remove);
        this.field_228.removeAll(hashSet);
        this.field_223 = Chunk.field_953;
        this.field_227 = true;

//        Util.combine(layers).complete(new ArrayList<>());
//        upload();
    }

//    public void upload() {
//        Runnable runnable;
//        while ((runnable = this.uploadQueue.poll()) != null) {
//            runnable.run();
//        }
//    }
//
//    public CompletableFuture<Void> scheduleUpload(BufferBuilder buffer, VertexBuffer glBuffer) {
//        return CompletableFuture.runAsync(() -> {}, this.uploadQueue::add).thenCompose(void_ -> this.upload(buffer, glBuffer));
//    }
//
//    private CompletableFuture<Void> upload(BufferBuilder buffer, VertexBuffer glBuffer) {
//        return glBuffer.submitUpload(buffer);
//    }

    @Override
    public Map<RenderLayer, VertexBuffer> getBuffers() {
        return buffers;
    }
}
