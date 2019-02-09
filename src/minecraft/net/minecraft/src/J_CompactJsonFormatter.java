// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.*;
import java.util.*;

// Referenced classes of package net.minecraft.src:
//            J_JsonFormatter, EnumJsonNodeTypeMappingHelper, J_JsonNode, EnumJsonNodeType, 
//            J_JsonStringNode, J_JsonEscapedString, J_JsonRootNode

public final class J_CompactJsonFormatter
    implements J_JsonFormatter
{

    public J_CompactJsonFormatter()
    {
    }

    public String func_27327_a(J_JsonRootNode j_jsonrootnode)
    {
        StringWriter stringwriter = new StringWriter();
        try
        {
            func_27329_a(j_jsonrootnode, stringwriter);
        }
        catch(IOException ioexception)
        {
            throw new RuntimeException("Coding failure in Argo:  StringWriter gave an IOException", ioexception);
        }
        return stringwriter.toString();
    }

    public void func_27329_a(J_JsonRootNode j_jsonrootnode, Writer writer)
        throws IOException
    {
        func_27328_a(j_jsonrootnode, writer);
    }

    private void func_27328_a(J_JsonNode j_jsonnode, Writer writer)
        throws IOException
    {
        boolean flag = true;
        switch(EnumJsonNodeTypeMappingHelper.field_27341_a[j_jsonnode.func_27218_a().ordinal()])
        {
        case 1: // '\001'
            writer.append('[');
            J_JsonNode j_jsonnode1;
            for(Iterator iterator = j_jsonnode.func_27215_d().iterator(); iterator.hasNext(); func_27328_a(j_jsonnode1, writer))
            {
                j_jsonnode1 = (J_JsonNode)iterator.next();
                if(!flag)
                {
                    writer.append(',');
                }
                flag = false;
            }

            writer.append(']');
            break;

        case 2: // '\002'
            writer.append('{');
            J_JsonStringNode j_jsonstringnode;
            for(Iterator iterator1 = (new TreeSet(j_jsonnode.func_27214_c().keySet())).iterator(); iterator1.hasNext(); func_27328_a((J_JsonNode)j_jsonnode.func_27214_c().get(j_jsonstringnode), writer))
            {
                j_jsonstringnode = (J_JsonStringNode)iterator1.next();
                if(!flag)
                {
                    writer.append(',');
                }
                flag = false;
                func_27328_a(((J_JsonNode) (j_jsonstringnode)), writer);
                writer.append(':');
            }

            writer.append('}');
            break;

        case 3: // '\003'
            writer.append('"').append((new J_JsonEscapedString(j_jsonnode.func_27216_b())).toString()).append('"');
            break;

        case 4: // '\004'
            writer.append(j_jsonnode.func_27216_b());
            break;

        case 5: // '\005'
            writer.append("false");
            break;

        case 6: // '\006'
            writer.append("true");
            break;

        case 7: // '\007'
            writer.append("null");
            break;

        default:
            throw new RuntimeException((new StringBuilder()).append("Coding failure in Argo:  Attempt to format a JsonNode of unknown type [").append(j_jsonnode.func_27218_a()).append("];").toString());
        }
    }
}
