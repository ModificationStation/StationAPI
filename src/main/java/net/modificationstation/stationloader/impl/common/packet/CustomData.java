package net.modificationstation.stationloader.impl.common.packet;

import net.minecraft.network.PacketHandler;
import net.minecraft.packet.AbstractPacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class CustomData extends AbstractPacket {

    @Override
    public void read(DataInputStream in) {

    }

    @Override
    public void write(DataOutputStream out) {

    }

    @Override
    public void handle(PacketHandler handler) {

    }

    @Override
    public int length() {
        return 0;
    }
}
