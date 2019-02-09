// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.LinkedList;
import java.util.List;

// Referenced classes of package net.minecraft.src:
//            J_JsonNodeBuilder, J_JsonObjectNodeList, J_JsonNodeFactories, J_JsonFieldBuilder, 
//            J_JsonRootNode, J_JsonNode

public final class J_JsonObjectNodeBuilder
    implements J_JsonNodeBuilder
{

    J_JsonObjectNodeBuilder()
    {
    }

    public J_JsonObjectNodeBuilder func_27237_a(J_JsonFieldBuilder j_jsonfieldbuilder)
    {
        field_27238_a.add(j_jsonfieldbuilder);
        return this;
    }

    public J_JsonRootNode func_27235_a()
    {
        return J_JsonNodeFactories.func_27312_a(new J_JsonObjectNodeList(this));
    }

    public J_JsonNode func_27234_b()
    {
        return func_27235_a();
    }

    static List func_27236_a(J_JsonObjectNodeBuilder j_jsonobjectnodebuilder)
    {
        return j_jsonobjectnodebuilder.field_27238_a;
    }

    private final List field_27238_a = new LinkedList();
}
