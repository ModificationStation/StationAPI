// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

// Referenced classes of package net.minecraft.src:
//            TexturePackBase, RenderEngine

public class TexturePackDefault extends TexturePackBase
{

    public TexturePackDefault()
    {
        texturePackName = -1;
        texturePackFileName = "Default";
        firstDescriptionLine = "The default look of Minecraft";
        try
        {
            texturePackThumbnail = ImageIO.read((net.minecraft.src.TexturePackDefault.class).getResource("/pack.png"));
        }
        catch(IOException ioexception)
        {
            ioexception.printStackTrace();
        }
    }

    public void func_6484_b(Minecraft minecraft)
    {
        if(texturePackThumbnail != null)
        {
            minecraft.renderEngine.deleteTexture(texturePackName);
        }
    }

    public void bindThumbnailTexture(Minecraft minecraft)
    {
        if(texturePackThumbnail != null && texturePackName < 0)
        {
            texturePackName = minecraft.renderEngine.allocateAndSetupTexture(texturePackThumbnail);
        }
        if(texturePackThumbnail != null)
        {
            minecraft.renderEngine.bindTexture(texturePackName);
        } else
        {
            GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, minecraft.renderEngine.getTexture("/gui/unknown_pack.png"));
        }
    }

    private int texturePackName;
    private BufferedImage texturePackThumbnail;
}
