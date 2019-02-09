// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.*;

// Referenced classes of package net.minecraft.src:
//            Packet, ItemStack, NetHandler

public class Packet104WindowItems extends Packet
{

    public Packet104WindowItems()
    {
    }

    public void readPacketData(DataInputStream datainputstream)
        throws IOException
    {
        windowId = datainputstream.readByte();
        short word0 = datainputstream.readShort();
        itemStack = new ItemStack[word0];
        for(int i = 0; i < word0; i++)
        {
            short word1 = datainputstream.readShort();
            if(word1 >= 0)
            {
                byte byte0 = datainputstream.readByte();
                short word2 = datainputstream.readShort();
                itemStack[i] = new ItemStack(word1, byte0, word2);
            }
        }

    }

    public void writePacketData(DataOutputStream dataoutputstream)
        throws IOException
    {
        dataoutputstream.writeByte(windowId);
        dataoutputstream.writeShort(itemStack.length);
        for(int i = 0; i < itemStack.length; i++)
        {
            if(itemStack[i] == null)
            {
                dataoutputstream.writeShort(-1);
            } else
            {
                dataoutputstream.writeShort((short)itemStack[i].itemID);
                dataoutputstream.writeByte((byte)itemStack[i].stackSize);
                dataoutputstream.writeShort((short)itemStack[i].getItemDamage());
            }
        }

    }

    public void processPacket(NetHandler nethandler)
    {
        nethandler.func_20094_a(this);
    }

    public int getPacketSize()
    {
        return 3 + itemStack.length * 5;
    }

    public int windowId;
    public ItemStack itemStack[];
}
