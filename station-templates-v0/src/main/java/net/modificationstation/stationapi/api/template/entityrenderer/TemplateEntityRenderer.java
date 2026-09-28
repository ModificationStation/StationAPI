package net.modificationstation.stationapi.api.template.entityrenderer;

import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;

@SuppressWarnings("unchecked")
public abstract class TemplateEntityRenderer<T extends Entity> extends EntityRenderer implements EntityRendererTemplate<T>{
    @Override
    public void render(Entity entity, double x, double y, double z, float yaw, float pitch) {
        this.renderEntity((T)entity, x, y, z, yaw, pitch);
    }

    @Override
    public void postRender(Entity entity, double dx, double dy, double dz, float yaw, float tickDelta) {
        this.postRenderEntity((T)entity, dx, dy, dz, yaw, tickDelta);
    }

    @Override
    public abstract void renderEntity(T entity, double x, double y, double z, float yaw, float tickDelta);

    @Override
    public abstract void postRenderEntity(T entity, double dx, double dy, double dz, float yaw, float tickDelta);
}
