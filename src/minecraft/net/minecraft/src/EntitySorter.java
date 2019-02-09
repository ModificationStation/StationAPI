// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.Comparator;

// Referenced classes of package net.minecraft.src:
//            Entity, WorldRenderer

public class EntitySorter
    implements Comparator
{

    public EntitySorter(Entity entity)
    {
        field_30008_a = -entity.posX;
        field_30007_b = -entity.posY;
        field_30009_c = -entity.posZ;
    }

    public int sortByDistanceToEntity(WorldRenderer worldrenderer, WorldRenderer worldrenderer1)
    {
        double d = (double)worldrenderer.posXPlus + field_30008_a;
        double d1 = (double)worldrenderer.posYPlus + field_30007_b;
        double d2 = (double)worldrenderer.posZPlus + field_30009_c;
        double d3 = (double)worldrenderer1.posXPlus + field_30008_a;
        double d4 = (double)worldrenderer1.posYPlus + field_30007_b;
        double d5 = (double)worldrenderer1.posZPlus + field_30009_c;
        return (int)(((d * d + d1 * d1 + d2 * d2) - (d3 * d3 + d4 * d4 + d5 * d5)) * 1024D);
    }

    public int compare(Object obj, Object obj1)
    {
        return sortByDistanceToEntity((WorldRenderer)obj, (WorldRenderer)obj1);
    }

    private double field_30008_a;
    private double field_30007_b;
    private double field_30009_c;
}
