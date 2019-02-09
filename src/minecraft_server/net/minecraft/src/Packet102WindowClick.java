// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.*;

// Referenced classes of package net.minecraft.src:
//            Packet, NetHandler, ItemStack

public class Packet102WindowClick extends Packet
{

    public Packet102WindowClick()
    {
    }

    public void processPacket(NetHandler nethandler)
    {
        nethandler.func_20007_a(this);
    }

    public void readPacketData(DataInputStream datainputstream)
        throws IOException
    {
        window_Id = datainputstream.readByte();
        inventorySlot = datainputstream.readShort();
        mouseClick = datainputstream.readByte();
        action = datainputstream.readShort();
        field_27039_f = datainputstream.readBoolean();
        short word0 = datainputstream.readShort();
        if(word0 >= 0)
        {
            byte byte0 = datainputstream.readByte();
            short word1 = datainputstream.readShort();
            itemStack = new ItemStack(word0, byte0, word1);
        } else
        {
            itemStack = null;
        }
    }

    public void writePacketData(DataOutputStream dataoutputstream)
        throws IOException
    {
        dataoutputstream.writeByte(window_Id);
        dataoutputstream.writeShort(inventorySlot);
        dataoutputstream.writeByte(mouseClick);
        dataoutputstream.writeShort(action);
        dataoutputstream.writeBoolean(field_27039_f);
        if(itemStack == null)
        {
            dataoutputstream.writeShort(-1);
        } else
        {
            dataoutputstream.writeShort(itemStack.itemID);
            dataoutputstream.writeByte(itemStack.stackSize);
            dataoutputstream.writeShort(itemStack.getItemDamage());
        }
    }

    public int getPacketSize()
    {
        return 11;
    }

    public int window_Id;
    public int inventorySlot;
    public int mouseClick;
    public short action;
    public ItemStack itemStack;
    public boolean field_27039_f;
}
