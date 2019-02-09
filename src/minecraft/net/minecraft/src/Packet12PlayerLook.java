// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.*;

// Referenced classes of package net.minecraft.src:
//            Packet10Flying

public class Packet12PlayerLook extends Packet10Flying
{

    public Packet12PlayerLook()
    {
        rotating = true;
    }

    public Packet12PlayerLook(float f, float f1, boolean flag)
    {
        yaw = f;
        pitch = f1;
        onGround = flag;
        rotating = true;
    }

    public void readPacketData(DataInputStream datainputstream)
        throws IOException
    {
        yaw = datainputstream.readFloat();
        pitch = datainputstream.readFloat();
        super.readPacketData(datainputstream);
    }

    public void writePacketData(DataOutputStream dataoutputstream)
        throws IOException
    {
        dataoutputstream.writeFloat(yaw);
        dataoutputstream.writeFloat(pitch);
        super.writePacketData(dataoutputstream);
    }

    public int getPacketSize()
    {
        return 9;
    }
}
