package net.modificationstation.stationapi.api.server.entity;

import net.minecraft.entity.Entity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.modificationstation.stationapi.api.entity.HasOwner;

public interface VanillaSpawnDataProvider extends CustomSpawnDataProvider {

    @Override
    default Packet getSpawnData() {
        Entity entityBase = (Entity) this;
        int ownerId = 0;
        if (entityBase instanceof HasOwner hasOwner) {
            Entity owner = hasOwner.getOwner();
            owner = owner == null ? entityBase : owner;
            ownerId = owner.id;
        }
        return new EntitySpawnS2CPacket(entityBase, getSpawnID(), ownerId);
    }

    short getSpawnID();
}
