// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   GuiFreezer.java

package net.modificationstation.sltest.gui;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.modificationstation.sltest.inventory.ContainerFreezer;
import net.modificationstation.sltest.tileentity.TileEntityFreezer;
import org.lwjgl.opengl.GL11;

// Referenced classes of package net.minecraft.src:
//            GuiContainer, ContainerFreezer, FontRenderer, RenderEngine, 
//            TileEntityFreezer, InventoryPlayer

public class GuiFreezer extends HandledScreen
{

    public GuiFreezer(PlayerInventory inventoryplayer, TileEntityFreezer tileentityFreezer)
    {
        super(new ContainerFreezer(inventoryplayer, tileentityFreezer));
        freezerInventory = tileentityFreezer;
    }

    @Override
    protected void drawForeground()
    {
        textRenderer.draw("Freezer", 60, 6, 0x404040);
        textRenderer.draw("Inventory", 8, (backgroundHeight - 96) + 2, 0x404040);
    }

    @Override
    protected void drawBackground(float f)
    {
        int i = minecraft.textureManager.getTextureId("/assets/sltest/textures/gui/enchanter.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.textureManager.bindTexture(i);
        int j = (width - backgroundWidth) / 2;
        int k = (height - backgroundHeight) / 2;
        drawTexture(j, k, 0, 0, backgroundWidth, backgroundHeight);
        if(freezerInventory.isBurning())
        {
            int l = freezerInventory.getBurnTimeRemainingScaled(12);
            drawTexture(j + 57, (k + 47) - l, 176, 12 - l, 14, l + 2);
        }
        int i1 = freezerInventory.getCookProgressScaled(24);
        drawTexture(j + 79, k + 35, 176, 14, i1 + 1, 16);
    }

    private TileEntityFreezer freezerInventory;
}
