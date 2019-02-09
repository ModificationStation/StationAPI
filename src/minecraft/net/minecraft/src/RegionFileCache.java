// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.*;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.*;

// Referenced classes of package net.minecraft.src:
//            RegionFile

public class RegionFileCache
{

    private RegionFileCache()
    {
    }

    public static synchronized RegionFile func_22193_a(File file, int i, int j)
    {
        File file1 = new File(file, "region");
        File file2 = new File(file1, (new StringBuilder()).append("r.").append(i >> 5).append(".").append(j >> 5).append(".mcr").toString());
        Reference reference = (Reference)cache.get(file2);
        if(reference != null)
        {
            RegionFile regionfile = (RegionFile)reference.get();
            if(regionfile != null)
            {
                return regionfile;
            }
        }
        if(!file1.exists())
        {
            file1.mkdirs();
        }
        if(cache.size() >= 256)
        {
            func_22192_a();
        }
        RegionFile regionfile1 = new RegionFile(file2);
        cache.put(file2, new SoftReference(regionfile1));
        return regionfile1;
    }

    public static synchronized void func_22192_a()
    {
        Iterator iterator = cache.values().iterator();
        do
        {
            if(!iterator.hasNext())
            {
                break;
            }
            Reference reference = (Reference)iterator.next();
            try
            {
                RegionFile regionfile = (RegionFile)reference.get();
                if(regionfile != null)
                {
                    regionfile.func_22196_b();
                }
            }
            catch(IOException ioexception)
            {
                ioexception.printStackTrace();
            }
        } while(true);
        cache.clear();
    }

    public static int getSizeDelta(File file, int i, int j)
    {
        RegionFile regionfile = func_22193_a(file, i, j);
        return regionfile.func_22209_a();
    }

    public static DataInputStream getChunkInputStream(File file, int i, int j)
    {
        RegionFile regionfile = func_22193_a(file, i, j);
        return regionfile.getChunkDataInputStream(i & 0x1f, j & 0x1f);
    }

    public static DataOutputStream getChunkOutputStream(File file, int i, int j)
    {
        RegionFile regionfile = func_22193_a(file, i, j);
        return regionfile.getChunkDataOutputStream(i & 0x1f, j & 0x1f);
    }

    private static final Map cache = new HashMap();

}
