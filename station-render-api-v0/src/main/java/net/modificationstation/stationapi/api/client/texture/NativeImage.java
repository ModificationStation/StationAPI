package net.modificationstation.stationapi.api.client.texture;

import com.google.common.primitives.Ints;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_214;
import net.minecraft.client.Minecraft;
import net.minecraft.client.options.GameOptions;
import net.modificationstation.stationapi.api.util.Util;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

import javax.imageio.*;
import java.awt.image.*;
import java.io.*;
import java.nio.*;
import java.nio.file.*;
import java.util.*;

@Environment(value=EnvType.CLIENT)
public final class NativeImage
implements AutoCloseable {

    private static final int[] FORMAT_TO_BUFFERED_IMAGE_FORMAT = Util.make(new int[Format.values().length], ints -> {
        ints[0] = BufferedImage.TYPE_INT_ARGB;
        ints[1] = BufferedImage.TYPE_INT_RGB;
        ints[2] = -1;
        ints[3] = -1;
    });

    private ByteBuffer buffer = class_214.method_744(1048576);

    private static final Set<StandardOpenOption> WRITE_TO_FILE_OPEN_OPTIONS = EnumSet.of(StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    private final Format format;
    private final int width;
    private final int height;
    private final boolean isStbImage;
//    private long pointer;
    private BufferedImage igiveup;
    private final int[] igiveup2;
    private final byte[] igiveup3;
    private final long sizeBytes;

    public NativeImage(int width, int height, boolean useStb) {
        this(Format.ABGR, width, height, useStb);
    }

    public NativeImage(Format format, int width, int height, boolean useStb) {
        this.format = format;
        this.width = width;
        this.height = height;
        this.sizeBytes = (long)width * (long)height * (long)format.getChannelCount();
        this.isStbImage = false;
//        this.pointer = useStb ? MemoryUtil.nmemCalloc((long)1L, (long)this.sizeBytes) : MemoryUtil.nmemAlloc((long)this.sizeBytes);
        igiveup = new BufferedImage(width, height, FORMAT_TO_BUFFERED_IMAGE_FORMAT[format.ordinal()]);
        igiveup2 = new int[getWidth() * getHeight()];
        igiveup3 = new byte[getWidth() * getHeight() * format.getChannelCount()];
    }

//    private NativeImage(Format format, int width, int height, boolean useStb, long pointer) {
//        this.format = format;
//        this.width = width;
//        this.height = height;
//        this.isStbImage = useStb;
//        this.pointer = pointer;
//        this.sizeBytes = (long) width * height * format.getChannelCount();
//    }

    @ApiStatus.Internal
    public NativeImage(Format format, int width, int height, boolean useStb, BufferedImage igiveup) {
        this.format = format;
        this.width = width;
        this.height = height;
        isStbImage = useStb;
        this.igiveup = igiveup;
        igiveup2 = new int[getWidth() * getHeight()];
        igiveup3 = new byte[getWidth() * getHeight() * format.getChannelCount()];
        sizeBytes = (long) width * height * format.getChannelCount();
    }

    public String toString() {
        return "NativeImage[" + this.format + " " + this.width + "x" + this.height + "@" + this.igiveup + (this.isStbImage ? "S" : "N") + "]";
    }

    public static NativeImage read(InputStream inputStream) throws IOException {
        return NativeImage.read(Format.ABGR, inputStream);
    }

    public static NativeImage read(@Nullable Format format, InputStream inputStream) throws IOException {
//        ByteBuffer byteBuffer;
        try {
//            byteBuffer = TextureUtil.readAllToByteBuffer(inputStream);
//            byteBuffer.rewind();
//            return NativeImage.read(format, byteBuffer);
            BufferedImage image = ImageIO.read(inputStream);
            return new NativeImage(format == null ? Format.values()[Ints.asList(FORMAT_TO_BUFFERED_IMAGE_FORMAT).indexOf(image.getType())] : format, image.getWidth(), image.getHeight(), true, image);
        }
        finally {
//            MemoryUtil.memFree((Buffer)byteBuffer);
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
        return new NativeImage(format == null ? Format.values()[Ints.asList(FORMAT_TO_BUFFERED_IMAGE_FORMAT).indexOf(image.getType())] : format, image.getWidth(), image.getHeight(), true, image);
//        if (format != null && !format.isWriteable()) {
//            throw new UnsupportedOperationException("Don't know how to read format " + format);
//        }
//        if (MemoryUtil.memAddress((ByteBuffer)byteBuffer) == 0L) {
//            throw new IllegalArgumentException("Invalid buffer");
//        }
//        try (MemoryStack memoryStack = MemoryStack.stackPush();){
//            IntBuffer intBuffer = memoryStack.mallocInt(1);
//            IntBuffer intBuffer2 = memoryStack.mallocInt(1);
//            IntBuffer intBuffer3 = memoryStack.mallocInt(1);
//            ByteBuffer byteBuffer2 = STBImage.stbi_load_from_memory((ByteBuffer)byteBuffer, (IntBuffer)intBuffer, (IntBuffer)intBuffer2, (IntBuffer)intBuffer3, (int)(format == null ? 0 : format.channelCount));
//            if (byteBuffer2 == null) {
//                throw new IOException("Could not load image: " + STBImage.stbi_failure_reason());
//            }
//            NativeImage nativeImage = new NativeImage(format == null ? Format.getFormat(intBuffer3.get(0)) : format, intBuffer.get(0), intBuffer2.get(0), true, MemoryUtil.memAddress((ByteBuffer)byteBuffer2));
//            return nativeImage;
//        }
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
        if (this.igiveup == null) {
            throw new IllegalStateException("Image is not allocated.");
        }
    }

    public void close() {
        if (this.igiveup != null) {
            igiveup.flush();
//            if (this.isStbImage) {
//                STBImage.nstbi_image_free((long)this.pointer);
//            } else {
//                MemoryUtil.nmemFree((long)this.pointer);
//            }
        }
        this.igiveup = null;
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

    public int getPixelColor(int x, int y) {
        if (this.format != Format.ABGR) {
            throw new IllegalArgumentException(String.format("getPixelRGBA only works on RGBA images; have %s", this.format));
        }
        if (x > this.width || y > this.height) {
            throw new IllegalArgumentException(String.format("(%s, %s) outside of image bounds (%s, %s)", x, y, this.width, this.height));
        }
        this.checkAllocated();
        return igiveup.getRGB(x, y);
//        long l = (x + (long) y * this.width) * 4;
//        return MemoryUtil.memGetInt((long)(this.pointer + l));
    }

    public void setPixelColor(int x, int y, int color) {
        if (this.format != Format.ABGR) {
            throw new IllegalArgumentException(String.format("getPixelRGBA only works on RGBA images; have %s", this.format));
        }
        if (x > this.width || y > this.height) {
            throw new IllegalArgumentException(String.format("(%s, %s) outside of image bounds (%s, %s)", x, y, this.width, this.height));
        }
        this.checkAllocated();
        igiveup.setRGB(x, y, color);
//        long l = (x + y * this.width) * 4;
//        MemoryUtil.memPutInt((long)(this.pointer + l), (int)color);
    }

    public byte getPixelOpacity(int x, int y) {
        if (!this.format.hasOpacityChannel()) {
            throw new IllegalArgumentException(String.format("no luminance or alpha in %s", this.format));
        }
        if (x > this.width || y > this.height) {
            throw new IllegalArgumentException(String.format("(%s, %s) outside of image bounds (%s, %s)", x, y, this.width, this.height));
        }
        return (byte) (igiveup.getRGB(x, y) >> (this.format.getOpacityOffset()));
//        int i = (x + y * this.width) * this.format.getChannelCount() + this.format.getOpacityOffset() / 8;
//        return MemoryUtil.memGetByte((long)(this.pointer + (long)i));
    }

    @Deprecated
    public int[] makePixelArray() {
        if (this.format != Format.ABGR) {
            throw new UnsupportedOperationException("can only call makePixelArray for RGBA images.");
        }
        this.checkAllocated();
        int[] is = new int[this.getWidth() * this.getHeight()];
        for (int i = 0; i < this.getHeight(); ++i) {
            for (int j = 0; j < this.getWidth(); ++j) {
                int k = this.getPixelColor(j, i);
                int l = NativeImage.getAlpha(k);
                int m = NativeImage.getBlue(k);
                int n = NativeImage.getGreen(k);
                int o = NativeImage.getRed(k);
                is[j + i * this.getWidth()] = l << 24 | m << 16 | n << 8 | o;
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
        NativeImage.setTextureFilter(blur, mipmap);
        NativeImage.setTextureClamp(clamp);
        if (width == this.getWidth()) {
            GL11.glPixelStorei(3314, 0);
        } else {
            GL11.glPixelStorei(3314, this.getWidth());
        }
        GL11.glPixelStorei(3316, unpackSkipPixels);
        GL11.glPixelStorei(3315, unpackSkipRows);
        this.format.setUnpackAlignment();

        igiveup.getRGB(0, 0, getWidth(), getHeight(), igiveup2, 0, getWidth());

        //noinspection deprecation
        GameOptions gameOptions = ((Minecraft) FabricLoader.getInstance().getGameInstance()).options;
        for(int var7 = 0; var7 < igiveup2.length; ++var7) {
            int var8 = (igiveup2[var7] >> 24) & 255;
            int var9 = (igiveup2[var7] >> 16) & 255;
            int var10 = (igiveup2[var7] >> 8) & 255;
            int var11 = igiveup2[var7] & 255;
            if (gameOptions != null && gameOptions.anaglyph3d) {
                int var12 = (var9 * 30 + var10 * 59 + var11 * 11) / 100;
                int var13 = (var9 * 30 + var10 * 70) / 100;
                int var14 = (var9 * 30 + var11 * 70) / 100;
                var9 = var12;
                var10 = var13;
                var11 = var14;
            }

            igiveup3[var7 * 4] = (byte)var9;
            igiveup3[var7 * 4 + 1] = (byte)var10;
            igiveup3[var7 * 4 + 2] = (byte)var11;
            igiveup3[var7 * 4 + 3] = (byte)var8;
        }

        this.buffer.clear();
        if (igiveup3.length > buffer.capacity())
            buffer = class_214.method_744(igiveup3.length);
        this.buffer.put(igiveup3);
        this.buffer.position(0).limit(igiveup3.length);
        GL11.glTexSubImage2D(3553, level, xOffset, yOffset, width, height, this.format.getPixelDataFormat(), 5121, this.buffer);
        if (close) {
            this.close();
        }
    }

    public void loadFromTextureImage(int level, boolean removeAlpha) {
        this.checkAllocated();
        this.format.setPackAlignment();
        int[] var5 = new int[getWidth() * getHeight()];
        byte[] var6 = new byte[getWidth() * getHeight() * format.getChannelCount()];
        igiveup.getRGB(0, 0, getWidth(), getHeight(), var5, 0, getWidth());

        //noinspection deprecation
        GameOptions gameOptions = ((Minecraft) FabricLoader.getInstance().getGameInstance()).options;
        for(int var7 = 0; var7 < var5.length; ++var7) {
            int var8 = var5[var7] >> 24 & 255;
            int var9 = var5[var7] >> 16 & 255;
            int var10 = var5[var7] >> 8 & 255;
            int var11 = var5[var7] & 255;
            if (gameOptions != null && gameOptions.anaglyph3d) {
                int var12 = (var9 * 30 + var10 * 59 + var11 * 11) / 100;
                int var13 = (var9 * 30 + var10 * 70) / 100;
                int var14 = (var9 * 30 + var11 * 70) / 100;
                var9 = var12;
                var10 = var13;
                var11 = var14;
            }

            var6[var7 * 4] = (byte)var9;
            var6[var7 * 4 + 1] = (byte)var10;
            var6[var7 * 4 + 2] = (byte)var11;
            var6[var7 * 4 + 3] = (byte)var8;
        }

        this.buffer.clear();
        if (var6.length != buffer.capacity())
            buffer = class_214.method_744(var6.length);
        this.buffer.put(var6);
        this.buffer.position(0).limit(var6.length);
        GL11.glGetTexImage(3553, level, this.format.getPixelDataFormat(), 5121, this.buffer);
        if (removeAlpha && this.format.hasAlphaChannel()) {
            for (int i = 0; i < this.getHeight(); ++i) {
                for (int j = 0; j < this.getWidth(); ++j) {
                    this.setPixelColor(j, i, this.getPixelColor(j, i) | 255 << this.format.getAlphaChannelOffset());
                }
            }
        }
    }

//    public void writeFile(File file) throws IOException {
//        this.writeFile(file.toPath());
//    }
//
//    public void makeGlyphBitmapSubpixel(STBTTFontinfo fontInfo, int glyphIndex, int width, int height, float scaleX, float scaleY, float shiftX, float shiftY, int startX, int startY) {
//        if (startX < 0 || startX + width > this.getWidth() || startY < 0 || startY + height > this.getHeight()) {
//            throw new IllegalArgumentException(String.format("Out of bounds: start: (%s, %s) (size: %sx%s); size: %sx%s", new Object[]{startX, startY, width, height, this.getWidth(), this.getHeight()}));
//        }
//        if (this.format.getChannelCount() != 1) {
//            throw new IllegalArgumentException("Can only write fonts into 1-component images.");
//        }
//        STBTruetype.nstbtt_MakeGlyphBitmapSubpixel((long)fontInfo.address(), (long)(this.pointer + (long)startX + (long)(startY * this.getWidth())), (int)width, (int)height, (int)this.getWidth(), (float)scaleX, (float)scaleY, (float)shiftX, (float)shiftY, (int)glyphIndex);
//    }
//
//    public void writeFile(Path path) throws IOException {
//        if (!this.format.isWriteable()) {
//            throw new UnsupportedOperationException("Don't know how to write format " + this.format);
//        }
//        this.checkAllocated();
//        try (SeekableByteChannel writableByteChannel = Files.newByteChannel(path, WRITE_TO_FILE_OPEN_OPTIONS, new FileAttribute[0]);){
//            if (!this.write(writableByteChannel)) {
//                throw new IOException("Could not write image to the PNG file \"" + path.toAbsolutePath() + "\": " + STBImage.stbi_failure_reason());
//            }
//        }
//    }

    /*
     * Exception decompiling
     */
    public byte[] getBytes() throws IOException {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [0[TRYBLOCK]], but top level block is 4[TRYBLOCK]
         * org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:429)
         * org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:478)
         * org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:728)
         * org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:806)
         * org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:258)
         * org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:192)
         * org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         * org.benf.cfr.reader.entities.Method.analyse(Method.java:521)
         * org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1035)
         * org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:922)
         * org.benf.cfr.reader.Driver.doClass(Driver.java:83)
         * org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:67)
         * net.fabricmc.loom.decompilers.cfr.FabricCFRDecompiler.lambda$decompile$2(FabricCFRDecompiler.java:182)
         * java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:511)
         * java.util.concurrent.FutureTask.run(FutureTask.java:266)
         * java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
         * java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
         * java.lang.Thread.run(Thread.java:748)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
//    private boolean write(WritableByteChannel writableByteChannel) throws IOException {
//        WriteCallback writeCallback = new WriteCallback(writableByteChannel);
//        try {
//            int i = Math.min(this.getHeight(), Integer.MAX_VALUE / this.getWidth() / this.format.getChannelCount());
//            if (i < this.getHeight()) {
//                LOGGER.warn("Dropping image height from {} to {} to fit the size into 32-bit signed int", Integer.valueOf(this.getHeight()), Integer.valueOf(i));
//            }
//            if (STBImageWrite.nstbi_write_png_to_func((long)writeCallback.address(), (long)0L, (int)this.getWidth(), (int)i, (int)this.format.getChannelCount(), (long)this.pointer, (int)0) == 0) {
//                boolean bl = false;
//                return bl;
//            }
//            writeCallback.throwStoredException();
//            boolean bl = true;
//            return bl;
//        }
//        finally {
//            writeCallback.free();
//        }
//    }

//    public void copyFrom(NativeImage image) {
//        if (image.getFormat() != this.format) {
//            throw new UnsupportedOperationException("Image formats don't match.");
//        }
//        int i = this.format.getChannelCount();
//        this.checkAllocated();
//        image.checkAllocated();
//        if (this.width == image.width) {
//            MemoryUtil.memCopy((long)image.pointer, (long)this.pointer, (long)Math.min(this.sizeBytes, image.sizeBytes));
//        } else {
//            int j = Math.min(this.getWidth(), image.getWidth());
//            int k = Math.min(this.getHeight(), image.getHeight());
//            for (int l = 0; l < k; ++l) {
//                int m = l * image.getWidth() * i;
//                int n = l * this.getWidth() * i;
//                MemoryUtil.memCopy((long)(image.pointer + (long)m), (long)(this.pointer + (long)n), (long)j);
//            }
//        }
//    }
//
//    public void fillRect(int x, int y, int width, int height, int color) {
//        for (int i = y; i < y + height; ++i) {
//            for (int j = x; j < x + width; ++j) {
//                this.setPixelColor(j, i, color);
//            }
//        }
//    }
//
//    public void copyRect(int x, int y, int translateX, int translateY, int width, int height, boolean flipX, boolean flipY) {
//        for (int i = 0; i < height; ++i) {
//            for (int j = 0; j < width; ++j) {
//                int k = flipX ? width - 1 - j : j;
//                int l = flipY ? height - 1 - i : i;
//                int m = this.getPixelColor(x + j, y + i);
//                this.setPixelColor(x + translateX + k, y + translateY + l, m);
//            }
//        }
//    }
//
//    public void mirrorVertically() {
//        this.checkAllocated();
//        try (MemoryStack memoryStack = MemoryStack.stackPush();){
//            int i = this.format.getChannelCount();
//            int j = this.getWidth() * i;
//            long l = memoryStack.nmalloc(j);
//            for (int k = 0; k < this.getHeight() / 2; ++k) {
//                int m = k * this.getWidth() * i;
//                int n = (this.getHeight() - 1 - k) * this.getWidth() * i;
//                MemoryUtil.memCopy((long)(this.pointer + (long)m), (long)l, (long)j);
//                MemoryUtil.memCopy((long)(this.pointer + (long)n), (long)(this.pointer + (long)m), (long)j);
//                MemoryUtil.memCopy((long)l, (long)(this.pointer + (long)n), (long)j);
//            }
//        }
//    }
//
//    public void resizeSubRectTo(int x, int y, int width, int height, NativeImage targetImage) {
//        this.checkAllocated();
//        if (targetImage.getFormat() != this.format) {
//            throw new UnsupportedOperationException("resizeSubRectTo only works for images of the same format.");
//        }
//        int i = this.format.getChannelCount();
//        STBImageResize.nstbir_resize_uint8((long)(this.pointer + (long)((x + y * this.getWidth()) * i)), (int)width, (int)height, (int)(this.getWidth() * i), (long)targetImage.pointer, (int)targetImage.getWidth(), (int)targetImage.getHeight(), (int)0, (int)i);
//    }

    public void untrack() {
//        Untracker.untrack(this.pointer);
    }

//    public static NativeImage read(String dataUri) throws IOException {
//        byte[] bs = Base64.getDecoder().decode(dataUri.replaceAll("\n", "").getBytes(Charsets.UTF_8));
//        try (MemoryStack memoryStack = MemoryStack.stackPush();){
//            ByteBuffer byteBuffer = memoryStack.malloc(bs.length);
//            byteBuffer.put(bs);
//            byteBuffer.rewind();
//            NativeImage nativeImage = NativeImage.read(byteBuffer);
//            return nativeImage;
//        }
//    }

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

    @Environment(EnvType.CLIENT)
    public enum GLFormat {
        ABGR(6408),
        BGR(6407),
        LUMINANCE_ALPHA(6410),
        LUMINANCE(6409),
        INTENSITY(32841);

        private final int glConstant;

        GLFormat(int glConstant) {
            this.glConstant = glConstant;
        }

        int getGlConstant() {
            return this.glConstant;
        }
    }

//    @Environment(value=EnvType.CLIENT)
//    static class WriteCallback
//    extends STBIWriteCallback {
//        private final WritableByteChannel channel;
//        @Nullable
//        private IOException exception;
//
//        private WriteCallback(WritableByteChannel channel) {
//            this.channel = channel;
//        }
//
//        public void invoke(long context, long data, int size) {
//            ByteBuffer byteBuffer = WriteCallback.getData((long)data, (int)size);
//            try {
//                this.channel.write(byteBuffer);
//            }
//            catch (IOException iOException) {
//                this.exception = iOException;
//            }
//        }
//
//        public void throwStoredException() throws IOException {
//            if (this.exception != null) {
//                throw this.exception;
//            }
//        }
//    }
}
