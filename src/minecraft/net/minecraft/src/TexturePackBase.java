// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.IOException;
import java.io.InputStream;
import net.minecraft.client.Minecraft;

public abstract class TexturePackBase
{

    public TexturePackBase()
    {
    }

    public void func_6482_a()
    {
    }

    public void closeTexturePackFile()
    {
    }

    public void func_6485_a(Minecraft minecraft)
        throws IOException
    {
    }

    public void func_6484_b(Minecraft minecraft)
    {
    }

    public void bindThumbnailTexture(Minecraft minecraft)
    {
    }

    public InputStream getResourceAsStream(String s)
    {
        return (net.minecraft.src.TexturePackBase.class).getResourceAsStream(s);
    }

    public String texturePackFileName;
    public String firstDescriptionLine;
    public String secondDescriptionLine;
    public String field_6488_d;
}
