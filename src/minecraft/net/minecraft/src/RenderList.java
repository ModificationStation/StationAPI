// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.nio.IntBuffer;
import org.lwjgl.opengl.GL11;

// Referenced classes of package net.minecraft.src:
//            GLAllocation

public class RenderList
{

    public RenderList()
    {
        field_1236_g = GLAllocation.createDirectIntBuffer(0x10000);
        field_1235_h = false;
        field_1234_i = false;
    }

    public void func_861_a(int i, int j, int k, double d, double d1, 
            double d2)
    {
        field_1235_h = true;
        field_1236_g.clear();
        field_1242_a = i;
        field_1241_b = j;
        field_1240_c = k;
        field_1239_d = (float)d;
        field_1238_e = (float)d1;
        field_1237_f = (float)d2;
    }

    public boolean func_862_a(int i, int j, int k)
    {
        if(!field_1235_h)
        {
            return false;
        } else
        {
            return i == field_1242_a && j == field_1241_b && k == field_1240_c;
        }
    }

    public void func_858_a(int i)
    {
        field_1236_g.put(i);
        if(field_1236_g.remaining() == 0)
        {
            func_860_a();
        }
    }

    public void func_860_a()
    {
        if(!field_1235_h)
        {
            return;
        }
        if(!field_1234_i)
        {
            field_1236_g.flip();
            field_1234_i = true;
        }
        if(field_1236_g.remaining() > 0)
        {
            GL11.glPushMatrix();
            GL11.glTranslatef((float)field_1242_a - field_1239_d, (float)field_1241_b - field_1238_e, (float)field_1240_c - field_1237_f);
            GL11.glCallLists(field_1236_g);
            GL11.glPopMatrix();
        }
    }

    public void func_859_b()
    {
        field_1235_h = false;
        field_1234_i = false;
    }

    private int field_1242_a;
    private int field_1241_b;
    private int field_1240_c;
    private float field_1239_d;
    private float field_1238_e;
    private float field_1237_f;
    private IntBuffer field_1236_g;
    private boolean field_1235_h;
    private boolean field_1234_i;
}
