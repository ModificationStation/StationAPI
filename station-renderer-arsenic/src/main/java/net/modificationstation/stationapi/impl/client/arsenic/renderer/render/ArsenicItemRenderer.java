package net.modificationstation.stationapi.impl.client.arsenic.renderer.render;

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
import net.modificationstation.stationapi.api.block.States;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.model.item.ItemWithRenderer;
import net.modificationstation.stationapi.api.client.render.model.BakedModel;
import net.modificationstation.stationapi.api.client.render.model.VanillaBakedModel;
import net.modificationstation.stationapi.api.client.render.model.json.ModelTransformation;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.client.texture.SpriteAtlasTexture;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.client.texture.atlas.CustomAtlasProvider;
import net.modificationstation.stationapi.mixin.arsenic.client.EntityRendererAccessor;
import net.modificationstation.stationapi.mixin.arsenic.client.ItemRendererAccessor;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public final class ArsenicItemRenderer {

    private final ItemRenderer itemRenderer;
    private final ItemRendererAccessor itemRendererAccessor;
    private final EntityRendererAccessor entityRendererAccessor;

    public ArsenicItemRenderer(ItemRenderer itemRenderer) {
        this.itemRenderer = itemRenderer;
        this.itemRendererAccessor = (ItemRendererAccessor) this.itemRenderer;
        this.entityRendererAccessor = (EntityRendererAccessor) this.itemRenderer;
    }

    public void render(Item item, double x, double y, double z, float rotation, float delta) {
        itemRendererAccessor.getRand().setSeed(187L);
        ItemInstance var10 = item.item;
        float var11 = MathHelper.sin(((float)item.age + delta) / 10.0F + item.field_567) * 0.1F + 0.1F;
        float var12 = (((float)item.age + delta) / 20.0F + item.field_567) * (180F / (float)Math.PI);
        byte renderedAmount = (byte) (item.item.count > 20 ? 4 : item.item.count > 5 ? 3 : item.item.count > 1 ? 2 : 1);

        GL11.glEnable(32826);
        SpriteAtlasTexture atlas = StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE);
        if (var10.itemId != States.AIR.get().getBlock().id) {
            BakedModel model = RendererHolder.RENDERER.getModel(var10, item.level, null, item.entityId);
            if (model instanceof VanillaBakedModel) {
                GL11.glPushMatrix();
                GL11.glTranslatef((float)x, (float)y + var11, (float)z);
                if (var10.itemId < BlockBase.BY_ID.length && BlockRenderer.method_42(BlockBase.BY_ID[var10.itemId].getRenderType())) {
                    GL11.glRotatef(var12, 0.0F, 1.0F, 0.0F);
                    atlas.bindTexture();
                    float var28 = 0.25F;
                    if (!BlockBase.BY_ID[var10.itemId].isFullCube() && var10.itemId != BlockBase.STONE_SLAB.id && BlockBase.BY_ID[var10.itemId].getRenderType() != 16) {
                        var28 = 0.5F;
                    }

                    GL11.glScalef(var28, var28, var28);

                    for (int var29 = 0; var29 < renderedAmount; ++var29) {
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
                    atlas.bindTexture();
                    Sprite texture = atlas.getSprite(((CustomAtlasProvider) var10.getType()).getAtlas().getTexture(var14).getId());

                    Tessellator var15 = Tessellator.INSTANCE;
                    float var20 = 1.0F;
                    float var21 = 0.5F;
                    float var22 = 0.25F;
                    if (itemRenderer.field_1707) {
                        int var23 = ItemBase.byId[var10.itemId].getColourMultiplier(var10.getDamage());
                        float var24 = (float) ((var23 >> 16) & 255) / 255.0F;
                        float var25 = (float) ((var23 >> 8) & 255) / 255.0F;
                        float var26 = (float) (var23 & 255) / 255.0F;
                        float var27 = item.getBrightnessAtEyes(delta);
                        GL11.glColor4f(var24 * var27, var25 * var27, var26 * var27, 1.0F);
                    }

                    for (int var33 = 0; var33 < renderedAmount; ++var33) {
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
                        var15.vertex(0.0F - var21, 0.0F - var22, 0.0D, texture.getMinU(), texture.getMaxV());
                        var15.vertex(var20 - var21, 0.0F - var22, 0.0D, texture.getMaxU(), texture.getMaxV());
                        var15.vertex(var20 - var21, 1.0F - var22, 0.0D, texture.getMaxU(), texture.getMinV());
                        var15.vertex(0.0F - var21, 1.0F - var22, 0.0D, texture.getMinU(), texture.getMinV());
                        var15.draw();
                        GL11.glPopMatrix();
                    }
                }
                GL11.glPopMatrix();
            } else if (!model.isBuiltin()) {
                GL11.glPushMatrix();
                atlas.bindTexture();
                GL11.glTranslated(x, y + var11, z);
                GL11.glRotatef(var12, 0, 1, 0);
                for (int var29 = 0; var29 < renderedAmount; ++var29) {
                    GL11.glPushMatrix();
                    if (var29 > 0) {
                        if (model.hasDepth()) {
                            GL11.glTranslatef((itemRendererAccessor.getRand().nextFloat() * 2.0F - 1.0F) * 0.15F, (itemRendererAccessor.getRand().nextFloat() * 2.0F - 1.0F) * 0.15F, (itemRendererAccessor.getRand().nextFloat() * 2.0F - 1.0F) * 0.2F * 0.15F);
                        } else {
                            GL11.glTranslatef(0, (itemRendererAccessor.getRand().nextFloat() * 2.0F - 1.0F) * 0.2F * 0.5F, (itemRendererAccessor.getRand().nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F);
                        }
                    }
                    Tessellator.INSTANCE.start();
                    RendererHolder.RENDERER.renderItem(var10, ModelTransformation.Mode.GROUND, item.getBrightnessAtEyes(delta), model);
                    Tessellator.INSTANCE.draw();
                    GL11.glPopMatrix();
                }
                GL11.glPopMatrix();
            }
        }

        GL11.glDisable(32826);
    }

    public void renderItemOnGui(TextRenderer textRenderer, TextureManager textureManager, ItemInstance itemStack, int x, int y, CallbackInfo ci) {
        if (itemStack != null) {
            ItemBase itemBase = itemStack.getType();
            if (itemBase instanceof ItemWithRenderer renderer) {
                renderer.renderItemOnGui(itemRenderer, textRenderer, textureManager, itemStack, x, y);
                ci.cancel();
            } else if (!(RendererHolder.RENDERER.getItemModels().getModel(itemStack) instanceof VanillaBakedModel)) { // TODO: implement a better check
                StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).bindTexture();
                RendererHolder.RENDERER.renderInGuiWithOverrides(itemStack, x, y);
                ci.cancel();
            }
        }
    }

    public void renderItemOnGui(TextRenderer textRenderer, TextureManager textureManager, int id, int damage, int texture, int x, int y) {
        ItemBase item = ItemBase.byId[id];
        if (item instanceof ItemWithRenderer renderer) {
            renderer.renderItemOnGui(itemRenderer, textRenderer, textureManager, id, damage, texture, x, y);
            return;
        }
        SpriteAtlasTexture atlas = StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE);
        if (id < BlockBase.BY_ID.length && BlockRenderer.method_42(BlockBase.BY_ID[id].getRenderType())) {
            atlas.bindTexture();
            BlockBase var14 = BlockBase.BY_ID[id];
            GL11.glPushMatrix();
            GL11.glTranslatef((float)(x - 2), (float)(y + 3), -3.0F);
            GL11.glScalef(10.0F, 10.0F, 10.0F);
            GL11.glTranslatef(1.0F, 0.5F, 1.0F);
            GL11.glScalef(1.0F, 1.0F, -1.0F);
            GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
            int var15 = item.getColourMultiplier(damage);
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
            atlas.bindTexture();
            Sprite sprite = atlas.getSprite(((CustomAtlasProvider) item).getAtlas().getTexture(texture).getId());

            int var8 = item.getColourMultiplier(damage);
            float var9 = (float)((var8 >> 16) & 255) / 255.0F;
            float var10 = (float)((var8 >> 8) & 255) / 255.0F;
            float var11 = (float)(var8 & 255) / 255.0F;
            if (itemRenderer.field_1707) {
                GL11.glColor4f(var9, var10, var11, 1.0F);
            }

            renderItemQuad(x, y, sprite.getMinU(), sprite.getMinV(), sprite.getMaxU(), sprite.getMaxV());
            GL11.glEnable(2896);
        }

        GL11.glEnable(2884);
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
