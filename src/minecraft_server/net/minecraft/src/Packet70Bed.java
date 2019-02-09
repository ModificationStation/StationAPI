// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.*;

// Referenced classes of package net.minecraft.src:
//            Packet, NetHandler

public class Packet70Bed extends Packet
{

    public Packet70Bed()
    {
    }

    public Packet70Bed(int i)
    {
        field_25015_b = i;
    }

    public void readPacketData(DataInputStream datainputstream)
        throws IOException
    {
        field_25015_b = datainputstream.readByte();
    }

    public void writePacketData(DataOutputStream dataoutputstream)
        throws IOException
    {
        dataoutputstream.writeByte(field_25015_b);
    }

    public void processPacket(NetHandler nethandler)
    {
        nethandler.func_25001_a(this);
    }

    public int getPacketSize()
    {
        return 1;
    }

    public static final String field_25016_a[] = {
        "tile.bed.notValid", null, null
    };
    public int field_25015_b;

}
