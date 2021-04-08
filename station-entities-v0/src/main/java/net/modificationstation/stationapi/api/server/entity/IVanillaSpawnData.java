package net.modificationstation.stationapi.api.server.entity;

import net.minecraft.entity.EntityBase;
import net.minecraft.packet.AbstractPacket;
import net.minecraft.packet.play.EntitySpawnS2C;
import net.modificationstation.stationapi.api.common.entity.HasOwner;

public interface IVanillaSpawnData extends CustomSpawnData {

    @Override
    default AbstractPacket getSpawnData() {
        EntityBase entityBase = (EntityBase) this;
        int ownerId = 0;
        if (entityBase instanceof HasOwner) {
            EntityBase owner = ((HasOwner) entityBase).getOwner();
            owner = owner == null ? entityBase : owner;
            ownerId = owner.entityId;
        }
        return new EntitySpawnS2C(entityBase, getSpawnID(), ownerId);
    }

    short getSpawnID();
}
