package net.modificationstation.stationapi.api.client.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntConsumer;
import java.util.stream.Stream;

public record VertexFormatElement(int id, int index, Type type, Usage usage, int count) {
    public static final int MAX_COUNT = 32;
    private static final VertexFormatElement[] BY_ID = new VertexFormatElement[MAX_COUNT];
    private static final List<VertexFormatElement> ELEMENTS = new ArrayList<>(MAX_COUNT);

    public static final VertexFormatElement POSITION = register(0, 0, Type.FLOAT, Usage.POSITION, 3);
    public static final VertexFormatElement COLOR = register(1, 0, Type.INT, VertexFormatElement.Usage.COLOR, 1);
    public static final VertexFormatElement UV0 = register(2, 0, Type.FLOAT, Usage.UV, 2);
    public static final VertexFormatElement UV1 = register(3, 1, Type.SHORT, Usage.UV, 2);
    public static final VertexFormatElement UV2 = register(4, 2, Type.SHORT, Usage.UV, 2);
    public static final VertexFormatElement NORMAL = register(5, 0, Type.INT, Usage.NORMAL, 1);

    public VertexFormatElement(int id, int index, Type type, Usage usage, int count) {
        if (id < 0 || id >= BY_ID.length) {
            throw new IllegalArgumentException("Element ID must be in range [0; " + BY_ID.length + ")");
        } else if (!this.supportsUsage(index, usage)) {
            throw new IllegalStateException("Multiple vertex elements of the same type other than UVs are not supported");
        } else {
            this.id = id;
            this.index = index;
            this.type = type;
            this.usage = usage;
            this.count = count;
        }
    }

    public static VertexFormatElement register(int id, int index, Type type, Usage usage, int count) {
        VertexFormatElement vertexFormatElement = new VertexFormatElement(id, index, type, usage, count);
        if (BY_ID[id] != null) {
            throw new IllegalArgumentException("Duplicate element registration for: " + id);
        } else {
            BY_ID[id] = vertexFormatElement;
            ELEMENTS.add(vertexFormatElement);
            return vertexFormatElement;
        }
    }

    private boolean supportsUsage(int uvIndex, VertexFormatElement.Usage usage) {
        return uvIndex == 0 || usage == VertexFormatElement.Usage.UV;
    }

    public String toString() {
        return this.count + "," + this.usage + "," + this.type + " (" + this.id + ")";
    }

    public int mask() {
        return 1 << this.id;
    }

    public int byteSize() {
        return this.type.size() * this.count;
    }

    @Nullable
    public static VertexFormatElement byId(int id) {
        return BY_ID[id];
    }

    public static Stream<VertexFormatElement> elementsFromMask(int mask) {
        return ELEMENTS.stream().filter(element -> element != null && (mask & element.mask()) != 0);
    }

    public void startDrawing(long pointer, int stride) {
        this.usage.startDrawing(this.count, this.type.getGlId(), stride, pointer, this.index);
    }

    public void endDrawing() {
        this.usage.endDrawing(this.index);
    }

    enum Type {
        FLOAT(4, "Float", GL11.GL_FLOAT),
        UBYTE(1, "Unsigned Byte", GL11.GL_UNSIGNED_BYTE),
        BYTE(1, "Byte", GL11.GL_BYTE),
        USHORT(2, "Unsigned Short", GL11.GL_UNSIGNED_SHORT),
        SHORT(2, "Short", GL11.GL_SHORT),
        UINT(4, "Unsigned Int", GL11.GL_UNSIGNED_INT),
        INT(4, "Int", GL11.GL_INT);

        private final int size;
        private final String name;
        private final int glId;

        Type(int size, String name, int glId) {
            this.size = size;
            this.name = name;
            this.glId = glId;
        }

        public int size() {
            return this.size;
        }

        public String toString() {
            return this.name;
        }

        public int getGlId() {
            return this.glId;
        }
    }

    @Environment(EnvType.CLIENT)
    enum Usage {
        POSITION("Position", (size, glId, stride, pointer, elementIndex) -> {
            GL11.glVertexPointer(size, glId, stride, pointer);
            GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        }, i -> GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY)),
        NORMAL("Normal", (size, glId, stride, pointer, elementIndex) -> {
            GL11.glNormalPointer(glId, stride, pointer);
            GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
        }, i -> GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY)),
        COLOR("Vertex Color", (size, glId, stride, pointer, elementIndex) -> {
            GL11.glColorPointer(size, glId, stride, pointer);
            GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
        }, i -> {
            GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
//            GlStateManager.clearCurrentColor();
        }),
        UV("UV", (size, glId, stride, pointer, elementIndex) -> {
            GL13.glClientActiveTexture(GL13.GL_TEXTURE0 + elementIndex);
            GL11.glTexCoordPointer(size, glId, stride, pointer);
            GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
            GL13.glClientActiveTexture(GL13.GL_TEXTURE0);
        }, i -> {
            GL13.glClientActiveTexture(GL13.GL_TEXTURE0 + i);
            GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
            GL13.glClientActiveTexture(GL13.GL_TEXTURE0);
        }),
        PADDING("Padding", (size, glId, stride, pointer, elementIndex) -> {
        }, i -> {
        });

        private final String name;
        private final Usage.Starter starter;
        private final IntConsumer finisher;

        Usage(String name, Usage.Starter starter, IntConsumer intConsumer) {
            this.name = name;
            this.starter = starter;
            this.finisher = intConsumer;
        }

        private void startDrawing(int count, int glId, int stride, long pointer, int elementIndex) {
            this.starter.setupBufferState(count, glId, stride, pointer, elementIndex);
        }

        public void endDrawing(int elementIndex) {
            this.finisher.accept(elementIndex);
        }

        public String getName() {
            return this.name;
        }

        @Environment(EnvType.CLIENT)
        interface Starter {
            void setupBufferState(int size, int glId, int stride, long pointer, int elementIndex);
        }
    }
}
