package net.modificationstation.stationapi.api.client.render;

public interface StateManager {
    void pushMatrix();

    void popMatrix();

    void rotate(float angle, float x, float y, float z);

    void translate(float x, float y, float z);

    void scale(float x, float y, float z);

    void blendFunc(int srcFactor, int dstFactor);

    void blendFuncSeparate(int srcRGBFactor, int dstRGBFactor, int srcAlphaFactor, int dstAlphaFactor);

    void depthFunc(int depthFunc);

    void depthMask(boolean depthMask);

    void enableBlend();

    void disableBlend();

    void enableDepthTest();

    void disableDepthTest();

    void enableCull();

    void disableCull();

    void disableLighting();

    void enableLighting();

    void colorMask(boolean red, boolean green, boolean blue, boolean alpha);
}
