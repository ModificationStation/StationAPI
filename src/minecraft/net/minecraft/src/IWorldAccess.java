// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            Entity, TileEntity, EntityPlayer

public interface IWorldAccess
{

    public abstract void markBlockAndNeighborsNeedsUpdate(int i, int j, int k);

    public abstract void markBlockRangeNeedsUpdate(int i, int j, int k, int l, int i1, int j1);

    public abstract void playSound(String s, double d, double d1, double d2, 
            float f, float f1);

    public abstract void spawnParticle(String s, double d, double d1, double d2, 
            double d3, double d4, double d5);

    public abstract void obtainEntitySkin(Entity entity);

    public abstract void releaseEntitySkin(Entity entity);

    public abstract void updateAllRenderers();

    public abstract void playRecord(String s, int i, int j, int k);

    public abstract void doNothingWithTileEntity(int i, int j, int k, TileEntity tileentity);

    public abstract void func_28136_a(EntityPlayer entityplayer, int i, int j, int k, int l, int i1);
}
