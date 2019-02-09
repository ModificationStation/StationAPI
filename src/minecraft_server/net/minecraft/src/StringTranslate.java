// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.IOException;
import java.util.Properties;

public class StringTranslate
{

    private StringTranslate()
    {
        translateTable = new Properties();
        try
        {
            translateTable.load((net.minecraft.src.StringTranslate.class).getResourceAsStream("/lang/en_US.lang"));
            translateTable.load((net.minecraft.src.StringTranslate.class).getResourceAsStream("/lang/stats_US.lang"));
        }
        catch(IOException ioexception)
        {
            ioexception.printStackTrace();
        }
    }

    public static StringTranslate getInstance()
    {
        return instance;
    }

    public String translateKey(String s)
    {
        return translateTable.getProperty(s, s);
    }

    public String translateKeyFormat(String s, Object aobj[])
    {
        String s1 = translateTable.getProperty(s, s);
        return String.format(s1, aobj);
    }

    private static StringTranslate instance = new StringTranslate();
    private Properties translateTable;

}
