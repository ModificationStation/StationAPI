package net.modificationstation.stationapi.api.client.render;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Environment(value=EnvType.CLIENT)
public interface VertexConsumerProvider {
    static Immediate immediate(BufferBuilder buffer) {
        return VertexConsumerProvider.immediate(ImmutableMap.of(), buffer);
    }

    static Immediate immediate(Map<RenderLayer, BufferBuilder> layerBuffers, BufferBuilder fallbackBuffer) {
        return new Immediate(fallbackBuffer, layerBuffers);
    }

    VertexConsumer getBuffer(RenderLayer var1);

    @Environment(value=EnvType.CLIENT)
    class Immediate
    implements VertexConsumerProvider {
        protected final BufferBuilder fallbackBuffer;
        protected final Map<RenderLayer, BufferBuilder> layerBuffers;
        protected Optional<RenderLayer> currentLayer = Optional.empty();
        protected final Set<BufferBuilder> activeConsumers = Sets.newHashSet();

        protected Immediate(BufferBuilder fallbackBuffer, Map<RenderLayer, BufferBuilder> layerBuffers) {
            this.fallbackBuffer = fallbackBuffer;
            this.layerBuffers = layerBuffers;
        }

        @Override
        public VertexConsumer getBuffer(RenderLayer renderLayer) {
            Optional<RenderLayer> optional = renderLayer.asOptional();
            BufferBuilder bufferBuilder = this.getBufferInternal(renderLayer);
            if (!Objects.equals(this.currentLayer, optional)) {
                RenderLayer renderLayer2;
                if (this.currentLayer.isPresent() && !this.layerBuffers.containsKey(renderLayer2 = this.currentLayer.get())) {
                    this.draw(renderLayer2);
                }
                if (this.activeConsumers.add(bufferBuilder)) {
                    bufferBuilder.begin(renderLayer.getDrawMode(), renderLayer.getVertexFormat());
                }
                this.currentLayer = optional;
            }
            return bufferBuilder;
        }

        private BufferBuilder getBufferInternal(RenderLayer layer) {
            return this.layerBuffers.getOrDefault(layer, this.fallbackBuffer);
        }

        public void drawCurrentLayer() {
            if (this.currentLayer.isPresent()) {
                RenderLayer renderLayer = this.currentLayer.get();
                if (!this.layerBuffers.containsKey(renderLayer)) {
                    this.draw(renderLayer);
                }
                this.currentLayer = Optional.empty();
            }
        }

        public void draw() {
            this.currentLayer.ifPresent(layer -> {
                VertexConsumer vertexConsumer = this.getBuffer(layer);
                if (vertexConsumer == this.fallbackBuffer) {
                    this.draw(layer);
                }
            });
            for (RenderLayer renderLayer : this.layerBuffers.keySet()) {
                this.draw(renderLayer);
            }
        }

        public void draw(RenderLayer layer) {
            BufferBuilder bufferBuilder = this.getBufferInternal(layer);
            boolean bl = Objects.equals(this.currentLayer, layer.asOptional());
            if (!bl && bufferBuilder == this.fallbackBuffer) {
                return;
            }
            if (!this.activeConsumers.remove(bufferBuilder)) {
                return;
            }
            layer.draw(bufferBuilder, 0, 0, 0);
            if (bl) {
                this.currentLayer = Optional.empty();
            }
        }
    }
}

