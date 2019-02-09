// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.*;

// Referenced classes of package net.minecraft.src:
//            Packet, Entity, MathHelper, NetHandler

public class Packet23VehicleSpawn extends Packet
{

    public Packet23VehicleSpawn()
    {
    }

    public Packet23VehicleSpawn(Entity entity, int i)
    {
        this(entity, i, 0);
    }

    public Packet23VehicleSpawn(Entity entity, int i, int j)
    {
        entityId = entity.entityId;
        xPosition = MathHelper.floor_double(entity.posX * 32D);
        yPosition = MathHelper.floor_double(entity.posY * 32D);
        zPosition = MathHelper.floor_double(entity.posZ * 32D);
        type = i;
        field_28041_i = j;
        if(j > 0)
        {
            double d = entity.motionX;
            double d1 = entity.motionY;
            double d2 = entity.motionZ;
            double d3 = 3.8999999999999999D;
            if(d < -d3)
            {
                d = -d3;
            }
            if(d1 < -d3)
            {
                d1 = -d3;
            }
            if(d2 < -d3)
            {
                d2 = -d3;
            }
            if(d > d3)
            {
                d = d3;
            }
            if(d1 > d3)
            {
                d1 = d3;
            }
            if(d2 > d3)
            {
                d2 = d3;
            }
            field_28044_e = (int)(d * 8000D);
            field_28043_f = (int)(d1 * 8000D);
            field_28042_g = (int)(d2 * 8000D);
        }
    }

    public void readPacketData(DataInputStream datainputstream)
        throws IOException
    {
        entityId = datainputstream.readInt();
        type = datainputstream.readByte();
        xPosition = datainputstream.readInt();
        yPosition = datainputstream.readInt();
        zPosition = datainputstream.readInt();
        field_28041_i = datainputstream.readInt();
        if(field_28041_i > 0)
        {
            field_28044_e = datainputstream.readShort();
            field_28043_f = datainputstream.readShort();
            field_28042_g = datainputstream.readShort();
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
        dataoutputstream.writeInt(field_28041_i);
        if(field_28041_i > 0)
        {
            dataoutputstream.writeShort(field_28044_e);
            dataoutputstream.writeShort(field_28043_f);
            dataoutputstream.writeShort(field_28042_g);
        }
    }

    public void processPacket(NetHandler nethandler)
    {
        nethandler.handleVehicleSpawn(this);
    }

    public int getPacketSize()
    {
        return 21 + field_28041_i <= 0 ? 0 : 6;
    }

    public int entityId;
    public int xPosition;
    public int yPosition;
    public int zPosition;
    public int field_28044_e;
    public int field_28043_f;
    public int field_28042_g;
    public int type;
    public int field_28041_i;
}
