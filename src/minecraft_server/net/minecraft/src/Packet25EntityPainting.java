// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.*;

// Referenced classes of package net.minecraft.src:
//            Packet, EntityPainting, EnumArt, NetHandler

public class Packet25EntityPainting extends Packet
{

    public Packet25EntityPainting()
    {
    }

    public Packet25EntityPainting(EntityPainting entitypainting)
    {
        entityId = entitypainting.entityId;
        xPosition = entitypainting.xPosition;
        yPosition = entitypainting.yPosition;
        zPosition = entitypainting.zPosition;
        direction = entitypainting.direction;
        title = entitypainting.art.title;
    }

    public void readPacketData(DataInputStream datainputstream)
        throws IOException
    {
        entityId = datainputstream.readInt();
        title = readString(datainputstream, EnumArt.field_27096_z);
        xPosition = datainputstream.readInt();
        yPosition = datainputstream.readInt();
        zPosition = datainputstream.readInt();
        direction = datainputstream.readInt();
    }

    public void writePacketData(DataOutputStream dataoutputstream)
        throws IOException
    {
        dataoutputstream.writeInt(entityId);
        writeString(title, dataoutputstream);
        dataoutputstream.writeInt(xPosition);
        dataoutputstream.writeInt(yPosition);
        dataoutputstream.writeInt(zPosition);
        dataoutputstream.writeInt(direction);
    }

    public void processPacket(NetHandler nethandler)
    {
        nethandler.func_21003_a(this);
    }

    public int getPacketSize()
    {
        return 24;
    }

    public int entityId;
    public int xPosition;
    public int yPosition;
    public int zPosition;
    public int direction;
    public String title;
}
