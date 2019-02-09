// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.*;

// Referenced classes of package net.minecraft.src:
//            Packet, NetHandler

public class Packet131MapData extends Packet
{

    public Packet131MapData()
    {
        isChunkDataPacket = true;
    }

    public Packet131MapData(short word0, short word1, byte abyte0[])
    {
        isChunkDataPacket = true;
        field_28052_a = word0;
        field_28051_b = word1;
        field_28053_c = abyte0;
    }

    public void readPacketData(DataInputStream datainputstream)
        throws IOException
    {
        field_28052_a = datainputstream.readShort();
        field_28051_b = datainputstream.readShort();
        field_28053_c = new byte[datainputstream.readByte() & 0xff];
        datainputstream.readFully(field_28053_c);
    }

    public void writePacketData(DataOutputStream dataoutputstream)
        throws IOException
    {
        dataoutputstream.writeShort(field_28052_a);
        dataoutputstream.writeShort(field_28051_b);
        dataoutputstream.writeByte(field_28053_c.length);
        dataoutputstream.write(field_28053_c);
    }

    public void processPacket(NetHandler nethandler)
    {
        nethandler.func_28001_a(this);
    }

    public int getPacketSize()
    {
        return 4 + field_28053_c.length;
    }

    public short field_28052_a;
    public short field_28051_b;
    public byte field_28053_c[];
}
