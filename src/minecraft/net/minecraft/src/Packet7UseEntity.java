// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.*;

// Referenced classes of package net.minecraft.src:
//            Packet, NetHandler

public class Packet7UseEntity extends Packet
{

    public Packet7UseEntity()
    {
    }

    public Packet7UseEntity(int i, int j, int k)
    {
        playerEntityId = i;
        targetEntity = j;
        isLeftClick = k;
    }

    public void readPacketData(DataInputStream datainputstream)
        throws IOException
    {
        playerEntityId = datainputstream.readInt();
        targetEntity = datainputstream.readInt();
        isLeftClick = datainputstream.readByte();
    }

    public void writePacketData(DataOutputStream dataoutputstream)
        throws IOException
    {
        dataoutputstream.writeInt(playerEntityId);
        dataoutputstream.writeInt(targetEntity);
        dataoutputstream.writeByte(isLeftClick);
    }

    public void processPacket(NetHandler nethandler)
    {
        nethandler.handleUseEntity(this);
    }

    public int getPacketSize()
    {
        return 9;
    }

    public int playerEntityId;
    public int targetEntity;
    public int isLeftClick;
}
