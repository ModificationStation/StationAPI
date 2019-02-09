// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.*;

// Referenced classes of package net.minecraft.src:
//            J_InvalidSyntaxException, J_JsonListenerToJdomAdapter, J_SajParser, J_JsonRootNode

public final class J_JdomParser
{

    public J_JdomParser()
    {
    }

    public J_JsonRootNode func_27366_a(Reader reader)
        throws J_InvalidSyntaxException, IOException
    {
        J_JsonListenerToJdomAdapter j_jsonlistenertojdomadapter = new J_JsonListenerToJdomAdapter();
        (new J_SajParser()).func_27463_a(reader, j_jsonlistenertojdomadapter);
        return j_jsonlistenertojdomadapter.func_27208_a();
    }

    public J_JsonRootNode func_27367_a(String s)
        throws J_InvalidSyntaxException
    {
        J_JsonRootNode j_jsonrootnode;
        try
        {
            j_jsonrootnode = func_27366_a(new StringReader(s));
        }
        catch(IOException ioexception)
        {
            throw new RuntimeException("Coding failure in Argo:  StringWriter gave an IOException", ioexception);
        }
        return j_jsonrootnode;
    }
}
