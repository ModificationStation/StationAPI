package net.modificationstation.stationapi.api.client.texture;

import net.minecraft.class_214;
import net.modificationstation.stationapi.api.client.blaze3d.platform.GlStateManager;
import net.modificationstation.stationapi.api.client.blaze3d.systems.RenderSystem;
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

public class TextureUtil {

    public static final int MIN_MIPMAP_LEVEL = 0;
    private static final int DEFAULT_IMAGE_BUFFER_SIZE = 8192;

    public static int generateTextureId() {
        RenderSystem.assertOnRenderThreadOrInit();
        return GlStateManager._genTexture();
    }

    public static void releaseTextureId(int id) {
        RenderSystem.assertOnRenderThreadOrInit();
        GlStateManager._deleteTexture(id);
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
        RenderSystem.assertOnRenderThreadOrInit();
        TextureUtil.bind(id);
        if (maxLevel >= 0) {
            GlStateManager._texParameter(3553, 33085, maxLevel);
            GlStateManager._texParameter(3553, 33082, 0);
            GlStateManager._texParameter(3553, 33083, maxLevel);
            GlStateManager._texParameter(3553, 34049, 0.0f);
        }
        for (int i = 0; i <= maxLevel; ++i) {
            GlStateManager._texImage2D(3553, i, internalFormat.getValue(), width >> i, height >> i, 0, 6408, 5121, null);
        }
    }

    private static void bind(int id) {
        RenderSystem.assertOnRenderThreadOrInit();
        GlStateManager._bindTexture(id);
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
        RenderSystem.assertOnRenderThread();
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

//    public static void writeAsPNG(String string, int i, int j, int k, int l) {
//        RenderSystem.assertOnRenderThread();
//        TextureUtil.bind(i);
//        for (int m = 0; m <= j; ++m) {
//            String string2 = string + "_" + m + ".png";
//            int n = k >> m;
//            int o = l >> m;
//            try (NativeImage nativeImage = new NativeImage(n, o, false);){
//                nativeImage.loadFromTextureImage(m, false);
//                nativeImage.writeTo(string2);
//                LOGGER.debug("Exported png to: {}", (Object)new File(string2).getAbsolutePath());
//            }
//            catch (IOException iOException) {
//                LOGGER.debug("Unable to write: ", iOException);
//            }
//        }
//    }

    public static void initTexture(IntBuffer imageData, int width, int height) {
        RenderSystem.assertOnRenderThread();
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
}
