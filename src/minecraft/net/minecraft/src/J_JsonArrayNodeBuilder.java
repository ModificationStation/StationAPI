// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.*;

// Referenced classes of package net.minecraft.src:
//            J_JsonNodeBuilder, J_JsonNodeFactories, J_JsonRootNode, J_JsonNode

public final class J_JsonArrayNodeBuilder
    implements J_JsonNodeBuilder
{

    J_JsonArrayNodeBuilder()
    {
    }

    public J_JsonArrayNodeBuilder func_27240_a(J_JsonNodeBuilder j_jsonnodebuilder)
    {
        field_27242_a.add(j_jsonnodebuilder);
        return this;
    }

    public J_JsonRootNode func_27241_a()
    {
        LinkedList linkedlist = new LinkedList();
        J_JsonNodeBuilder j_jsonnodebuilder;
        for(Iterator iterator = field_27242_a.iterator(); iterator.hasNext(); linkedlist.add(j_jsonnodebuilder.func_27234_b()))
        {
            j_jsonnodebuilder = (J_JsonNodeBuilder)iterator.next();
        }

        return J_JsonNodeFactories.func_27309_a(linkedlist);
    }

    public J_JsonNode func_27234_b()
    {
        return func_27241_a();
    }

    private final List field_27242_a = new LinkedList();
}
