// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.*;

// Referenced classes of package net.minecraft.src:
//            Packet, NetHandler

public class Packet5PlayerInventory extends Packet
{

    public Packet5PlayerInventory()
    {
    }

    public void readPacketData(DataInputStream datainputstream)
        throws IOException
    {
        entityID = datainputstream.readInt();
        slot = datainputstream.readShort();
        itemID = datainputstream.readShort();
        itemDamage = datainputstream.readShort();
    }

    public void writePacketData(DataOutputStream dataoutputstream)
        throws IOException
    {
        dataoutputstream.writeInt(entityID);
        dataoutputstream.writeShort(slot);
        dataoutputstream.writeShort(itemID);
        dataoutputstream.writeShort(itemDamage);
    }

    public void processPacket(NetHandler nethandler)
    {
        nethandler.handlePlayerInventory(this);
    }

    public int getPacketSize()
    {
        return 8;
    }

    public int entityID;
    public int slot;
    public int itemID;
    public int itemDamage;
}
