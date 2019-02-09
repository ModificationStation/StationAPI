// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            Block

public class ChunkBlockMap
{

    public ChunkBlockMap()
    {
    }

    public static void func_26002_a(byte abyte0[])
    {
        for(int i = 0; i < abyte0.length; i++)
        {
            abyte0[i] = field_26003_a[abyte0[i] & 0xff];
        }

    }

    private static byte field_26003_a[];

    static 
    {
        field_26003_a = new byte[256];
        try
        {
            for(int i = 0; i < 256; i++)
            {
                byte byte0 = (byte)i;
                if(byte0 != 0 && Block.blocksList[byte0 & 0xff] == null)
                {
                    byte0 = 0;
                }
                field_26003_a[i] = byte0;
            }

        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }
}
