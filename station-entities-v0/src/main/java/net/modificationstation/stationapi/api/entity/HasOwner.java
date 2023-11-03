package net.modificationstation.stationapi.api.entity;

import net.minecraft.entity.Entity;

public interface HasOwner {

    Entity getOwner();

    void setOwner(Entity entityBase);
}
