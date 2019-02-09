// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.Stack;

// Referenced classes of package net.minecraft.src:
//            J_JsonListener, J_JsonNodeBuilder, J_JsonRootNode, J_JsonNodeBuilders, 
//            J_ArrayNodeContainer, J_ObjectNodeContainer, J_JsonFieldBuilder, J_NodeContainer, 
//            J_FieldNodeContainer

final class J_JsonListenerToJdomAdapter
    implements J_JsonListener
{

    J_JsonListenerToJdomAdapter()
    {
    }

    J_JsonRootNode func_27208_a()
    {
        return (J_JsonRootNode)field_27209_b.func_27234_b();
    }

    public void func_27195_b()
    {
    }

    public void func_27204_c()
    {
    }

    public void func_27200_d()
    {
        J_JsonArrayNodeBuilder j_jsonarraynodebuilder = J_JsonNodeBuilders.func_27249_e();
        func_27207_a(j_jsonarraynodebuilder);
        field_27210_a.push(new J_ArrayNodeContainer(this, j_jsonarraynodebuilder));
    }

    public void func_27197_e()
    {
        field_27210_a.pop();
    }

    public void func_27194_f()
    {
        J_JsonObjectNodeBuilder j_jsonobjectnodebuilder = J_JsonNodeBuilders.func_27253_d();
        func_27207_a(j_jsonobjectnodebuilder);
        field_27210_a.push(new J_ObjectNodeContainer(this, j_jsonobjectnodebuilder));
    }

    public void func_27203_g()
    {
        field_27210_a.pop();
    }

    public void func_27205_a(String s)
    {
        J_JsonFieldBuilder j_jsonfieldbuilder = J_JsonFieldBuilder.func_27301_a().func_27304_a(J_JsonNodeBuilders.func_27254_b(s));
        ((J_NodeContainer)field_27210_a.peek()).func_27289_a(j_jsonfieldbuilder);
        field_27210_a.push(new J_FieldNodeContainer(this, j_jsonfieldbuilder));
    }

    public void func_27199_h()
    {
        field_27210_a.pop();
    }

    public void func_27201_b(String s)
    {
        func_27206_b(J_JsonNodeBuilders.func_27250_a(s));
    }

    public void func_27196_i()
    {
        func_27206_b(J_JsonNodeBuilders.func_27251_b());
    }

    public void func_27198_c(String s)
    {
        func_27206_b(J_JsonNodeBuilders.func_27254_b(s));
    }

    public void func_27193_j()
    {
        func_27206_b(J_JsonNodeBuilders.func_27252_c());
    }

    public void func_27202_k()
    {
        func_27206_b(J_JsonNodeBuilders.func_27248_a());
    }

    private void func_27207_a(J_JsonNodeBuilder j_jsonnodebuilder)
    {
        if(field_27209_b == null)
        {
            field_27209_b = j_jsonnodebuilder;
        } else
        {
            func_27206_b(j_jsonnodebuilder);
        }
    }

    private void func_27206_b(J_JsonNodeBuilder j_jsonnodebuilder)
    {
        ((J_NodeContainer)field_27210_a.peek()).func_27290_a(j_jsonnodebuilder);
    }

    private final Stack field_27210_a = new Stack();
    private J_JsonNodeBuilder field_27209_b;
}
