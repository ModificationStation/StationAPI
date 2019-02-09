// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.*;

// Referenced classes of package net.minecraft.src:
//            Packet, NetHandler

public class Packet14BlockDig extends Packet
{

    public Packet14BlockDig()
    {
    }

    public Packet14BlockDig(int i, int j, int k, int l, int i1)
    {
        status = i;
        xPosition = j;
        yPosition = k;
        zPosition = l;
        face = i1;
    }

    public void readPacketData(DataInputStream datainputstream)
        throws IOException
    {
        status = datainputstream.read();
        xPosition = datainputstream.readInt();
        yPosition = datainputstream.read();
        zPosition = datainputstream.readInt();
        face = datainputstream.read();
    }

    public void writePacketData(DataOutputStream dataoutputstream)
        throws IOException
    {
        dataoutputstream.write(status);
        dataoutputstream.writeInt(xPosition);
        dataoutputstream.write(yPosition);
        dataoutputstream.writeInt(zPosition);
        dataoutputstream.write(face);
    }

    public void processPacket(NetHandler nethandler)
    {
        nethandler.handleBlockDig(this);
    }

    public int getPacketSize()
    {
        return 11;
    }

    public int xPosition;
    public int yPosition;
    public int zPosition;
    public int face;
    public int status;
}
