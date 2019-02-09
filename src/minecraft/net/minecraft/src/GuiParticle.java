// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

// Referenced classes of package net.minecraft.src:
//            Gui, Particle, RenderEngine

public class GuiParticle extends Gui
{

    public GuiParticle(Minecraft minecraft)
    {
        field_25090_a = new ArrayList();
        field_25089_b = minecraft;
    }

    public void func_25088_a()
    {
        for(int i = 0; i < field_25090_a.size(); i++)
        {
            Particle particle = (Particle)field_25090_a.get(i);
            particle.func_25127_a();
            particle.func_25125_a(this);
            if(particle.field_25139_h)
            {
                field_25090_a.remove(i--);
            }
        }

    }

    public void func_25087_a(float f)
    {
        field_25089_b.renderEngine.bindTexture(field_25089_b.renderEngine.getTexture("/gui/particles.png"));
        for(int i = 0; i < field_25090_a.size(); i++)
        {
            Particle particle = (Particle)field_25090_a.get(i);
            int j = (int)((particle.field_25144_c + (particle.field_25146_a - particle.field_25144_c) * (double)f) - 4D);
            int k = (int)((particle.field_25143_d + (particle.field_25145_b - particle.field_25143_d) * (double)f) - 4D);
            float f1 = (float)(particle.field_25129_r + (particle.field_25133_n - particle.field_25129_r) * (double)f);
            float f2 = (float)(particle.field_25132_o + (particle.field_25136_k - particle.field_25132_o) * (double)f);
            float f3 = (float)(particle.field_25131_p + (particle.field_25135_l - particle.field_25131_p) * (double)f);
            float f4 = (float)(particle.field_25130_q + (particle.field_25134_m - particle.field_25130_q) * (double)f);
            GL11.glColor4f(f2, f3, f4, f1);
            drawTexturedModalRect(j, k, 40, 0, 8, 8);
        }

    }

    private List field_25090_a;
    private Minecraft field_25089_b;
}
