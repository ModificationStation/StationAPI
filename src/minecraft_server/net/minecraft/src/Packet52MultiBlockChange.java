// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.*;

// Referenced classes of package net.minecraft.src:
//            Packet, World, Chunk, NetHandler

public class Packet52MultiBlockChange extends Packet
{

    public Packet52MultiBlockChange()
    {
        isChunkDataPacket = true;
    }

    public Packet52MultiBlockChange(int i, int j, short aword0[], int k, World world)
    {
        isChunkDataPacket = true;
        xPosition = i;
        zPosition = j;
        size = k;
        coordinateArray = new short[k];
        typeArray = new byte[k];
        metadataArray = new byte[k];
        Chunk chunk = world.getChunkFromChunkCoords(i, j);
        for(int l = 0; l < k; l++)
        {
            int i1 = aword0[l] >> 12 & 0xf;
            int j1 = aword0[l] >> 8 & 0xf;
            int k1 = aword0[l] & 0xff;
            coordinateArray[l] = aword0[l];
            typeArray[l] = (byte)chunk.getBlockID(i1, k1, j1);
            metadataArray[l] = (byte)chunk.getBlockMetadata(i1, k1, j1);
        }

    }

    public void readPacketData(DataInputStream datainputstream)
        throws IOException
    {
        xPosition = datainputstream.readInt();
        zPosition = datainputstream.readInt();
        size = datainputstream.readShort() & 0xffff;
        coordinateArray = new short[size];
        typeArray = new byte[size];
        metadataArray = new byte[size];
        for(int i = 0; i < size; i++)
        {
            coordinateArray[i] = datainputstream.readShort();
        }

        datainputstream.readFully(typeArray);
        datainputstream.readFully(metadataArray);
    }

    public void writePacketData(DataOutputStream dataoutputstream)
        throws IOException
    {
        dataoutputstream.writeInt(xPosition);
        dataoutputstream.writeInt(zPosition);
        dataoutputstream.writeShort((short)size);
        for(int i = 0; i < size; i++)
        {
            dataoutputstream.writeShort(coordinateArray[i]);
        }

        dataoutputstream.write(typeArray);
        dataoutputstream.write(metadataArray);
    }

    public void processPacket(NetHandler nethandler)
    {
        nethandler.handleMultiBlockChange(this);
    }

    public int getPacketSize()
    {
        return 10 + size * 4;
    }

    public int xPosition;
    public int zPosition;
    public short coordinateArray[];
    public byte typeArray[];
    public byte metadataArray[];
    public int size;
}
