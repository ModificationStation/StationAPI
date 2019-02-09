// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.imageio.ImageIO;
import org.lwjgl.opengl.GL11;

// Referenced classes of package net.minecraft.src:
//            GLAllocation, TexturePackList, TexturePackBase, GameSettings, 
//            ThreadDownloadImageData, TextureFX, ImageBuffer

public class RenderEngine
{

    public RenderEngine(TexturePackList texturepacklist, GameSettings gamesettings)
    {
        textureMap = new HashMap();
        field_28151_c = new HashMap();
        textureNameToImageMap = new HashMap();
        singleIntBuffer = GLAllocation.createDirectIntBuffer(1);
        imageData = GLAllocation.createDirectByteBuffer(0x100000);
        textureList = new ArrayList();
        urlToImageDataMap = new HashMap();
        clampTexture = false;
        blurTexture = false;
        missingTextureImage = new BufferedImage(64, 64, 2);
        texturePack = texturepacklist;
        options = gamesettings;
        Graphics g = missingTextureImage.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 64, 64);
        g.setColor(Color.BLACK);
        g.drawString("missingtex", 1, 10);
        g.dispose();
    }

    public int[] func_28149_a(String s)
    {
        TexturePackBase texturepackbase = texturePack.selectedTexturePack;
        int ai[] = (int[])field_28151_c.get(s);
        if(ai != null)
        {
            return ai;
        }
        try
        {
            int ai1[] = null;
            if(s.startsWith("##"))
            {
                ai1 = func_28148_b(unwrapImageByColumns(readTextureImage(texturepackbase.getResourceAsStream(s.substring(2)))));
            } else
            if(s.startsWith("%clamp%"))
            {
                clampTexture = true;
                ai1 = func_28148_b(readTextureImage(texturepackbase.getResourceAsStream(s.substring(7))));
                clampTexture = false;
            } else
            if(s.startsWith("%blur%"))
            {
                blurTexture = true;
                ai1 = func_28148_b(readTextureImage(texturepackbase.getResourceAsStream(s.substring(6))));
                blurTexture = false;
            } else
            {
                InputStream inputstream = texturepackbase.getResourceAsStream(s);
                if(inputstream == null)
                {
                    ai1 = func_28148_b(missingTextureImage);
                } else
                {
                    ai1 = func_28148_b(readTextureImage(inputstream));
                }
            }
            field_28151_c.put(s, ai1);
            return ai1;
        }
        catch(IOException ioexception)
        {
            ioexception.printStackTrace();
        }
        int ai2[] = func_28148_b(missingTextureImage);
        field_28151_c.put(s, ai2);
        return ai2;
    }

    private int[] func_28148_b(BufferedImage bufferedimage)
    {
        int i = bufferedimage.getWidth();
        int j = bufferedimage.getHeight();
        int ai[] = new int[i * j];
        bufferedimage.getRGB(0, 0, i, j, ai, 0, i);
        return ai;
    }

    private int[] func_28147_a(BufferedImage bufferedimage, int ai[])
    {
        int i = bufferedimage.getWidth();
        int j = bufferedimage.getHeight();
        bufferedimage.getRGB(0, 0, i, j, ai, 0, i);
        return ai;
    }

    public int getTexture(String s)
    {
        TexturePackBase texturepackbase = texturePack.selectedTexturePack;
        Integer integer = (Integer)textureMap.get(s);
        if(integer != null)
        {
            return integer.intValue();
        }
        try
        {
            singleIntBuffer.clear();
            GLAllocation.generateTextureNames(singleIntBuffer);
            int i = singleIntBuffer.get(0);
            if(s.startsWith("##"))
            {
                setupTexture(unwrapImageByColumns(readTextureImage(texturepackbase.getResourceAsStream(s.substring(2)))), i);
            } else
            if(s.startsWith("%clamp%"))
            {
                clampTexture = true;
                setupTexture(readTextureImage(texturepackbase.getResourceAsStream(s.substring(7))), i);
                clampTexture = false;
            } else
            if(s.startsWith("%blur%"))
            {
                blurTexture = true;
                setupTexture(readTextureImage(texturepackbase.getResourceAsStream(s.substring(6))), i);
                blurTexture = false;
            } else
            {
                InputStream inputstream = texturepackbase.getResourceAsStream(s);
                if(inputstream == null)
                {
                    setupTexture(missingTextureImage, i);
                } else
                {
                    setupTexture(readTextureImage(inputstream), i);
                }
            }
            textureMap.put(s, Integer.valueOf(i));
            return i;
        }
        catch(IOException ioexception)
        {
            ioexception.printStackTrace();
        }
        GLAllocation.generateTextureNames(singleIntBuffer);
        int j = singleIntBuffer.get(0);
        setupTexture(missingTextureImage, j);
        textureMap.put(s, Integer.valueOf(j));
        return j;
    }

    private BufferedImage unwrapImageByColumns(BufferedImage bufferedimage)
    {
        int i = bufferedimage.getWidth() / 16;
        BufferedImage bufferedimage1 = new BufferedImage(16, bufferedimage.getHeight() * i, 2);
        Graphics g = bufferedimage1.getGraphics();
        for(int j = 0; j < i; j++)
        {
            g.drawImage(bufferedimage, -j * 16, j * bufferedimage.getHeight(), null);
        }

        g.dispose();
        return bufferedimage1;
    }

    public int allocateAndSetupTexture(BufferedImage bufferedimage)
    {
        singleIntBuffer.clear();
        GLAllocation.generateTextureNames(singleIntBuffer);
        int i = singleIntBuffer.get(0);
        setupTexture(bufferedimage, i);
        textureNameToImageMap.put(Integer.valueOf(i), bufferedimage);
        return i;
    }

    public void setupTexture(BufferedImage bufferedimage, int i)
    {
        GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, i);
        if(useMipmaps)
        {
            GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 10241 /*GL_TEXTURE_MIN_FILTER*/, 9986 /*GL_NEAREST_MIPMAP_LINEAR*/);
            GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 10240 /*GL_TEXTURE_MAG_FILTER*/, 9728 /*GL_NEAREST*/);
        } else
        {
            GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 10241 /*GL_TEXTURE_MIN_FILTER*/, 9728 /*GL_NEAREST*/);
            GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 10240 /*GL_TEXTURE_MAG_FILTER*/, 9728 /*GL_NEAREST*/);
        }
        if(blurTexture)
        {
            GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 10241 /*GL_TEXTURE_MIN_FILTER*/, 9729 /*GL_LINEAR*/);
            GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 10240 /*GL_TEXTURE_MAG_FILTER*/, 9729 /*GL_LINEAR*/);
        }
        if(clampTexture)
        {
            GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 10242 /*GL_TEXTURE_WRAP_S*/, 10496 /*GL_CLAMP*/);
            GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 10243 /*GL_TEXTURE_WRAP_T*/, 10496 /*GL_CLAMP*/);
        } else
        {
            GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 10242 /*GL_TEXTURE_WRAP_S*/, 10497 /*GL_REPEAT*/);
            GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 10243 /*GL_TEXTURE_WRAP_T*/, 10497 /*GL_REPEAT*/);
        }
        int j = bufferedimage.getWidth();
        int k = bufferedimage.getHeight();
        int ai[] = new int[j * k];
        byte abyte0[] = new byte[j * k * 4];
        bufferedimage.getRGB(0, 0, j, k, ai, 0, j);
        for(int l = 0; l < ai.length; l++)
        {
            int j1 = ai[l] >> 24 & 0xff;
            int l1 = ai[l] >> 16 & 0xff;
            int j2 = ai[l] >> 8 & 0xff;
            int l2 = ai[l] & 0xff;
            if(options != null && options.anaglyph)
            {
                int j3 = (l1 * 30 + j2 * 59 + l2 * 11) / 100;
                int l3 = (l1 * 30 + j2 * 70) / 100;
                int j4 = (l1 * 30 + l2 * 70) / 100;
                l1 = j3;
                j2 = l3;
                l2 = j4;
            }
            abyte0[l * 4 + 0] = (byte)l1;
            abyte0[l * 4 + 1] = (byte)j2;
            abyte0[l * 4 + 2] = (byte)l2;
            abyte0[l * 4 + 3] = (byte)j1;
        }

        imageData.clear();
        imageData.put(abyte0);
        imageData.position(0).limit(abyte0.length);
        GL11.glTexImage2D(3553 /*GL_TEXTURE_2D*/, 0, 6408 /*GL_RGBA*/, j, k, 0, 6408 /*GL_RGBA*/, 5121 /*GL_UNSIGNED_BYTE*/, imageData);
        if(useMipmaps)
        {
            for(int i1 = 1; i1 <= 4; i1++)
            {
                int k1 = j >> i1 - 1;
                int i2 = j >> i1;
                int k2 = k >> i1;
                for(int i3 = 0; i3 < i2; i3++)
                {
                    for(int k3 = 0; k3 < k2; k3++)
                    {
                        int i4 = imageData.getInt((i3 * 2 + 0 + (k3 * 2 + 0) * k1) * 4);
                        int k4 = imageData.getInt((i3 * 2 + 1 + (k3 * 2 + 0) * k1) * 4);
                        int l4 = imageData.getInt((i3 * 2 + 1 + (k3 * 2 + 1) * k1) * 4);
                        int i5 = imageData.getInt((i3 * 2 + 0 + (k3 * 2 + 1) * k1) * 4);
                        int j5 = weightedAverageColor(weightedAverageColor(i4, k4), weightedAverageColor(l4, i5));
                        imageData.putInt((i3 + k3 * i2) * 4, j5);
                    }

                }

                GL11.glTexImage2D(3553 /*GL_TEXTURE_2D*/, i1, 6408 /*GL_RGBA*/, i2, k2, 0, 6408 /*GL_RGBA*/, 5121 /*GL_UNSIGNED_BYTE*/, imageData);
            }

        }
    }

    public void func_28150_a(int ai[], int i, int j, int k)
    {
        GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, k);
        if(useMipmaps)
        {
            GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 10241 /*GL_TEXTURE_MIN_FILTER*/, 9986 /*GL_NEAREST_MIPMAP_LINEAR*/);
            GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 10240 /*GL_TEXTURE_MAG_FILTER*/, 9728 /*GL_NEAREST*/);
        } else
        {
            GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 10241 /*GL_TEXTURE_MIN_FILTER*/, 9728 /*GL_NEAREST*/);
            GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 10240 /*GL_TEXTURE_MAG_FILTER*/, 9728 /*GL_NEAREST*/);
        }
        if(blurTexture)
        {
            GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 10241 /*GL_TEXTURE_MIN_FILTER*/, 9729 /*GL_LINEAR*/);
            GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 10240 /*GL_TEXTURE_MAG_FILTER*/, 9729 /*GL_LINEAR*/);
        }
        if(clampTexture)
        {
            GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 10242 /*GL_TEXTURE_WRAP_S*/, 10496 /*GL_CLAMP*/);
            GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 10243 /*GL_TEXTURE_WRAP_T*/, 10496 /*GL_CLAMP*/);
        } else
        {
            GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 10242 /*GL_TEXTURE_WRAP_S*/, 10497 /*GL_REPEAT*/);
            GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 10243 /*GL_TEXTURE_WRAP_T*/, 10497 /*GL_REPEAT*/);
        }
        byte abyte0[] = new byte[i * j * 4];
        for(int l = 0; l < ai.length; l++)
        {
            int i1 = ai[l] >> 24 & 0xff;
            int j1 = ai[l] >> 16 & 0xff;
            int k1 = ai[l] >> 8 & 0xff;
            int l1 = ai[l] & 0xff;
            if(options != null && options.anaglyph)
            {
                int i2 = (j1 * 30 + k1 * 59 + l1 * 11) / 100;
                int j2 = (j1 * 30 + k1 * 70) / 100;
                int k2 = (j1 * 30 + l1 * 70) / 100;
                j1 = i2;
                k1 = j2;
                l1 = k2;
            }
            abyte0[l * 4 + 0] = (byte)j1;
            abyte0[l * 4 + 1] = (byte)k1;
            abyte0[l * 4 + 2] = (byte)l1;
            abyte0[l * 4 + 3] = (byte)i1;
        }

        imageData.clear();
        imageData.put(abyte0);
        imageData.position(0).limit(abyte0.length);
        GL11.glTexSubImage2D(3553 /*GL_TEXTURE_2D*/, 0, 0, 0, i, j, 6408 /*GL_RGBA*/, 5121 /*GL_UNSIGNED_BYTE*/, imageData);
    }

    public void deleteTexture(int i)
    {
        textureNameToImageMap.remove(Integer.valueOf(i));
        singleIntBuffer.clear();
        singleIntBuffer.put(i);
        singleIntBuffer.flip();
        GL11.glDeleteTextures(singleIntBuffer);
    }

    public int getTextureForDownloadableImage(String s, String s1)
    {
        ThreadDownloadImageData threaddownloadimagedata = (ThreadDownloadImageData)urlToImageDataMap.get(s);
        if(threaddownloadimagedata != null && threaddownloadimagedata.image != null && !threaddownloadimagedata.textureSetupComplete)
        {
            if(threaddownloadimagedata.textureName < 0)
            {
                threaddownloadimagedata.textureName = allocateAndSetupTexture(threaddownloadimagedata.image);
            } else
            {
                setupTexture(threaddownloadimagedata.image, threaddownloadimagedata.textureName);
            }
            threaddownloadimagedata.textureSetupComplete = true;
        }
        if(threaddownloadimagedata == null || threaddownloadimagedata.textureName < 0)
        {
            if(s1 == null)
            {
                return -1;
            } else
            {
                return getTexture(s1);
            }
        } else
        {
            return threaddownloadimagedata.textureName;
        }
    }

    public ThreadDownloadImageData obtainImageData(String s, ImageBuffer imagebuffer)
    {
        ThreadDownloadImageData threaddownloadimagedata = (ThreadDownloadImageData)urlToImageDataMap.get(s);
        if(threaddownloadimagedata == null)
        {
            urlToImageDataMap.put(s, new ThreadDownloadImageData(s, imagebuffer));
        } else
        {
            threaddownloadimagedata.referenceCount++;
        }
        return threaddownloadimagedata;
    }

    public void releaseImageData(String s)
    {
        ThreadDownloadImageData threaddownloadimagedata = (ThreadDownloadImageData)urlToImageDataMap.get(s);
        if(threaddownloadimagedata != null)
        {
            threaddownloadimagedata.referenceCount--;
            if(threaddownloadimagedata.referenceCount == 0)
            {
                if(threaddownloadimagedata.textureName >= 0)
                {
                    deleteTexture(threaddownloadimagedata.textureName);
                }
                urlToImageDataMap.remove(s);
            }
        }
    }

    public void registerTextureFX(TextureFX texturefx)
    {
        textureList.add(texturefx);
        texturefx.onTick();
    }

    public void updateDynamicTextures()
    {
        for(int i = 0; i < textureList.size(); i++)
        {
            TextureFX texturefx = (TextureFX)textureList.get(i);
            texturefx.anaglyphEnabled = options.anaglyph;
            texturefx.onTick();
            imageData.clear();
            imageData.put(texturefx.imageData);
            imageData.position(0).limit(texturefx.imageData.length);
            texturefx.bindImage(this);
            for(int k = 0; k < texturefx.tileSize; k++)
            {
label0:
                for(int i1 = 0; i1 < texturefx.tileSize; i1++)
                {
                    GL11.glTexSubImage2D(3553 /*GL_TEXTURE_2D*/, 0, (texturefx.iconIndex % 16) * 16 + k * 16, (texturefx.iconIndex / 16) * 16 + i1 * 16, 16, 16, 6408 /*GL_RGBA*/, 5121 /*GL_UNSIGNED_BYTE*/, imageData);
                    if(!useMipmaps)
                    {
                        continue;
                    }
                    int k1 = 1;
                    do
                    {
                        if(k1 > 4)
                        {
                            continue label0;
                        }
                        int i2 = 16 >> k1 - 1;
                        int k2 = 16 >> k1;
                        for(int i3 = 0; i3 < k2; i3++)
                        {
                            for(int k3 = 0; k3 < k2; k3++)
                            {
                                int i4 = imageData.getInt((i3 * 2 + 0 + (k3 * 2 + 0) * i2) * 4);
                                int k4 = imageData.getInt((i3 * 2 + 1 + (k3 * 2 + 0) * i2) * 4);
                                int i5 = imageData.getInt((i3 * 2 + 1 + (k3 * 2 + 1) * i2) * 4);
                                int k5 = imageData.getInt((i3 * 2 + 0 + (k3 * 2 + 1) * i2) * 4);
                                int l5 = averageColor(averageColor(i4, k4), averageColor(i5, k5));
                                imageData.putInt((i3 + k3 * k2) * 4, l5);
                            }

                        }

                        GL11.glTexSubImage2D(3553 /*GL_TEXTURE_2D*/, k1, (texturefx.iconIndex % 16) * k2, (texturefx.iconIndex / 16) * k2, k2, k2, 6408 /*GL_RGBA*/, 5121 /*GL_UNSIGNED_BYTE*/, imageData);
                        k1++;
                    } while(true);
                }

            }

        }

label1:
        for(int j = 0; j < textureList.size(); j++)
        {
            TextureFX texturefx1 = (TextureFX)textureList.get(j);
            if(texturefx1.textureId <= 0)
            {
                continue;
            }
            imageData.clear();
            imageData.put(texturefx1.imageData);
            imageData.position(0).limit(texturefx1.imageData.length);
            GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, texturefx1.textureId);
            GL11.glTexSubImage2D(3553 /*GL_TEXTURE_2D*/, 0, 0, 0, 16, 16, 6408 /*GL_RGBA*/, 5121 /*GL_UNSIGNED_BYTE*/, imageData);
            if(!useMipmaps)
            {
                continue;
            }
            int l = 1;
            do
            {
                if(l > 4)
                {
                    continue label1;
                }
                int j1 = 16 >> l - 1;
                int l1 = 16 >> l;
                for(int j2 = 0; j2 < l1; j2++)
                {
                    for(int l2 = 0; l2 < l1; l2++)
                    {
                        int j3 = imageData.getInt((j2 * 2 + 0 + (l2 * 2 + 0) * j1) * 4);
                        int l3 = imageData.getInt((j2 * 2 + 1 + (l2 * 2 + 0) * j1) * 4);
                        int j4 = imageData.getInt((j2 * 2 + 1 + (l2 * 2 + 1) * j1) * 4);
                        int l4 = imageData.getInt((j2 * 2 + 0 + (l2 * 2 + 1) * j1) * 4);
                        int j5 = averageColor(averageColor(j3, l3), averageColor(j4, l4));
                        imageData.putInt((j2 + l2 * l1) * 4, j5);
                    }

                }

                GL11.glTexSubImage2D(3553 /*GL_TEXTURE_2D*/, l, 0, 0, l1, l1, 6408 /*GL_RGBA*/, 5121 /*GL_UNSIGNED_BYTE*/, imageData);
                l++;
            } while(true);
        }

    }

    private int averageColor(int i, int j)
    {
        int k = (i & 0xff000000) >> 24 & 0xff;
        int l = (j & 0xff000000) >> 24 & 0xff;
        return ((k + l >> 1) << 24) + ((i & 0xfefefe) + (j & 0xfefefe) >> 1);
    }

    private int weightedAverageColor(int i, int j)
    {
        int k = (i & 0xff000000) >> 24 & 0xff;
        int l = (j & 0xff000000) >> 24 & 0xff;
        char c = '\377';
        if(k + l == 0)
        {
            k = 1;
            l = 1;
            c = '\0';
        }
        int i1 = (i >> 16 & 0xff) * k;
        int j1 = (i >> 8 & 0xff) * k;
        int k1 = (i & 0xff) * k;
        int l1 = (j >> 16 & 0xff) * l;
        int i2 = (j >> 8 & 0xff) * l;
        int j2 = (j & 0xff) * l;
        int k2 = (i1 + l1) / (k + l);
        int l2 = (j1 + i2) / (k + l);
        int i3 = (k1 + j2) / (k + l);
        return c << 24 | k2 << 16 | l2 << 8 | i3;
    }

    public void refreshTextures()
    {
        TexturePackBase texturepackbase = texturePack.selectedTexturePack;
        int i;
        BufferedImage bufferedimage;
        for(Iterator iterator = textureNameToImageMap.keySet().iterator(); iterator.hasNext(); setupTexture(bufferedimage, i))
        {
            i = ((Integer)iterator.next()).intValue();
            bufferedimage = (BufferedImage)textureNameToImageMap.get(Integer.valueOf(i));
        }

        for(Iterator iterator1 = urlToImageDataMap.values().iterator(); iterator1.hasNext();)
        {
            ThreadDownloadImageData threaddownloadimagedata = (ThreadDownloadImageData)iterator1.next();
            threaddownloadimagedata.textureSetupComplete = false;
        }

        for(Iterator iterator2 = textureMap.keySet().iterator(); iterator2.hasNext();)
        {
            String s = (String)iterator2.next();
            try
            {
                BufferedImage bufferedimage1;
                if(s.startsWith("##"))
                {
                    bufferedimage1 = unwrapImageByColumns(readTextureImage(texturepackbase.getResourceAsStream(s.substring(2))));
                } else
                if(s.startsWith("%clamp%"))
                {
                    clampTexture = true;
                    bufferedimage1 = readTextureImage(texturepackbase.getResourceAsStream(s.substring(7)));
                } else
                if(s.startsWith("%blur%"))
                {
                    blurTexture = true;
                    bufferedimage1 = readTextureImage(texturepackbase.getResourceAsStream(s.substring(6)));
                } else
                {
                    bufferedimage1 = readTextureImage(texturepackbase.getResourceAsStream(s));
                }
                int j = ((Integer)textureMap.get(s)).intValue();
                setupTexture(bufferedimage1, j);
                blurTexture = false;
                clampTexture = false;
            }
            catch(IOException ioexception)
            {
                ioexception.printStackTrace();
            }
        }

        for(Iterator iterator3 = field_28151_c.keySet().iterator(); iterator3.hasNext();)
        {
            String s1 = (String)iterator3.next();
            try
            {
                BufferedImage bufferedimage2;
                if(s1.startsWith("##"))
                {
                    bufferedimage2 = unwrapImageByColumns(readTextureImage(texturepackbase.getResourceAsStream(s1.substring(2))));
                } else
                if(s1.startsWith("%clamp%"))
                {
                    clampTexture = true;
                    bufferedimage2 = readTextureImage(texturepackbase.getResourceAsStream(s1.substring(7)));
                } else
                if(s1.startsWith("%blur%"))
                {
                    blurTexture = true;
                    bufferedimage2 = readTextureImage(texturepackbase.getResourceAsStream(s1.substring(6)));
                } else
                {
                    bufferedimage2 = readTextureImage(texturepackbase.getResourceAsStream(s1));
                }
                func_28147_a(bufferedimage2, (int[])field_28151_c.get(s1));
                blurTexture = false;
                clampTexture = false;
            }
            catch(IOException ioexception1)
            {
                ioexception1.printStackTrace();
            }
        }

    }

    private BufferedImage readTextureImage(InputStream inputstream)
        throws IOException
    {
        BufferedImage bufferedimage = ImageIO.read(inputstream);
        inputstream.close();
        return bufferedimage;
    }

    public void bindTexture(int i)
    {
        if(i < 0)
        {
            return;
        } else
        {
            GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, i);
            return;
        }
    }

    public static boolean useMipmaps = false;
    private HashMap textureMap;
    private HashMap field_28151_c;
    private HashMap textureNameToImageMap;
    private IntBuffer singleIntBuffer;
    private ByteBuffer imageData;
    private java.util.List textureList;
    private Map urlToImageDataMap;
    private GameSettings options;
    private boolean clampTexture;
    private boolean blurTexture;
    private TexturePackList texturePack;
    private BufferedImage missingTextureImage;

}
