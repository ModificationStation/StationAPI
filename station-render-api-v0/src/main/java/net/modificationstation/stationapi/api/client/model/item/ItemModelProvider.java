package net.modificationstation.stationapi.api.client.model.item;

import net.minecraft.client.render.TextRenderer;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.client.render.entity.ItemRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.client.model.BakedModel;
import net.modificationstation.stationapi.api.client.model.Model;
import net.modificationstation.stationapi.api.client.render.block.BlockRendererUtil;
import net.modificationstation.stationapi.mixin.render.client.ItemRendererAccessor;
import org.lwjgl.opengl.GL11;

public interface ItemModelProvider extends ItemWithRenderer {

    default Model getModel(ItemInstance itemInstance) {
        return getModel(itemInstance.getDamage());
    }

    Model getModel(int damage);

    @Override
    default void renderItemOnGui(ItemRenderer itemRenderer, TextRenderer textRenderer, TextureManager textureManager, ItemInstance itemInstance, int x, int y) {
        BakedModel model = getModel(itemInstance).getBaked();
        ItemRendererAccessor itemRendererAccessor = (ItemRendererAccessor) itemRenderer;
        GL11.glPushMatrix();
        GL11.glTranslatef((float) (x - 2), (float) (y + 3), -3.0F);
        GL11.glScalef(10.0F, 10.0F, 10.0F);
        GL11.glTranslatef(1.0F, 0.5F, 1.0F);
        GL11.glScalef(1.0F, 1.0F, -1.0F);
        GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
        int var15 = itemInstance.getType().getNameColour(itemInstance.getDamage());
        float var16 = (float)((var15 >> 16) & 255) / 255.0F;
        float var12 = (float)((var15 >> 8) & 255) / 255.0F;
        float var13 = (float)(var15 & 255) / 255.0F;
        if (itemRenderer.field_1707) {
            GL11.glColor4f(var16, var12, var13, 1.0F);
        }

        GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
        BlockRenderer blockRenderer = itemRendererAccessor.getField_1708();
        blockRenderer.itemColourEnabled = itemRenderer.field_1707;
        BlockRendererUtil.getBakedModelRenderer(blockRenderer).renderInventory(model);
        blockRenderer.itemColourEnabled = true;
        GL11.glPopMatrix();
    }

    @Override
    default void renderItemOnGui(ItemRenderer itemRenderer, TextRenderer textRenderer, TextureManager textureManager, int itemId, int damage, int textureIndex, int x, int y) {
        BakedModel model = getModel(damage).getBaked();
        ItemRendererAccessor itemRendererAccessor = (ItemRendererAccessor) itemRenderer;
        GL11.glPushMatrix();
        GL11.glTranslatef((float) (x - 2), (float) (y + 3), -3.0F);
        GL11.glScalef(10.0F, 10.0F, 10.0F);
        GL11.glTranslatef(1.0F, 0.5F, 1.0F);
        GL11.glScalef(1.0F, 1.0F, -1.0F);
        GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
        int var15 = ItemBase.byId[itemId].getNameColour(damage);
        float var16 = (float)((var15 >> 16) & 255) / 255.0F;
        float var12 = (float)((var15 >> 8) & 255) / 255.0F;
        float var13 = (float)(var15 & 255) / 255.0F;
        if (itemRenderer.field_1707) {
            GL11.glColor4f(var16, var12, var13, 1.0F);
        }

        GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
        BlockRenderer blockRenderer = itemRendererAccessor.getField_1708();
        blockRenderer.itemColourEnabled = itemRenderer.field_1707;
        BlockRendererUtil.getBakedModelRenderer(blockRenderer).renderInventory(model);
        blockRenderer.itemColourEnabled = true;
        GL11.glPopMatrix();
    }
}
