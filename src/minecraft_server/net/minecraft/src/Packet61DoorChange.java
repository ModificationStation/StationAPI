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

    public Packet61DoorChange(int i, int j, int k, int l, int i1)
    {
        field_28047_a = i;
        field_28050_c = j;
        field_28049_d = k;
        field_28048_e = l;
        field_28046_b = i1;
    }

    public void readPacketData(DataInputStream datainputstream)
        throws IOException
    {
        field_28047_a = datainputstream.readInt();
        field_28050_c = datainputstream.readInt();
        field_28049_d = datainputstream.readByte();
        field_28048_e = datainputstream.readInt();
        field_28046_b = datainputstream.readInt();
    }

    public void writePacketData(DataOutputStream dataoutputstream)
        throws IOException
    {
        dataoutputstream.writeInt(field_28047_a);
        dataoutputstream.writeInt(field_28050_c);
        dataoutputstream.writeByte(field_28049_d);
        dataoutputstream.writeInt(field_28048_e);
        dataoutputstream.writeInt(field_28046_b);
    }

    public void processPacket(NetHandler nethandler)
    {
        nethandler.func_28002_a(this);
    }

    public int getPacketSize()
    {
        return 20;
    }

    public int field_28047_a;
    public int field_28046_b;
    public int field_28050_c;
    public int field_28049_d;
    public int field_28048_e;
}
