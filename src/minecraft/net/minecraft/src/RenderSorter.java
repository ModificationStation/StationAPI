// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.Comparator;

// Referenced classes of package net.minecraft.src:
//            WorldRenderer, EntityLiving

public class RenderSorter
    implements Comparator
{

    public RenderSorter(EntityLiving entityliving)
    {
        baseEntity = entityliving;
    }

    public int doCompare(WorldRenderer worldrenderer, WorldRenderer worldrenderer1)
    {
        boolean flag = worldrenderer.isInFrustum;
        boolean flag1 = worldrenderer1.isInFrustum;
        if(flag && !flag1)
        {
            return 1;
        }
        if(flag1 && !flag)
        {
            return -1;
        }
        double d = worldrenderer.distanceToEntitySquared(baseEntity);
        double d1 = worldrenderer1.distanceToEntitySquared(baseEntity);
        if(d < d1)
        {
            return 1;
        }
        if(d > d1)
        {
            return -1;
        } else
        {
            return worldrenderer.chunkIndex >= worldrenderer1.chunkIndex ? -1 : 1;
        }
    }

    public int compare(Object obj, Object obj1)
    {
        return doCompare((WorldRenderer)obj, (WorldRenderer)obj1);
    }

    private EntityLiving baseEntity;
}
