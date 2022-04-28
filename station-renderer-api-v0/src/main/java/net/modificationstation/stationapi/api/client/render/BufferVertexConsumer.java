package net.modificationstation.stationapi.api.client.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public interface BufferVertexConsumer
extends VertexConsumer {
    VertexFormatElement getCurrentElement();

    void nextElement();

    void putByte(int var1, byte var2);

    void putShort(int var1, short var2);

    void putFloat(int var1, float var2);

    @Override
    default VertexConsumer vertex(double x, double y, double z) {
        if (this.getCurrentElement().getType() != VertexFormatElement.Type.POSITION) {
            return this;
        }
        if (this.getCurrentElement().getDataType() != VertexFormatElement.DataType.FLOAT || this.getCurrentElement().getLength() != 3) {
            throw new IllegalStateException();
        }
        this.putFloat(0, (float)x);
        this.putFloat(4, (float)y);
        this.putFloat(8, (float)z);
        this.nextElement();
        return this;
    }

    @Override
    default VertexConsumer color(int red, int green, int blue, int alpha) {
        VertexFormatElement vertexFormatElement = this.getCurrentElement();
        if (vertexFormatElement.getType() != VertexFormatElement.Type.COLOUR) {
            return this;
        }
        if (vertexFormatElement.getDataType() != VertexFormatElement.DataType.UBYTE || vertexFormatElement.getLength() != 4) {
            throw new IllegalStateException();
        }
        this.putByte(0, (byte)red);
        this.putByte(1, (byte)green);
        this.putByte(2, (byte)blue);
        this.putByte(3, (byte)alpha);
        this.nextElement();
        return this;
    }

    @Override
    default VertexConsumer texture(float u, float v) {
        VertexFormatElement vertexFormatElement = this.getCurrentElement();
        if (vertexFormatElement.getType() != VertexFormatElement.Type.UV || vertexFormatElement.getTextureIndex() != 0) {
            return this;
        }
        if (vertexFormatElement.getDataType() != VertexFormatElement.DataType.FLOAT || vertexFormatElement.getLength() != 2) {
            throw new IllegalStateException();
        }
        this.putFloat(0, u);
        this.putFloat(4, v);
        this.nextElement();
        return this;
    }

    @Override
    default VertexConsumer overlay(int u, int v) {
        return this.texture((short)u, (short)v, 1);
    }

    @Override
    default VertexConsumer light(int u, int v) {
        return this.texture((short)u, (short)v, 2);
    }

    default VertexConsumer texture(short u, short v, int index) {
        VertexFormatElement vertexFormatElement = this.getCurrentElement();
        if (vertexFormatElement.getType() != VertexFormatElement.Type.UV || vertexFormatElement.getTextureIndex() != index) {
            return this;
        }
        if (vertexFormatElement.getDataType() != VertexFormatElement.DataType.SHORT || vertexFormatElement.getLength() != 2) {
            throw new IllegalStateException();
        }
        this.putShort(0, u);
        this.putShort(2, v);
        this.nextElement();
        return this;
    }

    @Override
    default VertexConsumer normal(float x, float y, float z) {
        VertexFormatElement vertexFormatElement = this.getCurrentElement();
        if (vertexFormatElement.getType() != VertexFormatElement.Type.NORMAL) {
            return this;
        }
        if (vertexFormatElement.getDataType() != VertexFormatElement.DataType.BYTE || vertexFormatElement.getLength() != 3) {
            throw new IllegalStateException();
        }
        this.putByte(0, BufferVertexConsumer.packByte(x));
        this.putByte(1, BufferVertexConsumer.packByte(y));
        this.putByte(2, BufferVertexConsumer.packByte(z));
        this.nextElement();
        return this;
    }

    static byte packByte(float f) {
        return (byte)((int)(MathHelper.clamp(f, -1.0f, 1.0f) * 127.0f) & 0xFF);
    }
}

