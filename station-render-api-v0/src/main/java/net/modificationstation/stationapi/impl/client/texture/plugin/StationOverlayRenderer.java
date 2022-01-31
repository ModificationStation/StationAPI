package net.modificationstation.stationapi.impl.client.texture.plugin;

import net.minecraft.block.BlockBase;
import net.minecraft.class_556;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.entity.Living;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.client.texture.atlas.ExpandableAtlas;
import net.modificationstation.stationapi.api.client.texture.plugin.OverlayRendererPlugin;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

final class StationOverlayRenderer extends OverlayRendererPlugin {

    private final net.modificationstation.stationapi.mixin.render.client.class_556Accessor overlayRendererAccessor;

    StationOverlayRenderer(class_556 overlayRenderer) {
        super(overlayRenderer);
        overlayRendererAccessor = (net.modificationstation.stationapi.mixin.render.client.class_556Accessor) overlayRenderer;
    }

    @Override
    public void renderItem3D(Living entity, ItemInstance item, CallbackInfo ci) {
        renderItem3D(entity, item);
        ci.cancel();
    }

    public void renderItem3D(Living entity, ItemInstance item) {
        GL11.glPushMatrix();
        if (item.itemId < BlockBase.BY_ID.length && BlockRenderer.method_42(BlockBase.BY_ID[item.itemId].getRenderType()))
            overlayRendererAccessor.stationapi$getField_2405().method_48(BlockBase.BY_ID[item.itemId], item.getDamage(), entity.getBrightnessAtEyes(1.0F));
        else {
            Tessellator var3 = Tessellator.INSTANCE;
            int var4 = entity.method_917(item);
            ExpandableAtlas atlas = item.itemId < BlockBase.BY_ID.length ? Atlases.getTerrain() : Atlases.getGuiItems();
            atlas.bindAtlas();
            Atlas.Sprite texture = atlas.getTexture(var4);
            float var5 = (float) (texture.getX() + texture.getWidth() * 0.000625) / atlas.getWidth();
            float var6 = (float) texture.getEndU();
            float var7 = (float) texture.getStartV();
            float var8 = (float) texture.getEndV();
            float var9 = 1.0F;
            float var10 = 0.0F;
            float var11 = 0.3F;
            GL11.glEnable(32826);
            GL11.glTranslatef(-var10, -var11, 0.0F);
            float var12 = 1.5F;
            GL11.glScalef(var12, var12, var12);
            GL11.glRotatef(50.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(335.0F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef(-0.9375F, -0.0625F, 0.0F);
            float var13 = 0.0625F;
            var3.start();
            var3.setNormal(0.0F, 0.0F, 1.0F);
            var3.vertex(0.0D, 0.0D, 0.0D, var6, var8);
            var3.vertex(var9, 0.0D, 0.0D, var5, var8);
            var3.vertex(var9, 1.0D, 0.0D, var5, var7);
            var3.vertex(0.0D, 1.0D, 0.0D, var6, var7);
            var3.draw();
            var3.start();
            var3.setNormal(0.0F, 0.0F, -1.0F);
            var3.vertex(0.0D, 1.0D, 0.0F - var13, var6, var7);
            var3.vertex(var9, 1.0D, 0.0F - var13, var5, var7);
            var3.vertex(var9, 0.0D, 0.0F - var13, var5, var8);
            var3.vertex(0.0D, 0.0D, 0.0F - var13, var6, var8);
            var3.draw();
            var3.start();
            var3.setNormal(-1.0F, 0.0F, 0.0F);

            for(int var14 = 0; var14 < texture.getHeight(); ++var14) {
                float var15 = (float)var14 / texture.getHeight();
                float var16 = var6 + (var5 - var6) * var15 - 1F / (texture.getHeight() * texture.getHeight() * 2);
                float var17 = var9 * var15;
                var3.vertex(var17, 0.0D, 0.0F - var13, var16, var8);
                var3.vertex(var17, 0.0D, 0.0D, var16, var8);
                var3.vertex(var17, 1.0D, 0.0D, var16, var7);
                var3.vertex(var17, 1.0D, 0.0F - var13, var16, var7);
            }

            var3.draw();
            var3.start();
            var3.setNormal(1.0F, 0.0F, 0.0F);

            for(int var18 = 0; var18 < texture.getHeight(); ++var18) {
                float var21 = (float)var18 / texture.getHeight();
                float var24 = var6 + (var5 - var6) * var21 - 1F / (texture.getHeight() * texture.getHeight() * 2);
                float var27 = var9 * var21 + 1F / texture.getHeight();
                var3.vertex(var27, 1.0D, 0.0F - var13, var24, var7);
                var3.vertex(var27, 1.0D, 0.0D, var24, var7);
                var3.vertex(var27, 0.0D, 0.0D, var24, var8);
                var3.vertex(var27, 0.0D, 0.0F - var13, var24, var8);
            }

            var3.draw();
            var3.start();
            var3.setNormal(0.0F, 1.0F, 0.0F);

            for(int var19 = 0; var19 < texture.getWidth(); ++var19) {
                float var22 = (float)var19 / texture.getWidth();
                float var25 = var8 + (var7 - var8) * var22 - 1F / (texture.getWidth() * texture.getWidth() * 2);
                float var28 = var9 * var22 + 1F / texture.getWidth();
                var3.vertex(0.0D, var28, 0.0D, var6, var25);
                var3.vertex(var9, var28, 0.0D, var5, var25);
                var3.vertex(var9, var28, 0.0F - var13, var5, var25);
                var3.vertex(0.0D, var28, 0.0F - var13, var6, var25);
            }

            var3.draw();
            var3.start();
            var3.setNormal(0.0F, -1.0F, 0.0F);

            for(int var20 = 0; var20 < texture.getWidth(); ++var20) {
                float var23 = (float)var20 / texture.getWidth();
                float var26 = var8 + (var7 - var8) * var23 - 1F / (texture.getWidth() * texture.getWidth() * 2);
                float var29 = var9 * var23;
                var3.vertex(var9, var29, 0.0D, var5, var26);
                var3.vertex(0.0D, var29, 0.0D, var6, var26);
                var3.vertex(0.0D, var29, 0.0F - var13, var6, var26);
                var3.vertex(var9, var29, 0.0F - var13, var5, var26);
            }

            var3.draw();
            GL11.glDisable(32826);
        }

        GL11.glPopMatrix();
    }
}
