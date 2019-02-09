// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            J_NodeContainer, J_JsonFieldBuilder, J_JsonListenerToJdomAdapter, J_JsonNodeBuilder

class J_FieldNodeContainer
    implements J_NodeContainer
{

    J_FieldNodeContainer(J_JsonListenerToJdomAdapter j_jsonlistenertojdomadapter, J_JsonFieldBuilder j_jsonfieldbuilder)
    {
//        super();
        field_27291_b = j_jsonlistenertojdomadapter;
        field_27292_a = j_jsonfieldbuilder;
    }

    public void func_27290_a(J_JsonNodeBuilder j_jsonnodebuilder)
    {
        field_27292_a.func_27300_b(j_jsonnodebuilder);
    }

    public void func_27289_a(J_JsonFieldBuilder j_jsonfieldbuilder)
    {
        throw new RuntimeException("Coding failure in Argo:  Attempt to add a field to a field.");
    }

    final J_JsonFieldBuilder field_27292_a; /* synthetic field */
    final J_JsonListenerToJdomAdapter field_27291_b; /* synthetic field */
}
