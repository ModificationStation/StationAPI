// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.*;

// Referenced classes of package net.minecraft.src:
//            Packet, NetHandler

public class Packet27Position extends Packet
{

    public Packet27Position()
    {
    }

    public void readPacketData(DataInputStream datainputstream)
        throws IOException
    {
        field_22039_a = datainputstream.readFloat();
        field_22038_b = datainputstream.readFloat();
        field_22041_e = datainputstream.readFloat();
        field_22040_f = datainputstream.readFloat();
        field_22043_c = datainputstream.readBoolean();
        field_22042_d = datainputstream.readBoolean();
    }

    public void writePacketData(DataOutputStream dataoutputstream)
        throws IOException
    {
        dataoutputstream.writeFloat(field_22039_a);
        dataoutputstream.writeFloat(field_22038_b);
        dataoutputstream.writeFloat(field_22041_e);
        dataoutputstream.writeFloat(field_22040_f);
        dataoutputstream.writeBoolean(field_22043_c);
        dataoutputstream.writeBoolean(field_22042_d);
    }

    public void processPacket(NetHandler nethandler)
    {
        nethandler.func_22185_a(this);
    }

    public int getPacketSize()
    {
        return 18;
    }

    private float field_22039_a;
    private float field_22038_b;
    private boolean field_22043_c;
    private boolean field_22042_d;
    private float field_22041_e;
    private float field_22040_f;
}
