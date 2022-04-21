package net.modificationstation.stationapi.api.client.texture;

import com.google.common.base.Charsets;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntMaps;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_214;
import net.minecraft.client.Minecraft;
import net.minecraft.client.options.GameOptions;
import net.modificationstation.stationapi.api.util.UnsafeProvider;
import net.modificationstation.stationapi.api.util.Util;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.MemoryUtil;
import org.lwjgl.opengl.GL11;
import sun.misc.Unsafe;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.StandardOpenOption;
import java.util.Base64;
import java.util.EnumSet;
import java.util.Set;

@Environment(EnvType.CLIENT)
public final class NativeImage
implements AutoCloseable {

    private static final Unsafe UNSAFE = UnsafeProvider.theUnsafe;

    private static final Int2IntMap BUFFERED_TO_NATIVE = Int2IntMaps.unmodifiable(Util.make(new Int2IntOpenHashMap(), map -> {
        map.defaultReturnValue(-1);
        map.put(BufferedImage.TYPE_INT_ARGB, 0);
        map.put(BufferedImage.TYPE_INT_RGB, 1);
    }));

    private static final Set<StandardOpenOption> WRITE_TO_FILE_OPEN_OPTIONS = EnumSet.of(StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    private final Format format;
    private final int width;
    private final int height;
    private final boolean isStbImage;
    private ByteBuffer buffer;
    private final long sizeBytes;

    public NativeImage(int width, int height, boolean useStb) {
        this(Format.ABGR, width, height, useStb);
    }

    public NativeImage(Format format, int width, int height, boolean useStb) {
        this.format = format;
        this.width = width;
        this.height = height;
        this.sizeBytes = (long) width * (long) height * (long) format.getChannelCount();
        this.isStbImage = false;
        buffer = ByteBuffer.allocateDirect((int) sizeBytes);
    }

    private NativeImage(Format format, int width, int height, boolean useStb, ByteBuffer buffer) {
        this.format = format;
        this.width = width;
        this.height = height;
        this.isStbImage = useStb;
        this.buffer = buffer;
        this.sizeBytes = (long) width * (long) height * (long) format.getChannelCount();
    }

    @Override
    public String toString() {
        return "NativeImage[" + this.format + " " + this.width + "x" + this.height + "@" + MemoryUtil.getAddress(buffer) + (this.isStbImage ? "S" : "N") + "]";
    }

    public static NativeImage read(InputStream inputStream) throws IOException {
        return NativeImage.read(Format.ABGR, inputStream);
    }

    public static NativeImage read(@Nullable Format format, InputStream inputStream) throws IOException {
        try {
            BufferedImage image = ImageIO.read(inputStream);
            format = format == null ? Format.ALL[BUFFERED_TO_NATIVE.get(image.getType())] : format;
            return new NativeImage(format, image.getWidth(), image.getHeight(), true, read(format, image));
        }
        finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    public static NativeImage read(ByteBuffer byteBuffer) throws IOException {
        return NativeImage.read(Format.ABGR, byteBuffer);
    }

    public static NativeImage read(@Nullable Format format, ByteBuffer byteBuffer) throws IOException {
        byte[] imageBytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(imageBytes);
        ByteArrayInputStream imageStream = new ByteArrayInputStream(imageBytes);
        BufferedImage image = ImageIO.read(imageStream);
        imageStream.close();
        format = format == null ? Format.ALL[BUFFERED_TO_NATIVE.get(image.getType())] : format;
        return new NativeImage(format, image.getWidth(), image.getHeight(), true, read(format, image));
    }

    private static ByteBuffer read(@NotNull Format format, BufferedImage image) {
        int sizeBytes = image.getWidth() * image.getHeight() * format.getChannelCount();
        int[] ints = new int[sizeBytes / format.getChannelCount()];
        byte[] bytes = new byte[sizeBytes];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), ints, 0, image.getWidth());
        for(int var7 = 0; var7 < ints.length; ++var7) {
            int var8 = (ints[var7] >> 24) & 255;
            int var9 = (ints[var7] >> 16) & 255;
            int var10 = (ints[var7] >> 8) & 255;
            int var11 = ints[var7] & 255;
            //noinspection deprecation
            GameOptions gameOptions = ((Minecraft) FabricLoader.getInstance().getGameInstance()).options;
            if (gameOptions != null && gameOptions.anaglyph3d) {
                int var12 = (var9 * 30 + var10 * 59 + var11 * 11) / 100;
                int var13 = (var9 * 30 + var10 * 70) / 100;
                int var14 = (var9 * 30 + var11 * 70) / 100;
                var9 = var12;
                var10 = var13;
                var11 = var14;
            }

            bytes[var7 * 4] = (byte)var9;
            bytes[var7 * 4 + 1] = (byte)var10;
            bytes[var7 * 4 + 2] = (byte)var11;
            bytes[var7 * 4 + 3] = (byte)var8;
        }
        ByteBuffer directBuffer = class_214.method_744(sizeBytes);
        directBuffer.put(bytes);
        directBuffer.position(0);
        return directBuffer;
    }

    private static void setTextureClamp(boolean clamp) {
        if (clamp) {
            GL11.glTexParameteri(3553, 10242, 10496);
            GL11.glTexParameteri(3553, 10243, 10496);
        } else {
            GL11.glTexParameteri(3553, 10242, 10497);
            GL11.glTexParameteri(3553, 10243, 10497);
        }
    }

    private static void setTextureFilter(boolean blur, boolean mipmap) {
        if (blur) {
            GL11.glTexParameteri(3553, 10241, mipmap ? 9987 : 9729);
            GL11.glTexParameteri(3553, 10240, 9729);
        } else {
            GL11.glTexParameteri(3553, 10241, mipmap ? 9986 : 9728);
            GL11.glTexParameteri(3553, 10240, 9728);
        }
    }

    private void checkAllocated() {
        if (buffer == null) {
            throw new IllegalStateException("Image is not allocated.");
        }
    }

    @Override
    public void close() {
        if (buffer != null)
            UNSAFE.invokeCleaner(buffer);
        buffer = null;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public Format getFormat() {
        return this.format;
    }

    public int getColour(int x, int y) {
        if (this.format != NativeImage.Format.ABGR) {
            throw new IllegalArgumentException(String.format("getPixelRGBA only works on RGBA images; have %s", this.format));
        } else if (x <= this.width && y <= this.height) {
            this.checkAllocated();
            int l = (x + y * this.width) * 4;
            return buffer.getInt(l);
        } else {
            throw new IllegalArgumentException(String.format("(%s, %s) outside of image bounds (%s, %s)", x, y, this.width, this.height));
        }
    }

    public void setColour(int x, int y, int color) {
        if (this.format != NativeImage.Format.ABGR) {
            throw new IllegalArgumentException(String.format("getPixelRGBA only works on RGBA images; have %s", this.format));
        } else if (x <= this.width && y <= this.height) {
            this.checkAllocated();
            int l = (x + y * this.width) * 4;
            // I really don't know why but putting an integer into buffer requires inverting the byte order, otherwise it dies
            buffer.putInt(l, getAbgrColor(getRed(color), getGreen(color), getBlue(color), getAlpha(color)));
        } else {
            throw new IllegalArgumentException(String.format("(%s, %s) outside of image bounds (%s, %s)", x, y, this.width, this.height));
        }
    }

    public byte getPixelOpacity(int x, int y) {
        if (!this.format.hasOpacityChannel()) {
            throw new IllegalArgumentException(String.format("no luminance or alpha in %s", this.format));
        } else if (x <= this.width && y <= this.height) {
            int i = (x + y * this.width) * this.format.getChannelCount() + this.format.getOpacityOffset() / 8;
            return buffer.get(i);
        } else {
            throw new IllegalArgumentException(String.format("(%s, %s) outside of image bounds (%s, %s)", x, y, this.width, this.height));
        }
    }

    public int[] makePixelArray() {
        if (this.format != Format.ABGR) {
            throw new UnsupportedOperationException("can only call makePixelArray for RGBA images.");
        }
        this.checkAllocated();
        int[] is = new int[this.getWidth() * this.getHeight()];
        for (int i = 0; i < this.getHeight(); ++i) {
            for (int j = 0; j < this.getWidth(); ++j) {
                int k = this.getColour(j, i);
                int l = NativeImage.getAlpha(k);
                int m = NativeImage.getBlue(k);
                int n = NativeImage.getGreen(k);
                int o = NativeImage.getRed(k);
                is[j + i * this.getWidth()] = l << 24 | o << 16 | n << 8 | m;
            }
        }
        return is;
    }

    public void upload(int level, int offsetX, int offsetY, boolean close) {
        this.upload(level, offsetX, offsetY, 0, 0, this.width, this.height, false, close);
    }

    public void upload(int level, int offsetX, int offsetY, int unpackSkipPixels, int unpackSkipRows, int width, int height, boolean mipmap, boolean close) {
        this.upload(level, offsetX, offsetY, unpackSkipPixels, unpackSkipRows, width, height, false, false, mipmap, close);
    }

    public void upload(int level, int offsetX, int offsetY, int unpackSkipPixels, int unpackSkipRows, int width, int height, boolean blur, boolean clamp, boolean mipmap, boolean close) {
        this.uploadInternal(level, offsetX, offsetY, unpackSkipPixels, unpackSkipRows, width, height, blur, clamp, mipmap, close);
    }

    private void uploadInternal(int level, int xOffset, int yOffset, int unpackSkipPixels, int unpackSkipRows, int width, int height, boolean blur, boolean clamp, boolean mipmap, boolean close) {
        this.checkAllocated();
        setTextureFilter(blur, mipmap);
        setTextureClamp(clamp);
        if (width == this.getWidth()) {
            GL11.glPixelStorei(3314, 0);
        } else {
            GL11.glPixelStorei(3314, this.getWidth());
        }

        GL11.glPixelStorei(3316, unpackSkipPixels);
        GL11.glPixelStorei(3315, unpackSkipRows);
        this.format.setUnpackAlignment();
        GL11.glTexSubImage2D(3553, level, xOffset, yOffset, width, height, this.format.getPixelDataFormat(), 5121, buffer);
        if (close) {
            this.close();
        }
    }

    public void loadFromTextureImage(int level, boolean removeAlpha) {
        this.checkAllocated();
        this.format.setPackAlignment();
        GL11.glGetTexImage(3553, level, this.format.getPixelDataFormat(), 5121, buffer);
        if (removeAlpha && this.format.hasAlphaChannel()) {
            for(int i = 0; i < this.getHeight(); ++i) {
                for(int j = 0; j < this.getWidth(); ++j) {
                    this.setColour(j, i, this.getColour(j, i) | (255 << this.format.getAlphaChannelOffset()));
                }
            }
        }
    }

    public void copyFrom(NativeImage image) {
        if (image.getFormat() != this.format) {
            throw new UnsupportedOperationException("Image formats don't match.");
        }
        int i = this.format.getChannelCount();
        this.checkAllocated();
        image.checkAllocated();
        if (this.width == image.width) {
            buffer.put(image.buffer);
        } else {
            int j = Math.min(this.getWidth(), image.getWidth());
            int k = Math.min(this.getHeight(), image.getHeight());
            byte[] tmp = new byte[j];
            for (int l = 0; l < k; ++l) {
                int m = l * image.getWidth() * i;
                int n = l * this.getWidth() * i;
                image.buffer.get(n, tmp);
                buffer.put(m, tmp);
            }
        }
        buffer.position(0);
    }

    public void fillRect(int x, int y, int width, int height, int color) {
        for (int i = y; i < y + height; ++i) {
            for (int j = x; j < x + width; ++j) {
                this.setColour(j, i, color);
            }
        }
    }

    public void copyRect(int x, int y, int translateX, int translateY, int width, int height, boolean flipX, boolean flipY) {
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                int k = flipX ? width - 1 - j : j;
                int l = flipY ? height - 1 - i : i;
                int m = this.getColour(x + j, y + i);
                this.setColour(x + translateX + k, y + translateY + l, m);
            }
        }
    }

    public void mirrorVertically() {
        this.checkAllocated();
        int i = this.format.getChannelCount();
        int j = this.getWidth() * i;
        ByteBuffer l = class_214.method_744(j);
        byte[] tmp = new byte[j];
        for (int k = 0; k < this.getHeight() / 2; ++k) {
            int m = k * this.getWidth() * i;
            int n = (this.getHeight() - 1 - k) * this.getWidth() * i;
            buffer.get(m, tmp);
            l.put(0, tmp);
            buffer.get(n, tmp);
            buffer.put(m, tmp);
            buffer.put(n, l, 0, j);
        }
    }

    public void untrack() {}

    public static NativeImage read(String dataUri) throws IOException {
        byte[] bs = Base64.getDecoder().decode(dataUri.replaceAll("\n", "").getBytes(Charsets.UTF_8));
        ByteBuffer byteBuffer = class_214.method_744(bs.length);
        byteBuffer.put(bs);
        byteBuffer.rewind();
        return NativeImage.read(byteBuffer);
    }

    public static int getAlpha(int color) {
        return color >> 24 & 0xFF;
    }

    public static int getRed(int color) {
        return color & 0xFF;
    }

    public static int getGreen(int color) {
        return color >> 8 & 0xFF;
    }

    public static int getBlue(int color) {
        return color >> 16 & 0xFF;
    }

    public static int getAbgrColor(int alpha, int blue, int green, int red) {
        return (alpha & 0xFF) << 24 | (blue & 0xFF) << 16 | (green & 0xFF) << 8 | (red & 0xFF);
    }

    @Environment(value=EnvType.CLIENT)
    public enum Format {
        ABGR(4, 6408, true, true, true, false, true, 0, 8, 16, 255, 24, true),
        BGR(3, 6407, true, true, true, false, false, 0, 8, 16, 255, 255, true),
        LUMINANCE_ALPHA(2, 6410, false, false, false, true, true, 255, 255, 255, 0, 8, true),
        LUMINANCE(1, 6409, false, false, false, true, false, 0, 0, 0, 0, 255, true);

        private static final Format[] ALL = values();

        private final int channelCount;
        private final int pixelDataFormat;
        private final boolean hasRed;
        private final boolean hasGreen;
        private final boolean hasBlue;
        private final boolean hasLuminance;
        private final boolean hasAlpha;
        private final int redOffset;
        private final int greenOffset;
        private final int blueOffset;
        private final int luminanceChannelOffset;
        private final int alphaChannelOffset;
        private final boolean writeable;

        Format(int channels, int glFormat, boolean hasRed, boolean hasGreen, boolean hasBlue, boolean hasLuminance, boolean hasAlpha, int redOffset, int greenOffset, int blueOffset, int luminanceOffset, int alphaOffset, boolean writeable) {
            this.channelCount = channels;
            this.pixelDataFormat = glFormat;
            this.hasRed = hasRed;
            this.hasGreen = hasGreen;
            this.hasBlue = hasBlue;
            this.hasLuminance = hasLuminance;
            this.hasAlpha = hasAlpha;
            this.redOffset = redOffset;
            this.greenOffset = greenOffset;
            this.blueOffset = blueOffset;
            this.luminanceChannelOffset = luminanceOffset;
            this.alphaChannelOffset = alphaOffset;
            this.writeable = writeable;
        }

        public int getChannelCount() {
            return this.channelCount;
        }

        public void setPackAlignment() {
            GL11.glPixelStorei(3333, this.getChannelCount());
        }

        public void setUnpackAlignment() {
            GL11.glPixelStorei(3317, this.getChannelCount());
        }

        public int getPixelDataFormat() {
            return this.pixelDataFormat;
        }

        public boolean hasAlphaChannel() {
            return this.hasAlpha;
        }

        public int getAlphaChannelOffset() {
            return this.alphaChannelOffset;
        }

        public boolean hasOpacityChannel() {
            return this.hasLuminance || this.hasAlpha;
        }

        public int getOpacityOffset() {
            return this.hasLuminance ? this.luminanceChannelOffset : this.alphaChannelOffset;
        }

        public boolean isWriteable() {
            return this.writeable;
        }

        private static Format getFormat(int glFormat) {
            switch (glFormat) {
                case 1 -> {
                    return LUMINANCE;
                }
                case 2 -> {
                    return LUMINANCE_ALPHA;
                }
                case 3 -> {
                    return BGR;
                }
            }
            return ABGR;
        }
    }

    @Environment(value=EnvType.CLIENT)
    public enum InternalFormat {
        RGBA(6408),
        RGB(6407),
        RG(33319),
        RED(6403);

        private final int value;

        InternalFormat(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }
}
