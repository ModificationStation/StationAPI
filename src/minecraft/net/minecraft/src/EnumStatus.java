// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


public enum EnumStatus
{
    OK("OK", 0),
    NOT_POSSIBLE_HERE("NOT_POSSIBLE_HERE", 1),
    NOT_POSSIBLE_NOW("NOT_POSSIBLE_NOW", 2),
    TOO_FAR_AWAY("TOO_FAR_AWAY", 3),
    OTHER_PROBLEM("OTHER_PROBLEM", 4);
/*
    public static EnumStatus[] values()
    {
        return (EnumStatus[])field_25204_f.clone();
    }

    public static EnumStatus valueOf(String s)
    {
        return (EnumStatus)Enum.valueOf(net.minecraft.src.EnumStatus.class, s);
    }
*/
    private EnumStatus(String s, int i)
    {
//        super(s, i);
    }
/*
    public static final EnumStatus OK;
    public static final EnumStatus NOT_POSSIBLE_HERE;
    public static final EnumStatus NOT_POSSIBLE_NOW;
    public static final EnumStatus TOO_FAR_AWAY;
    public static final EnumStatus OTHER_PROBLEM;
*/
    private static final EnumStatus field_25204_f[]; /* synthetic field */

    static 
    {
/*
        OK = new EnumStatus("OK", 0);
        NOT_POSSIBLE_HERE = new EnumStatus("NOT_POSSIBLE_HERE", 1);
        NOT_POSSIBLE_NOW = new EnumStatus("NOT_POSSIBLE_NOW", 2);
        TOO_FAR_AWAY = new EnumStatus("TOO_FAR_AWAY", 3);
        OTHER_PROBLEM = new EnumStatus("OTHER_PROBLEM", 4);
*/
        field_25204_f = (new EnumStatus[] {
            OK, NOT_POSSIBLE_HERE, NOT_POSSIBLE_NOW, TOO_FAR_AWAY, OTHER_PROBLEM
        });
    }
}
