// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.*;
import java.util.zip.*;

// Referenced classes of package net.minecraft.src:
//            Packet, World, NetHandler

public class Packet51MapChunk extends Packet
{

    public Packet51MapChunk()
    {
        isChunkDataPacket = true;
    }

    public Packet51MapChunk(int i, int j, int k, int l, int i1, int j1, World world)
    {
        isChunkDataPacket = true;
        xPosition = i;
        yPosition = j;
        zPosition = k;
        xSize = l;
        ySize = i1;
        zSize = j1;
        byte abyte0[] = world.getChunkData(i, j, k, l, i1, j1);
        Deflater deflater = new Deflater(-1);
        try
        {
            deflater.setInput(abyte0);
            deflater.finish();
            chunk = new byte[(l * i1 * j1 * 5) / 2];
            chunkSize = deflater.deflate(chunk);
        }
        finally
        {
            deflater.end();
        }
    }

    public void readPacketData(DataInputStream datainputstream)
        throws IOException
    {
        xPosition = datainputstream.readInt();
        yPosition = datainputstream.readShort();
        zPosition = datainputstream.readInt();
        xSize = datainputstream.read() + 1;
        ySize = datainputstream.read() + 1;
        zSize = datainputstream.read() + 1;
        chunkSize = datainputstream.readInt();
        byte abyte0[] = new byte[chunkSize];
        datainputstream.readFully(abyte0);
        chunk = new byte[(xSize * ySize * zSize * 5) / 2];
        Inflater inflater = new Inflater();
        inflater.setInput(abyte0);
        try
        {
            inflater.inflate(chunk);
        }
        catch(DataFormatException dataformatexception)
        {
            throw new IOException("Bad compressed data format");
        }
        finally
        {
            inflater.end();
        }
    }

    public void writePacketData(DataOutputStream dataoutputstream)
        throws IOException
    {
        dataoutputstream.writeInt(xPosition);
        dataoutputstream.writeShort(yPosition);
        dataoutputstream.writeInt(zPosition);
        dataoutputstream.write(xSize - 1);
        dataoutputstream.write(ySize - 1);
        dataoutputstream.write(zSize - 1);
        dataoutputstream.writeInt(chunkSize);
        dataoutputstream.write(chunk, 0, chunkSize);
    }

    public void processPacket(NetHandler nethandler)
    {
        nethandler.handleMapChunk(this);
    }

    public int getPacketSize()
    {
        return 17 + chunkSize;
    }

    public int xPosition;
    public int yPosition;
    public int zPosition;
    public int xSize;
    public int ySize;
    public int zSize;
    public byte chunk[];
    private int chunkSize;
}
