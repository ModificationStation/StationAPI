// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.*;

// Referenced classes of package net.minecraft.src:
//            Packet, NetHandler

public class Packet17Sleep extends Packet
{

    public Packet17Sleep()
    {
    }

    public void readPacketData(DataInputStream datainputstream)
        throws IOException
    {
        field_22045_a = datainputstream.readInt();
        field_22046_e = datainputstream.readByte();
        field_22044_b = datainputstream.readInt();
        field_22048_c = datainputstream.readByte();
        field_22047_d = datainputstream.readInt();
    }

    public void writePacketData(DataOutputStream dataoutputstream)
        throws IOException
    {
        dataoutputstream.writeInt(field_22045_a);
        dataoutputstream.writeByte(field_22046_e);
        dataoutputstream.writeInt(field_22044_b);
        dataoutputstream.writeByte(field_22048_c);
        dataoutputstream.writeInt(field_22047_d);
    }

    public void processPacket(NetHandler nethandler)
    {
        nethandler.func_22186_a(this);
    }

    public int getPacketSize()
    {
        return 14;
    }

    public int field_22045_a;
    public int field_22044_b;
    public int field_22048_c;
    public int field_22047_d;
    public int field_22046_e;
}
