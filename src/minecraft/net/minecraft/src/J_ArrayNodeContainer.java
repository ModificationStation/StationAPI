// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            J_NodeContainer, J_JsonArrayNodeBuilder, J_JsonListenerToJdomAdapter, J_JsonNodeBuilder, 
//            J_JsonFieldBuilder

class J_ArrayNodeContainer
    implements J_NodeContainer
{

    J_ArrayNodeContainer(J_JsonListenerToJdomAdapter j_jsonlistenertojdomadapter, J_JsonArrayNodeBuilder j_jsonarraynodebuilder)
    {
//        super();
        field_27293_b = j_jsonlistenertojdomadapter;
        field_27294_a = j_jsonarraynodebuilder;
    }

    public void func_27290_a(J_JsonNodeBuilder j_jsonnodebuilder)
    {
        field_27294_a.func_27240_a(j_jsonnodebuilder);
    }

    public void func_27289_a(J_JsonFieldBuilder j_jsonfieldbuilder)
    {
        throw new RuntimeException("Coding failure in Argo:  Attempt to add a field to an array.");
    }

    final J_JsonArrayNodeBuilder field_27294_a; /* synthetic field */
    final J_JsonListenerToJdomAdapter field_27293_b; /* synthetic field */
}
