package net.modificationstation.stationapi.api.client.blaze3d.systems;

import com.google.common.collect.Queues;
import it.unimi.dsi.fastutil.ints.IntConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.Tessellator;
import net.modificationstation.stationapi.api.client.blaze3d.platform.GlStateManager;
import net.modificationstation.stationapi.api.client.render.BufferRenderer;
import net.modificationstation.stationapi.api.client.render.FogShape;
import net.modificationstation.stationapi.api.client.render.Shader;
import net.modificationstation.stationapi.api.client.render.VertexFormat;
import net.modificationstation.stationapi.api.client.texture.AbstractTexture;
import net.modificationstation.stationapi.api.client.texture.StationTextureManager;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.util.math.*;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

import static net.modificationstation.stationapi.impl.client.texture.StationRenderImpl.LOGGER;

public class RenderSystem {

    private static final ConcurrentLinkedQueue<RenderCall> recordingQueue = Queues.newConcurrentLinkedQueue();
    private static final Tessellator RENDER_THREAD_TESSELLATOR = Tessellator.INSTANCE;
    private static final int MINIMUM_ATLAS_TEXTURE_SIZE = 1024;
    private static boolean isReplayingQueue;
    @Nullable
    private static Thread gameThread;
    @Nullable
    private static Thread renderThread;
    private static int MAX_SUPPORTED_TEXTURE_SIZE;
    private static boolean isInInit;
    private static double lastDrawTime;
    private static final IndexBuffer sharedSequential;
    private static final IndexBuffer sharedSequentialQuad;
    private static final IndexBuffer sharedSequentialLines;
    private static Matrix3f inverseViewRotationMatrix;
    private static Matrix4f projectionMatrix;
    private static Matrix4f savedProjectionMatrix;
    private static final MatrixStack modelViewStack;
    private static Matrix4f modelViewMatrix;
    private static Matrix4f textureMatrix;
    private static final int[] shaderTextures;
    private static final float[] shaderColor;
    private static float shaderFogStart;
    private static float shaderFogEnd;
    private static final float[] shaderFogColor;
    private static FogShape shaderFogShape;
    private static final Vec3f[] shaderLightDirections;
    private static float shaderGameTime;
    private static float shaderLineWidth;
    private static String apiDescription;
    @Nullable
    private static Shader shader;

    public static void initRenderThread() {
        if (renderThread != null || gameThread == Thread.currentThread()) {
            throw new IllegalStateException("Could not initialize render thread");
        }
        renderThread = Thread.currentThread();
    }

    public static boolean isOnRenderThread() {
        return Thread.currentThread() == renderThread;
    }

    public static boolean isOnRenderThreadOrInit() {
        return isInInit || RenderSystem.isOnRenderThread();
    }

    public static void initGameThread(boolean assertNotRenderThread) {
        boolean bl;
        boolean bl2 = bl = renderThread == Thread.currentThread();
        if (gameThread != null || renderThread == null || bl == assertNotRenderThread) {
            throw new IllegalStateException("Could not initialize tick thread");
        }
        gameThread = Thread.currentThread();
    }

    public static boolean isOnGameThread() {
        return true;
    }

    public static void assertInInitPhase() {
        if (!RenderSystem.isInInitPhase()) {
            throw RenderSystem.constructThreadException();
        }
    }

    public static void assertOnGameThreadOrInit() {
        if (isInInit || RenderSystem.isOnGameThread()) {
            return;
        }
        throw RenderSystem.constructThreadException();
    }

    public static void assertOnRenderThreadOrInit() {
        if (isInInit || RenderSystem.isOnRenderThread()) {
            return;
        }
        throw RenderSystem.constructThreadException();
    }

    public static void assertOnRenderThread() {
        if (!RenderSystem.isOnRenderThread()) {
            throw RenderSystem.constructThreadException();
        }
    }

    public static void assertOnGameThread() {
        if (!RenderSystem.isOnGameThread()) {
            throw RenderSystem.constructThreadException();
        }
    }

    private static IllegalStateException constructThreadException() {
        return new IllegalStateException("Rendersystem called from wrong thread");
    }

    public static boolean isInInitPhase() {
        return true;
    }

    public static void recordRenderCall(RenderCall renderCall) {
        recordingQueue.add(renderCall);
    }

    public static void flipFrame(long window) {
//        GLFW.glfwPollEvents();
        RenderSystem.replayQueue();
//        Tessellator.getInstance().getBuffer().clear();
//        GLFW.glfwSwapBuffers(window);
        try {
            Display.swapBuffers();
        } catch (LWJGLException e) {
            throw new RuntimeException(e);
        }
//        GLFW.glfwPollEvents();
    }

    public static void replayQueue() {
        isReplayingQueue = true;
        while (!recordingQueue.isEmpty()) {
            RenderCall renderCall = recordingQueue.poll();
            renderCall.execute();
        }
        isReplayingQueue = false;
    }

//    public static void limitDisplayFPS(int fps) {
//        double d = lastDrawTime + 1.0 / (double)fps;
//        double e = GLFW.glfwGetTime();
//        while (e < d) {
//            GLFW.glfwWaitEventsTimeout(d - e);
//            e = GLFW.glfwGetTime();
//        }
//        lastDrawTime = e;
//    }

    public static void disableDepthTest() {
        RenderSystem.assertOnRenderThread();
        GlStateManager._disableDepthTest();
    }

    public static void enableDepthTest() {
        RenderSystem.assertOnGameThreadOrInit();
        GlStateManager._enableDepthTest();
    }

    public static void enableScissor(int i, int j, int k, int l) {
        RenderSystem.assertOnGameThreadOrInit();
        GlStateManager._enableScissorTest();
        GlStateManager._scissorBox(i, j, k, l);
    }

    public static void disableScissor() {
        RenderSystem.assertOnGameThreadOrInit();
        GlStateManager._disableScissorTest();
    }

    public static void depthFunc(int func) {
        RenderSystem.assertOnRenderThread();
        GlStateManager._depthFunc(func);
    }

    public static void depthMask(boolean mask) {
        RenderSystem.assertOnRenderThread();
        GlStateManager._depthMask(mask);
    }

    public static void enableBlend() {
        RenderSystem.assertOnRenderThread();
        GlStateManager._enableBlend();
    }

    public static void disableBlend() {
        RenderSystem.assertOnRenderThread();
        GlStateManager._disableBlend();
    }

    public static void blendFunc(GlStateManager.SrcFactor srcFactor, GlStateManager.DstFactor dstFactor) {
        RenderSystem.assertOnRenderThread();
        GlStateManager._blendFunc(srcFactor.value, dstFactor.value);
    }

    public static void blendFunc(int srcFactor, int dstFactor) {
        RenderSystem.assertOnRenderThread();
        GlStateManager._blendFunc(srcFactor, dstFactor);
    }

    public static void blendFuncSeparate(GlStateManager.SrcFactor srcFactor, GlStateManager.DstFactor dstFactor, GlStateManager.SrcFactor srcAlpha, GlStateManager.DstFactor dstAlpha) {
        RenderSystem.assertOnRenderThread();
        GlStateManager._blendFuncSeparate(srcFactor.value, dstFactor.value, srcAlpha.value, dstAlpha.value);
    }

    public static void blendFuncSeparate(int srcFactorRGB, int dstFactorRGB, int srcFactorAlpha, int dstFactorAlpha) {
        RenderSystem.assertOnRenderThread();
        GlStateManager._blendFuncSeparate(srcFactorRGB, dstFactorRGB, srcFactorAlpha, dstFactorAlpha);
    }

    public static void blendEquation(int mode) {
        RenderSystem.assertOnRenderThread();
        GlStateManager._blendEquation(mode);
    }

    public static void enableCull() {
        RenderSystem.assertOnRenderThread();
        GlStateManager._enableCull();
    }

    public static void disableCull() {
        RenderSystem.assertOnRenderThread();
        GlStateManager._disableCull();
    }

    public static void polygonMode(int i, int j) {
        RenderSystem.assertOnRenderThread();
        GlStateManager._polygonMode(i, j);
    }

    public static void enablePolygonOffset() {
        RenderSystem.assertOnRenderThread();
        GlStateManager._enablePolygonOffset();
    }

    public static void disablePolygonOffset() {
        RenderSystem.assertOnRenderThread();
        GlStateManager._disablePolygonOffset();
    }

    public static void polygonOffset(float factor, float units) {
        RenderSystem.assertOnRenderThread();
        GlStateManager._polygonOffset(factor, units);
    }

    public static void enableColorLogicOp() {
        RenderSystem.assertOnRenderThread();
        GlStateManager._enableColorLogicOp();
    }

    public static void disableColorLogicOp() {
        RenderSystem.assertOnRenderThread();
        GlStateManager._disableColorLogicOp();
    }

    public static void logicOp(GlStateManager.LogicOp op) {
        RenderSystem.assertOnRenderThread();
        GlStateManager._logicOp(op.value);
    }

    public static void activeTexture(int texture) {
        RenderSystem.assertOnRenderThread();
        GlStateManager._activeTexture(texture);
    }

    public static void enableTexture() {
        RenderSystem.assertOnRenderThread();
        GlStateManager._enableTexture();
    }

    public static void disableTexture() {
        RenderSystem.assertOnRenderThread();
        GlStateManager._disableTexture();
    }

    public static void texParameter(int target, int pname, int param) {
        GlStateManager._texParameter(target, pname, param);
    }

    public static void deleteTexture(int texture) {
        RenderSystem.assertOnGameThreadOrInit();
        GlStateManager._deleteTexture(texture);
    }

    public static void bindTextureForSetup(int i) {
        RenderSystem.bindTexture(i);
    }

    public static void bindTexture(int texture) {
        GlStateManager._bindTexture(texture);
    }

    public static void viewport(int x, int y, int width, int height) {
        RenderSystem.assertOnGameThreadOrInit();
        GlStateManager._viewport(x, y, width, height);
    }

    public static void colorMask(boolean red, boolean green, boolean blue, boolean alpha) {
        RenderSystem.assertOnRenderThread();
        GlStateManager._colorMask(red, green, blue, alpha);
    }

    public static void stencilFunc(int func, int ref, int mask) {
        RenderSystem.assertOnRenderThread();
        GlStateManager._stencilFunc(func, ref, mask);
    }

    public static void stencilMask(int i) {
        RenderSystem.assertOnRenderThread();
        GlStateManager._stencilMask(i);
    }

    public static void stencilOp(int sfail, int dpfail, int dppass) {
        RenderSystem.assertOnRenderThread();
        GlStateManager._stencilOp(sfail, dpfail, dppass);
    }

    public static void clearDepth(double depth) {
        RenderSystem.assertOnGameThreadOrInit();
        GlStateManager._clearDepth(depth);
    }

    public static void clearColor(float red, float green, float blue, float alpha) {
        RenderSystem.assertOnGameThreadOrInit();
        GlStateManager._clearColor(red, green, blue, alpha);
    }

    public static void clearStencil(int i) {
        RenderSystem.assertOnRenderThread();
        GlStateManager._clearStencil(i);
    }

    public static void clear(int mask, boolean getError) {
        RenderSystem.assertOnGameThreadOrInit();
        GlStateManager._clear(mask, getError);
    }

    public static void setShaderFogStart(float f) {
        RenderSystem.assertOnRenderThread();
        RenderSystem._setShaderFogStart(f);
    }

    private static void _setShaderFogStart(float f) {
        shaderFogStart = f;
    }

    public static float getShaderFogStart() {
        RenderSystem.assertOnRenderThread();
        return shaderFogStart;
    }

    public static void setShaderFogEnd(float f) {
        RenderSystem.assertOnRenderThread();
        RenderSystem._setShaderFogEnd(f);
    }

    private static void _setShaderFogEnd(float f) {
        shaderFogEnd = f;
    }

    public static float getShaderFogEnd() {
        RenderSystem.assertOnRenderThread();
        return shaderFogEnd;
    }

    public static void setShaderFogColor(float f, float g, float h, float i) {
        RenderSystem.assertOnRenderThread();
        RenderSystem._setShaderFogColor(f, g, h, i);
    }

    public static void setShaderFogColor(float f, float g, float h) {
        RenderSystem.setShaderFogColor(f, g, h, 1.0f);
    }

    private static void _setShaderFogColor(float f, float g, float h, float i) {
        RenderSystem.shaderFogColor[0] = f;
        RenderSystem.shaderFogColor[1] = g;
        RenderSystem.shaderFogColor[2] = h;
        RenderSystem.shaderFogColor[3] = i;
    }

    public static float[] getShaderFogColor() {
        RenderSystem.assertOnRenderThread();
        return shaderFogColor;
    }

    public static void setShaderFogShape(FogShape fogShape) {
        RenderSystem.assertOnRenderThread();
        RenderSystem._setShaderFogShape(fogShape);
    }

    private static void _setShaderFogShape(FogShape fogShape) {
        shaderFogShape = fogShape;
    }

    public static FogShape getShaderFogShape() {
        RenderSystem.assertOnRenderThread();
        return shaderFogShape;
    }

    public static void setShaderLights(Vec3f vec3f, Vec3f vec3f2) {
        RenderSystem.assertOnRenderThread();
        RenderSystem._setShaderLights(vec3f, vec3f2);
    }

    public static void _setShaderLights(Vec3f vec3f, Vec3f vec3f2) {
        RenderSystem.shaderLightDirections[0] = vec3f;
        RenderSystem.shaderLightDirections[1] = vec3f2;
    }

    public static void setupShaderLights(Shader shader) {
        RenderSystem.assertOnRenderThread();
        if (shader.light0Direction != null) {
            shader.light0Direction.set(shaderLightDirections[0]);
        }
        if (shader.light1Direction != null) {
            shader.light1Direction.set(shaderLightDirections[1]);
        }
    }

    public static void setShaderColor(float f, float g, float h, float i) {
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(() -> RenderSystem._setShaderColor(f, g, h, i));
        } else {
            RenderSystem._setShaderColor(f, g, h, i);
        }
    }

    private static void _setShaderColor(float f, float g, float h, float i) {
        RenderSystem.shaderColor[0] = f;
        RenderSystem.shaderColor[1] = g;
        RenderSystem.shaderColor[2] = h;
        RenderSystem.shaderColor[3] = i;
    }

    public static float[] getShaderColor() {
        RenderSystem.assertOnRenderThread();
        return shaderColor;
    }

    public static void drawElements(int mode, int count, int type) {
        RenderSystem.assertOnRenderThread();
        GlStateManager._drawElements(mode, count, type, 0L);
    }

    public static void lineWidth(float width) {
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(() -> {
                shaderLineWidth = width;
            });
        } else {
            shaderLineWidth = width;
        }
    }

    public static float getShaderLineWidth() {
        RenderSystem.assertOnRenderThread();
        return shaderLineWidth;
    }

    public static void pixelStore(int pname, int param) {
        RenderSystem.assertOnGameThreadOrInit();
        GlStateManager._pixelStore(pname, param);
    }

    public static void readPixels(int x, int y, int width, int height, int format, int type, ByteBuffer pixels) {
        RenderSystem.assertOnRenderThread();
        GlStateManager._readPixels(x, y, width, height, format, type, pixels);
    }

    public static void getString(int name, Consumer<String> consumer) {
        RenderSystem.assertOnRenderThread();
        consumer.accept(GlStateManager._getString(name));
    }

//    public static String getBackendDescription() {
//        RenderSystem.assertInInitPhase();
//        return String.format("LWJGL version %s", GLX._getLWJGLVersion());
//    }

    public static String getApiDescription() {
        return apiDescription;
    }

//    public static LongSupplier initBackendSystem() {
//        RenderSystem.assertInInitPhase();
//        return GLX._initGlfw();
//    }

//    public static void initRenderer(int debugVerbosity, boolean debugSync) {
//        RenderSystem.assertInInitPhase();
//        GLX._init(debugVerbosity, debugSync);
//        apiDescription = GLX.getOpenGLVersionString();
//    }

//    public static void setErrorCallback(GLFWErrorCallbackI callback) {
//        RenderSystem.assertInInitPhase();
//        GLX._setGlfwErrorCallback(callback);
//    }

//    public static void renderCrosshair(int size) {
//        RenderSystem.assertOnRenderThread();
//        GLX._renderCrosshair(size, true, true, true);
//    }

    public static String getCapsString() {
        RenderSystem.assertOnRenderThread();
        return "Using framebuffer using OpenGL 3.2";
    }

    public static void setupDefaultState(int x, int y, int width, int height) {
        RenderSystem.assertInInitPhase();
        GlStateManager._enableTexture();
        GlStateManager._clearDepth(1.0);
        GlStateManager._enableDepthTest();
        GlStateManager._depthFunc(515);
        projectionMatrix.loadIdentity();
        savedProjectionMatrix.loadIdentity();
        modelViewMatrix.loadIdentity();
        textureMatrix.loadIdentity();
        GlStateManager._viewport(x, y, width, height);
    }

    public static int maxSupportedTextureSize() {
        if (MAX_SUPPORTED_TEXTURE_SIZE == -1) {
            RenderSystem.assertOnRenderThreadOrInit();
            int i = GlStateManager._getInteger(3379);
            for (int j = Math.max(32768, i); j >= 1024; j >>= 1) {
                GlStateManager._texImage2D(32868, 0, 6408, j, j, 0, 6408, 5121, null);
                int k = GlStateManager._getTexLevelParameter(32868, 0, 4096);
                if (k == 0) continue;
                MAX_SUPPORTED_TEXTURE_SIZE = j;
                return j;
            }
            MAX_SUPPORTED_TEXTURE_SIZE = Math.max(i, 1024);
            LOGGER.info("Failed to determine maximum texture size by probing, trying GL_MAX_TEXTURE_SIZE = {}", MAX_SUPPORTED_TEXTURE_SIZE);
        }
        return MAX_SUPPORTED_TEXTURE_SIZE;
    }

    public static void glBindBuffer(int i, IntSupplier intSupplier) {
        GlStateManager._glBindBuffer(i, intSupplier.getAsInt());
    }

    public static void glBindVertexArray(Supplier<Integer> supplier) {
        GlStateManager._glBindVertexArray(supplier.get());
    }

    public static void glBufferData(int target, ByteBuffer data, int usage) {
        RenderSystem.assertOnRenderThreadOrInit();
        GlStateManager._glBufferData(target, data, usage);
    }

    public static void glDeleteBuffers(int buffer) {
        RenderSystem.assertOnRenderThread();
        GlStateManager._glDeleteBuffers(buffer);
    }

    public static void glDeleteVertexArrays(int i) {
        RenderSystem.assertOnRenderThread();
        GlStateManager._glDeleteVertexArrays(i);
    }

    public static void glUniform1i(int location, int value) {
        RenderSystem.assertOnRenderThread();
        GlStateManager._glUniform1i(location, value);
    }

    public static void glUniform1(int location, IntBuffer value) {
        RenderSystem.assertOnRenderThread();
        GlStateManager._glUniform1(location, value);
    }

    public static void glUniform2(int location, IntBuffer value) {
        RenderSystem.assertOnRenderThread();
        GlStateManager._glUniform2(location, value);
    }

    public static void glUniform3(int location, IntBuffer value) {
        RenderSystem.assertOnRenderThread();
        GlStateManager._glUniform3(location, value);
    }

    public static void glUniform4(int location, IntBuffer value) {
        RenderSystem.assertOnRenderThread();
        GlStateManager._glUniform4(location, value);
    }

    public static void glUniform1(int location, FloatBuffer value) {
        RenderSystem.assertOnRenderThread();
        GlStateManager._glUniform1(location, value);
    }

    public static void glUniform2(int location, FloatBuffer value) {
        RenderSystem.assertOnRenderThread();
        GlStateManager._glUniform2(location, value);
    }

    public static void glUniform3(int location, FloatBuffer value) {
        RenderSystem.assertOnRenderThread();
        GlStateManager._glUniform3(location, value);
    }

    public static void glUniform4(int location, FloatBuffer value) {
        RenderSystem.assertOnRenderThread();
        GlStateManager._glUniform4(location, value);
    }

    public static void glUniformMatrix2(int location, boolean transpose, FloatBuffer value) {
        RenderSystem.assertOnRenderThread();
        GlStateManager._glUniformMatrix2(location, transpose, value);
    }

    public static void glUniformMatrix3(int location, boolean transpose, FloatBuffer value) {
        RenderSystem.assertOnRenderThread();
        GlStateManager._glUniformMatrix3(location, transpose, value);
    }

    public static void glUniformMatrix4(int location, boolean transpose, FloatBuffer value) {
        RenderSystem.assertOnRenderThread();
        GlStateManager._glUniformMatrix4(location, transpose, value);
    }

    public static void setupOverlayColor(IntSupplier texture, int size) {
        RenderSystem.assertOnRenderThread();
        int i = texture.getAsInt();
        RenderSystem.setShaderTexture(1, i);
    }

    public static void teardownOverlayColor() {
        RenderSystem.assertOnRenderThread();
        RenderSystem.setShaderTexture(1, 0);
    }

    public static void setupLevelDiffuseLighting(Vec3f vec3f, Vec3f vec3f2, Matrix4f matrix4f) {
        RenderSystem.assertOnRenderThread();
        GlStateManager.setupLevelDiffuseLighting(vec3f, vec3f2, matrix4f);
    }

    public static void setupGuiFlatDiffuseLighting(Vec3f vec3f, Vec3f vec3f2) {
        RenderSystem.assertOnRenderThread();
        GlStateManager.setupGuiFlatDiffuseLighting(vec3f, vec3f2);
    }

    public static void setupGui3DDiffuseLighting(Vec3f vec3f, Vec3f vec3f2) {
        RenderSystem.assertOnRenderThread();
        GlStateManager.setupGui3DDiffuseLighting(vec3f, vec3f2);
    }

    public static void beginInitialization() {
        isInInit = true;
    }

    public static void finishInitialization() {
        isInInit = false;
        if (!recordingQueue.isEmpty()) {
            RenderSystem.replayQueue();
        }
        if (!recordingQueue.isEmpty()) {
            throw new IllegalStateException("Recorded to render queue during initialization");
        }
    }

    public static void glGenBuffers(Consumer<Integer> consumer) {
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(() -> consumer.accept(GlStateManager._glGenBuffers()));
        } else {
            consumer.accept(GlStateManager._glGenBuffers());
        }
    }

    public static void glGenVertexArrays(Consumer<Integer> consumer) {
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(() -> consumer.accept(GlStateManager._glGenVertexArrays()));
        } else {
            consumer.accept(GlStateManager._glGenVertexArrays());
        }
    }

    public static Tessellator renderThreadTesselator() {
        RenderSystem.assertOnRenderThread();
        return RENDER_THREAD_TESSELLATOR;
    }

    public static void defaultBlendFunc() {
        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
    }

    public static void setShader(Supplier<Shader> supplier) {
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(() -> {
                shader = supplier.get();
            });
        } else {
            shader = supplier.get();
        }
    }

    @Nullable
    public static Shader getShader() {
        RenderSystem.assertOnRenderThread();
        return shader;
    }

    public static int getTextureId(int i) {
        return GlStateManager._getTextureId(i);
    }

    public static void setShaderTexture(int i, Identifier identifier) {
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(() -> RenderSystem._setShaderTexture(i, identifier));
        } else {
            RenderSystem._setShaderTexture(i, identifier);
        }
    }

    public static void _setShaderTexture(int i, Identifier identifier) {
        if (i >= 0 && i < shaderTextures.length) {
            StationTextureManager textureManager = StationTextureManager.get(((Minecraft) FabricLoader.getInstance().getGameInstance()).textureManager);
            AbstractTexture abstractTexture = textureManager.getTexture(identifier);
            RenderSystem.shaderTextures[i] = abstractTexture.getGlId();
        }
    }

    public static void setShaderTexture(int i, int j) {
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(() -> RenderSystem._setShaderTexture(i, j));
        } else {
            RenderSystem._setShaderTexture(i, j);
        }
    }

    public static void _setShaderTexture(int i, int j) {
        if (i >= 0 && i < shaderTextures.length) {
            RenderSystem.shaderTextures[i] = j;
        }
    }

    public static int getShaderTexture(int i) {
        RenderSystem.assertOnRenderThread();
        if (i >= 0 && i < shaderTextures.length) {
            return shaderTextures[i];
        }
        return 0;
    }

    public static void setProjectionMatrix(Matrix4f matrix4f) {
        Matrix4f matrix4f2 = matrix4f.copy();
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(() -> {
                projectionMatrix = matrix4f2;
            });
        } else {
            projectionMatrix = matrix4f2;
        }
    }

    public static void setInverseViewRotationMatrix(Matrix3f matrix3f) {
        Matrix3f matrix3f2 = matrix3f.copy();
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(() -> {
                inverseViewRotationMatrix = matrix3f2;
            });
        } else {
            inverseViewRotationMatrix = matrix3f2;
        }
    }

    public static void setTextureMatrix(Matrix4f matrix4f) {
        Matrix4f matrix4f2 = matrix4f.copy();
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(() -> {
                textureMatrix = matrix4f2;
            });
        } else {
            textureMatrix = matrix4f2;
        }
    }

    public static void resetTextureMatrix() {
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(() -> textureMatrix.loadIdentity());
        } else {
            textureMatrix.loadIdentity();
        }
    }

    public static void applyModelViewMatrix() {
        Matrix4f matrix4f = modelViewStack.peek().getPositionMatrix().copy();
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(() -> {
                modelViewMatrix = matrix4f;
            });
        } else {
            modelViewMatrix = matrix4f;
        }
    }

    public static void backupProjectionMatrix() {
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(RenderSystem::_backupProjectionMatrix);
        } else {
            RenderSystem._backupProjectionMatrix();
        }
    }

    private static void _backupProjectionMatrix() {
        savedProjectionMatrix = projectionMatrix;
    }

    public static void restoreProjectionMatrix() {
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(RenderSystem::_restoreProjectionMatrix);
        } else {
            RenderSystem._restoreProjectionMatrix();
        }
    }

    private static void _restoreProjectionMatrix() {
        projectionMatrix = savedProjectionMatrix;
    }

    public static Matrix4f getProjectionMatrix() {
        RenderSystem.assertOnRenderThread();
        return projectionMatrix;
    }

    public static Matrix3f getInverseViewRotationMatrix() {
        RenderSystem.assertOnRenderThread();
        return inverseViewRotationMatrix;
    }

    public static Matrix4f getModelViewMatrix() {
        RenderSystem.assertOnRenderThread();
        return modelViewMatrix;
    }

    public static MatrixStack getModelViewStack() {
        return modelViewStack;
    }

    public static Matrix4f getTextureMatrix() {
        RenderSystem.assertOnRenderThread();
        return textureMatrix;
    }

    public static IndexBuffer getSequentialBuffer(VertexFormat.DrawMode drawMode, int i) {
        RenderSystem.assertOnRenderThread();
        IndexBuffer indexBuffer = drawMode == VertexFormat.DrawMode.QUADS ? sharedSequentialQuad : (drawMode == VertexFormat.DrawMode.LINES ? sharedSequentialLines : sharedSequential);
        indexBuffer.grow(i);
        return indexBuffer;
    }

    public static void setShaderGameTime(long l, float f) {
        float g = ((float)(l % 24000L) + f) / 24000.0f;
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(() -> {
                shaderGameTime = g;
            });
        } else {
            shaderGameTime = g;
        }
    }

    public static float getShaderGameTime() {
        RenderSystem.assertOnRenderThread();
        return shaderGameTime;
    }

    static {
        MAX_SUPPORTED_TEXTURE_SIZE = -1;
        lastDrawTime = Double.MIN_VALUE;
        sharedSequential = new IndexBuffer(1, 1, java.util.function.IntConsumer::accept);
        sharedSequentialQuad = new IndexBuffer(4, 6, (intConsumer, i) -> {
            intConsumer.accept(i);
            intConsumer.accept(i + 1);
            intConsumer.accept(i + 2);
            intConsumer.accept(i + 2);
            intConsumer.accept(i + 3);
            intConsumer.accept(i);
        });
        sharedSequentialLines = new IndexBuffer(4, 6, (intConsumer, i) -> {
            intConsumer.accept(i);
            intConsumer.accept(i + 1);
            intConsumer.accept(i + 2);
            intConsumer.accept(i + 3);
            intConsumer.accept(i + 2);
            intConsumer.accept(i + 1);
        });
        inverseViewRotationMatrix = new Matrix3f();
        projectionMatrix = new Matrix4f();
        savedProjectionMatrix = new Matrix4f();
        modelViewStack = new MatrixStack();
        modelViewMatrix = new Matrix4f();
        textureMatrix = new Matrix4f();
        shaderTextures = new int[12];
        shaderColor = new float[]{1.0f, 1.0f, 1.0f, 1.0f};
        shaderFogEnd = 1.0f;
        shaderFogColor = new float[]{0.0f, 0.0f, 0.0f, 0.0f};
        shaderFogShape = FogShape.SPHERE;
        shaderLightDirections = new Vec3f[2];
        shaderLineWidth = 1.0f;
        apiDescription = "Unknown";
        projectionMatrix.loadIdentity();
        savedProjectionMatrix.loadIdentity();
        modelViewMatrix.loadIdentity();
        textureMatrix.loadIdentity();
    }

    @Environment(EnvType.CLIENT)
    public static final class IndexBuffer {
        private final int sizeMultiplier;
        private final int increment;
        private final IndexMapper indexMapper;
        private int id;
        private VertexFormat.IntType elementFormat = VertexFormat.IntType.BYTE;
        private int size;

        IndexBuffer(int i, int j, IndexMapper indexMapper) {
            this.sizeMultiplier = i;
            this.increment = j;
            this.indexMapper = indexMapper;
        }

        void grow(int newSize) {
            if (newSize <= this.size) {
                return;
            }
            newSize = MathHelper.roundUpToMultiple(newSize * 2, this.increment);
            LOGGER.debug("Growing IndexBuffer: Old limit {}, new limit {}.", this.size, newSize);
            if (this.id == 0) {
                this.id = GlStateManager._glGenBuffers();
            }
            VertexFormat.IntType intType = VertexFormat.IntType.getSmallestTypeFor(newSize);
            int i = MathHelper.roundUpToMultiple(newSize * intType.size, 4);
            GlStateManager._glBindBuffer(34963, this.id);
            GlStateManager._glBufferData(34963, i, 35048);
            ByteBuffer byteBuffer = GlStateManager.mapBuffer(34963, 35001);
            if (byteBuffer == null) {
                throw new RuntimeException("Failed to map GL buffer");
            }
            this.elementFormat = intType;
            IntConsumer intConsumer = this.getIndexConsumer(byteBuffer);
            for (int j = 0; j < newSize; j += this.increment) {
                this.indexMapper.accept(intConsumer, j * this.sizeMultiplier / this.increment);
            }
            GlStateManager._glUnmapBuffer(34963);
            GlStateManager._glBindBuffer(34963, 0);
            this.size = newSize;
            BufferRenderer.unbindElementBuffer();
        }

        private IntConsumer getIndexConsumer(ByteBuffer indicesBuffer) {
            switch (this.elementFormat) {
                case BYTE -> {
                    return i -> indicesBuffer.put((byte) i);
                }
                case SHORT -> {
                    return i -> indicesBuffer.putShort((short) i);
                }
            }
            return indicesBuffer::putInt;
        }

        public int getId() {
            return this.id;
        }

        public VertexFormat.IntType getElementFormat() {
            return this.elementFormat;
        }

        @Environment(value=EnvType.CLIENT)
        interface IndexMapper {
            void accept(IntConsumer var1, int var2);
        }
    }
}
