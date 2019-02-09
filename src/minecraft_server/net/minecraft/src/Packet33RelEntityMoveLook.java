// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.*;

// Referenced classes of package net.minecraft.src:
//            Packet30Entity

public class Packet33RelEntityMoveLook extends Packet30Entity
{

    public Packet33RelEntityMoveLook()
    {
        rotating = true;
    }

    public Packet33RelEntityMoveLook(int i, byte byte0, byte byte1, byte byte2, byte byte3, byte byte4)
    {
        super(i);
        xPosition = byte0;
        yPosition = byte1;
        zPosition = byte2;
        yaw = byte3;
        pitch = byte4;
        rotating = true;
    }

    public void readPacketData(DataInputStream datainputstream)
        throws IOException
    {
        super.readPacketData(datainputstream);
        xPosition = datainputstream.readByte();
        yPosition = datainputstream.readByte();
        zPosition = datainputstream.readByte();
        yaw = datainputstream.readByte();
        pitch = datainputstream.readByte();
    }

    public void writePacketData(DataOutputStream dataoutputstream)
        throws IOException
    {
        super.writePacketData(dataoutputstream);
        dataoutputstream.writeByte(xPosition);
        dataoutputstream.writeByte(yPosition);
        dataoutputstream.writeByte(zPosition);
        dataoutputstream.writeByte(yaw);
        dataoutputstream.writeByte(pitch);
    }

    public int getPacketSize()
    {
        return 9;
    }
}
