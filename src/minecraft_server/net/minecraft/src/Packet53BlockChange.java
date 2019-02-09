// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.*;

// Referenced classes of package net.minecraft.src:
//            Packet, World, NetHandler

public class Packet53BlockChange extends Packet
{

    public Packet53BlockChange()
    {
        isChunkDataPacket = true;
    }

    public Packet53BlockChange(int i, int j, int k, World world)
    {
        isChunkDataPacket = true;
        xPosition = i;
        yPosition = j;
        zPosition = k;
        type = world.getBlockId(i, j, k);
        metadata = world.getBlockMetadata(i, j, k);
    }

    public void readPacketData(DataInputStream datainputstream)
        throws IOException
    {
        xPosition = datainputstream.readInt();
        yPosition = datainputstream.read();
        zPosition = datainputstream.readInt();
        type = datainputstream.read();
        metadata = datainputstream.read();
    }

    public void writePacketData(DataOutputStream dataoutputstream)
        throws IOException
    {
        dataoutputstream.writeInt(xPosition);
        dataoutputstream.write(yPosition);
        dataoutputstream.writeInt(zPosition);
        dataoutputstream.write(type);
        dataoutputstream.write(metadata);
    }

    public void processPacket(NetHandler nethandler)
    {
        nethandler.handleBlockChange(this);
    }

    public int getPacketSize()
    {
        return 11;
    }

    public int xPosition;
    public int yPosition;
    public int zPosition;
    public int type;
    public int metadata;
}
