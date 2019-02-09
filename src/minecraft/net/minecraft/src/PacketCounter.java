// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            Empty1

class PacketCounter
{

    private PacketCounter()
    {
    }

    public void addPacket(int i)
    {
        totalPackets++;
        totalBytes += i;
    }

    PacketCounter(Empty1 empty1)
    {
        this();
    }

    private int totalPackets;
    private long totalBytes;
}
