package net.modificationstation.stationapi.impl.client.texture;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.opengl.GL11;

@Deprecated
@Environment(EnvType.CLIENT)
public class OpenGLHelperOld {

    public static void bindTexture(int target, int texture) {
        if (target == GL11.GL_TEXTURE_2D)
            TextureRegistryOld.unbind();
        GL11.glBindTexture(target, texture);
    }
}
