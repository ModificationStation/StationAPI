package net.modificationstation.stationapi.api.client.texture;

import org.lwjgl.opengl.GL11;

import java.nio.ByteBuffer;

public class TextureUtil {

    public static int generateId() {
        return GL11.glGenTextures();
    }

    public static void deleteId(int id) {
        GL11.glDeleteTextures(id);
    }

    public static void allocate(int id, int width, int height) {
        allocate(NativeImage.GLFormat.ABGR, id, 0, width, height);
    }

    public static void allocate(int id, int maxLevel, int width, int height) {
        allocate(NativeImage.GLFormat.ABGR, id, maxLevel, width, height);
    }

    public static void allocate(NativeImage.GLFormat format, int id, int maxLevel, int width, int height) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
        if (maxLevel >= 0) {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, 33085, maxLevel);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, 33082, 0);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, 33083, maxLevel);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, 34049, 0.0f);
        }
        for (int i = 0; i <= maxLevel; ++i) {
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, i, format.getGlConstant(), width >> i, height >> i, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer) null);
        }
    }
}
