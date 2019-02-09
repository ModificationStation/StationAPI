// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


public class ChunkCoordinates
    implements Comparable
{

    public ChunkCoordinates()
    {
    }

    public ChunkCoordinates(int i, int j, int k)
    {
        x = i;
        y = j;
        z = k;
    }

    public ChunkCoordinates(ChunkCoordinates chunkcoordinates)
    {
        x = chunkcoordinates.x;
        y = chunkcoordinates.y;
        z = chunkcoordinates.z;
    }

    public boolean equals(Object obj)
    {
        if(!(obj instanceof ChunkCoordinates))
        {
            return false;
        } else
        {
            ChunkCoordinates chunkcoordinates = (ChunkCoordinates)obj;
            return x == chunkcoordinates.x && y == chunkcoordinates.y && z == chunkcoordinates.z;
        }
    }

    public int hashCode()
    {
        return x + z << 8 + y << 16;
    }

    public int compareChunkCoordinate(ChunkCoordinates chunkcoordinates)
    {
        if(y == chunkcoordinates.y)
        {
            if(z == chunkcoordinates.z)
            {
                return x - chunkcoordinates.x;
            } else
            {
                return z - chunkcoordinates.z;
            }
        } else
        {
            return y - chunkcoordinates.y;
        }
    }

    public double getSqDistanceTo(int i, int j, int k)
    {
        int l = x - i;
        int i1 = y - j;
        int j1 = z - k;
        return Math.sqrt(l * l + i1 * i1 + j1 * j1);
    }

    public int compareTo(Object obj)
    {
        return compareChunkCoordinate((ChunkCoordinates)obj);
    }

    public int x;
    public int y;
    public int z;
}
