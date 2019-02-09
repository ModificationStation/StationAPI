// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import net.minecraft.client.Minecraft;

// Referenced classes of package net.minecraft.src:
//            PlayerController, EntityPlayer, World, EntityPlayerSP, 
//            Block, ItemStack, StepSound, SoundManager, 
//            GuiIngame, RenderGlobal

public class PlayerControllerSP extends PlayerController
{

    public PlayerControllerSP(Minecraft minecraft)
    {
        super(minecraft);
        field_1074_c = -1;
        field_1073_d = -1;
        field_1072_e = -1;
        curBlockDamage = 0.0F;
        prevBlockDamage = 0.0F;
        field_1069_h = 0.0F;
        blockHitWait = 0;
    }

    public void flipPlayer(EntityPlayer entityplayer)
    {
        entityplayer.rotationYaw = -180F;
    }

    public boolean sendBlockRemoved(int i, int j, int k, int l)
    {
        int i1 = mc.theWorld.getBlockId(i, j, k);
        int j1 = mc.theWorld.getBlockMetadata(i, j, k);
        boolean flag = super.sendBlockRemoved(i, j, k, l);
        ItemStack itemstack = mc.thePlayer.getCurrentEquippedItem();
        boolean flag1 = mc.thePlayer.canHarvestBlock(Block.blocksList[i1]);
        if(itemstack != null)
        {
            itemstack.onDestroyBlock(i1, i, j, k, mc.thePlayer);
            if(itemstack.stackSize == 0)
            {
                itemstack.func_1097_a(mc.thePlayer);
                mc.thePlayer.destroyCurrentEquippedItem();
            }
        }
        if(flag && flag1)
        {
            Block.blocksList[i1].harvestBlock(mc.theWorld, mc.thePlayer, i, j, k, j1);
        }
        return flag;
    }

    public void clickBlock(int i, int j, int k, int l)
    {
        mc.theWorld.onBlockHit(mc.thePlayer, i, j, k, l);
        int i1 = mc.theWorld.getBlockId(i, j, k);
        if(i1 > 0 && curBlockDamage == 0.0F)
        {
            Block.blocksList[i1].onBlockClicked(mc.theWorld, i, j, k, mc.thePlayer);
        }
        if(i1 > 0 && Block.blocksList[i1].blockStrength(mc.thePlayer) >= 1.0F)
        {
            sendBlockRemoved(i, j, k, l);
        }
    }

    public void resetBlockRemoving()
    {
        curBlockDamage = 0.0F;
        blockHitWait = 0;
    }

    public void sendBlockRemoving(int i, int j, int k, int l)
    {
        if(blockHitWait > 0)
        {
            blockHitWait--;
            return;
        }
        if(i == field_1074_c && j == field_1073_d && k == field_1072_e)
        {
            int i1 = mc.theWorld.getBlockId(i, j, k);
            if(i1 == 0)
            {
                return;
            }
            Block block = Block.blocksList[i1];
            curBlockDamage += block.blockStrength(mc.thePlayer);
            if(field_1069_h % 4F == 0.0F && block != null)
            {
                mc.sndManager.playSound(block.stepSound.func_1145_d(), (float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, (block.stepSound.getVolume() + 1.0F) / 8F, block.stepSound.getPitch() * 0.5F);
            }
            field_1069_h++;
            if(curBlockDamage >= 1.0F)
            {
                sendBlockRemoved(i, j, k, l);
                curBlockDamage = 0.0F;
                prevBlockDamage = 0.0F;
                field_1069_h = 0.0F;
                blockHitWait = 5;
            }
        } else
        {
            curBlockDamage = 0.0F;
            prevBlockDamage = 0.0F;
            field_1069_h = 0.0F;
            field_1074_c = i;
            field_1073_d = j;
            field_1072_e = k;
        }
    }

    public void setPartialTime(float f)
    {
        if(curBlockDamage <= 0.0F)
        {
            mc.ingameGUI.damageGuiPartialTime = 0.0F;
            mc.renderGlobal.damagePartialTime = 0.0F;
        } else
        {
            float f1 = prevBlockDamage + (curBlockDamage - prevBlockDamage) * f;
            mc.ingameGUI.damageGuiPartialTime = f1;
            mc.renderGlobal.damagePartialTime = f1;
        }
    }

    public float getBlockReachDistance()
    {
        return 4F;
    }

    public void func_717_a(World world)
    {
        super.func_717_a(world);
    }

    public void updateController()
    {
        prevBlockDamage = curBlockDamage;
        mc.sndManager.playRandomMusicIfReady();
    }

    private int field_1074_c;
    private int field_1073_d;
    private int field_1072_e;
    private float curBlockDamage;
    private float prevBlockDamage;
    private float field_1069_h;
    private int blockHitWait;
}
