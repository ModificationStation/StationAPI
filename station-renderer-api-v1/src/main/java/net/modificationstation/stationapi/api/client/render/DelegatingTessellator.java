package net.modificationstation.stationapi.api.client.render;

import net.minecraft.client.render.Tessellator;
import net.modificationstation.stationapi.api.client.render.model.BakedQuad;
import net.modificationstation.stationapi.api.util.math.MatrixStack;

public class DelegatingTessellator extends Tessellator {

    private VertexConsumer delegate = Tessellator.INSTANCE;

    public DelegatingTessellator() {
        super(0);
    }

    public void setDelegate(VertexConsumer delegate) {
        this.delegate = delegate;
    }

    @Override
    public void draw() {
        if (this.delegate instanceof Tessellator t)
            t.draw();
    }

    @Override
    public void start(int mode) {
        if (this.delegate instanceof Tessellator t)
            t.start(mode);
    }

    @Override
    public void translate(double x, double y, double z) {
        if (this.delegate instanceof Tessellator t)
            t.translate(x, y, z);
    }

    @Override
    public void disableColor() {
        if (this.delegate instanceof Tessellator t)
            t.disableColor();
    }

    @Override
    public void translate(float x, float y, float z) {
        if (this.delegate instanceof Tessellator t)
            t.translate(x, y, z);
    }

    @Override
    public void vertex(double x, double y, double z) {
        this.delegate.setVertex((float) x, (float) y, (float) z);
    }

    @Override
    public VertexConsumer setVertex(float x, float y, float z) {
        return this.delegate.setVertex(x, y, z);
    }

    @Override
    public void vertex(double x, double y, double z, double u, double v) {
        this.delegate.setTexture((float) u, (float) v);
        this.delegate.setVertex((float) x, (float) y, (float) z);
    }

    @Override
    public void texture(double u, double v) {
        this.delegate.setTexture((float) u, (float) v);
    }

    @Override
    public VertexConsumer setTexture(float u, float v) {
        return this.delegate.setTexture(u, v);
    }

    @Override
    public void normal(float x, float y, float z) {
        this.delegate.setNormal(x, y, z);
    }

    @Override
    public VertexConsumer setNormal(float x, float y, float z) {
        return this.delegate.setNormal(x, y, z);
    }

    @Override
    public void color(int r, int g, int b, int a) {
        this.delegate.setColor(r, g, b, a);
    }

    @Override
    public VertexConsumer setColor(int red, int green, int blue, int alpha) {
        return this.delegate.setColor(red, green, blue, alpha);
    }

    @Override
    public VertexConsumer setLight(int u, int v) {
        return this.delegate.setLight(u, v);
    }

    @Override
    public VertexConsumer setOverlay(int u, int v) {
        return this.delegate.setOverlay(u, v);
    }

    @Override
    public void quad(BakedQuad quad, float x, float y, float z, int colour0, int colour1, int colour2, int colour3, float normalX, float normalY, float normalZ, boolean spreadUV) {
        if (this.delegate instanceof StationTessellator t)
            t.quad(quad, x, y, z, colour0, colour1, colour2, colour3, normalX, normalY, normalZ, spreadUV);
    }

    @Override
    public void ensureBufferCapacity(int criticalCapacity) {
        if (this.delegate instanceof StationTessellator t)
            t.ensureBufferCapacity(criticalCapacity);
    }

    @Override
    public VertexConsumer quad(MatrixStack.Entry entry, BakedQuad quad, int colour0, int colour1, int colour2, int colour3, float normalX, float normalY, float normalZ, boolean spreadUV) {
        return this.delegate.quad(entry, quad, colour0, colour1, colour2, colour3, normalX, normalY, normalZ, spreadUV);
    }
}
