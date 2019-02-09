// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Referenced classes of package net.minecraft.src:
//            ChunkFilePattern

class ChunkFile
    implements Comparable
{

    public ChunkFile(File file)
    {
        field_22209_a = file;
        Matcher matcher = ChunkFilePattern.field_22119_a.matcher(file.getName());
        if(matcher.matches())
        {
            field_22208_b = Integer.parseInt(matcher.group(1), 36);
            field_22210_c = Integer.parseInt(matcher.group(2), 36);
        } else
        {
            field_22208_b = 0;
            field_22210_c = 0;
        }
    }

    public int func_22206_a(ChunkFile chunkfile)
    {
        int i = field_22208_b >> 5;
        int j = chunkfile.field_22208_b >> 5;
        if(i == j)
        {
            int k = field_22210_c >> 5;
            int l = chunkfile.field_22210_c >> 5;
            return k - l;
        } else
        {
            return i - j;
        }
    }

    public File func_22207_a()
    {
        return field_22209_a;
    }

    public int func_22205_b()
    {
        return field_22208_b;
    }

    public int func_22204_c()
    {
        return field_22210_c;
    }

    public int compareTo(Object obj)
    {
        return func_22206_a((ChunkFile)obj);
    }

    private final File field_22209_a;
    private final int field_22208_b;
    private final int field_22210_c;
}
