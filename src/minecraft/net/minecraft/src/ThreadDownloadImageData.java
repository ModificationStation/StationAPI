// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.awt.image.BufferedImage;

// Referenced classes of package net.minecraft.src:
//            ThreadDownloadImage, ImageBuffer

public class ThreadDownloadImageData
{

    public ThreadDownloadImageData(String s, ImageBuffer imagebuffer)
    {
        referenceCount = 1;
        textureName = -1;
        textureSetupComplete = false;
        (new ThreadDownloadImage(this, s, imagebuffer)).start();
    }

    public BufferedImage image;
    public int referenceCount;
    public int textureName;
    public boolean textureSetupComplete;
}
