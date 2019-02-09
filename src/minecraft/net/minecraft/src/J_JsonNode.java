// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.List;
import java.util.Map;

// Referenced classes of package net.minecraft.src:
//            J_JsonNodeSelectors, J_JsonNodeDoesNotMatchChainedJsonNodeSelectorException, J_JsonNodeSelector, J_JsonNodeFactories, 
//            J_JsonNodeDoesNotMatchPathElementsException, EnumJsonNodeType

public abstract class J_JsonNode
{

    J_JsonNode()
    {
    }

    public abstract EnumJsonNodeType func_27218_a();

    public abstract String func_27216_b();

    public abstract Map func_27214_c();

    public abstract List func_27215_d();

    public final String func_27213_a(Object aobj[])
    {
        return (String)func_27219_a(J_JsonNodeSelectors.func_27349_a(aobj), this, aobj);
    }

    public final List func_27217_b(Object aobj[])
    {
        return (List)func_27219_a(J_JsonNodeSelectors.func_27346_b(aobj), this, aobj);
    }

    private Object func_27219_a(J_JsonNodeSelector j_jsonnodeselector, J_JsonNode j_jsonnode, Object aobj[])
    {
        try
        {
            return j_jsonnodeselector.func_27357_b(j_jsonnode);
        }
        catch(J_JsonNodeDoesNotMatchChainedJsonNodeSelectorException j_jsonnodedoesnotmatchchainedjsonnodeselectorexception)
        {
            throw J_JsonNodeDoesNotMatchPathElementsException.func_27319_a(j_jsonnodedoesnotmatchchainedjsonnodeselectorexception, aobj, J_JsonNodeFactories.func_27315_a(new J_JsonNode[] {
                j_jsonnode
            }));
        }
    }
}
