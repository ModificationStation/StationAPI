// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            J_Functor, J_JsonNodeSelector, J_JsonNodeDoesNotMatchChainedJsonNodeSelectorException

final class J_ChainedFunctor
    implements J_Functor
{

    J_ChainedFunctor(J_JsonNodeSelector j_jsonnodeselector, J_JsonNodeSelector j_jsonnodeselector1)
    {
        field_27062_a = j_jsonnodeselector;
        field_27061_b = j_jsonnodeselector1;
    }

    public boolean func_27058_a(Object obj)
    {
        return field_27062_a.func_27356_a(obj) && field_27061_b.func_27356_a(field_27062_a.func_27357_b(obj));
    }

    public Object func_27059_b(Object obj)
    {
        Object obj1;
        try
        {
            obj1 = field_27062_a.func_27357_b(obj);
        }
        catch(J_JsonNodeDoesNotMatchChainedJsonNodeSelectorException j_jsonnodedoesnotmatchchainedjsonnodeselectorexception)
        {
            throw J_JsonNodeDoesNotMatchChainedJsonNodeSelectorException.func_27321_b(j_jsonnodedoesnotmatchchainedjsonnodeselectorexception, field_27062_a);
        }
        Object obj2;
        try
        {
            obj2 = field_27061_b.func_27357_b(obj1);
        }
        catch(J_JsonNodeDoesNotMatchChainedJsonNodeSelectorException j_jsonnodedoesnotmatchchainedjsonnodeselectorexception1)
        {
            throw J_JsonNodeDoesNotMatchChainedJsonNodeSelectorException.func_27323_a(j_jsonnodedoesnotmatchchainedjsonnodeselectorexception1, field_27062_a);
        }
        return obj2;
    }

    public String func_27060_a()
    {
        return field_27061_b.func_27358_a();
    }

    public String toString()
    {
        return (new StringBuilder()).append(field_27062_a.toString()).append(", with ").append(field_27061_b.toString()).toString();
    }

    private final J_JsonNodeSelector field_27062_a;
    private final J_JsonNodeSelector field_27061_b;
}
