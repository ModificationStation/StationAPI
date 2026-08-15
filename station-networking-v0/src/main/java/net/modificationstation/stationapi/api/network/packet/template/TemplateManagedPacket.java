package net.modificationstation.stationapi.api.network.packet.template;

import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.impl.network.stream.TrackingOutputStream;

import java.io.DataOutputStream;

/**
 * A simple packet template that implements ManagedPacket and a simple size tracker so you don't have to do it yourself.
 * @param <P> Your packet class.
 */
public abstract class TemplateManagedPacket<P extends Packet & ManagedPacket<P>> extends Packet implements ManagedPacket<P> {
    private static final TrackingOutputStream TRACKER = new TrackingOutputStream();

    @Override
    public void write(DataOutputStream stream) {
        TRACKER.reset(stream);
        write(TRACKER);
    }

    @Override
    public int size() {
        return TRACKER.size();
    }

    /**
     * Implement your mod's packet writing here.
     */
    public abstract void write(TrackingOutputStream outputStream);
}
