// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.*;

// Referenced classes of package net.minecraft.src:
//            Packet, NetHandler

public class Packet29DestroyEntity extends Packet
{

    public Packet29DestroyEntity()
    {
    }

    public Packet29DestroyEntity(int i)
    {
        entityId = i;
    }

    public void readPacketData(DataInputStream datainputstream)
        throws IOException
    {
        entityId = datainputstream.readInt();
    }

    public void writePacketData(DataOutputStream dataoutputstream)
        throws IOException
    {
        dataoutputstream.writeInt(entityId);
    }

    public void processPacket(NetHandler nethandler)
    {
        nethandler.handleDestroyEntity(this);
    }

    public int getPacketSize()
    {
        return 4;
    }

    public int entityId;
}
