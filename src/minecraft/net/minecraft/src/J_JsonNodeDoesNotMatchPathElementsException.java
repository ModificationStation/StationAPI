// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            J_JsonNodeDoesNotMatchJsonNodeSelectorException, J_JsonNodeDoesNotMatchChainedJsonNodeSelectorException, J_JsonFormatter, J_CompactJsonFormatter, 
//            J_JsonRootNode

public final class J_JsonNodeDoesNotMatchPathElementsException extends J_JsonNodeDoesNotMatchJsonNodeSelectorException
{

    static J_JsonNodeDoesNotMatchPathElementsException func_27319_a(J_JsonNodeDoesNotMatchChainedJsonNodeSelectorException j_jsonnodedoesnotmatchchainedjsonnodeselectorexception, Object aobj[], J_JsonRootNode j_jsonrootnode)
    {
        return new J_JsonNodeDoesNotMatchPathElementsException(j_jsonnodedoesnotmatchchainedjsonnodeselectorexception, aobj, j_jsonrootnode);
    }

    private J_JsonNodeDoesNotMatchPathElementsException(J_JsonNodeDoesNotMatchChainedJsonNodeSelectorException j_jsonnodedoesnotmatchchainedjsonnodeselectorexception, Object aobj[], J_JsonRootNode j_jsonrootnode)
    {
        super(func_27318_b(j_jsonnodedoesnotmatchchainedjsonnodeselectorexception, aobj, j_jsonrootnode));
    }

    private static String func_27318_b(J_JsonNodeDoesNotMatchChainedJsonNodeSelectorException j_jsonnodedoesnotmatchchainedjsonnodeselectorexception, Object aobj[], J_JsonRootNode j_jsonrootnode)
    {
        return (new StringBuilder()).append("Failed to find ").append(j_jsonnodedoesnotmatchchainedjsonnodeselectorexception.field_27326_a.toString()).append(" at [").append(J_JsonNodeDoesNotMatchChainedJsonNodeSelectorException.func_27324_a(j_jsonnodedoesnotmatchchainedjsonnodeselectorexception.field_27325_b)).append("] while resolving [").append(func_27317_a(aobj)).append("] in ").append(field_27320_a.func_27327_a(j_jsonrootnode)).append(".").toString();
    }

    private static String func_27317_a(Object aobj[])
    {
        StringBuilder stringbuilder = new StringBuilder();
        boolean flag = true;
        Object aobj1[] = aobj;
        int i = aobj1.length;
        for(int j = 0; j < i; j++)
        {
            Object obj = aobj1[j];
            if(!flag)
            {
                stringbuilder.append(".");
            }
            flag = false;
            if(obj instanceof String)
            {
                stringbuilder.append("\"").append(obj).append("\"");
            } else
            {
                stringbuilder.append(obj);
            }
        }

        return stringbuilder.toString();
    }

    private static final J_JsonFormatter field_27320_a = new J_CompactJsonFormatter();

}
