// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            StringTranslate

public class StatCollector
{

    public StatCollector()
    {
    }

    public static String translateToLocal(String s)
    {
        return localizedName.translateKey(s);
    }

    public static String translateToLocalFormatted(String s, Object aobj[])
    {
        return localizedName.translateKeyFormat(s, aobj);
    }

    private static StringTranslate localizedName = StringTranslate.getInstance();

}
