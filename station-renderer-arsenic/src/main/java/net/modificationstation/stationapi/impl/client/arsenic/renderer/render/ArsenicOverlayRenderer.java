package net.modificationstation.stationapi.impl.client.arsenic.renderer.render;

import net.minecraft.block.BlockBase;
import net.minecraft.class_556;
import net.minecraft.client.render.RenderHelper;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.PlayerRenderer;
import net.minecraft.entity.Living;
import net.minecraft.entity.player.AbstractClientPlayer;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.storage.MapStorage;
import net.minecraft.util.maths.MathHelper;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.render.model.BakedModel;
import net.modificationstation.stationapi.api.client.render.model.VanillaBakedModel;
import net.modificationstation.stationapi.api.client.render.model.json.ModelTransformation;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.client.texture.SpriteAtlasTexture;
import net.modificationstation.stationapi.api.client.texture.SpriteContents;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.client.texture.atlas.CustomAtlasProvider;
import net.modificationstation.stationapi.api.item.BlockItemForm;
import net.modificationstation.stationapi.mixin.arsenic.client.class_556Accessor;
import org.lwjgl.opengl.GL11;

public final class ArsenicOverlayRenderer {

    private final class_556Accessor access;

    public ArsenicOverlayRenderer(class_556 overlayRenderer) {
        access = (class_556Accessor) overlayRenderer;
    }

    public void renderItem3D(Living entity, ItemInstance item) {
        SpriteAtlasTexture atlas = StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE);
        atlas.bindTexture();
        GL11.glPushMatrix();
        BakedModel model = RendererHolder.RENDERER.getModel(item, entity.level, entity, entity.entityId);
        if (model instanceof VanillaBakedModel) {
            renderVanilla(entity, item, atlas);
        } else {
            renderModel(entity, item);
        }

        GL11.glPopMatrix();
    }

    private void renderVanilla(Living entity, ItemInstance item, SpriteAtlasTexture atlas) {
        BlockBase block;
        if (item.getType() instanceof BlockItemForm blockItemForm && BlockRenderer.method_42((block = blockItemForm.getBlock()).getRenderType()))
            access.stationapi$getField_2405().method_48(block, item.getDamage(), entity.getBrightnessAtEyes(1.0F));
        else {
            Tessellator var3 = Tessellator.INSTANCE;
            int var4 = entity.method_917(item);
            Sprite texture = atlas.getSprite(((CustomAtlasProvider) item.getType()).getAtlas().getTexture(var4).getId());
            float var5 = texture.getMinU();
            float var6 = texture.getMinU() + (texture.getMaxU() - texture.getMinU()) * 0.999375F;
            float var7 = texture.getMinV();
            float var8 = texture.getMinV() + (texture.getMaxV() - texture.getMinV()) * 0.999375F;
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

            final SpriteContents contents = texture.getContents();
            int atlasWidth = (int) (contents.getWidth() / (texture.getMaxU() - texture.getMinU()));
            int atlasHeight = (int) (contents.getHeight() / (texture.getMaxV() - texture.getMinV()));
            int width = contents.getWidth();
            int height = contents.getHeight();
            float du = 1F / (atlasWidth * 2);
            float dv = 1F / (atlasHeight * 2);

            for (int var14 = 0; var14 < width; ++var14) {
                float var15 = (float) var14 / width;
                float var16 = var6 + (var5 - var6) * var15 - du;
                float var17 = var9 * var15;
                var3.vertex(var17, 0.0D, 0.0F - var13, var16, var8);
                var3.vertex(var17, 0.0D, 0.0D, var16, var8);
                var3.vertex(var17, 1.0D, 0.0D, var16, var7);
                var3.vertex(var17, 1.0D, 0.0F - var13, var16, var7);
            }

            var3.draw();
            var3.start();
            var3.setNormal(1.0F, 0.0F, 0.0F);

            for (int var18 = 0; var18 < width; ++var18) {
                float var21 = (float) var18 / width;
                float var24 = var6 + (var5 - var6) * var21 - du;
                float var27 = var9 * var21 + 1F / width;
                var3.vertex(var27, 1.0D, 0.0F - var13, var24, var7);
                var3.vertex(var27, 1.0D, 0.0D, var24, var7);
                var3.vertex(var27, 0.0D, 0.0D, var24, var8);
                var3.vertex(var27, 0.0D, 0.0F - var13, var24, var8);
            }

            var3.draw();
            var3.start();
            var3.setNormal(0.0F, 1.0F, 0.0F);

            for (int var19 = 0; var19 < height; ++var19) {
                float var22 = (float) var19 / height;
                float var25 = var8 + (var7 - var8) * var22 - dv;
                float var28 = var9 * var22 + 1F / height;
                var3.vertex(0.0D, var28, 0.0D, var6, var25);
                var3.vertex(var9, var28, 0.0D, var5, var25);
                var3.vertex(var9, var28, 0.0F - var13, var5, var25);
                var3.vertex(0.0D, var28, 0.0F - var13, var6, var25);
            }

            var3.draw();
            var3.start();
            var3.setNormal(0.0F, -1.0F, 0.0F);

            for (int var20 = 0; var20 < height; ++var20) {
                float var23 = (float) var20 / height;
                float var26 = var8 + (var7 - var8) * var23 - dv;
                float var29 = var9 * var23;
                var3.vertex(var9, var29, 0.0D, var5, var26);
                var3.vertex(0.0D, var29, 0.0D, var6, var26);
                var3.vertex(0.0D, var29, 0.0F - var13, var6, var26);
                var3.vertex(var9, var29, 0.0F - var13, var5, var26);
            }

            var3.draw();
            GL11.glDisable(32826);
        }
    }

    private void renderModel(Living entity, ItemInstance item) {
        GL11.glTranslated(0, 3D / 16, -5D / 16);
        GL11.glRotatef(20, 1, 0, 0);
        GL11.glRotatef(45, 0, 1, 0);
        GL11.glScalef(1, -1, 1);

        // funny little workaround to undo default third person transformation
        GL11.glRotatef(-45, 0, 1, 0);
        GL11.glRotatef(-75, 1, 0, 0);
        GL11.glTranslated(0, -2.5 / 16, 0);

        Tessellator.INSTANCE.start();
        renderItem(entity, item, ModelTransformation.Mode.THIRD_PERSON_RIGHT_HAND);
        Tessellator.INSTANCE.draw();
    }

    public void renderItem(float f) {
        float var2 = access.stationapi$getField_2404() + (access.stationapi$getField_2403() - access.stationapi$getField_2404()) * f;
        AbstractClientPlayer var3 = access.stationapi$getField_2401().player;
        float var4 = var3.prevPitch + (var3.pitch - var3.prevPitch) * f;
        GL11.glPushMatrix();
        GL11.glRotatef(var4, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(var3.prevYaw + (var3.yaw - var3.prevYaw) * f, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableLighting();
        GL11.glPopMatrix();
        ItemInstance var5 = access.stationapi$getField_2402();
        float var6 = access.stationapi$getField_2401().level.getBrightness(MathHelper.floor(var3.x), MathHelper.floor(var3.y), MathHelper.floor(var3.z));
        if (var5 != null) {
            int var7 = ItemBase.byId[var5.itemId].getColourMultiplier(var5.getDamage());
            float var8 = (float)(var7 >> 16 & 255) / 255.0F;
            float var9 = (float)(var7 >> 8 & 255) / 255.0F;
            float var10 = (float)(var7 & 255) / 255.0F;
            GL11.glColor4f(var6 * var8, var6 * var9, var6 * var10, 1.0F);
        } else {
            GL11.glColor4f(var6, var6, var6, 1.0F);
        }

        if (var5 != null) {
            if (var5.itemId == ItemBase.map.id) {
                renderMap(f, var2, var3, var4, var5);
            } else if (RendererHolder.RENDERER.getItemModels().getModel(var5) instanceof VanillaBakedModel) {
                renderVanilla(f, var2, var3, var5);
            } else {
                renderModel(f, var2, var3, var5);
            }
        } else {
            renderHand(f, var2, var3);
        }

        GL11.glDisable(32826);
        RenderHelper.disableLighting();
    }

    private void renderMap(float f, float var2, AbstractClientPlayer var3, float var4, ItemInstance var5) {
        GL11.glPushMatrix();
        float var16 = 0.8F;
        float var23 = var3.method_930(f);
        float var31 = MathHelper.sin(var23 * (float) Math.PI);
        float var40 = MathHelper.sin(MathHelper.sqrt(var23) * (float) Math.PI);
        GL11.glTranslatef(-var40 * 0.4F, MathHelper.sin(MathHelper.sqrt(var23) * (float) Math.PI * 2.0F) * 0.2F, -var31 * 0.2F);
        var23 = 1.0F - var4 / 45.0F + 0.1F;
        if (var23 < 0.0F) {
            var23 = 0.0F;
        }

        if (var23 > 1.0F) {
            var23 = 1.0F;
        }

        var23 = -MathHelper.cos(var23 * (float) Math.PI) * 0.5F + 0.5F;
        GL11.glTranslatef(0.0F, 0.0F * var16 - (1.0F - var2) * 1.2F - var23 * 0.5F + 0.04F, -0.9F * var16);
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(var23 * -85.0F, 0.0F, 0.0F, 1.0F);
        GL11.glEnable(32826);
        GL11.glBindTexture(3553, access.stationapi$getField_2401().textureManager.getOnlineImageOrDefaultTextureId(access.stationapi$getField_2401().player.skinUrl, access.stationapi$getField_2401().player.getTextured()));

        for (int var32 = 0; var32 < 2; ++var32) {
            int var41 = var32 * 2 - 1;
            GL11.glPushMatrix();
            GL11.glTranslatef(-0.0F, -0.6F, 1.1F * (float) var41);
            GL11.glRotatef((float) (-45 * var41), 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(-90.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(59.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef((float) (-65 * var41), 0.0F, 1.0F, 0.0F);
            EntityRenderer var11 = EntityRenderDispatcher.INSTANCE.get(access.stationapi$getField_2401().player);
            PlayerRenderer var12 = (PlayerRenderer) var11;
            float var13 = 1.0F;
            GL11.glScalef(var13, var13, var13);
            var12.method_345();
            GL11.glPopMatrix();
        }

        var31 = var3.method_930(f);
        var40 = MathHelper.sin(var31 * var31 * (float) Math.PI);
        float var44 = MathHelper.sin(MathHelper.sqrt(var31) * (float) Math.PI);
        GL11.glRotatef(-var40 * 20.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-var44 * 20.0F, 0.0F, 0.0F, 1.0F);
        GL11.glRotatef(-var44 * 80.0F, 1.0F, 0.0F, 0.0F);
        var31 = 0.38F;
        GL11.glScalef(var31, var31, var31);
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
        GL11.glTranslatef(-1.0F, -1.0F, 0.0F);
        var40 = 0.015625F;
        GL11.glScalef(var40, var40, var40);
        access.stationapi$getField_2401().textureManager.bindTexture(access.stationapi$getField_2401().textureManager.getTextureId("/misc/mapbg.png"));
        Tessellator var45 = Tessellator.INSTANCE;
        GL11.glNormal3f(0.0F, 0.0F, -1.0F);
        var45.start();
        byte var46 = 7;
        var45.vertex(-var46, 128 + var46, 0.0D, 0.0D, 1.0D);
        var45.vertex(128 + var46, 128 + var46, 0.0D, 1.0D, 1.0D);
        var45.vertex(128 + var46, -var46, 0.0D, 1.0D, 0.0D);
        var45.vertex(-var46, -var46, 0.0D, 0.0D, 0.0D);
        var45.draw();
        MapStorage var47 = ItemBase.map.method_1730(var5, access.stationapi$getField_2401().level);
        access.stationapi$getField_2406().method_1046(access.stationapi$getField_2401().player, access.stationapi$getField_2401().textureManager, var47);
        GL11.glPopMatrix();
    }

    private void renderVanilla(float f, float var2, AbstractClientPlayer var3, ItemInstance var5) {
        GL11.glPushMatrix();
        float f12 = 0.8f;
        float f4 = var3.method_930(f);
        float f3 = MathHelper.sin(f4 * (float)Math.PI);
        float f2 = MathHelper.sin(MathHelper.sqrt(f4) * (float)Math.PI);
        GL11.glTranslatef(-f2 * 0.4f, MathHelper.sin(MathHelper.sqrt(f4) * (float)Math.PI * 2.0f) * 0.2f, -f3 * 0.2f);
        GL11.glTranslatef(0.7f * f12, -0.65f * f12 - (1.0f - var2) * 0.6f, -0.9f * f12);
        GL11.glRotatef(45.0f, 0.0f, 1.0f, 0.0f);
        GL11.glEnable(32826);
        f4 = var3.method_930(f);
        f3 = MathHelper.sin(f4 * f4 * (float)Math.PI);
        f2 = MathHelper.sin(MathHelper.sqrt(f4) * (float)Math.PI);
        GL11.glRotatef(-f3 * 20.0f, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(-f2 * 20.0f, 0.0f, 0.0f, 1.0f);
        GL11.glRotatef(-f2 * 80.0f, 1.0f, 0.0f, 0.0f);
        f4 = 0.4f;
        GL11.glScalef(f4, f4, f4);
        if (var5.getType().shouldSpinWhenRendering()) {
            GL11.glRotatef(180.0f, 0.0f, 1.0f, 0.0f);
        }
        this.renderItem3D(var3, var5);
        GL11.glPopMatrix();
    }

    private void renderModel(float f, float var2, AbstractClientPlayer var3, ItemInstance var5) {
        GL11.glPushMatrix();
        float var17 = var3.method_930(f);
        GL11.glTranslated(0.56, var2 * 0.6 - 1.12, -0.72);
        GL11.glEnable(32826);

        float fu = -0.4f * MathHelper.sin(MathHelper.sqrt(var17) * (float) Math.PI);
        float g = 0.2f * MathHelper.sin(MathHelper.sqrt(var17) * ((float) Math.PI * 2));
        float h = -0.2f * MathHelper.sin(var17 * (float) Math.PI);
        GL11.glTranslatef(fu, g, h);
        applySwingOffset(var17);

        if (var5.getType().shouldSpinWhenRendering()) {
            GL11.glRotatef(180, 0, 1, 0);
        }

        SpriteAtlasTexture atlas = StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE);
        atlas.bindTexture();
        Tessellator.INSTANCE.start();
        this.renderItem(var3, var5, ModelTransformation.Mode.FIRST_PERSON_RIGHT_HAND);
        Tessellator.INSTANCE.draw();
        GL11.glPopMatrix();
    }

    private void renderHand(float f, float var2, AbstractClientPlayer var3) {
        GL11.glPushMatrix();
        float var15 = 0.8F;
        float var20 = var3.method_930(f);
        float var28 = MathHelper.sin(var20 * (float) Math.PI);
        float var37 = MathHelper.sin(MathHelper.sqrt(var20) * (float) Math.PI);
        GL11.glTranslatef(-var37 * 0.3F, MathHelper.sin(MathHelper.sqrt(var20) * (float) Math.PI * 2.0F) * 0.4F, -var28 * 0.4F);
        GL11.glTranslatef(0.8F * var15, -0.75F * var15 - (1.0F - var2) * 0.6F, -0.9F * var15);
        GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
        GL11.glEnable(32826);
        var20 = var3.method_930(f);
        var28 = MathHelper.sin(var20 * var20 * (float) Math.PI);
        var37 = MathHelper.sin(MathHelper.sqrt(var20) * (float) Math.PI);
        GL11.glRotatef(var37 * 70.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-var28 * 20.0F, 0.0F, 0.0F, 1.0F);
        GL11.glBindTexture(3553, access.stationapi$getField_2401().textureManager.getOnlineImageOrDefaultTextureId(access.stationapi$getField_2401().player.skinUrl, access.stationapi$getField_2401().player.getTextured()));
        GL11.glTranslatef(-1.0F, 3.6F, 3.5F);
        GL11.glRotatef(120.0F, 0.0F, 0.0F, 1.0F);
        GL11.glRotatef(200.0F, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
        GL11.glScalef(1.0F, 1.0F, 1.0F);
        GL11.glTranslatef(5.6F, 0.0F, 0.0F);
        EntityRenderer var22 = EntityRenderDispatcher.INSTANCE.get(access.stationapi$getField_2401().player);
        PlayerRenderer var30 = (PlayerRenderer) var22;
        var37 = 1.0F;
        GL11.glScalef(var37, var37, var37);
        var30.method_345();
        GL11.glPopMatrix();
    }

    private void applySwingOffset(float swingProgress) {
        float f = MathHelper.sin(swingProgress * swingProgress * (float)Math.PI);
        GL11.glRotatef(45.0f + f * -20.0f, 0, 1, 0);
        float g = MathHelper.sin(MathHelper.sqrt(swingProgress) * (float)Math.PI);
        GL11.glRotatef(g * -20.0f, 0, 0, 1);
        GL11.glRotatef(g * -80.0f, 1, 0, 0);
        GL11.glRotatef(-45.0f, 0, 1, 0);
    }

    public void renderItem(Living entity, ItemInstance item, ModelTransformation.Mode renderMode) {
        if (item == null || item.itemId == 0 || item.count < 1) return;
        RendererHolder.RENDERER.renderItem(entity, item, renderMode, entity.level, entity.getBrightnessAtEyes(1), entity.entityId + renderMode.ordinal());
    }
}
