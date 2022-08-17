package net.modificationstation.stationapi.api.client.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.client.blaze3d.platform.GlConst;
import net.modificationstation.stationapi.api.client.blaze3d.platform.GlStateManager;

/**
 * Represents a singular field within a larger {@link
 * net.modificationstation.stationapi.api.client.render.VertexFormat vertex format}.
 * 
 * <p>This element comprises a component type, the number of components,
 * and a type that describes how the components should be interpreted.
 */
@Environment(EnvType.CLIENT)
public class VertexFormatElement {
    private final ComponentType componentType;
    private final Type type;
    private final int uvIndex;
    private final int componentCount;
    /**
     * The total length of this element (in bytes).
     */
    private final int byteLength;

    public VertexFormatElement(int uvIndex, ComponentType componentType, Type type, int componentCount) {
        if (!this.isValidType(uvIndex, type)) {
            throw new IllegalStateException("Multiple vertex elements of the same type other than UVs are not supported");
        }
        this.type = type;
        this.componentType = componentType;
        this.uvIndex = uvIndex;
        this.componentCount = componentCount;
        this.byteLength = componentType.getByteLength() * this.componentCount;
    }

    private boolean isValidType(int uvIndex, Type type) {
        return uvIndex == 0 || type == Type.UV;
    }

    public final ComponentType getComponentType() {
        return this.componentType;
    }

    public final Type getType() {
        return this.type;
    }

    public final int getComponentCount() {
        return this.componentCount;
    }

    public final int getUvIndex() {
        return this.uvIndex;
    }

    public String toString() {
        return this.componentCount + "," + this.type.getName() + "," + this.componentType.getName();
    }

    public final int getByteLength() {
        return this.byteLength;
    }

    public final boolean isPosition() {
        return this.type == Type.POSITION;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        VertexFormatElement vertexFormatElement = (VertexFormatElement)o;
        if (this.componentCount != vertexFormatElement.componentCount) {
            return false;
        }
        if (this.uvIndex != vertexFormatElement.uvIndex) {
            return false;
        }
        if (this.componentType != vertexFormatElement.componentType) {
            return false;
        }
        return this.type == vertexFormatElement.type;
    }

    public int hashCode() {
        int i = this.componentType.hashCode();
        i = 31 * i + this.type.hashCode();
        i = 31 * i + this.uvIndex;
        i = 31 * i + this.componentCount;
        return i;
    }

    /**
     * Specifies for OpenGL how the vertex data corresponding to this element
     * should be interpreted.
     * 
     * @param elementIndex the index of the element in a vertex format
     * @param offset the distance between the start of the buffer and the first instance of
     * the element in the buffer
     * @param stride the distance between consecutive instances of the element in the buffer
     */
    public void setupState(int elementIndex, long offset, int stride) {
        this.type.setupState(this.componentCount, this.componentType.getGlType(), stride, offset, this.uvIndex, elementIndex);
    }

    public void clearState(int elementIndex) {
        this.type.clearState(this.uvIndex, elementIndex);
    }

    @Environment(value=EnvType.CLIENT)
    public static enum Type {
        POSITION("Position", (componentCount, componentType, stride, offset, uvIndex, elementIndex) -> {
            GlStateManager._enableVertexAttribArray(elementIndex);
            GlStateManager._vertexAttribPointer(elementIndex, componentCount, componentType, false, stride, offset);
        }, (uvIndex, elementIndex) -> GlStateManager._disableVertexAttribArray(elementIndex)),
        NORMAL("Normal", (componentCount, componentType, stride, offset, uvIndex, elementIndex) -> {
            GlStateManager._enableVertexAttribArray(elementIndex);
            GlStateManager._vertexAttribPointer(elementIndex, componentCount, componentType, true, stride, offset);
        }, (uvIndex, elementIndex) -> GlStateManager._disableVertexAttribArray(elementIndex)),
        COLOR("Vertex Color", (componentCount, componentType, stride, offset, uvIndex, elementIndex) -> {
            GlStateManager._enableVertexAttribArray(elementIndex);
            GlStateManager._vertexAttribPointer(elementIndex, componentCount, componentType, true, stride, offset);
        }, (uvIndex, elementIndex) -> GlStateManager._disableVertexAttribArray(elementIndex)),
        UV("UV", (componentCount, componentType, stride, offset, uvIndex, elementIndex) -> {
            GlStateManager._enableVertexAttribArray(elementIndex);
            if (componentType == 5126) {
                GlStateManager._vertexAttribPointer(elementIndex, componentCount, componentType, false, stride, offset);
            } else {
                GlStateManager._vertexAttribIPointer(elementIndex, componentCount, componentType, stride, offset);
            }
        }, (uvIndex, elementIndex) -> GlStateManager._disableVertexAttribArray(elementIndex)),
        PADDING("Padding", (componentCount, componentType, stride, offset, uvIndex, elementIndex) -> {}, (uvIndex, elementIndex) -> {}),
        GENERIC("Generic", (componentCount, componentType, stride, offset, uvIndex, elementIndex) -> {
            GlStateManager._enableVertexAttribArray(elementIndex);
            GlStateManager._vertexAttribPointer(elementIndex, componentCount, componentType, false, stride, offset);
        }, (uvIndex, elementIndex) -> GlStateManager._disableVertexAttribArray(elementIndex));

        private final String name;
        private final SetupTask setupTask;
        private final ClearTask clearTask;

        private Type(String name, SetupTask setupTask, ClearTask clearTask) {
            this.name = name;
            this.setupTask = setupTask;
            this.clearTask = clearTask;
        }

        void setupState(int componentCount, int componentType, int stride, long offset, int uvIndex, int elementIndex) {
            this.setupTask.setupBufferState(componentCount, componentType, stride, offset, uvIndex, elementIndex);
        }

        public void clearState(int uvIndex, int elementIndex) {
            this.clearTask.clearBufferState(uvIndex, elementIndex);
        }

        public String getName() {
            return this.name;
        }

        @FunctionalInterface
        @Environment(value=EnvType.CLIENT)
        static interface SetupTask {
            public void setupBufferState(int var1, int var2, int var3, long var4, int var6, int var7);
        }

        @FunctionalInterface
        @Environment(value=EnvType.CLIENT)
        static interface ClearTask {
            public void clearBufferState(int var1, int var2);
        }
    }

    @Environment(value=EnvType.CLIENT)
    public static enum ComponentType {
        FLOAT(4, "Float", GlConst.GL_FLOAT),
        UBYTE(1, "Unsigned Byte", GlConst.GL_UNSIGNED_BYTE),
        BYTE(1, "Byte", GlConst.GL_BYTE),
        USHORT(2, "Unsigned Short", GlConst.GL_UNSIGNED_SHORT),
        SHORT(2, "Short", GlConst.GL_SHORT),
        UINT(4, "Unsigned Int", GlConst.GL_UNSIGNED_INT),
        INT(4, "Int", GlConst.GL_INT);

        private final int byteLength;
        private final String name;
        private final int glType;

        private ComponentType(int byteLength, String name, int glType) {
            this.byteLength = byteLength;
            this.name = name;
            this.glType = glType;
        }

        public int getByteLength() {
            return this.byteLength;
        }

        public String getName() {
            return this.name;
        }

        public int getGlType() {
            return this.glType;
        }
    }
}

