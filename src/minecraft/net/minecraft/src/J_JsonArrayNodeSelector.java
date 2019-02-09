// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.List;

// Referenced classes of package net.minecraft.src:
//            J_LeafFunctor, EnumJsonNodeType, J_JsonNode

final class J_JsonArrayNodeSelector extends J_LeafFunctor
{

    J_JsonArrayNodeSelector()
    {
    }

    public boolean func_27074_a(J_JsonNode j_jsonnode)
    {
        return EnumJsonNodeType.ARRAY == j_jsonnode.func_27218_a();
    }

    public String func_27060_a()
    {
        return "A short form array";
    }

    public List func_27075_b(J_JsonNode j_jsonnode)
    {
        return j_jsonnode.func_27215_d();
    }

    public String toString()
    {
        return "an array";
    }

    public Object func_27063_c(Object obj)
    {
        return func_27075_b((J_JsonNode)obj);
    }

    public boolean func_27058_a(Object obj)
    {
        return func_27074_a((J_JsonNode)obj);
    }
}
