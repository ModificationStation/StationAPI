// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.*;

// Referenced classes of package net.minecraft.src:
//            Packet, NetHandler

public class Packet23VehicleSpawn extends Packet
{

    public Packet23VehicleSpawn()
    {
    }

    public void readPacketData(DataInputStream datainputstream)
        throws IOException
    {
        entityId = datainputstream.readInt();
        type = datainputstream.readByte();
        xPosition = datainputstream.readInt();
        yPosition = datainputstream.readInt();
        zPosition = datainputstream.readInt();
        field_28044_i = datainputstream.readInt();
        if(field_28044_i > 0)
        {
            field_28047_e = datainputstream.readShort();
            field_28046_f = datainputstream.readShort();
            field_28045_g = datainputstream.readShort();
        }
    }

    public void writePacketData(DataOutputStream dataoutputstream)
        throws IOException
    {
        dataoutputstream.writeInt(entityId);
        dataoutputstream.writeByte(type);
        dataoutputstream.writeInt(xPosition);
        dataoutputstream.writeInt(yPosition);
        dataoutputstream.writeInt(zPosition);
        dataoutputstream.writeInt(field_28044_i);
        if(field_28044_i > 0)
        {
            dataoutputstream.writeShort(field_28047_e);
            dataoutputstream.writeShort(field_28046_f);
            dataoutputstream.writeShort(field_28045_g);
        }
    }

    public void processPacket(NetHandler nethandler)
    {
        nethandler.handleVehicleSpawn(this);
    }

    public int getPacketSize()
    {
        return 21 + field_28044_i <= 0 ? 0 : 6;
    }

    public int entityId;
    public int xPosition;
    public int yPosition;
    public int zPosition;
    public int field_28047_e;
    public int field_28046_f;
    public int field_28045_g;
    public int type;
    public int field_28044_i;
}
