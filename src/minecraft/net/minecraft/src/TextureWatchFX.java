// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;

// Referenced classes of package net.minecraft.src:
//            TextureFX, Item, World, WorldProvider

public class TextureWatchFX extends TextureFX
{

    public TextureWatchFX(Minecraft minecraft)
    {
        super(Item.pocketSundial.getIconFromDamage(0));
        watchIconImageData = new int[256];
        dialImageData = new int[256];
        mc = minecraft;
        tileImage = 1;
        try
        {
            BufferedImage bufferedimage = ImageIO.read((net.minecraft.client.Minecraft.class).getResource("/gui/items.png"));
            int i = (iconIndex % 16) * 16;
            int j = (iconIndex / 16) * 16;
            bufferedimage.getRGB(i, j, 16, 16, watchIconImageData, 0, 16);
            bufferedimage = ImageIO.read((net.minecraft.client.Minecraft.class).getResource("/misc/dial.png"));
            bufferedimage.getRGB(0, 0, 16, 16, dialImageData, 0, 16);
        }
        catch(IOException ioexception)
        {
            ioexception.printStackTrace();
        }
    }

    public void onTick()
    {
        double d = 0.0D;
        if(mc.theWorld != null && mc.thePlayer != null)
        {
            float f = mc.theWorld.getCelestialAngle(1.0F);
            d = -f * 3.141593F * 2.0F;
            if(mc.theWorld.worldProvider.isNether)
            {
                d = Math.random() * 3.1415927410125732D * 2D;
            }
        }
        double d1;
        for(d1 = d - field_4222_j; d1 < -3.1415926535897931D; d1 += 6.2831853071795862D) { }
        for(; d1 >= 3.1415926535897931D; d1 -= 6.2831853071795862D) { }
        if(d1 < -1D)
        {
            d1 = -1D;
        }
        if(d1 > 1.0D)
        {
            d1 = 1.0D;
        }
        field_4221_k += d1 * 0.10000000000000001D;
        field_4221_k *= 0.80000000000000004D;
        field_4222_j += field_4221_k;
        double d2 = Math.sin(field_4222_j);
        double d3 = Math.cos(field_4222_j);
        for(int i = 0; i < 256; i++)
        {
            int j = watchIconImageData[i] >> 24 & 0xff;
            int k = watchIconImageData[i] >> 16 & 0xff;
            int l = watchIconImageData[i] >> 8 & 0xff;
            int i1 = watchIconImageData[i] >> 0 & 0xff;
            if(k == i1 && l == 0 && i1 > 0)
            {
                double d4 = -((double)(i % 16) / 15D - 0.5D);
                double d5 = (double)(i / 16) / 15D - 0.5D;
                int i2 = k;
                int j2 = (int)((d4 * d3 + d5 * d2 + 0.5D) * 16D);
                int k2 = (int)(((d5 * d3 - d4 * d2) + 0.5D) * 16D);
                int l2 = (j2 & 0xf) + (k2 & 0xf) * 16;
                j = dialImageData[l2] >> 24 & 0xff;
                k = ((dialImageData[l2] >> 16 & 0xff) * i2) / 255;
                l = ((dialImageData[l2] >> 8 & 0xff) * i2) / 255;
                i1 = ((dialImageData[l2] >> 0 & 0xff) * i2) / 255;
            }
            if(anaglyphEnabled)
            {
                int j1 = (k * 30 + l * 59 + i1 * 11) / 100;
                int k1 = (k * 30 + l * 70) / 100;
                int l1 = (k * 30 + i1 * 70) / 100;
                k = j1;
                l = k1;
                i1 = l1;
            }
            imageData[i * 4 + 0] = (byte)k;
            imageData[i * 4 + 1] = (byte)l;
            imageData[i * 4 + 2] = (byte)i1;
            imageData[i * 4 + 3] = (byte)j;
        }

    }

    private Minecraft mc;
    private int watchIconImageData[];
    private int dialImageData[];
    private double field_4222_j;
    private double field_4221_k;
}
