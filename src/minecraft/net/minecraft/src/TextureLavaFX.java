// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            TextureFX, Block, MathHelper

public class TextureLavaFX extends TextureFX
{

    public TextureLavaFX()
    {
        super(Block.lavaMoving.blockIndexInTexture);
        field_1147_g = new float[256];
        field_1146_h = new float[256];
        field_1145_i = new float[256];
        field_1144_j = new float[256];
    }

    public void onTick()
    {
        for(int i = 0; i < 16; i++)
        {
            for(int j = 0; j < 16; j++)
            {
                float f = 0.0F;
                int l = (int)(MathHelper.sin(((float)j * 3.141593F * 2.0F) / 16F) * 1.2F);
                int i1 = (int)(MathHelper.sin(((float)i * 3.141593F * 2.0F) / 16F) * 1.2F);
                for(int k1 = i - 1; k1 <= i + 1; k1++)
                {
                    for(int i2 = j - 1; i2 <= j + 1; i2++)
                    {
                        int k2 = k1 + l & 0xf;
                        int i3 = i2 + i1 & 0xf;
                        f += field_1147_g[k2 + i3 * 16];
                    }

                }

                field_1146_h[i + j * 16] = f / 10F + ((field_1145_i[(i + 0 & 0xf) + (j + 0 & 0xf) * 16] + field_1145_i[(i + 1 & 0xf) + (j + 0 & 0xf) * 16] + field_1145_i[(i + 1 & 0xf) + (j + 1 & 0xf) * 16] + field_1145_i[(i + 0 & 0xf) + (j + 1 & 0xf) * 16]) / 4F) * 0.8F;
                field_1145_i[i + j * 16] += field_1144_j[i + j * 16] * 0.01F;
                if(field_1145_i[i + j * 16] < 0.0F)
                {
                    field_1145_i[i + j * 16] = 0.0F;
                }
                field_1144_j[i + j * 16] -= 0.06F;
                if(Math.random() < 0.0050000000000000001D)
                {
                    field_1144_j[i + j * 16] = 1.5F;
                }
            }

        }

        float af[] = field_1146_h;
        field_1146_h = field_1147_g;
        field_1147_g = af;
        for(int k = 0; k < 256; k++)
        {
            float f1 = field_1147_g[k] * 2.0F;
            if(f1 > 1.0F)
            {
                f1 = 1.0F;
            }
            if(f1 < 0.0F)
            {
                f1 = 0.0F;
            }
            float f2 = f1;
            int j1 = (int)(f2 * 100F + 155F);
            int l1 = (int)(f2 * f2 * 255F);
            int j2 = (int)(f2 * f2 * f2 * f2 * 128F);
            if(anaglyphEnabled)
            {
                int l2 = (j1 * 30 + l1 * 59 + j2 * 11) / 100;
                int j3 = (j1 * 30 + l1 * 70) / 100;
                int k3 = (j1 * 30 + j2 * 70) / 100;
                j1 = l2;
                l1 = j3;
                j2 = k3;
            }
            imageData[k * 4 + 0] = (byte)j1;
            imageData[k * 4 + 1] = (byte)l1;
            imageData[k * 4 + 2] = (byte)j2;
            imageData[k * 4 + 3] = -1;
        }

    }

    protected float field_1147_g[];
    protected float field_1146_h[];
    protected float field_1145_i[];
    protected float field_1144_j[];
}
