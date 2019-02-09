// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.File;
import java.util.List;

// Referenced classes of package net.minecraft.src:
//            WorldInfo, WorldProvider, IChunkLoader, IPlayerFileData

public interface ISaveHandler
{

    public abstract WorldInfo func_22096_c();

    public abstract void func_22091_b();

    public abstract IChunkLoader func_22092_a(WorldProvider worldprovider);

    public abstract void func_22095_a(WorldInfo worldinfo, List list);

    public abstract void func_22094_a(WorldInfo worldinfo);

    public abstract IPlayerFileData func_22090_d();

    public abstract void func_22093_e();

    public abstract File func_28111_b(String s);
}
