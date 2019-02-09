// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.File;
import java.util.List;

// Referenced classes of package net.minecraft.src:
//            WorldInfo, WorldProvider, IChunkLoader

public interface ISaveHandler
{

    public abstract WorldInfo loadWorldInfo();

    public abstract void func_22150_b();

    public abstract IChunkLoader getChunkLoader(WorldProvider worldprovider);

    public abstract void saveWorldInfoAndPlayer(WorldInfo worldinfo, List list);

    public abstract void saveWorldInfo(WorldInfo worldinfo);

    public abstract File func_28113_a(String s);
}
