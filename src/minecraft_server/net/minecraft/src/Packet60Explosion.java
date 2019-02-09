// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.*;
import java.util.*;

// Referenced classes of package net.minecraft.src:
//            Packet, ChunkPosition, NetHandler

public class Packet60Explosion extends Packet
{

    public Packet60Explosion()
    {
    }

    public Packet60Explosion(double d, double d1, double d2, float f, 
            Set set)
    {
        explosionX = d;
        explosionY = d1;
        explosionZ = d2;
        explosionSize = f;
        destroyedBlockPositions = new HashSet(set);
    }

    public void readPacketData(DataInputStream datainputstream)
        throws IOException
    {
        explosionX = datainputstream.readDouble();
        explosionY = datainputstream.readDouble();
        explosionZ = datainputstream.readDouble();
        explosionSize = datainputstream.readFloat();
        int i = datainputstream.readInt();
        destroyedBlockPositions = new HashSet();
        int j = (int)explosionX;
        int k = (int)explosionY;
        int l = (int)explosionZ;
        for(int i1 = 0; i1 < i; i1++)
        {
            int j1 = datainputstream.readByte() + j;
            int k1 = datainputstream.readByte() + k;
            int l1 = datainputstream.readByte() + l;
            destroyedBlockPositions.add(new ChunkPosition(j1, k1, l1));
        }

    }

    public void writePacketData(DataOutputStream dataoutputstream)
        throws IOException
    {
        dataoutputstream.writeDouble(explosionX);
        dataoutputstream.writeDouble(explosionY);
        dataoutputstream.writeDouble(explosionZ);
        dataoutputstream.writeFloat(explosionSize);
        dataoutputstream.writeInt(destroyedBlockPositions.size());
        int i = (int)explosionX;
        int j = (int)explosionY;
        int k = (int)explosionZ;
        int j1;
        for(Iterator iterator = destroyedBlockPositions.iterator(); iterator.hasNext(); dataoutputstream.writeByte(j1))
        {
            ChunkPosition chunkposition = (ChunkPosition)iterator.next();
            int l = chunkposition.x - i;
            int i1 = chunkposition.y - j;
            j1 = chunkposition.z - k;
            dataoutputstream.writeByte(l);
            dataoutputstream.writeByte(i1);
        }

    }

    public void processPacket(NetHandler nethandler)
    {
        nethandler.func_12001_a(this);
    }

    public int getPacketSize()
    {
        return 32 + destroyedBlockPositions.size() * 3;
    }

    public double explosionX;
    public double explosionY;
    public double explosionZ;
    public float explosionSize;
    public Set destroyedBlockPositions;
}
