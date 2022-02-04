package net.modificationstation.stationapi.impl.client.texture;

import org.lwjgl.opengl.GL11;

import java.nio.*;

import static net.modificationstation.stationapi.impl.client.texture.StationRenderAPI.LOGGER;

public class RenderSystem {

    private static int MAX_SUPPORTED_TEXTURE_SIZE = -1;

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
