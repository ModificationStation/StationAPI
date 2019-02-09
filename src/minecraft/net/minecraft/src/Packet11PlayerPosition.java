// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.*;

// Referenced classes of package net.minecraft.src:
//            Packet10Flying

public class Packet11PlayerPosition extends Packet10Flying
{

    public Packet11PlayerPosition()
    {
        moving = true;
    }

    public Packet11PlayerPosition(double d, double d1, double d2, double d3, boolean flag)
    {
        xPosition = d;
        yPosition = d1;
        stance = d2;
        zPosition = d3;
        onGround = flag;
        moving = true;
    }

    public void readPacketData(DataInputStream datainputstream)
        throws IOException
    {
        xPosition = datainputstream.readDouble();
        yPosition = datainputstream.readDouble();
        stance = datainputstream.readDouble();
        zPosition = datainputstream.readDouble();
        super.readPacketData(datainputstream);
    }

    public void writePacketData(DataOutputStream dataoutputstream)
        throws IOException
    {
        dataoutputstream.writeDouble(xPosition);
        dataoutputstream.writeDouble(yPosition);
        dataoutputstream.writeDouble(stance);
        dataoutputstream.writeDouble(zPosition);
        super.writePacketData(dataoutputstream);
    }

    public int getPacketSize()
    {
        return 33;
    }
}
