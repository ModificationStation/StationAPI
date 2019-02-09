// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.List;

// Referenced classes of package net.minecraft.src:
//            J_LeafFunctor, J_JsonNode

final class J_JsonElementNodeSelector extends J_LeafFunctor
{

    J_JsonElementNodeSelector(int i)
    {
//        super();
        field_27069_a = i;
    }

    public boolean func_27067_a(List list)
    {
        return list.size() > field_27069_a;
    }

    public String func_27060_a()
    {
        return Integer.toString(field_27069_a);
    }

    public J_JsonNode func_27068_b(List list)
    {
        return (J_JsonNode)list.get(field_27069_a);
    }

    public String toString()
    {
        return (new StringBuilder()).append("an element at index [").append(field_27069_a).append("]").toString();
    }

    public Object func_27063_c(Object obj)
    {
        return func_27068_b((List)obj);
    }

    public boolean func_27058_a(Object obj)
    {
        return func_27067_a((List)obj);
    }

    final int field_27069_a; /* synthetic field */
}
