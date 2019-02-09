// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            J_ThingWithPosition

public final class J_InvalidSyntaxException extends Exception
{

    J_InvalidSyntaxException(String s, J_ThingWithPosition j_thingwithposition)
    {
        super((new StringBuilder()).append("At line ").append(j_thingwithposition.func_27330_b()).append(", column ").append(j_thingwithposition.func_27331_a()).append(":  ").append(s).toString());
        field_27191_a = j_thingwithposition.func_27331_a();
        field_27190_b = j_thingwithposition.func_27330_b();
    }

    J_InvalidSyntaxException(String s, Throwable throwable, J_ThingWithPosition j_thingwithposition)
    {
        super((new StringBuilder()).append("At line ").append(j_thingwithposition.func_27330_b()).append(", column ").append(j_thingwithposition.func_27331_a()).append(":  ").append(s).toString(), throwable);
        field_27191_a = j_thingwithposition.func_27331_a();
        field_27190_b = j_thingwithposition.func_27330_b();
    }

    private final int field_27191_a;
    private final int field_27190_b;
}
