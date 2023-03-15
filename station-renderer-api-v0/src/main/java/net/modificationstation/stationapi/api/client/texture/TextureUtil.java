package net.modificationstation.stationapi.api.client.texture;

import net.minecraft.class_214;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;

import static net.modificationstation.stationapi.impl.client.texture.StationRenderImpl.LOGGER;

public class TextureUtil {

    public static final int MIN_MIPMAP_LEVEL = 0;
    private static final int DEFAULT_IMAGE_BUFFER_SIZE = 8192;
    private static int MAX_SUPPORTED_TEXTURE_SIZE = -1;

    public static int generateTextureId() {
        return GL11.glGenTextures();
    }

    public static void releaseTextureId(int id) {
        GL11.glDeleteTextures(id);
    }

    public static void prepareImage(int id, int width, int height) {
        TextureUtil.prepareImage(NativeImage.InternalFormat.RGBA, id, 0, width, height);
    }

    public static void prepareImage(NativeImage.InternalFormat internalFormat, int id, int width, int height) {
        TextureUtil.prepareImage(internalFormat, id, 0, width, height);
    }

    public static void prepareImage(int id, int maxLevel, int width, int height) {
        TextureUtil.prepareImage(NativeImage.InternalFormat.RGBA, id, maxLevel, width, height);
    }

    public static void prepareImage(NativeImage.InternalFormat internalFormat, int id, int maxLevel, int width, int height) {
        TextureUtil.bind(id);
        if (maxLevel >= 0) {
            GL11.glTexParameteri(3553, 33085, maxLevel);
            GL11.glTexParameteri(3553, 33082, 0);
            GL11.glTexParameteri(3553, 33083, maxLevel);
            GL11.glTexParameterf(3553, 34049, 0.0f);
        }
        for (int i = 0; i <= maxLevel; ++i) {
            GL11.glTexImage2D(3553, i, internalFormat.getValue(), width >> i, height >> i, 0, 6408, 5121, (IntBuffer) null);
        }
    }

    private static void bind(int id) {
        GL11.glBindTexture(3553, id);
    }

    public static ByteBuffer readResource(InputStream inputStream) throws IOException {
        ByteBuffer byteBuffer;
        if (inputStream instanceof FileInputStream fileInputStream) {
            FileChannel fileChannel = fileInputStream.getChannel();
            byteBuffer = class_214.method_744((int)fileChannel.size() + 1);
            while (true) {
                if (fileChannel.read(byteBuffer) == -1) break;
            }
        } else {
            byteBuffer = class_214.method_744(DEFAULT_IMAGE_BUFFER_SIZE);
            ReadableByteChannel readableByteChannel = Channels.newChannel(inputStream);
            while (readableByteChannel.read(byteBuffer) != -1) {
                if (byteBuffer.remaining() != 0) continue;
                byteBuffer = class_214.method_744(byteBuffer.capacity() * 2);
            }
        }
        return byteBuffer;
    }

    @Nullable
    public static String readResourceAsString(InputStream inputStream) {
        ByteBuffer byteBuffer;
        try {
            byteBuffer = TextureUtil.readResource(inputStream);
            int i = byteBuffer.position();
            byteBuffer.rewind();
            byte[] ascii = new byte[byteBuffer.capacity()];
            byteBuffer.get(ascii);
            byteBuffer.rewind();
            //noinspection deprecation
            return new String(ascii, 0, 0, i);
        }
        catch (IOException ignored) {
            return null;
        }
    }

    public static void writeAsPNG(Path directory, String prefix, int textureId, int scales, int width, int height) {
        TextureUtil.bind(textureId);
        for (int i = 0; i <= scales; ++i) {
            int j = width >> i;
            int k = height >> i;
            try (NativeImage nativeImage = new NativeImage(j, k, false);){
                nativeImage.loadFromTextureImage(i, false);
                Path path = directory.resolve(prefix + "_" + i + ".png");
                nativeImage.writeTo(path);
                LOGGER.debug("Exported png to: {}", path.toAbsolutePath());
                continue;
            } catch (IOException iOException) {
                LOGGER.debug("Unable to write: ", iOException);
            }
        }
    }

    public static void initTexture(IntBuffer imageData, int width, int height) {
        GL11.glPixelStorei(3312, 0);
        GL11.glPixelStorei(3313, 0);
        GL11.glPixelStorei(3314, 0);
        GL11.glPixelStorei(3315, 0);
        GL11.glPixelStorei(3316, 0);
        GL11.glPixelStorei(3317, 4);
        GL11.glTexImage2D(3553, 0, 6408, width, height, 0, 32993, 33639, imageData);
        GL11.glTexParameteri(3553, 10240, 9728);
        GL11.glTexParameteri(3553, 10241, 9729);
    }

    public static int maxSupportedTextureSize() {
        if (MAX_SUPPORTED_TEXTURE_SIZE == -1) {
            int i = GL11.glGetInteger(3379);
            for (int j = Math.max(32768, i); j >= 1024; j >>= 1) {
                GL11.glTexImage2D(32868, 0, 6408, j, j, 0, 6408, 5121, (IntBuffer) null);
                int k = GL11.glGetTexLevelParameteri(32868, 0, 4096);
                if (k == 0) continue;
                MAX_SUPPORTED_TEXTURE_SIZE = j;
                return j;
            }
            MAX_SUPPORTED_TEXTURE_SIZE = Math.max(i, 1024);
            LOGGER.info("Failed to determine maximum texture size by probing, trying GL_MAX_TEXTURE_SIZE = {}", MAX_SUPPORTED_TEXTURE_SIZE);
        }
        return MAX_SUPPORTED_TEXTURE_SIZE;
    }
}
