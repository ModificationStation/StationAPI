// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.zip.ZipFile;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

// Referenced classes of package net.minecraft.src:
//            TexturePackBase, RenderEngine

public class TexturePackCustom extends TexturePackBase
{

    public TexturePackCustom(File file)
    {
        texturePackName = -1;
        texturePackFileName = file.getName();
        texturePackFile = file;
    }

    private String truncateString(String s)
    {
        if(s != null && s.length() > 34)
        {
            s = s.substring(0, 34);
        }
        return s;
    }

    public void func_6485_a(Minecraft minecraft)
        throws IOException
    {
        ZipFile zipfile = null;
        InputStream inputstream = null;
        try
        {
            zipfile = new ZipFile(texturePackFile);
            try
            {
                inputstream = zipfile.getInputStream(zipfile.getEntry("pack.txt"));
                BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputstream));
                firstDescriptionLine = truncateString(bufferedreader.readLine());
                secondDescriptionLine = truncateString(bufferedreader.readLine());
                bufferedreader.close();
                inputstream.close();
            }
            catch(Exception exception) { }
            try
            {
                inputstream = zipfile.getInputStream(zipfile.getEntry("pack.png"));
                texturePackThumbnail = ImageIO.read(inputstream);
                inputstream.close();
            }
            catch(Exception exception1) { }
            zipfile.close();
        }
        catch(Exception exception2)
        {
            exception2.printStackTrace();
        }
        finally
        {
            try
            {
                inputstream.close();
            }
            catch(Exception exception4) { }
            try
            {
                zipfile.close();
            }
            catch(Exception exception5) { }
        }
    }

    public void func_6484_b(Minecraft minecraft)
    {
        if(texturePackThumbnail != null)
        {
            minecraft.renderEngine.deleteTexture(texturePackName);
        }
        closeTexturePackFile();
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

    public void func_6482_a()
    {
        try
        {
            texturePackZipFile = new ZipFile(texturePackFile);
        }
        catch(Exception exception) { }
    }

    public void closeTexturePackFile()
    {
        try
        {
            texturePackZipFile.close();
        }
        catch(Exception exception) { }
        texturePackZipFile = null;
    }

    public InputStream getResourceAsStream(String s)
    {
        try
        {
            java.util.zip.ZipEntry zipentry = texturePackZipFile.getEntry(s.substring(1));
            if(zipentry != null)
            {
                return texturePackZipFile.getInputStream(zipentry);
            }
        }
        catch(Exception exception) { }
        return (net.minecraft.src.TexturePackBase.class).getResourceAsStream(s);
    }

    private ZipFile texturePackZipFile;
    private int texturePackName;
    private BufferedImage texturePackThumbnail;
    private File texturePackFile;
}
