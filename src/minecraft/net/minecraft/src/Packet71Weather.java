// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.*;

// Referenced classes of package net.minecraft.src:
//            Packet, Entity, MathHelper, EntityLightningBolt, 
//            NetHandler

public class Packet71Weather extends Packet
{

    public Packet71Weather()
    {
    }

    public Packet71Weather(Entity entity)
    {
        field_27054_a = entity.entityId;
        field_27053_b = MathHelper.floor_double(entity.posX * 32D);
        field_27057_c = MathHelper.floor_double(entity.posY * 32D);
        field_27056_d = MathHelper.floor_double(entity.posZ * 32D);
        if(entity instanceof EntityLightningBolt)
        {
            field_27055_e = 1;
        }
    }

    public void readPacketData(DataInputStream datainputstream)
        throws IOException
    {
        field_27054_a = datainputstream.readInt();
        field_27055_e = datainputstream.readByte();
        field_27053_b = datainputstream.readInt();
        field_27057_c = datainputstream.readInt();
        field_27056_d = datainputstream.readInt();
    }

    public void writePacketData(DataOutputStream dataoutputstream)
        throws IOException
    {
        dataoutputstream.writeInt(field_27054_a);
        dataoutputstream.writeByte(field_27055_e);
        dataoutputstream.writeInt(field_27053_b);
        dataoutputstream.writeInt(field_27057_c);
        dataoutputstream.writeInt(field_27056_d);
    }

    public void processPacket(NetHandler nethandler)
    {
        nethandler.handleWeather(this);
    }

    public int getPacketSize()
    {
        return 17;
    }

    public int field_27054_a;
    public int field_27053_b;
    public int field_27057_c;
    public int field_27056_d;
    public int field_27055_e;
}
