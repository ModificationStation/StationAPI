// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.*;

// Referenced classes of package net.minecraft.src:
//            J_JsonRootNode, EnumJsonNodeType, J_JsonNodeList

final class J_JsonArray extends J_JsonRootNode
{

    J_JsonArray(Iterable iterable)
    {
        field_27221_a = func_27220_a(iterable);
    }

    public EnumJsonNodeType func_27218_a()
    {
        return EnumJsonNodeType.ARRAY;
    }

    public List func_27215_d()
    {
        return new ArrayList(field_27221_a);
    }

    public String func_27216_b()
    {
        throw new IllegalStateException("Attempt to get text on a JsonNode without text.");
    }

    public Map func_27214_c()
    {
        throw new IllegalStateException("Attempt to get fields on a JsonNode without fields.");
    }

    public boolean equals(Object obj)
    {
        if(this == obj)
        {
            return true;
        }
        if(obj == null || getClass() != obj.getClass())
        {
            return false;
        } else
        {
            J_JsonArray j_jsonarray = (J_JsonArray)obj;
            return field_27221_a.equals(j_jsonarray.field_27221_a);
        }
    }

    public int hashCode()
    {
        return field_27221_a.hashCode();
    }

    public String toString()
    {
        return (new StringBuilder()).append("JsonArray elements:[").append(field_27221_a).append("]").toString();
    }

    private static List func_27220_a(Iterable iterable)
    {
        return new J_JsonNodeList(iterable);
    }

    private final List field_27221_a;
}
