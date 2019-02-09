// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.*;

// Referenced classes of package net.minecraft.src:
//            Packet30Entity

public class Packet31RelEntityMove extends Packet30Entity
{

    public Packet31RelEntityMove()
    {
    }

    public void readPacketData(DataInputStream datainputstream)
        throws IOException
    {
        super.readPacketData(datainputstream);
        xPosition = datainputstream.readByte();
        yPosition = datainputstream.readByte();
        zPosition = datainputstream.readByte();
    }

    public void writePacketData(DataOutputStream dataoutputstream)
        throws IOException
    {
        super.writePacketData(dataoutputstream);
        dataoutputstream.writeByte(xPosition);
        dataoutputstream.writeByte(yPosition);
        dataoutputstream.writeByte(zPosition);
    }

    public int getPacketSize()
    {
        return 7;
    }
}
