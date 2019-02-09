// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.List;
import java.util.Map;

// Referenced classes of package net.minecraft.src:
//            J_JsonNode, EnumJsonNodeType

public final class J_JsonStringNode extends J_JsonNode
    implements Comparable
{

    J_JsonStringNode(String s)
    {
        if(s == null)
        {
            throw new NullPointerException("Attempt to construct a JsonString with a null value.");
        } else
        {
            field_27224_a = s;
            return;
        }
    }

    public EnumJsonNodeType func_27218_a()
    {
        return EnumJsonNodeType.STRING;
    }

    public String func_27216_b()
    {
        return field_27224_a;
    }

    public Map func_27214_c()
    {
        throw new IllegalStateException("Attempt to get fields on a JsonNode without fields.");
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
            J_JsonStringNode j_jsonstringnode = (J_JsonStringNode)obj;
            return field_27224_a.equals(j_jsonstringnode.field_27224_a);
        }
    }

    public int hashCode()
    {
        return field_27224_a.hashCode();
    }

    public String toString()
    {
        return (new StringBuilder()).append("JsonStringNode value:[").append(field_27224_a).append("]").toString();
    }

    public int func_27223_a(J_JsonStringNode j_jsonstringnode)
    {
        return field_27224_a.compareTo(j_jsonstringnode.field_27224_a);
    }

    public int compareTo(Object obj)
    {
        return func_27223_a((J_JsonStringNode)obj);
    }

    private final String field_27224_a;
}
