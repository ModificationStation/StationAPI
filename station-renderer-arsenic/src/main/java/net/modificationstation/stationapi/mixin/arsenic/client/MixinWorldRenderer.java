package net.modificationstation.stationapi.mixin.arsenic.client;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_66;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.RenderList;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.entity.Living;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.level.Level;
import net.minecraft.tileentity.TileEntityBase;
import net.minecraft.util.maths.MathHelper;
import net.modificationstation.stationapi.api.client.blaze3d.systems.RenderSystem;
import net.modificationstation.stationapi.api.client.gl.GlUniform;
import net.modificationstation.stationapi.api.client.gl.VertexBuffer;
import net.modificationstation.stationapi.api.client.render.BufferRenderer;
import net.modificationstation.stationapi.api.client.render.RenderLayer;
import net.modificationstation.stationapi.api.client.render.Shader;
import net.modificationstation.stationapi.api.client.render.VertexFormat;
import net.modificationstation.stationapi.api.util.math.Matrix3f;
import net.modificationstation.stationapi.api.util.math.Matrix4f;
import net.modificationstation.stationapi.api.util.math.MatrixStack;
import net.modificationstation.stationapi.api.util.math.Vec3f;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.render.ChunkBuilderVBO;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.nio.IntBuffer;
import java.util.List;

@Mixin(WorldRenderer.class)
public abstract class MixinWorldRenderer {

    @Shadow private Minecraft client;

    @Shadow private int field_1782;

    @Shadow private class_66[] field_1809;

    @Shadow private int field_1810;

    @Shadow private int field_1811;

    @Shadow private int field_1812;

    @Shadow private class_66[] field_1808;

    @Shadow private int field_1776;

    @Shadow private int field_1777;

    @Shadow private int field_1778;

    @Shadow private int field_1779;

    @Shadow private int field_1780;

    @Shadow private int field_1781;

    @Shadow private List<class_66> field_1807;

    @Shadow public List<TileEntityBase> field_1795;

    @Shadow private boolean field_1817;

    @Shadow private IntBuffer field_1816;

    @Shadow private Level level;

    @Shadow private int field_1813;

    @Shadow protected abstract void method_1553(int i, int j, int k);

    @Shadow private int field_1783;

    private static final MatrixStack PROJECTION_MATRICES = new MatrixStack();
    @Unique
    private static final MatrixStack MATRICES = new MatrixStack();

//    /**
//     * @reason early version
//     * @author mine_diver
//     */
//    @Overwrite
//    public void method_1537() {
//        Living living;
//        int n;
//        int n2;
//        BlockBase.LEAVES.method_991(this.client.options.fancyGraphics);
//        this.field_1782 = this.client.options.viewDistance;
//        if (this.field_1809 != null) {
//            for (n2 = 0; n2 < this.field_1809.length; ++n2) {
//                this.field_1809[n2].method_302();
//            }
//        }
//        if ((n2 = 64 << 3 - this.field_1782) > 400) {
//            n2 = 400;
//        }
//        this.field_1810 = n2 / 16 + 1;
//        this.field_1811 = 8;
//        this.field_1812 = n2 / 16 + 1;
//        this.field_1809 = new class_66[this.field_1810 * this.field_1811 * this.field_1812];
//        this.field_1808 = new class_66[this.field_1810 * this.field_1811 * this.field_1812];
//        int n3 = 0;
//        int n4 = 0;
//        this.field_1776 = 0;
//        this.field_1777 = 0;
//        this.field_1778 = 0;
//        this.field_1779 = this.field_1810;
//        this.field_1780 = this.field_1811;
//        this.field_1781 = this.field_1812;
//        for (n = 0; n < this.field_1807.size(); ++n) {
//            this.field_1807.get(n).field_249 = false;
//        }
//        this.field_1807.clear();
//        this.field_1795.clear();
//        for (n = 0; n < this.field_1810; ++n) {
//            for (int i = 0; i < this.field_1811; ++i) {
//                for (int j = 0; j < this.field_1812; ++j) {
//                    this.field_1809[(j * this.field_1811 + i) * this.field_1810 + n] = new class_66(this.level, this.field_1795, n * 16, i * 16, j * 16, 16, this.field_1813 + n3);
//                    if (this.field_1817) {
//                        this.field_1809[(j * this.field_1811 + i) * this.field_1810 + n].field_254 = this.field_1816.get(n4);
//                    }
//                    this.field_1809[(j * this.field_1811 + i) * this.field_1810 + n].field_253 = false;
//                    this.field_1809[(j * this.field_1811 + i) * this.field_1810 + n].field_252 = true;
//                    this.field_1809[(j * this.field_1811 + i) * this.field_1810 + n].field_243 = true;
//                    this.field_1809[(j * this.field_1811 + i) * this.field_1810 + n].field_251 = n4++;
//                    this.field_1809[(j * this.field_1811 + i) * this.field_1810 + n].method_305();
//                    this.field_1808[(j * this.field_1811 + i) * this.field_1810 + n] = this.field_1809[(j * this.field_1811 + i) * this.field_1810 + n];
//                    this.field_1807.add(this.field_1809[(j * this.field_1811 + i) * this.field_1810 + n]);
//                    n3 += 3;
//                }
//            }
//        }
//        if (this.level != null && (living = this.client.viewEntity) != null) {
//            this.method_1553(MathHelper.floor(living.x), MathHelper.floor(living.y), MathHelper.floor(living.z));
//            Function<Living, Comparator<class_66>> notchCodeEvasion = ComparatorReverseEntityThing::new;
//            Arrays.sort(this.field_1808, notchCodeEvasion.apply(living));
//        }
//        this.field_1783 = 2;
//    }

    @Shadow private List<class_66> field_1793;

    @Shadow private int field_1787;

    @Shadow private int field_1791;

    @Shadow private int field_1788;

    @Shadow private int field_1789;

    @Shadow private int field_1790;

    @Shadow private RenderList[] field_1794;

    /**
     * @reason early version
     * @author mine_diver
     */
    @Overwrite
    private int method_1542(int i, int j, int k, double d) {
        int n;
        this.field_1793.clear();
        int n2 = 0;
        for (int i2 = i; i2 < j; ++i2) {
            if (k == 0) {
                ++this.field_1787;
                if (this.field_1808[i2].field_244[k]) {
                    ++this.field_1791;
                } else if (!this.field_1808[i2].field_243) {
                    ++this.field_1788;
                } else if (this.field_1817 && !this.field_1808[i2].field_252) {
                    ++this.field_1789;
                } else {
                    ++this.field_1790;
                }
            }
            if (this.field_1808[i2].field_244[k] || !this.field_1808[i2].field_243 || this.field_1817 && !this.field_1808[i2].field_252 || this.field_1808[i2].method_297(k) < 0) continue;
            this.field_1793.add(this.field_1808[i2]);
            ++n2;
        }
        PlayerBase living = (PlayerBase) this.client.viewEntity;
        double d2 = living.prevRenderX + (living.x - living.prevRenderX) * d;
        double d3 = living.prevRenderY + (living.y - living.prevRenderY) * d;
        double d4 = living.prevRenderZ + (living.z - living.prevRenderZ) * d;
        int n4 = 0;
        for (n = 0; n < this.field_1794.length; ++n) {
            this.field_1794[n].method_1913();
        }
        PROJECTION_MATRICES.push();
        Matrix4f projectionMatrix = PROJECTION_MATRICES.peek().getPositionMatrix();
        projectionMatrix.multiply(getBasicProjectionMatrix(70));

        // Bob view when hurt
        {
            float f2;
            float f3 = (float) ((float) living.hurtTime - d);
            if (living.health <= 0) {
                f2 = (float) ((float) living.deathTime + d);
                projectionMatrix.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(40.0f - 8000.0f / (f2 + 200.0f)));
            }
            if (f3 >= 0.0f) {
                f3 /= (float) living.field_1039;
                f3 = MathHelper.sin(f3 * f3 * f3 * f3 * (float) Math.PI);
                f2 = living.field_1040;
                projectionMatrix.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-f2));
                projectionMatrix.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(-f3 * 14.0f));
                projectionMatrix.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(f2));
            }
        }

        // Bob view
        {
            float f2 = living.field_1635 - living.field_1634;
            float f3 = (float) -(living.field_1635 + f2 * d);
            float f4 = (float) (living.field_524 + (living.field_525 - living.field_524) * d);
            float f5 = (float) (living.field_1043 + (living.field_1044 - living.field_1043) * d);
            projectionMatrix.multiply(Matrix4f.translateTmp(MathHelper.sin(f3 * (float) Math.PI) * f4 * 0.5f, -Math.abs(MathHelper.cos(f3 * (float) Math.PI) * f4), 0.0f));
            projectionMatrix.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(MathHelper.sin(f3 * (float) Math.PI) * f4 * 3.0f));
            projectionMatrix.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(Math.abs(MathHelper.cos(f3 * (float) Math.PI - 0.2f) * f4) * 5.0f));
            projectionMatrix.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(f5));
            RenderSystem.setProjectionMatrix(projectionMatrix);
        }

        MATRICES.push();
        //noinspection deprecation
        Living camera = ((Minecraft) FabricLoader.getInstance().getGameInstance()).viewEntity;
        MATRICES.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(camera.pitch));
        MATRICES.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(camera.yaw + 180.0f));
        Matrix3f matrix3f = MATRICES.peek().getNormalMatrix().copy();
        if (matrix3f.invert()) {
            RenderSystem.setInverseViewRotationMatrix(matrix3f);
        }
        RenderSystem.assertOnRenderThread();
        RenderLayer layer = k == 0 ? RenderLayer.getSolid() : RenderLayer.getTranslucent();
        layer.startDrawing();
        VertexFormat format = layer.getVertexFormat();
        Shader shader = RenderSystem.getShader();
        BufferRenderer.unbindAll();

        for(int shaderTex = 0; shaderTex < 12; ++shaderTex) {
            int l = RenderSystem.getShaderTexture(shaderTex);
            shader.addSampler("Sampler" + shaderTex, l);
        }

        if (shader.modelViewMat != null) {
            shader.modelViewMat.set(MATRICES.peek().getPositionMatrix());
        }

        if (shader.projectionMat != null) {
            shader.projectionMat.set(projectionMatrix);
        }

        if (shader.colorModulator != null) {
            shader.colorModulator.set(RenderSystem.getShaderColor());
        }

        if (shader.fogStart != null) {
            shader.fogStart.set(RenderSystem.getShaderFogStart());
        }

        if (shader.fogEnd != null) {
            shader.fogEnd.set(RenderSystem.getShaderFogEnd());
        }

        if (shader.fogColor != null) {
            shader.fogColor.set(RenderSystem.getShaderFogColor());
        }

        if (shader.fogShape != null) {
            shader.fogShape.set(RenderSystem.getShaderFogShape().getId());
        }

        if (shader.textureMat != null) {
            shader.textureMat.set(RenderSystem.getTextureMatrix());
        }

        if (shader.gameTime != null) {
            shader.gameTime.set(RenderSystem.getShaderGameTime());
        }

        RenderSystem.setupShaderLights(shader);
        shader.bind();
        GlUniform glUniform = shader.chunkOffset;

        boolean rendered = false;
        for (n = 0; n < this.field_1793.size(); ++n) {
            class_66 class_662 = this.field_1793.get(n);
            int n5 = -1;
            for (int i3 = 0; i3 < n4; ++i3) {
                if (!this.field_1794[i3].method_1911(class_662.field_237, class_662.field_238, class_662.field_239)) continue;
                n5 = i3;
            }
            if (n5 < 0) {
                n5 = n4++;
                this.field_1794[n5].method_1912(class_662.field_237, class_662.field_238, class_662.field_239, d2, d3, d4);
            }
            RenderListAccessor renderList = (RenderListAccessor) this.field_1794[n5];
//            GL11.glPushMatrix();
//            GL11.glTranslated(renderList.stationapi$getField_2480() - renderList.stationapi$getField_2483(), renderList.stationapi$getField_2481() - renderList.stationapi$getField_2484(), renderList.stationapi$getField_2482() - renderList.stationapi$getField_2485());
            VertexBuffer vbo = ((ChunkBuilderVBO) class_662).getBuffers().get(layer);
//            BlockPos blockPos = builtChunk.getOrigin();
            if (glUniform != null) {
                glUniform.set(renderList.stationapi$getField_2480() - renderList.stationapi$getField_2483(), renderList.stationapi$getField_2481() - renderList.stationapi$getField_2484(), renderList.stationapi$getField_2482() - renderList.stationapi$getField_2485());
                glUniform.upload();
            }
            vbo.drawVertices();
//            GL11.glPopMatrix();
            rendered = true;
//            this.field_1794[n5].method_1910(class_662.method_297(k));
        }

        if (glUniform != null) {
            glUniform.set(Vec3f.ZERO);
        }

        shader.unbind();

        if (rendered) {
            format.endDrawing();
        }
        VertexBuffer.unbind();
        VertexBuffer.unbindVertexArray();
        layer.endDrawing();
        this.method_1540(k, d);
        GL11.glEnable(2929);
        MATRICES.pop();
        PROJECTION_MATRICES.pop();
        return n2;
    }

    /**
     * @reason early version
     * @author mine_diver
     */
    @Overwrite
    public void method_1540(int i, double d) {

    }

    @Unique
    public Matrix4f getBasicProjectionMatrix(double fov) {
        MatrixStack matrixStack = new MatrixStack();
        matrixStack.peek().getPositionMatrix().loadIdentity();
//        if (this.zoom != 1.0f) {
//            matrixStack.translate(this.zoomX, -this.zoomY, 0.0);
//            matrixStack.scale(this.zoom, this.zoom, 1.0f);
//        }
        matrixStack.peek().getPositionMatrix().multiply(Matrix4f.viewboxMatrix(fov, (float) Display.getWidth() / (float)Display.getHeight(), 0.05f, this.method_32796()));
        return matrixStack.peek().getPositionMatrix();
    }

    @Unique
    public float method_32796() {
        return (256 >> client.options.viewDistance) * 2;
    }
}
