// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.LinkedList;
import java.util.List;

// Referenced classes of package net.minecraft.src:
//            J_JsonNodeDoesNotMatchJsonNodeSelectorException, J_JsonNodeSelector, J_Functor

public final class J_JsonNodeDoesNotMatchChainedJsonNodeSelectorException extends J_JsonNodeDoesNotMatchJsonNodeSelectorException
{

    static J_JsonNodeDoesNotMatchJsonNodeSelectorException func_27322_a(J_Functor j_functor)
    {
        return new J_JsonNodeDoesNotMatchChainedJsonNodeSelectorException(j_functor, new LinkedList());
    }

    static J_JsonNodeDoesNotMatchJsonNodeSelectorException func_27323_a(J_JsonNodeDoesNotMatchChainedJsonNodeSelectorException j_jsonnodedoesnotmatchchainedjsonnodeselectorexception, J_JsonNodeSelector j_jsonnodeselector)
    {
        LinkedList linkedlist = new LinkedList(j_jsonnodedoesnotmatchchainedjsonnodeselectorexception.field_27325_b);
        linkedlist.add(j_jsonnodeselector);
        return new J_JsonNodeDoesNotMatchChainedJsonNodeSelectorException(j_jsonnodedoesnotmatchchainedjsonnodeselectorexception.field_27326_a, linkedlist);
    }

    static J_JsonNodeDoesNotMatchJsonNodeSelectorException func_27321_b(J_JsonNodeDoesNotMatchChainedJsonNodeSelectorException j_jsonnodedoesnotmatchchainedjsonnodeselectorexception, J_JsonNodeSelector j_jsonnodeselector)
    {
        LinkedList linkedlist = new LinkedList();
        linkedlist.add(j_jsonnodeselector);
        return new J_JsonNodeDoesNotMatchChainedJsonNodeSelectorException(j_jsonnodedoesnotmatchchainedjsonnodeselectorexception.field_27326_a, linkedlist);
    }

    private J_JsonNodeDoesNotMatchChainedJsonNodeSelectorException(J_Functor j_functor, List list)
    {
        super((new StringBuilder()).append("Failed to match any JSON node at [").append(func_27324_a(list)).append("]").toString());
        field_27326_a = j_functor;
        field_27325_b = list;
    }

    static String func_27324_a(List list)
    {
        StringBuilder stringbuilder = new StringBuilder();
        for(int i = list.size() - 1; i >= 0; i--)
        {
            stringbuilder.append(((J_JsonNodeSelector)list.get(i)).func_27358_a());
            if(i != 0)
            {
                stringbuilder.append(".");
            }
        }

        return stringbuilder.toString();
    }

    public String toString()
    {
        return (new StringBuilder()).append("JsonNodeDoesNotMatchJsonNodeSelectorException{failedNode=").append(field_27326_a).append(", failPath=").append(field_27325_b).append('}').toString();
    }

    final J_Functor field_27326_a;
    final List field_27325_b;
}
