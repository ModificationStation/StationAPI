package net.modificationstation.stationapi.api.client.render;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.client.blaze3d.platform.GlConst;
import net.modificationstation.stationapi.api.client.blaze3d.systems.RenderSystem;
import net.modificationstation.stationapi.api.client.gl.VertexBuffer;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Collectors;

@Environment(EnvType.CLIENT)
public class VertexFormat {
    private final ImmutableList<VertexFormatElement> elements;
    private final ImmutableMap<String, VertexFormatElement> elementMap;
    private final IntList offsets = new IntArrayList();
    private final int vertexSizeByte;
    @Nullable
    private VertexBuffer buffer;

    public VertexFormat(ImmutableMap<String, VertexFormatElement> elementMap) {
        this.elementMap = elementMap;
        this.elements = elementMap.values().asList();
        int i = 0;
        for (VertexFormatElement vertexFormatElement : elementMap.values()) {
            this.offsets.add(i);
            i += vertexFormatElement.getByteLength();
        }
        this.vertexSizeByte = i;
    }

    public String toString() {
        return "format: " + this.elementMap.size() + " elements: " + this.elementMap.entrySet().stream().map(Object::toString).collect(Collectors.joining(" "));
    }

    public int getVertexSizeInteger() {
        return this.getVertexSizeByte() / 4;
    }

    public int getVertexSizeByte() {
        return this.vertexSizeByte;
    }

    public ImmutableList<VertexFormatElement> getElements() {
        return this.elements;
    }

    public ImmutableList<String> getAttributeNames() {
        return this.elementMap.keySet().asList();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        VertexFormat vertexFormat = (VertexFormat)o;
        if (this.vertexSizeByte != vertexFormat.vertexSizeByte) {
            return false;
        }
        return this.elementMap.equals(vertexFormat.elementMap);
    }

    public int hashCode() {
        return this.elementMap.hashCode();
    }

    /**
     * Specifies for OpenGL how the vertex data should be interpreted.
     */
    public void setupState() {
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(this::setupStateInternal);
            return;
        }
        this.setupStateInternal();
    }

    private void setupStateInternal() {
        int i = this.getVertexSizeByte();
        ImmutableList<VertexFormatElement> list = this.getElements();
        for (int j = 0; j < list.size(); ++j) {
            list.get(j).setupState(j, this.offsets.getInt(j), i);
        }
    }

    public void clearState() {
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(this::clearStateInternal);
            return;
        }
        this.clearStateInternal();
    }

    private void clearStateInternal() {
        ImmutableList<VertexFormatElement> immutableList = this.getElements();
        for (int i = 0; i < immutableList.size(); ++i) {
            VertexFormatElement vertexFormatElement = immutableList.get(i);
            vertexFormatElement.clearState(i);
        }
    }

    public VertexBuffer getBuffer() {
        VertexBuffer vertexBuffer = this.buffer;
        if (vertexBuffer == null) {
            this.buffer = vertexBuffer = new VertexBuffer();
        }
        return vertexBuffer;
    }

    @Environment(value=EnvType.CLIENT)
    public enum DrawMode {
        LINES(4, 2, 2, false),
        LINE_STRIP(5, 2, 1, true),
        DEBUG_LINES(1, 2, 2, false),
        DEBUG_LINE_STRIP(3, 2, 1, true),
        TRIANGLES(4, 3, 3, false),
        TRIANGLE_STRIP(5, 3, 1, true),
        TRIANGLE_FAN(6, 3, 1, true),
        QUADS(4, 4, 4, false);

        public final int glMode;
        public final int firstVertexCount;
        public final int additionalVertexCount;
        public final boolean shareVertices;

        DrawMode(int glMode, int firstVertexCount, int additionalVertexCount, boolean shareVertices) {
            this.glMode = glMode;
            this.firstVertexCount = firstVertexCount;
            this.additionalVertexCount = additionalVertexCount;
            this.shareVertices = shareVertices;
        }

        public int getIndexCount(int vertexCount) {
            return switch (this) {
                case LINE_STRIP, DEBUG_LINES, DEBUG_LINE_STRIP, TRIANGLES, TRIANGLE_STRIP, TRIANGLE_FAN -> vertexCount;
                case LINES, QUADS -> vertexCount / 4 * 6;
            };
        }
    }

    @Environment(value=EnvType.CLIENT)
    public enum IndexType {
        BYTE(GlConst.GL_UNSIGNED_BYTE, 1),
        SHORT(GlConst.GL_UNSIGNED_SHORT, 2),
        INT(GlConst.GL_UNSIGNED_INT, 4);

        public final int glType;
        public final int size;

        IndexType(int glType, int size) {
            this.glType = glType;
            this.size = size;
        }

        public static IndexType smallestFor(int indexCount) {
            if ((indexCount & 0xFFFF0000) != 0) {
                return INT;
            }
            if ((indexCount & 0xFF00) != 0) {
                return SHORT;
            }
            return BYTE;
        }
    }
}

