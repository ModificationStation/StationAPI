// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            EnumJsonNodeType

class EnumJsonNodeTypeMappingHelper
{

    static final int field_27341_a[]; /* synthetic field */

    static 
    {
        field_27341_a = new int[EnumJsonNodeType.values().length];
        try
        {
            field_27341_a[EnumJsonNodeType.ARRAY.ordinal()] = 1;
        }
        catch(NoSuchFieldError nosuchfielderror) { }
        try
        {
            field_27341_a[EnumJsonNodeType.OBJECT.ordinal()] = 2;
        }
        catch(NoSuchFieldError nosuchfielderror1) { }
        try
        {
            field_27341_a[EnumJsonNodeType.STRING.ordinal()] = 3;
        }
        catch(NoSuchFieldError nosuchfielderror2) { }
        try
        {
            field_27341_a[EnumJsonNodeType.NUMBER.ordinal()] = 4;
        }
        catch(NoSuchFieldError nosuchfielderror3) { }
        try
        {
            field_27341_a[EnumJsonNodeType.FALSE.ordinal()] = 5;
        }
        catch(NoSuchFieldError nosuchfielderror4) { }
        try
        {
            field_27341_a[EnumJsonNodeType.TRUE.ordinal()] = 6;
        }
        catch(NoSuchFieldError nosuchfielderror5) { }
        try
        {
            field_27341_a[EnumJsonNodeType.NULL.ordinal()] = 7;
        }
        catch(NoSuchFieldError nosuchfielderror6) { }
    }
}
