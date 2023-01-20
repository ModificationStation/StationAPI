package net.modificationstation.stationapi.api.client.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.function.Consumer;

/**
 * A utility for combining multiple VertexConsumers into one.
 */
@Environment(value=EnvType.CLIENT)
public class VertexConsumers {
    /**
     * Generates a union of zero VertexConsumers.
     * <p>
     * Obviously this is not possible.
     * 
     * @throws IllegalArgumentException
     */
    public static VertexConsumer union() {
        throw new IllegalArgumentException();
    }

    public static VertexConsumer union(VertexConsumer first) {
        return first;
    }

    public static VertexConsumer union(VertexConsumer first, VertexConsumer second) {
        return new Dual(first, second);
    }

    public static VertexConsumer union(VertexConsumer ... delegates) {
        return new Union(delegates);
    }

    @Environment(value=EnvType.CLIENT)
    static class Dual
    implements VertexConsumer {
        private final VertexConsumer first;
        private final VertexConsumer second;

        public Dual(VertexConsumer first, VertexConsumer second) {
            if (first == second) {
                throw new IllegalArgumentException("Duplicate delegates");
            }
            this.first = first;
            this.second = second;
        }

        @Override
        public VertexConsumer vertex(double x, double y, double z) {
            this.first.vertex(x, y, z);
            this.second.vertex(x, y, z);
            return this;
        }

        @Override
        public VertexConsumer color(int red, int green, int blue, int alpha) {
            this.first.color(red, green, blue, alpha);
            this.second.color(red, green, blue, alpha);
            return this;
        }

        @Override
        public VertexConsumer texture(float u, float v) {
            this.first.texture(u, v);
            this.second.texture(u, v);
            return this;
        }

        @Override
        public VertexConsumer overlay(int u, int v) {
            this.first.overlay(u, v);
            this.second.overlay(u, v);
            return this;
        }

        @Override
        public VertexConsumer light(int u, int v) {
            this.first.light(u, v);
            this.second.light(u, v);
            return this;
        }

        @Override
        public VertexConsumer normal(float x, float y, float z) {
            this.first.normal(x, y, z);
            this.second.normal(x, y, z);
            return this;
        }

        @Override
        public void vertex(float x, float y, float z, float red, float green, float blue, float alpha, float u, float v, int overlay, int light, float normalX, float normalY, float normalZ) {
            this.first.vertex(x, y, z, red, green, blue, alpha, u, v, overlay, light, normalX, normalY, normalZ);
            this.second.vertex(x, y, z, red, green, blue, alpha, u, v, overlay, light, normalX, normalY, normalZ);
        }

        @Override
        public void next() {
            this.first.next();
            this.second.next();
        }

        @Override
        public void fixedColor(int red, int green, int blue, int alpha) {
            this.first.fixedColor(red, green, blue, alpha);
            this.second.fixedColor(red, green, blue, alpha);
        }

        @Override
        public void unfixColor() {
            this.first.unfixColor();
            this.second.unfixColor();
        }
    }

    @Environment(value=EnvType.CLIENT)
    static class Union
    implements VertexConsumer {
        private final VertexConsumer[] delegates;

        public Union(VertexConsumer[] delegates) {
            for (int i = 0; i < delegates.length; ++i) {
                for (int j = i + 1; j < delegates.length; ++j) {
                    if (delegates[i] != delegates[j]) continue;
                    throw new IllegalArgumentException("Duplicate delegates");
                }
            }
            this.delegates = delegates;
        }

        private void delegate(Consumer<VertexConsumer> action) {
            for (VertexConsumer vertexConsumer : this.delegates) {
                action.accept(vertexConsumer);
            }
        }

        @Override
        public VertexConsumer vertex(double x, double y, double z) {
            this.delegate(i -> i.vertex(x, y, z));
            return this;
        }

        @Override
        public VertexConsumer color(int red, int green, int blue, int alpha) {
            this.delegate(i -> i.color(red, green, blue, alpha));
            return this;
        }

        @Override
        public VertexConsumer texture(float u, float v) {
            this.delegate(i -> i.texture(u, v));
            return this;
        }

        @Override
        public VertexConsumer overlay(int u, int v) {
            this.delegate(i -> i.overlay(u, v));
            return this;
        }

        @Override
        public VertexConsumer light(int u, int v) {
            this.delegate(i -> i.light(u, v));
            return this;
        }

        @Override
        public VertexConsumer normal(float x, float y, float z) {
            this.delegate(i -> i.normal(x, y, z));
            return this;
        }

        @Override
        public void vertex(float x, float y, float z, float red, float green, float blue, float alpha, float u, float v, int overlay, int light, float normalX, float normalY, float normalZ) {
            this.delegate(i -> i.vertex(x, y, z, red, green, blue, alpha, u, v, overlay, light, normalX, normalY, normalZ));
        }

        @Override
        public void next() {
            this.delegate(VertexConsumer::next);
        }

        @Override
        public void fixedColor(int red, int green, int blue, int alpha) {
            this.delegate(i -> i.fixedColor(red, green, blue, alpha));
        }

        @Override
        public void unfixColor() {
            this.delegate(VertexConsumer::unfixColor);
        }
    }
}

