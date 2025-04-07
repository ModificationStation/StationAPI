package net.modificationstation.stationapi.impl.client.arsenic.renderer.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.client.render.StateManager;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

public class StateManagerImpl implements StateManager {
    private static final BlendFuncState BLEND = new BlendFuncState();
    private static final DepthTestState DEPTH = new DepthTestState();
    private static final CullFaceState CULL = new CullFaceState();
    private static final LightState LIGHT = new LightState();
    private static final ColorMask COLOR_MASK = new ColorMask();

    @Override
    public void pushMatrix() {
        GL11.glPushMatrix();
    }

    @Override
    public void popMatrix() {
        GL11.glPopMatrix();
    }

    @Override
    public void rotate(float angle, float x, float y, float z) {
        GL11.glRotatef(angle, x, y, z);
    }

    @Override
    public void translate(float x, float y, float z) {
        GL11.glTranslatef(x, y, z);
    }

    @Override
    public void scale(float x, float y, float z) {
        GL11.glScalef(x, y, z);
    }

    @Override
    public void blendFunc(int srcFactor, int dstFactor) {
        if (srcFactor != BLEND.srcFactorRGB || dstFactor != BLEND.dstFactorRGB) {
            BLEND.srcFactorRGB = srcFactor;
            BLEND.dstFactorRGB = dstFactor;
            GL11.glBlendFunc(srcFactor, dstFactor);
        }
    }

    @Override
    public void blendFuncSeparate(int srcFactorRGB, int dstFactorRGB, int srcFactorAlpha, int dstFactorAlpha) {
        if (srcFactorRGB != BLEND.srcFactorRGB
                || dstFactorRGB != BLEND.dstFactorRGB
                || srcFactorAlpha != BLEND.srcFactorAlpha
                || dstFactorAlpha != BLEND.dstFactorAlpha) {
            BLEND.srcFactorRGB = srcFactorRGB;
            BLEND.dstFactorRGB = dstFactorRGB;
            BLEND.srcFactorAlpha = srcFactorAlpha;
            BLEND.dstFactorAlpha = dstFactorAlpha;
            GL14.glBlendFuncSeparate(srcFactorRGB, dstFactorRGB, srcFactorAlpha, dstFactorAlpha);
        }
    }

    @Override
    public void depthFunc(int func) {
        if (func != DEPTH.func) {
            DEPTH.func = func;
            GL11.glDepthFunc(func);
        }
    }

    @Override
    public void depthMask(boolean mask) {
        if (mask != DEPTH.mask) {
            DEPTH.mask = mask;
            GL11.glDepthMask(mask);
        }
    }

    @Override
    public void enableBlend() {
        BLEND.capState.enable();
    }

    @Override
    public void disableBlend() {
        BLEND.capState.disable();
    }

    @Override
    public void enableDepthTest() {
        DEPTH.capState.enable();
    }

    @Override
    public void disableDepthTest() {
        DEPTH.capState.disable();
    }

    @Override
    public void enableCull() {
        CULL.capState.enable();
    }

    @Override
    public void disableCull() {
        CULL.capState.disable();
    }

    @Override
    public void enableLighting() {
        LIGHT.capState.enable();
    }

    @Override
    public void disableLighting() {
        LIGHT.capState.disable();
    }

    @Override
    public void colorMask(boolean red, boolean green, boolean blue, boolean alpha) {
        if (red != COLOR_MASK.red || green != COLOR_MASK.green || blue != COLOR_MASK.blue || alpha != COLOR_MASK.alpha) {
            COLOR_MASK.red = red;
            COLOR_MASK.green = green;
            COLOR_MASK.blue = blue;
            COLOR_MASK.alpha = alpha;
            GL11.glColorMask(red, green, blue, alpha);
        }
    }

    static class ColorMask {
        public boolean red = true;
        public boolean green = true;
        public boolean blue = true;
        public boolean alpha = true;
    }

    static class CapabilityTracker {
        private final int cap;
        private boolean state;

        public CapabilityTracker(int cap) {
            this.cap = cap;
        }

        public void disable() {
            this.setState(false);
        }

        public void enable() {
            this.setState(true);
        }

        public void setState(boolean state) {
            if (state != this.state) {
                this.state = state;
                if (state) {
                    GL11.glEnable(this.cap);
                } else {
                    GL11.glDisable(this.cap);
                }
            }
        }
    }

    static class BlendFuncState {
        public final CapabilityTracker capState = new CapabilityTracker(GL11.GL_BLEND);
        public int srcFactorRGB = 1;
        public int dstFactorRGB = 0;
        public int srcFactorAlpha = 1;
        public int dstFactorAlpha = 0;
    }

    static class CullFaceState {
        public final CapabilityTracker capState = new CapabilityTracker(GL11.GL_CULL_FACE);
        public int mode = GL11.GL_BACK;
    }

    static class DepthTestState {
        public final CapabilityTracker capState = new CapabilityTracker(GL11.GL_DEPTH_TEST);
        public boolean mask = true;
        public int func = GL11.GL_LESS;
    }

    static class LightState {
        public final CapabilityTracker capState = new CapabilityTracker(GL11.GL_LIGHTING);
        public boolean enabled = true;
    }
}
