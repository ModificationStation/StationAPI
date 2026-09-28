package net.modificationstation.sltest.render.entity;

import net.minecraft.block.Block;
import net.minecraft.client.render.block.BlockRenderManager;
import net.modificationstation.sltest.entity.StoneEntity;
import net.modificationstation.stationapi.api.template.entityrenderer.TemplateEntityRenderer;
import org.lwjgl.opengl.GL11;

public class StoneEntityRenderer extends TemplateEntityRenderer<StoneEntity> {
    private static final BlockRenderManager blockRenderManager = new BlockRenderManager();

    @Override
    public void renderEntity(StoneEntity entity, double x, double y, double z, float yaw, float tickDelta) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x, (float)y + 0.5F, (float)z);
        this.bindTexture("/terrain.png");
        blockRenderManager.render(entity.isCobblestone() ? Block.COBBLESTONE : Block.STONE, 0, entity.getBrightnessAtEyes(tickDelta));
        GL11.glPopMatrix();
    }

    @Override
    public void postRenderEntity(StoneEntity entity, double dx, double dy, double dz, float yaw, float tickDelta) {

    }
}
