// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.*;

// Referenced classes of package net.minecraft.src:
//            Packet, NetHandler

public class Packet255KickDisconnect extends Packet
{

    public Packet255KickDisconnect()
    {
    }

    public Packet255KickDisconnect(String s)
    {
        reason = s;
    }

    public void readPacketData(DataInputStream datainputstream)
        throws IOException
    {
        reason = readString(datainputstream, 100);
    }

    public void writePacketData(DataOutputStream dataoutputstream)
        throws IOException
    {
        writeString(reason, dataoutputstream);
    }

    public void processPacket(NetHandler nethandler)
    {
        nethandler.handleKickDisconnect(this);
    }

    public int getPacketSize()
    {
        return reason.length();
    }

    public String reason;
}
