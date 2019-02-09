// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            J_LeafFunctor, EnumJsonNodeType, J_JsonNode

final class J_JsonStringNodeSelector extends J_LeafFunctor
{

    J_JsonStringNodeSelector()
    {
    }

    public boolean func_27072_a(J_JsonNode j_jsonnode)
    {
        return EnumJsonNodeType.STRING == j_jsonnode.func_27218_a();
    }

    public String func_27060_a()
    {
        return "A short form string";
    }

    public String func_27073_b(J_JsonNode j_jsonnode)
    {
        return j_jsonnode.func_27216_b();
    }

    public String toString()
    {
        return "a value that is a string";
    }

    public Object func_27063_c(Object obj)
    {
        return func_27073_b((J_JsonNode)obj);
    }

    public boolean func_27058_a(Object obj)
    {
        return func_27072_a((J_JsonNode)obj);
    }
}
