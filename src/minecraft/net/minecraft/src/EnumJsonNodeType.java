// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


public enum EnumJsonNodeType
{
    OBJECT("OBJECT", 0),
    ARRAY("ARRAY", 1),
    STRING("STRING", 2),
    NUMBER("NUMBER", 3),
    TRUE("TRUE", 4),
    FALSE("FALSE", 5),
    NULL("NULL", 6);
/*
    public static EnumJsonNodeType[] values()
    {
        return (EnumJsonNodeType[])field_27442_h.clone();
    }

    public static EnumJsonNodeType valueOf(String s)
    {
        return (EnumJsonNodeType)Enum.valueOf(net.minecraft.src.EnumJsonNodeType.class, s);
    }
*/
    private EnumJsonNodeType(String s, int i)
    {
//        super(s, i);
    }
/*
    public static final EnumJsonNodeType OBJECT;
    public static final EnumJsonNodeType ARRAY;
    public static final EnumJsonNodeType STRING;
    public static final EnumJsonNodeType NUMBER;
    public static final EnumJsonNodeType TRUE;
    public static final EnumJsonNodeType FALSE;
    public static final EnumJsonNodeType NULL;
*/
    private static final EnumJsonNodeType field_27442_h[]; /* synthetic field */

    static 
    {
/*
        OBJECT = new EnumJsonNodeType("OBJECT", 0);
        ARRAY = new EnumJsonNodeType("ARRAY", 1);
        STRING = new EnumJsonNodeType("STRING", 2);
        NUMBER = new EnumJsonNodeType("NUMBER", 3);
        TRUE = new EnumJsonNodeType("TRUE", 4);
        FALSE = new EnumJsonNodeType("FALSE", 5);
        NULL = new EnumJsonNodeType("NULL", 6);
*/
        field_27442_h = (new EnumJsonNodeType[] {
            OBJECT, ARRAY, STRING, NUMBER, TRUE, FALSE, NULL
        });
    }
}
