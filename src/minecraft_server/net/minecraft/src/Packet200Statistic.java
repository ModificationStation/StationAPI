// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.*;

// Referenced classes of package net.minecraft.src:
//            Packet, NetHandler

public class Packet200Statistic extends Packet
{

    public Packet200Statistic()
    {
    }

    public Packet200Statistic(int i, int j)
    {
        field_27041_a = i;
        field_27040_b = j;
    }

    public void processPacket(NetHandler nethandler)
    {
        nethandler.func_27001_a(this);
    }

    public void readPacketData(DataInputStream datainputstream)
        throws IOException
    {
        field_27041_a = datainputstream.readInt();
        field_27040_b = datainputstream.readByte();
    }

    public void writePacketData(DataOutputStream dataoutputstream)
        throws IOException
    {
        dataoutputstream.writeInt(field_27041_a);
        dataoutputstream.writeByte(field_27040_b);
    }

    public int getPacketSize()
    {
        return 6;
    }

    public int field_27041_a;
    public int field_27040_b;
}
