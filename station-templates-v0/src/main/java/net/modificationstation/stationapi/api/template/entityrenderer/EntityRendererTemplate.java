package net.modificationstation.stationapi.api.template.entityrenderer;

import net.minecraft.entity.Entity;

public interface EntityRendererTemplate<T extends Entity> {
    void renderEntity(T entity, double x, double y, double z, float yaw, float tickDelta);

    void postRenderEntity(T entity, double dx, double dy, double dz, float yaw, float tickDelta);
}
