// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.*;

// Referenced classes of package net.minecraft.src:
//            Packet, NetHandler

public class Packet9Respawn extends Packet
{

    public Packet9Respawn()
    {
    }

    public Packet9Respawn(byte byte0)
    {
        field_28045_a = byte0;
    }

    public void processPacket(NetHandler nethandler)
    {
        nethandler.handleRespawnPacket(this);
    }

    public void readPacketData(DataInputStream datainputstream)
        throws IOException
    {
        field_28045_a = datainputstream.readByte();
    }

    public void writePacketData(DataOutputStream dataoutputstream)
        throws IOException
    {
        dataoutputstream.writeByte(field_28045_a);
    }

    public int getPacketSize()
    {
        return 1;
    }

    public byte field_28045_a;
}
