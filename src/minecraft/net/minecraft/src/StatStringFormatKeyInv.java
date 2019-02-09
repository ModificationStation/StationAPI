// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

// Referenced classes of package net.minecraft.src:
//            IStatStringFormat, GameSettings, KeyBinding

public class StatStringFormatKeyInv
    implements IStatStringFormat
{

    public StatStringFormatKeyInv(Minecraft minecraft)
    {
//        super();
        mc = minecraft;
    }

    public String formatString(String s)
    {
        return String.format(s, new Object[] {
            Keyboard.getKeyName(mc.gameSettings.keyBindInventory.keyCode)
        });
    }

    final Minecraft mc; /* synthetic field */
}
