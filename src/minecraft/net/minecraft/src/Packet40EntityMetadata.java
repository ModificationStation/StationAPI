// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.*;
import java.util.List;

// Referenced classes of package net.minecraft.src:
//            Packet, DataWatcher, NetHandler

public class Packet40EntityMetadata extends Packet
{

    public Packet40EntityMetadata()
    {
    }

    public void readPacketData(DataInputStream datainputstream)
        throws IOException
    {
        entityId = datainputstream.readInt();
        field_21048_b = DataWatcher.readWatchableObjects(datainputstream);
    }

    public void writePacketData(DataOutputStream dataoutputstream)
        throws IOException
    {
        dataoutputstream.writeInt(entityId);
        DataWatcher.writeObjectsInListToStream(field_21048_b, dataoutputstream);
    }

    public void processPacket(NetHandler nethandler)
    {
        nethandler.func_21148_a(this);
    }

    public int getPacketSize()
    {
        return 5;
    }

    public List func_21047_b()
    {
        return field_21048_b;
    }

    public int entityId;
    private List field_21048_b;
}
