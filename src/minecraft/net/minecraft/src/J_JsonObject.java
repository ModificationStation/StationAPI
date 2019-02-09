// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.*;

// Referenced classes of package net.minecraft.src:
//            J_JsonRootNode, EnumJsonNodeType

final class J_JsonObject extends J_JsonRootNode
{

    J_JsonObject(Map map)
    {
        field_27222_a = new HashMap(map);
    }

    public Map func_27214_c()
    {
        return new HashMap(field_27222_a);
    }

    public EnumJsonNodeType func_27218_a()
    {
        return EnumJsonNodeType.OBJECT;
    }

    public String func_27216_b()
    {
        throw new IllegalStateException("Attempt to get text on a JsonNode without text.");
    }

    public List func_27215_d()
    {
        throw new IllegalStateException("Attempt to get elements on a JsonNode without elements.");
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
            J_JsonObject j_jsonobject = (J_JsonObject)obj;
            return field_27222_a.equals(j_jsonobject.field_27222_a);
        }
    }

    public int hashCode()
    {
        return field_27222_a.hashCode();
    }

    public String toString()
    {
        return (new StringBuilder()).append("JsonObject fields:[").append(field_27222_a).append("]").toString();
    }

    private final Map field_27222_a;
}
