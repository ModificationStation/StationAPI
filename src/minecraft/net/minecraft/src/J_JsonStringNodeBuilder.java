// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            J_JsonNodeBuilder, J_JsonNodeFactories, J_JsonStringNode, J_JsonNode

public final class J_JsonStringNodeBuilder
    implements J_JsonNodeBuilder
{

    J_JsonStringNodeBuilder(String s)
    {
        field_27244_a = s;
    }

    public J_JsonStringNode func_27243_a()
    {
        return J_JsonNodeFactories.func_27316_a(field_27244_a);
    }

    public J_JsonNode func_27234_b()
    {
        return func_27243_a();
    }

    private final String field_27244_a;
}
