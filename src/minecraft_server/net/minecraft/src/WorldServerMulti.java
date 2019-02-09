// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

// Referenced classes of package net.minecraft.src:
//            WorldServer, ISaveHandler

public class WorldServerMulti extends WorldServer
{

    public WorldServerMulti(MinecraftServer minecraftserver, ISaveHandler isavehandler, String s, int i, long l, WorldServer worldserver)
    {
        super(minecraftserver, isavehandler, s, i, l);
        field_28105_z = worldserver.field_28105_z;
    }
}
