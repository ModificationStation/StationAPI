package net.modificationstation.stationapi.mixin.arsenic.client;

import net.minecraft.block.BlockBase;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Living;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.sortme.GameRenderer;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.maths.MathHelper;
import net.modificationstation.stationapi.api.client.blaze3d.systems.RenderSystem;
import net.modificationstation.stationapi.api.util.math.Matrix4f;
import net.modificationstation.stationapi.api.util.math.MatrixStack;
import net.modificationstation.stationapi.api.util.math.Vec3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer {

    @Shadow private Minecraft minecraft;

    @Shadow private float field_2350;

    @Shadow private double field_2331;

    @Shadow private double field_2332;

    @Shadow private double field_2333;

    @Shadow protected abstract float method_1848(float f);

    @Shadow private int field_2351;

    @Shadow private boolean field_2330;

    @Shadow private float field_2329;

    @Shadow private float field_2328;

    @Shadow private float field_2360;

    @Shadow private float field_2359;

    @Shadow private float field_2362;

    @Shadow private float field_2361;

    @Shadow private float field_2364;

    @Shadow private float field_2363;

    /**
     * @author mine_diver
     */
    @Overwrite
    private void method_1840(float f, int i) {
        float f2;
        this.field_2350 = 256 >> this.minecraft.options.viewDistance;

        Matrix4f projection = RenderSystem.getProjectionMatrix();
        projection.loadIdentity();

        // DEPRECATED
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        // ----------

        float f3 = 0.07f;
        if (this.minecraft.options.anaglyph3d) {

            projection.multiply(Matrix4f.translateTmp((float)(-(i * 2 - 1)) * f3, 0.0f, 0.0f));

            // DEPRECATED
            GL11.glTranslatef((float)(-(i * 2 - 1)) * f3, 0.0f, 0.0f);
            // ----------
        }
        if (this.field_2331 != 1.0) {

            projection.multiply(Matrix4f.translateTmp((float)this.field_2332, (float)(-this.field_2333), 0.0f));
            projection.multiply(Matrix4f.scaleTmp((float) this.field_2331, (float) this.field_2331, 1.0F));

            // DEPRECATED
            GL11.glTranslatef((float)this.field_2332, (float)(-this.field_2333), 0.0f);
            GL11.glScaled(this.field_2331, this.field_2331, 1.0);
            // ----------
        }

        projection.multiply(Matrix4f.viewboxMatrix(this.method_1848(f), (float)this.minecraft.actualWidth / (float)this.minecraft.actualHeight, 0.05f, this.field_2350 * 2.0f));

        // DEPRECATED
        GLU.gluPerspective(this.method_1848(f), (float)this.minecraft.actualWidth / (float)this.minecraft.actualHeight, 0.05f, this.field_2350 * 2.0f);
        // ----------

        MatrixStack modelview = RenderSystem.getModelViewStack();
        modelview.loadIdentity();

        // DEPRECATED
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        // ----------
        if (this.minecraft.options.anaglyph3d) {

            modelview.translate((float)(i * 2 - 1) * 0.1f, 0.0f, 0.0f);
            RenderSystem.applyModelViewMatrix();

            // DEPRECATED
            GL11.glTranslatef((float)(i * 2 - 1) * 0.1f, 0.0f, 0.0f);
            // ----------
        }
        this.method_1849(f);
        if (this.minecraft.options.bobView) {
            this.method_1850(f);
        }
        if ((f2 = this.minecraft.player.field_505 + (this.minecraft.player.field_504 - this.minecraft.player.field_505) * f) > 0.0f) {
            float f4 = 5.0f / (f2 * f2 + 5.0f) - f2 * 0.04f;
            f4 *= f4;

            Vec3f vec3f = new Vec3f(0.0F, net.modificationstation.stationapi.api.util.math.MathHelper.SQUARE_ROOT_OF_TWO / 2.0F, net.modificationstation.stationapi.api.util.math.MathHelper.SQUARE_ROOT_OF_TWO / 2.0F);
            modelview.multiply(vec3f.getDegreesQuaternion(((float)this.field_2351 + f) * 20.0f));
            modelview.scale(1.0f / f4, 1.0f, 1.0f);
            modelview.multiply(vec3f.getDegreesQuaternion(-((float)this.field_2351 + f) * 20.0f));
            RenderSystem.applyModelViewMatrix();

            // DEPRECATED
            GL11.glRotatef(((float)this.field_2351 + f) * 20.0f, 0.0f, 1.0f, 1.0f);
            GL11.glScalef(1.0f / f4, 1.0f, 1.0f);
            GL11.glRotatef(-((float)this.field_2351 + f) * 20.0f, 0.0f, 1.0f, 1.0f);
            // ----------
        }
        this.method_1851(f);
    }

    /**
     * @author mine_diver
     */
    @Overwrite
    private void method_1849(float f) {
        MatrixStack modelview = RenderSystem.getModelViewStack();
        float f2;
        Living living = this.minecraft.viewEntity;
        float f3 = (float)living.hurtTime - f;
        if (living.health <= 0) {
            f2 = (float)living.deathTime + f;

            modelview.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(40.0f - 8000.0f / (f2 + 200.0f)));

            // DEPRECATED
            GL11.glRotatef(40.0f - 8000.0f / (f2 + 200.0f), 0.0f, 0.0f, 1.0f);
            // ----------
        }
        if (f3 < 0.0f) {
            return;
        }
        f3 /= (float)living.field_1039;
        f3 = MathHelper.sin(f3 * f3 * f3 * f3 * (float)Math.PI);
        f2 = living.field_1040;

        modelview.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-f2));
        modelview.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(-f3 * 14.0f));
        modelview.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(f2));
        RenderSystem.applyModelViewMatrix();

        // DEPRECATED
        GL11.glRotatef(-f2, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(-f3 * 14.0f, 0.0f, 0.0f, 1.0f);
        GL11.glRotatef(f2, 0.0f, 1.0f, 0.0f);
        // ----------
    }

    /**
     * @author mine_diver
     */
    @Overwrite
    private void method_1850(float f) {
        MatrixStack modelview = RenderSystem.getModelViewStack();
        if (!(this.minecraft.viewEntity instanceof PlayerBase playerBase)) {
            return;
        }
        float f2 = playerBase.field_1635 - playerBase.field_1634;
        float f3 = -(playerBase.field_1635 + f2 * f);
        float f4 = playerBase.field_524 + (playerBase.field_525 - playerBase.field_524) * f;
        float f5 = playerBase.field_1043 + (playerBase.field_1044 - playerBase.field_1043) * f;

        modelview.translate(MathHelper.sin(f3 * (float) Math.PI) * f4 * 0.5f, -Math.abs(MathHelper.cos(f3 * (float) Math.PI) * f4), 0.0f);
        modelview.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(MathHelper.sin(f3 * (float) Math.PI) * f4 * 3.0f));
        modelview.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(Math.abs(MathHelper.cos(f3 * (float) Math.PI - 0.2f) * f4) * 5.0f));
        modelview.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(f5));
        RenderSystem.applyModelViewMatrix();

        // DEPRECATED
        GL11.glTranslatef(MathHelper.sin(f3 * (float)Math.PI) * f4 * 0.5f, -Math.abs(MathHelper.cos(f3 * (float)Math.PI) * f4), 0.0f);
        GL11.glRotatef(MathHelper.sin(f3 * (float)Math.PI) * f4 * 3.0f, 0.0f, 0.0f, 1.0f);
        GL11.glRotatef(Math.abs(MathHelper.cos(f3 * (float)Math.PI - 0.2f) * f4) * 5.0f, 1.0f, 0.0f, 0.0f);
        GL11.glRotatef(f5, 1.0f, 0.0f, 0.0f);
        // ----------
    }

    /**
     * @author mine_diver
     */
    @Overwrite
    private void method_1851(float f) {
        MatrixStack modelview = RenderSystem.getModelViewStack();
        Living living = this.minecraft.viewEntity;
        float f2 = living.standingEyeHeight - 1.62f;
        double d = living.prevX + (living.x - living.prevX) * (double)f;
        double d2 = living.prevY + (living.y - living.prevY) * (double)f - (double)f2;
        double d3 = living.prevZ + (living.z - living.prevZ) * (double)f;

        modelview.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(this.field_2329 + (this.field_2328 - this.field_2329) * f));

        // DEPRECATED
        GL11.glRotatef(this.field_2329 + (this.field_2328 - this.field_2329) * f, 0.0f, 0.0f, 1.0f);
        // ----------
        if (living.isSleeping()) {
            f2 = (float)((double)f2 + 1.0);

            modelview.translate(0.0f, 0.3f, 0.0f);

            // DEPRECATED
            GL11.glTranslatef(0.0f, 0.3f, 0.0f);
            // ----------
            if (!this.minecraft.options.field_1447) {
                int n = this.minecraft.level.getTileId(MathHelper.floor(living.x), MathHelper.floor(living.y), MathHelper.floor(living.z));
                if (n == BlockBase.BED.id) {
                    int n2 = this.minecraft.level.getTileMeta(MathHelper.floor(living.x), MathHelper.floor(living.y), MathHelper.floor(living.z));
                    int n3 = n2 & 3;

                    modelview.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(n3 * 90));

                    // DEPRECATED
                    GL11.glRotatef(n3 * 90, 0.0f, 1.0f, 0.0f);
                    // ----------
                }

                modelview.multiply(Vec3f.NEGATIVE_Y.getDegreesQuaternion(living.prevYaw + (living.yaw - living.prevYaw) * f + 180.0f));
                modelview.multiply(Vec3f.NEGATIVE_X.getDegreesQuaternion(living.prevPitch + (living.pitch - living.prevPitch) * f));

                // DEPRECATED
                GL11.glRotatef(living.prevYaw + (living.yaw - living.prevYaw) * f + 180.0f, 0.0f, -1.0f, 0.0f);
                GL11.glRotatef(living.prevPitch + (living.pitch - living.prevPitch) * f, -1.0f, 0.0f, 0.0f);
                // ----------
            }
        } else if (this.minecraft.options.thirdPerson) {
            double d4 = this.field_2360 + (this.field_2359 - this.field_2360) * f;
            if (this.minecraft.options.field_1447) {
                float f3 = this.field_2362 + (this.field_2361 - this.field_2362) * f;
                float f4 = this.field_2364 + (this.field_2363 - this.field_2364) * f;

                modelview.translate(0.0f, 0.0f, (float)(-d4));
                modelview.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(f4));
                modelview.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(f3));

                // DEPRECATED
                GL11.glTranslatef(0.0f, 0.0f, (float)(-d4));
                GL11.glRotatef(f4, 1.0f, 0.0f, 0.0f);
                GL11.glRotatef(f3, 0.0f, 1.0f, 0.0f);
                // ----------
            } else {
                float f5 = living.yaw;
                float f6 = living.pitch;
                double d5 = (double)(-MathHelper.sin(f5 / 180.0f * (float)Math.PI) * MathHelper.cos(f6 / 180.0f * (float)Math.PI)) * d4;
                double d6 = (double)(MathHelper.cos(f5 / 180.0f * (float)Math.PI) * MathHelper.cos(f6 / 180.0f * (float)Math.PI)) * d4;
                double d7 = (double)(-MathHelper.sin(f6 / 180.0f * (float)Math.PI)) * d4;
                for (int i = 0; i < 8; ++i) {
                    double d8;
                    HitResult hitResult;
                    float f7 = (i & 1) * 2 - 1;
                    float f8 = (i >> 1 & 1) * 2 - 1;
                    float f9 = (i >> 2 & 1) * 2 - 1;
                    if ((hitResult = this.minecraft.level.method_160(net.minecraft.util.maths.Vec3f.from(d + (double)(f7 *= 0.1f), d2 + (double)(f8 *= 0.1f), d3 + (double)(f9 *= 0.1f)), net.minecraft.util.maths.Vec3f.from(d - d5 + (double)f7 + (double)f9, d2 - d7 + (double)f8, d3 - d6 + (double)f9))) == null || !((d8 = hitResult.field_1988.method_1294(net.minecraft.util.maths.Vec3f.from(d, d2, d3))) < d4)) continue;
                    d4 = d8;
                }

                modelview.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(living.pitch - f6));
                modelview.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(living.yaw - f5));
                modelview.translate(0.0f, 0.0f, (float)(-d4));
                modelview.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(f5 - living.yaw));
                modelview.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(f6 - living.pitch));

                // DEPRECATED
                GL11.glRotatef(living.pitch - f6, 1.0f, 0.0f, 0.0f);
                GL11.glRotatef(living.yaw - f5, 0.0f, 1.0f, 0.0f);
                GL11.glTranslatef(0.0f, 0.0f, (float)(-d4));
                GL11.glRotatef(f5 - living.yaw, 0.0f, 1.0f, 0.0f);
                GL11.glRotatef(f6 - living.pitch, 1.0f, 0.0f, 0.0f);
                // ----------
            }
        } else {

            modelview.translate(0.0f, 0.0f, -0.1f);

            // DEPRECATED
            GL11.glTranslatef(0.0f, 0.0f, -0.1f);
            // ----------
        }
        if (!this.minecraft.options.field_1447) {

            modelview.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(living.prevPitch + (living.pitch - living.prevPitch) * f));
            modelview.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(living.prevYaw + (living.yaw - living.prevYaw) * f + 180.0f));

            // DEPRECATED
            GL11.glRotatef(living.prevPitch + (living.pitch - living.prevPitch) * f, 1.0f, 0.0f, 0.0f);
            GL11.glRotatef(living.prevYaw + (living.yaw - living.prevYaw) * f + 180.0f, 0.0f, 1.0f, 0.0f);
            // ----------
        }

        modelview.translate(0.0f, f2, 0.0f);
        RenderSystem.applyModelViewMatrix();

        // DEPRECATED
        GL11.glTranslatef(0.0f, f2, 0.0f);
        // ----------
        d = living.prevX + (living.x - living.prevX) * (double)f;
        d2 = living.prevY + (living.y - living.prevY) * (double)f - (double)f2;
        d3 = living.prevZ + (living.z - living.prevZ) * (double)f;
        this.field_2330 = this.minecraft.worldRenderer.method_1538(d, d2, d3, f);
    }
}
