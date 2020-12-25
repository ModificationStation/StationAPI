package net.modificationstation.stationapi.impl.client.texture;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class OpenGLHelper {

    public static void bindTexture(int target, int texture) {
        if (target == GL11.GL_TEXTURE_2D)
            TextureRegistry.unbind();
        GL11.glBindTexture(target, texture);
    }
}
