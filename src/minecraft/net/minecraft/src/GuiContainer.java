// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.List;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

// Referenced classes of package net.minecraft.src:
//            GuiScreen, EntityPlayerSP, RenderHelper, Container, 
//            Slot, InventoryPlayer, RenderItem, StringTranslate, 
//            ItemStack, FontRenderer, RenderEngine, PlayerController, 
//            GameSettings, KeyBinding

public abstract class GuiContainer extends GuiScreen
{

    public GuiContainer(Container container)
    {
        xSize = 176;
        ySize = 166;
        inventorySlots = container;
    }

    public void initGui()
    {
        super.initGui();
        mc.thePlayer.craftingInventory = inventorySlots;
    }

    public void drawScreen(int i, int j, float f)
    {
        drawDefaultBackground();
        int k = (width - xSize) / 2;
        int l = (height - ySize) / 2;
        drawGuiContainerBackgroundLayer(f);
        GL11.glPushMatrix();
        GL11.glRotatef(120F, 1.0F, 0.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glTranslatef(k, l, 0.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(32826 /*GL_RESCALE_NORMAL_EXT*/);
        Slot slot = null;
        for(int i1 = 0; i1 < inventorySlots.slots.size(); i1++)
        {
            Slot slot1 = (Slot)inventorySlots.slots.get(i1);
            drawSlotInventory(slot1);
            if(getIsMouseOverSlot(slot1, i, j))
            {
                slot = slot1;
                GL11.glDisable(2896 /*GL_LIGHTING*/);
                GL11.glDisable(2929 /*GL_DEPTH_TEST*/);
                int j1 = slot1.xDisplayPosition;
                int l1 = slot1.yDisplayPosition;
                drawGradientRect(j1, l1, j1 + 16, l1 + 16, 0x80ffffff, 0x80ffffff);
                GL11.glEnable(2896 /*GL_LIGHTING*/);
                GL11.glEnable(2929 /*GL_DEPTH_TEST*/);
            }
        }

        InventoryPlayer inventoryplayer = mc.thePlayer.inventory;
        if(inventoryplayer.getItemStack() != null)
        {
            GL11.glTranslatef(0.0F, 0.0F, 32F);
            itemRenderer.renderItemIntoGUI(fontRenderer, mc.renderEngine, inventoryplayer.getItemStack(), i - k - 8, j - l - 8);
            itemRenderer.renderItemOverlayIntoGUI(fontRenderer, mc.renderEngine, inventoryplayer.getItemStack(), i - k - 8, j - l - 8);
        }
        GL11.glDisable(32826 /*GL_RESCALE_NORMAL_EXT*/);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(2896 /*GL_LIGHTING*/);
        GL11.glDisable(2929 /*GL_DEPTH_TEST*/);
        drawGuiContainerForegroundLayer();
        if(inventoryplayer.getItemStack() == null && slot != null && slot.getHasStack())
        {
            String s = (new StringBuilder()).append("").append(StringTranslate.getInstance().translateNamedKey(slot.getStack().getItemName())).toString().trim();
            if(s.length() > 0)
            {
                int k1 = (i - k) + 12;
                int i2 = j - l - 12;
                int j2 = fontRenderer.getStringWidth(s);
                drawGradientRect(k1 - 3, i2 - 3, k1 + j2 + 3, i2 + 8 + 3, 0xc0000000, 0xc0000000);
                fontRenderer.drawStringWithShadow(s, k1, i2, -1);
            }
        }
        GL11.glPopMatrix();
        super.drawScreen(i, j, f);
        GL11.glEnable(2896 /*GL_LIGHTING*/);
        GL11.glEnable(2929 /*GL_DEPTH_TEST*/);
    }

    protected void drawGuiContainerForegroundLayer()
    {
    }

    protected abstract void drawGuiContainerBackgroundLayer(float f);

    private void drawSlotInventory(Slot slot)
    {
        int i = slot.xDisplayPosition;
        int j = slot.yDisplayPosition;
        ItemStack itemstack = slot.getStack();
        if(itemstack == null)
        {
            int k = slot.getBackgroundIconIndex();
            if(k >= 0)
            {
                GL11.glDisable(2896 /*GL_LIGHTING*/);
                mc.renderEngine.bindTexture(mc.renderEngine.getTexture("/gui/items.png"));
                drawTexturedModalRect(i, j, (k % 16) * 16, (k / 16) * 16, 16, 16);
                GL11.glEnable(2896 /*GL_LIGHTING*/);
                return;
            }
        }
        itemRenderer.renderItemIntoGUI(fontRenderer, mc.renderEngine, itemstack, i, j);
        itemRenderer.renderItemOverlayIntoGUI(fontRenderer, mc.renderEngine, itemstack, i, j);
    }

    private Slot getSlotAtPosition(int i, int j)
    {
        for(int k = 0; k < inventorySlots.slots.size(); k++)
        {
            Slot slot = (Slot)inventorySlots.slots.get(k);
            if(getIsMouseOverSlot(slot, i, j))
            {
                return slot;
            }
        }

        return null;
    }

    private boolean getIsMouseOverSlot(Slot slot, int i, int j)
    {
        int k = (width - xSize) / 2;
        int l = (height - ySize) / 2;
        i -= k;
        j -= l;
        return i >= slot.xDisplayPosition - 1 && i < slot.xDisplayPosition + 16 + 1 && j >= slot.yDisplayPosition - 1 && j < slot.yDisplayPosition + 16 + 1;
    }

    protected void mouseClicked(int i, int j, int k)
    {
        super.mouseClicked(i, j, k);
        if(k == 0 || k == 1)
        {
            Slot slot = getSlotAtPosition(i, j);
            int l = (width - xSize) / 2;
            int i1 = (height - ySize) / 2;
            boolean flag = i < l || j < i1 || i >= l + xSize || j >= i1 + ySize;
            int j1 = -1;
            if(slot != null)
            {
                j1 = slot.slotNumber;
            }
            if(flag)
            {
                j1 = -999;
            }
            if(j1 != -1)
            {
                boolean flag1 = j1 != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
                mc.playerController.func_27174_a(inventorySlots.windowId, j1, k, flag1, mc.thePlayer);
            }
        }
    }

    protected void mouseMovedOrUp(int i, int j, int k)
    {
        if(k != 0);
    }

    protected void keyTyped(char c, int i)
    {
        if(i == 1 || i == mc.gameSettings.keyBindInventory.keyCode)
        {
            mc.thePlayer.closeScreen();
        }
    }

    public void onGuiClosed()
    {
        if(mc.thePlayer == null)
        {
            return;
        } else
        {
            mc.playerController.func_20086_a(inventorySlots.windowId, mc.thePlayer);
            return;
        }
    }

    public boolean doesGuiPauseGame()
    {
        return false;
    }

    public void updateScreen()
    {
        super.updateScreen();
        if(!mc.thePlayer.isEntityAlive() || mc.thePlayer.isDead)
        {
            mc.thePlayer.closeScreen();
        }
    }

    private static RenderItem itemRenderer = new RenderItem();
    protected int xSize;
    protected int ySize;
    public Container inventorySlots;

}
