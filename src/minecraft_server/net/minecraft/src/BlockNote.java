// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            BlockContainer, Material, Block, World, 
//            TileEntityNote, EntityPlayer, TileEntity

public class BlockNote extends BlockContainer
{

    public BlockNote(int i)
    {
        super(i, 74, Material.wood);
    }

    public int getBlockTextureFromSide(int i)
    {
        return blockIndexInTexture;
    }

    public void onNeighborBlockChange(World world, int i, int j, int k, int l)
    {
        if(l > 0 && Block.blocksList[l].canProvidePower())
        {
            boolean flag = world.isBlockGettingPowered(i, j, k);
            TileEntityNote tileentitynote = (TileEntityNote)world.getBlockTileEntity(i, j, k);
            if(tileentitynote.previousRedstoneState != flag)
            {
                if(flag)
                {
                    tileentitynote.triggerNote(world, i, j, k);
                }
                tileentitynote.previousRedstoneState = flag;
            }
        }
    }

    public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
        if(world.singleplayerWorld)
        {
            return true;
        } else
        {
            TileEntityNote tileentitynote = (TileEntityNote)world.getBlockTileEntity(i, j, k);
            tileentitynote.changePitch();
            tileentitynote.triggerNote(world, i, j, k);
            return true;
        }
    }

    public void onBlockClicked(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
        if(world.singleplayerWorld)
        {
            return;
        } else
        {
            TileEntityNote tileentitynote = (TileEntityNote)world.getBlockTileEntity(i, j, k);
            tileentitynote.triggerNote(world, i, j, k);
            return;
        }
    }

    protected TileEntity getBlockEntity()
    {
        return new TileEntityNote();
    }

    public void playBlock(World world, int i, int j, int k, int l, int i1)
    {
        float f = (float)Math.pow(2D, (double)(i1 - 12) / 12D);
        String s = "harp";
        if(l == 1)
        {
            s = "bd";
        }
        if(l == 2)
        {
            s = "snare";
        }
        if(l == 3)
        {
            s = "hat";
        }
        if(l == 4)
        {
            s = "bassattack";
        }
        world.playSoundEffect((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, (new StringBuilder()).append("note.").append(s).toString(), 3F, f);
        world.spawnParticle("note", (double)i + 0.5D, (double)j + 1.2D, (double)k + 0.5D, (double)i1 / 24D, 0.0D, 0.0D);
    }
}
