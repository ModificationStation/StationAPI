// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.*;
import java.util.*;

// Referenced classes of package net.minecraft.src:
//            PacketCounter, Packet0KeepAlive, Packet1Login, Packet2Handshake, 
//            Packet3Chat, Packet4UpdateTime, Packet5PlayerInventory, Packet6SpawnPosition, 
//            Packet7UseEntity, Packet8UpdateHealth, Packet9Respawn, Packet10Flying, 
//            Packet11PlayerPosition, Packet12PlayerLook, Packet13PlayerLookMove, Packet14BlockDig, 
//            Packet15Place, Packet16BlockItemSwitch, Packet17Sleep, Packet18Animation, 
//            Packet19EntityAction, Packet20NamedEntitySpawn, Packet21PickupSpawn, Packet22Collect, 
//            Packet23VehicleSpawn, Packet24MobSpawn, Packet25EntityPainting, Packet27Position, 
//            Packet28EntityVelocity, Packet29DestroyEntity, Packet30Entity, Packet31RelEntityMove, 
//            Packet32EntityLook, Packet33RelEntityMoveLook, Packet34EntityTeleport, Packet38EntityStatus, 
//            Packet39AttachEntity, Packet40EntityMetadata, Packet50PreChunk, Packet51MapChunk, 
//            Packet52MultiBlockChange, Packet53BlockChange, Packet54PlayNoteBlock, Packet60Explosion, 
//            Packet61DoorChange, Packet70Bed, Packet71Weather, Packet100OpenWindow, 
//            Packet101CloseWindow, Packet102WindowClick, Packet103SetSlot, Packet104WindowItems, 
//            Packet105UpdateProgressbar, Packet106Transaction, Packet130UpdateSign, Packet131MapData, 
//            Packet200Statistic, Packet255KickDisconnect, NetHandler

public abstract class Packet
{

    public Packet()
    {
        isChunkDataPacket = false;
    }

    static void addIdClassMapping(int i, boolean flag, boolean flag1, Class class1)
    {
        if(packetIdToClassMap.containsKey(Integer.valueOf(i)))
        {
            throw new IllegalArgumentException((new StringBuilder()).append("Duplicate packet id:").append(i).toString());
        }
        if(packetClassToIdMap.containsKey(class1))
        {
            throw new IllegalArgumentException((new StringBuilder()).append("Duplicate packet class:").append(class1).toString());
        }
        packetIdToClassMap.put(Integer.valueOf(i), class1);
        packetClassToIdMap.put(class1, Integer.valueOf(i));
        if(flag)
        {
            clientPacketIdList.add(Integer.valueOf(i));
        }
        if(flag1)
        {
            serverPacketIdList.add(Integer.valueOf(i));
        }
    }

    public static Packet getNewPacket(int i)
    {
        try
        {
            Class class1 = (Class)packetIdToClassMap.get(Integer.valueOf(i));
            if(class1 == null)
            {
                return null;
            } else
            {
                return (Packet)class1.newInstance();
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        System.out.println((new StringBuilder()).append("Skipping packet with id ").append(i).toString());
        return null;
    }

    public final int getPacketId()
    {
        return ((Integer)packetClassToIdMap.get(getClass())).intValue();
    }

    public static Packet readPacket(DataInputStream datainputstream, boolean flag)
        throws IOException
    {
        int i = 0;
        Packet packet = null;
        try
        {
            i = datainputstream.read();
            if(i == -1)
            {
                return null;
            }
            if(flag && !serverPacketIdList.contains(Integer.valueOf(i)) || !flag && !clientPacketIdList.contains(Integer.valueOf(i)))
            {
                throw new IOException((new StringBuilder()).append("Bad packet id ").append(i).toString());
            }
            packet = getNewPacket(i);
            if(packet == null)
            {
                throw new IOException((new StringBuilder()).append("Bad packet id ").append(i).toString());
            }
            packet.readPacketData(datainputstream);
        }
        catch(EOFException eofexception)
        {
            System.out.println("Reached end of stream");
            return null;
        }
        PacketCounter packetcounter = (PacketCounter)packetStats.get(Integer.valueOf(i));
        if(packetcounter == null)
        {
            packetcounter = new PacketCounter(null);
            packetStats.put(Integer.valueOf(i), packetcounter);
        }
        packetcounter.addPacket(packet.getPacketSize());
        totalPacketsCount++;
        if(totalPacketsCount % 1000 != 0);
        return packet;
    }

    public static void writePacket(Packet packet, DataOutputStream dataoutputstream)
        throws IOException
    {
        dataoutputstream.write(packet.getPacketId());
        packet.writePacketData(dataoutputstream);
    }

    public static void writeString(String s, DataOutputStream dataoutputstream)
        throws IOException
    {
        if(s.length() > 32767)
        {
            throw new IOException("String too big");
        } else
        {
            dataoutputstream.writeShort(s.length());
            dataoutputstream.writeChars(s);
            return;
        }
    }

    public static String readString(DataInputStream datainputstream, int i)
        throws IOException
    {
        short word0 = datainputstream.readShort();
        if(word0 > i)
        {
            throw new IOException((new StringBuilder()).append("Received string length longer than maximum allowed (").append(word0).append(" > ").append(i).append(")").toString());
        }
        if(word0 < 0)
        {
            throw new IOException("Received string length is less than zero! Weird string!");
        }
        StringBuilder stringbuilder = new StringBuilder();
        for(int j = 0; j < word0; j++)
        {
            stringbuilder.append(datainputstream.readChar());
        }

        return stringbuilder.toString();
    }

    public abstract void readPacketData(DataInputStream datainputstream)
        throws IOException;

    public abstract void writePacketData(DataOutputStream dataoutputstream)
        throws IOException;

    public abstract void processPacket(NetHandler nethandler);

    public abstract int getPacketSize();

    static Class _mthclass$(String s)
    {
        try
        {
            return Class.forName(s);
        }
        catch(ClassNotFoundException classnotfoundexception)
        {
            throw new NoClassDefFoundError(classnotfoundexception.getMessage());
        }
    }

    private static Map packetIdToClassMap = new HashMap();
    private static Map packetClassToIdMap = new HashMap();
    private static Set clientPacketIdList = new HashSet();
    private static Set serverPacketIdList = new HashSet();
    public final long creationTimeMillis = System.currentTimeMillis();
    public boolean isChunkDataPacket;
    private static HashMap packetStats = new HashMap();
    private static int totalPacketsCount = 0;

    static 
    {
        addIdClassMapping(0, true, true, net.minecraft.src.Packet0KeepAlive.class);
        addIdClassMapping(1, true, true, net.minecraft.src.Packet1Login.class);
        addIdClassMapping(2, true, true, net.minecraft.src.Packet2Handshake.class);
        addIdClassMapping(3, true, true, net.minecraft.src.Packet3Chat.class);
        addIdClassMapping(4, true, false, net.minecraft.src.Packet4UpdateTime.class);
        addIdClassMapping(5, true, false, net.minecraft.src.Packet5PlayerInventory.class);
        addIdClassMapping(6, true, false, net.minecraft.src.Packet6SpawnPosition.class);
        addIdClassMapping(7, false, true, net.minecraft.src.Packet7UseEntity.class);
        addIdClassMapping(8, true, false, net.minecraft.src.Packet8UpdateHealth.class);
        addIdClassMapping(9, true, true, net.minecraft.src.Packet9Respawn.class);
        addIdClassMapping(10, true, true, net.minecraft.src.Packet10Flying.class);
        addIdClassMapping(11, true, true, net.minecraft.src.Packet11PlayerPosition.class);
        addIdClassMapping(12, true, true, net.minecraft.src.Packet12PlayerLook.class);
        addIdClassMapping(13, true, true, net.minecraft.src.Packet13PlayerLookMove.class);
        addIdClassMapping(14, false, true, net.minecraft.src.Packet14BlockDig.class);
        addIdClassMapping(15, false, true, net.minecraft.src.Packet15Place.class);
        addIdClassMapping(16, false, true, net.minecraft.src.Packet16BlockItemSwitch.class);
        addIdClassMapping(17, true, false, net.minecraft.src.Packet17Sleep.class);
        addIdClassMapping(18, true, true, net.minecraft.src.Packet18Animation.class);
        addIdClassMapping(19, false, true, net.minecraft.src.Packet19EntityAction.class);
        addIdClassMapping(20, true, false, net.minecraft.src.Packet20NamedEntitySpawn.class);
        addIdClassMapping(21, true, false, net.minecraft.src.Packet21PickupSpawn.class);
        addIdClassMapping(22, true, false, net.minecraft.src.Packet22Collect.class);
        addIdClassMapping(23, true, false, net.minecraft.src.Packet23VehicleSpawn.class);
        addIdClassMapping(24, true, false, net.minecraft.src.Packet24MobSpawn.class);
        addIdClassMapping(25, true, false, net.minecraft.src.Packet25EntityPainting.class);
        addIdClassMapping(27, false, true, net.minecraft.src.Packet27Position.class);
        addIdClassMapping(28, true, false, net.minecraft.src.Packet28EntityVelocity.class);
        addIdClassMapping(29, true, false, net.minecraft.src.Packet29DestroyEntity.class);
        addIdClassMapping(30, true, false, net.minecraft.src.Packet30Entity.class);
        addIdClassMapping(31, true, false, net.minecraft.src.Packet31RelEntityMove.class);
        addIdClassMapping(32, true, false, net.minecraft.src.Packet32EntityLook.class);
        addIdClassMapping(33, true, false, net.minecraft.src.Packet33RelEntityMoveLook.class);
        addIdClassMapping(34, true, false, net.minecraft.src.Packet34EntityTeleport.class);
        addIdClassMapping(38, true, false, net.minecraft.src.Packet38EntityStatus.class);
        addIdClassMapping(39, true, false, net.minecraft.src.Packet39AttachEntity.class);
        addIdClassMapping(40, true, false, net.minecraft.src.Packet40EntityMetadata.class);
        addIdClassMapping(50, true, false, net.minecraft.src.Packet50PreChunk.class);
        addIdClassMapping(51, true, false, net.minecraft.src.Packet51MapChunk.class);
        addIdClassMapping(52, true, false, net.minecraft.src.Packet52MultiBlockChange.class);
        addIdClassMapping(53, true, false, net.minecraft.src.Packet53BlockChange.class);
        addIdClassMapping(54, true, false, net.minecraft.src.Packet54PlayNoteBlock.class);
        addIdClassMapping(60, true, false, net.minecraft.src.Packet60Explosion.class);
        addIdClassMapping(61, true, false, net.minecraft.src.Packet61DoorChange.class);
        addIdClassMapping(70, true, false, net.minecraft.src.Packet70Bed.class);
        addIdClassMapping(71, true, false, net.minecraft.src.Packet71Weather.class);
        addIdClassMapping(100, true, false, net.minecraft.src.Packet100OpenWindow.class);
        addIdClassMapping(101, true, true, net.minecraft.src.Packet101CloseWindow.class);
        addIdClassMapping(102, false, true, net.minecraft.src.Packet102WindowClick.class);
        addIdClassMapping(103, true, false, net.minecraft.src.Packet103SetSlot.class);
        addIdClassMapping(104, true, false, net.minecraft.src.Packet104WindowItems.class);
        addIdClassMapping(105, true, false, net.minecraft.src.Packet105UpdateProgressbar.class);
        addIdClassMapping(106, true, true, net.minecraft.src.Packet106Transaction.class);
        addIdClassMapping(130, true, true, net.minecraft.src.Packet130UpdateSign.class);
        addIdClassMapping(131, true, false, net.minecraft.src.Packet131MapData.class);
        addIdClassMapping(200, true, false, net.minecraft.src.Packet200Statistic.class);
        addIdClassMapping(255, true, true, net.minecraft.src.Packet255KickDisconnect.class);
    }
}
