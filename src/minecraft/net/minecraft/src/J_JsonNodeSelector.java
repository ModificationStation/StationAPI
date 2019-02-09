// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            J_Functor, J_ChainedFunctor

public final class J_JsonNodeSelector
{

    J_JsonNodeSelector(J_Functor j_functor)
    {
        field_27359_a = j_functor;
    }

    public boolean func_27356_a(Object obj)
    {
        return field_27359_a.func_27058_a(obj);
    }

    public Object func_27357_b(Object obj)
    {
        return field_27359_a.func_27059_b(obj);
    }

    public J_JsonNodeSelector func_27355_a(J_JsonNodeSelector j_jsonnodeselector)
    {
        return new J_JsonNodeSelector(new J_ChainedFunctor(this, j_jsonnodeselector));
    }

    String func_27358_a()
    {
        return field_27359_a.func_27060_a();
    }

    public String toString()
    {
        return field_27359_a.toString();
    }

    final J_Functor field_27359_a;
}
