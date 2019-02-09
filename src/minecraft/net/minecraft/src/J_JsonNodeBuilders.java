// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            J_JsonNullNodeBuilder, J_JsonTrueNodeBuilder, J_JsonFalseNodeBuilder, J_JsonNumberNodeBuilder, 
//            J_JsonStringNodeBuilder, J_JsonObjectNodeBuilder, J_JsonArrayNodeBuilder, J_JsonNodeBuilder

public final class J_JsonNodeBuilders
{

    private J_JsonNodeBuilders()
    {
    }

    public static J_JsonNodeBuilder func_27248_a()
    {
        return new J_JsonNullNodeBuilder();
    }

    public static J_JsonNodeBuilder func_27251_b()
    {
        return new J_JsonTrueNodeBuilder();
    }

    public static J_JsonNodeBuilder func_27252_c()
    {
        return new J_JsonFalseNodeBuilder();
    }

    public static J_JsonNodeBuilder func_27250_a(String s)
    {
        return new J_JsonNumberNodeBuilder(s);
    }

    public static J_JsonStringNodeBuilder func_27254_b(String s)
    {
        return new J_JsonStringNodeBuilder(s);
    }

    public static J_JsonObjectNodeBuilder func_27253_d()
    {
        return new J_JsonObjectNodeBuilder();
    }

    public static J_JsonArrayNodeBuilder func_27249_e()
    {
        return new J_JsonArrayNodeBuilder();
    }
}
