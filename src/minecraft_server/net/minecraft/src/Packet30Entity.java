// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.*;

// Referenced classes of package net.minecraft.src:
//            Packet, NetHandler

public class Packet30Entity extends Packet
{

    public Packet30Entity()
    {
        rotating = false;
    }

    public Packet30Entity(int i)
    {
        rotating = false;
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
        nethandler.handleEntity(this);
    }

    public int getPacketSize()
    {
        return 4;
    }

    public int entityId;
    public byte xPosition;
    public byte yPosition;
    public byte zPosition;
    public byte yaw;
    public byte pitch;
    public boolean rotating;
}
