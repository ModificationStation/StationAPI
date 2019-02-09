// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Referenced classes of package net.minecraft.src:
//            Empty2

class ChunkFilePattern
    implements FilenameFilter
{

    private ChunkFilePattern()
    {
    }

    public boolean accept(File file, String s)
    {
        Matcher matcher = field_22189_a.matcher(s);
        return matcher.matches();
    }

    ChunkFilePattern(Empty2 empty2)
    {
        this();
    }

    public static final Pattern field_22189_a = Pattern.compile("c\\.(-?[0-9a-z]+)\\.(-?[0-9a-z]+)\\.dat");

}
