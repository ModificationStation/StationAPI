// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.*;

// Referenced classes of package net.minecraft.src:
//            Packet, NetHandler

public class Packet27Position extends Packet
{

    public Packet27Position()
    {
    }

    public void readPacketData(DataInputStream datainputstream)
        throws IOException
    {
        strafeMovement = datainputstream.readFloat();
        fowardMovement = datainputstream.readFloat();
        pitchRotation = datainputstream.readFloat();
        yawRotation = datainputstream.readFloat();
        field_22039_c = datainputstream.readBoolean();
        isInJump = datainputstream.readBoolean();
    }

    public void writePacketData(DataOutputStream dataoutputstream)
        throws IOException
    {
        dataoutputstream.writeFloat(strafeMovement);
        dataoutputstream.writeFloat(fowardMovement);
        dataoutputstream.writeFloat(pitchRotation);
        dataoutputstream.writeFloat(yawRotation);
        dataoutputstream.writeBoolean(field_22039_c);
        dataoutputstream.writeBoolean(isInJump);
    }

    public void processPacket(NetHandler nethandler)
    {
        nethandler.handleMovementTypePacket(this);
    }

    public int getPacketSize()
    {
        return 18;
    }

    public float func_22031_c()
    {
        return strafeMovement;
    }

    public float func_22029_d()
    {
        return pitchRotation;
    }

    public float func_22028_e()
    {
        return fowardMovement;
    }

    public float func_22033_f()
    {
        return yawRotation;
    }

    public boolean func_22032_g()
    {
        return field_22039_c;
    }

    public boolean func_22030_h()
    {
        return isInJump;
    }

    private float strafeMovement;
    private float fowardMovement;
    private boolean field_22039_c;
    private boolean isInJump;
    private float pitchRotation;
    private float yawRotation;
}
