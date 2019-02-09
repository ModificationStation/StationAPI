// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            GuiButton, EnumOptions

public class GuiSmallButton extends GuiButton
{

    public GuiSmallButton(int i, int j, int k, String s)
    {
        this(i, j, k, null, s);
    }

    public GuiSmallButton(int i, int j, int k, int l, int i1, String s)
    {
        super(i, j, k, l, i1, s);
        enumOptions = null;
    }

    public GuiSmallButton(int i, int j, int k, EnumOptions enumoptions, String s)
    {
        super(i, j, k, 150, 20, s);
        enumOptions = enumoptions;
    }

    public EnumOptions returnEnumOptions()
    {
        return enumOptions;
    }

    private final EnumOptions enumOptions;
}
