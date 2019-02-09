// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.Map;

// Referenced classes of package net.minecraft.src:
//            J_LeafFunctor, J_JsonStringNode, J_JsonNode

final class J_JsonFieldNodeSelector extends J_LeafFunctor
{

    J_JsonFieldNodeSelector(J_JsonStringNode j_jsonstringnode)
    {
//        super();
        field_27066_a = j_jsonstringnode;
    }

    public boolean func_27065_a(Map map)
    {
        return map.containsKey(field_27066_a);
    }

    public String func_27060_a()
    {
        return (new StringBuilder()).append("\"").append(field_27066_a.func_27216_b()).append("\"").toString();
    }

    public J_JsonNode func_27064_b(Map map)
    {
        return (J_JsonNode)map.get(field_27066_a);
    }

    public String toString()
    {
        return (new StringBuilder()).append("a field called [\"").append(field_27066_a.func_27216_b()).append("\"]").toString();
    }

    public Object func_27063_c(Object obj)
    {
        return func_27064_b((Map)obj);
    }

    public boolean func_27058_a(Object obj)
    {
        return func_27065_a((Map)obj);
    }

    final J_JsonStringNode field_27066_a; /* synthetic field */
}
