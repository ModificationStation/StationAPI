// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.File;
import java.util.List;

// Referenced classes of package net.minecraft.src:
//            PlayerNBTManager, WorldProviderHell, McRegionChunkLoader, WorldInfo, 
//            RegionFileCache, WorldProvider, IChunkLoader

public class SaveOldDir extends PlayerNBTManager
{

    public SaveOldDir(File file, String s, boolean flag)
    {
        super(file, s, flag);
    }

    public IChunkLoader func_22092_a(WorldProvider worldprovider)
    {
        File file = getWorldDir();
        if(worldprovider instanceof WorldProviderHell)
        {
            File file1 = new File(file, "DIM-1");
            file1.mkdirs();
            return new McRegionChunkLoader(file1);
        } else
        {
            return new McRegionChunkLoader(file);
        }
    }

    public void func_22095_a(WorldInfo worldinfo, List list)
    {
        worldinfo.setVersion(19132);
        super.func_22095_a(worldinfo, list);
    }

    public void func_22093_e()
    {
        RegionFileCache.func_22122_a();
    }
}
