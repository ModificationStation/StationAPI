package net.modificationstation.stationapi.api.client.render;

import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Floats;
import it.unimi.dsi.fastutil.ints.IntArrays;
import it.unimi.dsi.fastutil.ints.IntConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_214;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import net.modificationstation.stationapi.api.util.math.Vec3f;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static net.modificationstation.stationapi.impl.client.texture.StationRenderImpl.LOGGER;

@Environment(value=EnvType.CLIENT)
public class BufferBuilder
extends FixedColorVertexConsumer
implements BufferVertexConsumer {
    private static final int MAX_BUFFER_SIZE = 0x200000;
    private ByteBuffer buffer;
    private int builtBufferCount;
    private int batchOffset;
    private int elementOffset;
    private int vertexCount;
    @Nullable
    private VertexFormatElement currentElement;
    private int currentElementId;
    private VertexFormat format;
    private VertexFormat.DrawMode drawMode;
    private boolean textured;
    private boolean hasOverlay;
    private boolean building;
    @Nullable
    private Vec3f[] sortingPrimitiveCenters;
    private float sortingCameraX = Float.NaN;
    private float sortingCameraY = Float.NaN;
    private float sortingCameraZ = Float.NaN;
    private boolean hasNoVertexBuffer;

    public BufferBuilder(int initialCapacity) {
        this.buffer = class_214.method_744(initialCapacity * 6);
    }

    private void grow() {
        this.grow(this.format.getVertexSizeByte());
    }

    private void grow(int size) {
        if (this.elementOffset + size <= this.buffer.capacity()) {
            return;
        }
        int i = this.buffer.capacity();
        int j = i + BufferBuilder.roundBufferSize(size);
        LOGGER.debug("Needed to grow BufferBuilder buffer: Old size {} bytes, new size {} bytes.", i, j);
        ByteBuffer byteBuffer = class_214.method_744(j);
        byteBuffer.rewind();
        this.buffer = byteBuffer;
    }

    private static int roundBufferSize(int amount) {
        int j;
        int i = 0x200000;
        if (amount == 0) {
            return i;
        }
        if (amount < 0) {
            i *= -1;
        }
        if ((j = amount % i) == 0) {
            return amount;
        }
        return amount + i - j;
    }

    public void sortFrom(float cameraX, float cameraY, float cameraZ) {
        if (this.drawMode != VertexFormat.DrawMode.QUADS) {
            return;
        }
        if (this.sortingCameraX != cameraX || this.sortingCameraY != cameraY || this.sortingCameraZ != cameraZ) {
            this.sortingCameraX = cameraX;
            this.sortingCameraY = cameraY;
            this.sortingCameraZ = cameraZ;
            if (this.sortingPrimitiveCenters == null) {
                this.sortingPrimitiveCenters = this.buildPrimitiveCenters();
            }
        }
    }

    public State popState() {
        return new State(this.drawMode, this.vertexCount, this.sortingPrimitiveCenters, this.sortingCameraX, this.sortingCameraY, this.sortingCameraZ);
    }

    public void restoreState(State state) {
        this.buffer.rewind();
        this.drawMode = state.drawMode;
        this.vertexCount = state.vertexCount;
        this.elementOffset = this.batchOffset;
        this.sortingPrimitiveCenters = state.sortingPrimitiveCenters;
        this.sortingCameraX = state.sortingCameraX;
        this.sortingCameraY = state.sortingCameraY;
        this.sortingCameraZ = state.sortingCameraZ;
        this.hasNoVertexBuffer = true;
    }

    public void begin(VertexFormat.DrawMode drawMode, VertexFormat format) {
        if (this.building) {
            throw new IllegalStateException("Already building!");
        }
        this.building = true;
        this.drawMode = drawMode;
        this.setFormat(format);
        this.currentElement = format.getElements().get(0);
        this.currentElementId = 0;
        this.buffer.rewind();
    }

    private void setFormat(VertexFormat format) {
        if (this.format == format) {
            return;
        }
        this.format = format;
//        boolean bl = format == VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL;
        boolean bl2 = format == VertexFormats.POSITION_TEXTURE_COLOR_NORMAL;
        this.textured = /*bl ||*/ bl2;
//        this.hasOverlay = bl;
    }

    private IntConsumer createIndexWriter(int offset, VertexFormat.IndexType indexType) {
        MutableInt mutableInt = new MutableInt(offset);
        return switch (indexType) {
            case BYTE -> index -> this.buffer.put(mutableInt.getAndIncrement(), (byte)index);
            case SHORT -> index -> this.buffer.putShort(mutableInt.getAndAdd(2), (short)index);
            case INT -> index -> this.buffer.putInt(mutableInt.getAndAdd(4), index);
        };
    }

    private Vec3f[] buildPrimitiveCenters() {
        FloatBuffer floatBuffer = this.buffer.asFloatBuffer();
        int i = this.batchOffset / 4;
        int j = this.format.getVertexSizeInteger();
        int k = j * this.drawMode.additionalVertexCount;
        int l = this.vertexCount / this.drawMode.additionalVertexCount;
        Vec3f[] vec3fs = new Vec3f[l];
        for (int m = 0; m < l; ++m) {
            float f = floatBuffer.get(i + m * k);
            float g = floatBuffer.get(i + m * k + 1);
            float h = floatBuffer.get(i + m * k + 2);
            float n = floatBuffer.get(i + m * k + j * 2);
            float o = floatBuffer.get(i + m * k + j * 2 + 1);
            float p = floatBuffer.get(i + m * k + j * 2 + 2);
            float q = (f + n) / 2.0f;
            float r = (g + o) / 2.0f;
            float s = (h + p) / 2.0f;
            vec3fs[m] = new Vec3f(q, r, s);
        }
        return vec3fs;
    }

    private void writeSortedIndices(VertexFormat.IndexType indexType) {
        float[] fs = new float[this.sortingPrimitiveCenters.length];
        int[] is = new int[this.sortingPrimitiveCenters.length];
        for (int i = 0; i < this.sortingPrimitiveCenters.length; ++i) {
            assert this.sortingPrimitiveCenters[i] != null;
            float f = this.sortingPrimitiveCenters[i].getX() - this.sortingCameraX;
            float g = this.sortingPrimitiveCenters[i].getY() - this.sortingCameraY;
            float h = this.sortingPrimitiveCenters[i].getZ() - this.sortingCameraZ;
            fs[i] = f * f + g * g + h * h;
            is[i] = i;
        }
        IntArrays.mergeSort(is, (a, b) -> Floats.compare(fs[b], fs[a]));
        IntConsumer intConsumer = this.createIndexWriter(this.elementOffset, indexType);
        for (int j : is) {
            intConsumer.accept(j * this.drawMode.additionalVertexCount);
            intConsumer.accept(j * this.drawMode.additionalVertexCount + 1);
            intConsumer.accept(j * this.drawMode.additionalVertexCount + 2);
            intConsumer.accept(j * this.drawMode.additionalVertexCount + 2);
            intConsumer.accept(j * this.drawMode.additionalVertexCount + 3);
            intConsumer.accept(j * this.drawMode.additionalVertexCount);
        }
    }

    public boolean isBatchEmpty() {
        return this.vertexCount == 0;
    }

    /**
     * Builds a buffer if there are vertices in the current batch and resets
     * the building state.
     * 
     * @throws IllegalStateException if this builder has not begun building
     * 
     * @return the built buffer if there are vertices, otherwise {@code null}
     * 
     * @see #end()
     */
    @Nullable
    public BuiltBuffer endNullable() {
        this.ensureBuilding();
        if (this.isBatchEmpty()) {
            this.resetBuilding();
            return null;
        }
        BuiltBuffer builtBuffer = this.build();
        this.resetBuilding();
        return builtBuffer;
    }

    /**
     * Builds a buffer from the current batch and resets the building state.
     * 
     * <p>Unlike {@link #endNullable()}, this always builds a buffer even if
     * there are no vertices in the current batch.
     * 
     * @throws IllegalStateException if this builder has not begun building
     * 
     * @return the buffer built from the current batch
     */
    public BuiltBuffer end() {
        this.ensureBuilding();
        BuiltBuffer builtBuffer = this.build();
        this.resetBuilding();
        return builtBuffer;
    }

    private void ensureBuilding() {
        if (!this.building) {
            throw new IllegalStateException("Not building!");
        }
    }

    private BuiltBuffer build() {
        int l;
        boolean bl;
        int k;
        int i = this.drawMode.getIndexCount(this.vertexCount);
        int j = !this.hasNoVertexBuffer ? this.vertexCount * this.format.getVertexSizeByte() : 0;
        VertexFormat.IndexType indexType = VertexFormat.IndexType.smallestFor(i);
        if (this.sortingPrimitiveCenters != null) {
            k = MathHelper.roundUpToMultiple(i * indexType.size, 4);
            this.grow(k);
            this.writeSortedIndices(indexType);
            bl = false;
            this.elementOffset += k;
            l = j + k;
        } else {
            bl = true;
            l = j;
        }
        k = this.batchOffset;
        this.batchOffset += l;
        ++this.builtBufferCount;
        DrawArrayParameters drawArrayParameters = new DrawArrayParameters(this.format, this.vertexCount, i, this.drawMode, indexType, this.hasNoVertexBuffer, bl);
        return new BuiltBuffer(k, drawArrayParameters);
    }

    private void resetBuilding() {
        this.building = false;
        this.vertexCount = 0;
        this.currentElement = null;
        this.currentElementId = 0;
        this.sortingPrimitiveCenters = null;
        this.sortingCameraX = Float.NaN;
        this.sortingCameraY = Float.NaN;
        this.sortingCameraZ = Float.NaN;
        this.hasNoVertexBuffer = false;
    }

    @Override
    public void putByte(int index, byte value) {
        this.buffer.put(this.elementOffset + index, value);
    }

    @Override
    public void putShort(int index, short value) {
        this.buffer.putShort(this.elementOffset + index, value);
    }

    @Override
    public void putFloat(int index, float value) {
        this.buffer.putFloat(this.elementOffset + index, value);
    }

    @Override
    public void next() {
        if (this.currentElementId != 0) {
            throw new IllegalStateException("Not filled all elements of the vertex");
        }
        ++this.vertexCount;
        this.grow();
        if (this.drawMode == VertexFormat.DrawMode.LINES || this.drawMode == VertexFormat.DrawMode.LINE_STRIP) {
            int i = this.format.getVertexSizeByte();
            this.buffer.put(this.elementOffset, this.buffer, this.elementOffset - i, i);
            this.elementOffset += i;
            ++this.vertexCount;
            this.grow();
        }
    }

    @Override
    public void nextElement() {
        VertexFormatElement vertexFormatElement;
        ImmutableList<VertexFormatElement> immutableList = this.format.getElements();
        this.currentElementId = (this.currentElementId + 1) % immutableList.size();
        assert this.currentElement != null;
        this.elementOffset += this.currentElement.getByteLength();
        this.currentElement = vertexFormatElement = immutableList.get(this.currentElementId);
        if (vertexFormatElement.getType() == VertexFormatElement.Type.PADDING) {
            this.nextElement();
        }
        if (this.colorFixed && this.currentElement.getType() == VertexFormatElement.Type.COLOR) {
            BufferVertexConsumer.super.color(this.fixedRed, this.fixedGreen, this.fixedBlue, this.fixedAlpha);
        }
    }

    @Override
    public VertexConsumer color(int red, int green, int blue, int alpha) {
        if (this.colorFixed) {
            throw new IllegalStateException();
        }
        return BufferVertexConsumer.super.color(red, green, blue, alpha);
    }

    @Override
    public void vertex(float x, float y, float z, float red, float green, float blue, float alpha, float u, float v, int overlay, int light, float normalX, float normalY, float normalZ) {
        if (this.colorFixed) {
            throw new IllegalStateException();
        }
        if (this.textured) {
            this.putFloat(0, x);
            this.putFloat(4, y);
            this.putFloat(8, z);
            this.putFloat(12, u);
            this.putFloat(16, v);
            this.putByte(20, (byte)(red * 255.0f));
            this.putByte(21, (byte)(green * 255.0f));
            this.putByte(22, (byte)(blue * 255.0f));
            this.putByte(23, (byte)(alpha * 255.0f));
            this.putByte(24, BufferVertexConsumer.packByte(normalX));
            this.putByte(25, BufferVertexConsumer.packByte(normalY));
            this.putByte(26, BufferVertexConsumer.packByte(normalZ));
            this.elementOffset += 28;
            this.next();
            return;
        }
        super.vertex(x, y, z, red, green, blue, alpha, u, v, overlay, light, normalX, normalY, normalZ);
    }

    void releaseBuiltBuffer() {
        if (this.builtBufferCount > 0 && --this.builtBufferCount == 0) {
            this.clear();
        }
    }

    public void clear() {
        if (this.builtBufferCount > 0) {
            LOGGER.warn("Clearing BufferBuilder with unused batches");
        }
        this.reset();
    }

    public void reset() {
        this.builtBufferCount = 0;
        this.batchOffset = 0;
        this.elementOffset = 0;
    }

    @Override
    public VertexFormatElement getCurrentElement() {
        if (this.currentElement == null) {
            throw new IllegalStateException("BufferBuilder not started");
        }
        return this.currentElement;
    }

    public boolean isBuilding() {
        return this.building;
    }

    ByteBuffer slice(int start, int end) {
        final ByteBuffer tmp = buffer.duplicate();
        tmp.position(start).limit(end);
        return tmp.slice();
    }

    @Environment(value=EnvType.CLIENT)
    public static class State {
        final VertexFormat.DrawMode drawMode;
        final int vertexCount;
        @Nullable
        final Vec3f[] sortingPrimitiveCenters;
        final float sortingCameraX;
        final float sortingCameraY;
        final float sortingCameraZ;

        State(VertexFormat.DrawMode drawMode, int vertexCount, @Nullable Vec3f[] currentParameters, float cameraX, float cameraY, float cameraZ) {
            this.drawMode = drawMode;
            this.vertexCount = vertexCount;
            this.sortingPrimitiveCenters = currentParameters;
            this.sortingCameraX = cameraX;
            this.sortingCameraY = cameraY;
            this.sortingCameraZ = cameraZ;
        }
    }

    @Environment(EnvType.CLIENT)
    public class BuiltBuffer {
        private final int batchOffset;
        private final DrawArrayParameters parameters;
        private boolean released;

        BuiltBuffer(int batchOffset, DrawArrayParameters parameters) {
            this.batchOffset = batchOffset;
            this.parameters = parameters;
        }

        public ByteBuffer getVertexBuffer() {
            int i = this.batchOffset + this.parameters.getVertexBufferPosition();
            int j = this.batchOffset + this.parameters.getVertexBufferLimit();
            return BufferBuilder.this.slice(i, j);
        }

        public ByteBuffer getIndexBuffer() {
            int i = this.batchOffset + this.parameters.getIndexBufferPosition();
            int j = this.batchOffset + this.parameters.getIndexBufferLimit();
            return BufferBuilder.this.slice(i, j);
        }

        public DrawArrayParameters getParameters() {
            return this.parameters;
        }

        public boolean isEmpty() {
            return this.parameters.vertexCount == 0;
        }

        public void release() {
            if (this.released) {
                throw new IllegalStateException("Buffer has already been released!");
            }
            BufferBuilder.this.releaseBuiltBuffer();
            this.released = true;
        }
    }

    @Environment(value=EnvType.CLIENT)
    public record DrawArrayParameters(VertexFormat format, int vertexCount, int indexCount, VertexFormat.DrawMode mode, VertexFormat.IndexType indexType, boolean indexOnly, boolean sequentialIndex) {
        public int getIndexBufferStart() {
            return this.vertexCount * this.format.getVertexSizeByte();
        }

        public int getVertexBufferPosition() {
            return 0;
        }

        public int getVertexBufferLimit() {
            return this.getIndexBufferStart();
        }

        public int getIndexBufferPosition() {
            return this.indexOnly ? 0 : this.getVertexBufferLimit();
        }

        public int getIndexBufferLimit() {
            return this.getIndexBufferPosition() + this.getIndexBufferLength();
        }

        private int getIndexBufferLength() {
            return this.sequentialIndex ? 0 : this.indexCount * this.indexType.size;
        }

        public int getIndexBufferEnd() {
            return this.getIndexBufferLimit();
        }
    }
}

