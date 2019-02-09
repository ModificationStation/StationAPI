// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.*;

// Referenced classes of package net.minecraft.src:
//            Packet, NetHandler

public class Packet61DoorChange extends Packet
{

    public Packet61DoorChange()
    {
    }

    public void readPacketData(DataInputStream datainputstream)
        throws IOException
    {
        field_28050_a = datainputstream.readInt();
        field_28053_c = datainputstream.readInt();
        field_28052_d = datainputstream.readByte();
        field_28051_e = datainputstream.readInt();
        field_28049_b = datainputstream.readInt();
    }

    public void writePacketData(DataOutputStream dataoutputstream)
        throws IOException
    {
        dataoutputstream.writeInt(field_28050_a);
        dataoutputstream.writeInt(field_28053_c);
        dataoutputstream.writeByte(field_28052_d);
        dataoutputstream.writeInt(field_28051_e);
        dataoutputstream.writeInt(field_28049_b);
    }

    public void processPacket(NetHandler nethandler)
    {
        nethandler.func_28115_a(this);
    }

    public int getPacketSize()
    {
        return 20;
    }

    public int field_28050_a;
    public int field_28049_b;
    public int field_28053_c;
    public int field_28052_d;
    public int field_28051_e;
}
