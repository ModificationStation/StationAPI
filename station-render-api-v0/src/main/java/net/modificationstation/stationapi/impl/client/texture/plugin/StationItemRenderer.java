package net.modificationstation.stationapi.impl.client.texture.plugin;

import net.minecraft.block.BlockBase;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.TextRenderer;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.client.render.entity.ItemRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.entity.Item;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.util.maths.MathHelper;
import net.modificationstation.stationapi.api.client.model.item.ItemWithRenderer;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.CustomAtlasProvider;
import net.modificationstation.stationapi.api.client.texture.plugin.ItemRendererPlugin;
import net.modificationstation.stationapi.mixin.render.client.EntityRendererAccessor;
import net.modificationstation.stationapi.mixin.render.client.ItemRendererAccessor;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class StationItemRenderer extends ItemRendererPlugin {

    private final ItemRendererAccessor itemRendererAccessor;
    private final EntityRendererAccessor entityRendererAccessor;

    public StationItemRenderer(ItemRenderer itemRenderer) {
        super(itemRenderer);
        this.itemRendererAccessor = (ItemRendererAccessor) this.itemRenderer;
        this.entityRendererAccessor = (EntityRendererAccessor) this.itemRenderer;
    }

    @Override
    public void render(Item item, double x, double y, double z, float rotation, float delta, CallbackInfo ci) {
        itemRendererAccessor.getRand().setSeed(187L);
        ItemInstance var10 = item.item;
        GL11.glPushMatrix();
        float var11 = MathHelper.sin(((float)item.age + delta) / 10.0F + item.field_567) * 0.1F + 0.1F;
        float var12 = (((float)item.age + delta) / 20.0F + item.field_567) * (180F / (float)Math.PI);
        byte var13 = 1;
        if (item.item.count > 1) {
            var13 = 2;
        }

        if (item.item.count > 5) {
            var13 = 3;
        }

        if (item.item.count > 20) {
            var13 = 4;
        }

        GL11.glTranslatef((float)x, (float)y + var11, (float)z);
        GL11.glEnable(32826);
        Atlas topAtlas = ((CustomAtlasProvider) var10.getType()).getAtlas();
        if (var10.itemId < BlockBase.BY_ID.length && BlockRenderer.method_42(BlockBase.BY_ID[var10.itemId].getRenderType())) {
            GL11.glRotatef(var12, 0.0F, 1.0F, 0.0F);
            entityRendererAccessor.invokeBindTexture("/terrain.png");
            float var28 = 0.25F;
            if (!BlockBase.BY_ID[var10.itemId].isFullCube() && var10.itemId != BlockBase.STONE_SLAB.id && BlockBase.BY_ID[var10.itemId].getRenderType() != 16) {
                var28 = 0.5F;
            }

            GL11.glScalef(var28, var28, var28);

            for(int var29 = 0; var29 < var13; ++var29) {
                GL11.glPushMatrix();
                if (var29 > 0) {
                    float var30 = (itemRendererAccessor.getRand().nextFloat() * 2.0F - 1.0F) * 0.2F / var28;
                    float var31 = (itemRendererAccessor.getRand().nextFloat() * 2.0F - 1.0F) * 0.2F / var28;
                    float var32 = (itemRendererAccessor.getRand().nextFloat() * 2.0F - 1.0F) * 0.2F / var28;
                    GL11.glTranslatef(var30, var31, var32);
                }

                itemRendererAccessor.getField_1708().method_48(BlockBase.BY_ID[var10.itemId], var10.getDamage(), item.getBrightnessAtEyes(delta));
                GL11.glPopMatrix();
            }
        } else {
            GL11.glScalef(0.5F, 0.5F, 0.5F);
            int var14 = var10.getTexturePosition();
            Atlas atlas = topAtlas.of(var14);
            atlas.bindAtlas();
            Atlas.Sprite texture = atlas.getTexture(var14);

            Tessellator var15 = Tessellator.INSTANCE;
            float var20 = 1.0F;
            float var21 = 0.5F;
            float var22 = 0.25F;
            if (itemRenderer.field_1707) {
                int var23 = ItemBase.byId[var10.itemId].getNameColour(var10.getDamage());
                float var24 = (float)((var23 >> 16) & 255) / 255.0F;
                float var25 = (float)((var23 >> 8) & 255) / 255.0F;
                float var26 = (float)(var23 & 255) / 255.0F;
                float var27 = item.getBrightnessAtEyes(delta);
                GL11.glColor4f(var24 * var27, var25 * var27, var26 * var27, 1.0F);
            }

            for(int var33 = 0; var33 < var13; ++var33) {
                GL11.glPushMatrix();
                if (var33 > 0) {
                    float var34 = (itemRendererAccessor.getRand().nextFloat() * 2.0F - 1.0F) * 0.3F;
                    float var35 = (itemRendererAccessor.getRand().nextFloat() * 2.0F - 1.0F) * 0.3F;
                    float var36 = (itemRendererAccessor.getRand().nextFloat() * 2.0F - 1.0F) * 0.3F;
                    GL11.glTranslatef(var34, var35, var36);
                }

                GL11.glRotatef(180.0F - entityRendererAccessor.getDispatcher().field_2497, 0.0F, 1.0F, 0.0F);
                var15.start();
                var15.setNormal(0.0F, 1.0F, 0.0F);
                var15.vertex(0.0F - var21, 0.0F - var22, 0.0D, texture.getStartU(), texture.getEndV());
                var15.vertex(var20 - var21, 0.0F - var22, 0.0D, texture.getEndU(), texture.getEndV());
                var15.vertex(var20 - var21, 1.0F - var22, 0.0D, texture.getEndU(), texture.getStartV());
                var15.vertex(0.0F - var21, 1.0F - var22, 0.0D, texture.getStartU(), texture.getStartV());
                var15.draw();
                GL11.glPopMatrix();
            }
        }

        GL11.glDisable(32826);
        GL11.glPopMatrix();
        ci.cancel();
    }

    @Override
    public void renderItemOnGui(TextRenderer textRenderer, TextureManager textureManager, ItemInstance itemInstance, int x, int y, CallbackInfo ci) {
        if (itemInstance != null) {
            ItemBase itemBase = itemInstance.getType();
            if (itemBase instanceof ItemWithRenderer) {
                ((ItemWithRenderer) itemBase).renderItemOnGui(itemRenderer, textRenderer, textureManager, itemInstance, x, y);
                ci.cancel();
            }
        }
    }

    @Override
    public void renderItemOnGui(TextRenderer textRenderer, TextureManager textureManager, int id, int damage, int texture, int x, int y, CallbackInfo ci) {
        ItemBase item = ItemBase.byId[id];
        if (item instanceof ItemWithRenderer) {
            ((ItemWithRenderer) item).renderItemOnGui(itemRenderer, textRenderer, textureManager, id, damage, texture, x, y);
            ci.cancel();
            return;
        }
        Atlas atlas = ((CustomAtlasProvider) item).getAtlas().of(texture);
        if (id < BlockBase.BY_ID.length && BlockRenderer.method_42(BlockBase.BY_ID[id].getRenderType())) {
            textureManager.bindTexture(textureManager.getTextureId("/terrain.png"));
            BlockBase var14 = BlockBase.BY_ID[id];
            GL11.glPushMatrix();
            GL11.glTranslatef((float)(x - 2), (float)(y + 3), -3.0F);
            GL11.glScalef(10.0F, 10.0F, 10.0F);
            GL11.glTranslatef(1.0F, 0.5F, 1.0F);
            GL11.glScalef(1.0F, 1.0F, -1.0F);
            GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
            int var15 = item.getNameColour(damage);
            float var16 = (float)((var15 >> 16) & 255) / 255.0F;
            float var12 = (float)((var15 >> 8) & 255) / 255.0F;
            float var13 = (float)(var15 & 255) / 255.0F;
            if (itemRenderer.field_1707) {
                GL11.glColor4f(var16, var12, var13, 1.0F);
            }

            GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
            itemRendererAccessor.getField_1708().itemColourEnabled = itemRenderer.field_1707;
            itemRendererAccessor.getField_1708().method_48(var14, damage, 1.0F);
            itemRendererAccessor.getField_1708().itemColourEnabled = true;
            GL11.glPopMatrix();
        } else if (texture >= 0) {
            GL11.glDisable(2896);
            atlas.bindAtlas();
            Atlas.Sprite sprite = atlas.getTexture(texture);

            int var8 = item.getNameColour(damage);
            float var9 = (float)((var8 >> 16) & 255) / 255.0F;
            float var10 = (float)((var8 >> 8) & 255) / 255.0F;
            float var11 = (float)(var8 & 255) / 255.0F;
            if (itemRenderer.field_1707) {
                GL11.glColor4f(var9, var10, var11, 1.0F);
            }

            renderItemQuad(x, y, sprite.getStartU(), sprite.getStartV(), sprite.getEndU(), sprite.getEndV());
            GL11.glEnable(2896);
        }

        GL11.glEnable(2884);
        ci.cancel();
    }

    public void renderItemQuad(int x, int y, double startU, double startV, double endU, double endV) {
        Tessellator var10 = Tessellator.INSTANCE;
        var10.start();
        var10.vertex(x, y + 16, 0, startU, endV);
        var10.vertex(x + 16, y + 16, 0, endU, endV);
        var10.vertex(x + 16, y, 0, endU, startV);
        var10.vertex(x, y, 0, startU, startV);
        var10.draw();
    }
}
