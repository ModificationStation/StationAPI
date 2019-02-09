// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.Arrays;
import java.util.Map;

// Referenced classes of package net.minecraft.src:
//            J_JsonConstants, J_JsonStringNode, J_JsonNumberNode, J_JsonArray, 
//            J_JsonObject, J_JsonNode, J_JsonRootNode

public final class J_JsonNodeFactories
{

    private J_JsonNodeFactories()
    {
    }

    public static J_JsonNode func_27310_a()
    {
        return J_JsonConstants.field_27228_a;
    }

    public static J_JsonNode func_27313_b()
    {
        return J_JsonConstants.field_27227_b;
    }

    public static J_JsonNode func_27314_c()
    {
        return J_JsonConstants.field_27230_c;
    }

    public static J_JsonStringNode func_27316_a(String s)
    {
        return new J_JsonStringNode(s);
    }

    public static J_JsonNode func_27311_b(String s)
    {
        return new J_JsonNumberNode(s);
    }

    public static J_JsonRootNode func_27309_a(Iterable iterable)
    {
        return new J_JsonArray(iterable);
    }

    public static J_JsonRootNode func_27315_a(J_JsonNode aj_jsonnode[])
    {
        return func_27309_a(Arrays.asList(aj_jsonnode));
    }

    public static J_JsonRootNode func_27312_a(Map map)
    {
        return new J_JsonObject(map);
    }
}
