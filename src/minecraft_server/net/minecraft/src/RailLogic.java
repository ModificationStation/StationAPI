// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;

// Referenced classes of package net.minecraft.src:
//            World, Block, BlockRail, ChunkPosition

class RailLogic
{

    public RailLogic(BlockRail blockrail, World world, int i, int j, int k)
    {
//        super();
        minecartTrack = blockrail;
        connectedTracks = new ArrayList();
        worldObj = world;
        trackX = i;
        trackY = j;
        trackZ = k;
        int l = world.getBlockId(i, j, k);
        int i1 = world.getBlockMetadata(i, j, k);
        if(BlockRail.func_27033_a((BlockRail)Block.blocksList[l]))
        {
            field_27084_f = true;
            i1 &= -9;
        } else
        {
            field_27084_f = false;
        }
        func_27083_a(i1);
    }

    private void func_27083_a(int i)
    {
        connectedTracks.clear();
        if(i == 0)
        {
            connectedTracks.add(new ChunkPosition(trackX, trackY, trackZ - 1));
            connectedTracks.add(new ChunkPosition(trackX, trackY, trackZ + 1));
        } else
        if(i == 1)
        {
            connectedTracks.add(new ChunkPosition(trackX - 1, trackY, trackZ));
            connectedTracks.add(new ChunkPosition(trackX + 1, trackY, trackZ));
        } else
        if(i == 2)
        {
            connectedTracks.add(new ChunkPosition(trackX - 1, trackY, trackZ));
            connectedTracks.add(new ChunkPosition(trackX + 1, trackY + 1, trackZ));
        } else
        if(i == 3)
        {
            connectedTracks.add(new ChunkPosition(trackX - 1, trackY + 1, trackZ));
            connectedTracks.add(new ChunkPosition(trackX + 1, trackY, trackZ));
        } else
        if(i == 4)
        {
            connectedTracks.add(new ChunkPosition(trackX, trackY + 1, trackZ - 1));
            connectedTracks.add(new ChunkPosition(trackX, trackY, trackZ + 1));
        } else
        if(i == 5)
        {
            connectedTracks.add(new ChunkPosition(trackX, trackY, trackZ - 1));
            connectedTracks.add(new ChunkPosition(trackX, trackY + 1, trackZ + 1));
        } else
        if(i == 6)
        {
            connectedTracks.add(new ChunkPosition(trackX + 1, trackY, trackZ));
            connectedTracks.add(new ChunkPosition(trackX, trackY, trackZ + 1));
        } else
        if(i == 7)
        {
            connectedTracks.add(new ChunkPosition(trackX - 1, trackY, trackZ));
            connectedTracks.add(new ChunkPosition(trackX, trackY, trackZ + 1));
        } else
        if(i == 8)
        {
            connectedTracks.add(new ChunkPosition(trackX - 1, trackY, trackZ));
            connectedTracks.add(new ChunkPosition(trackX, trackY, trackZ - 1));
        } else
        if(i == 9)
        {
            connectedTracks.add(new ChunkPosition(trackX + 1, trackY, trackZ));
            connectedTracks.add(new ChunkPosition(trackX, trackY, trackZ - 1));
        }
    }

    private void func_591_b()
    {
        for(int i = 0; i < connectedTracks.size(); i++)
        {
            RailLogic raillogic = getMinecartTrackLogic((ChunkPosition)connectedTracks.get(i));
            if(raillogic == null || !raillogic.isConnectedTo(this))
            {
                connectedTracks.remove(i--);
            } else
            {
                connectedTracks.set(i, new ChunkPosition(raillogic.trackX, raillogic.trackY, raillogic.trackZ));
            }
        }

    }

    private boolean isMinecartTrack(int i, int j, int k)
    {
        if(BlockRail.func_27029_g(worldObj, i, j, k))
        {
            return true;
        }
        if(BlockRail.func_27029_g(worldObj, i, j + 1, k))
        {
            return true;
        }
        return BlockRail.func_27029_g(worldObj, i, j - 1, k);
    }

    private RailLogic getMinecartTrackLogic(ChunkPosition chunkposition)
    {
        if(BlockRail.func_27029_g(worldObj, chunkposition.x, chunkposition.y, chunkposition.z))
        {
            return new RailLogic(minecartTrack, worldObj, chunkposition.x, chunkposition.y, chunkposition.z);
        }
        if(BlockRail.func_27029_g(worldObj, chunkposition.x, chunkposition.y + 1, chunkposition.z))
        {
            return new RailLogic(minecartTrack, worldObj, chunkposition.x, chunkposition.y + 1, chunkposition.z);
        }
        if(BlockRail.func_27029_g(worldObj, chunkposition.x, chunkposition.y - 1, chunkposition.z))
        {
            return new RailLogic(minecartTrack, worldObj, chunkposition.x, chunkposition.y - 1, chunkposition.z);
        } else
        {
            return null;
        }
    }

    private boolean isConnectedTo(RailLogic raillogic)
    {
        for(int i = 0; i < connectedTracks.size(); i++)
        {
            ChunkPosition chunkposition = (ChunkPosition)connectedTracks.get(i);
            if(chunkposition.x == raillogic.trackX && chunkposition.z == raillogic.trackZ)
            {
                return true;
            }
        }

        return false;
    }

    private boolean func_599_b(int i, int j, int k)
    {
        for(int l = 0; l < connectedTracks.size(); l++)
        {
            ChunkPosition chunkposition = (ChunkPosition)connectedTracks.get(l);
            if(chunkposition.x == i && chunkposition.z == k)
            {
                return true;
            }
        }

        return false;
    }

    private int getAdjacentTracks()
    {
        int i = 0;
        if(isMinecartTrack(trackX, trackY, trackZ - 1))
        {
            i++;
        }
        if(isMinecartTrack(trackX, trackY, trackZ + 1))
        {
            i++;
        }
        if(isMinecartTrack(trackX - 1, trackY, trackZ))
        {
            i++;
        }
        if(isMinecartTrack(trackX + 1, trackY, trackZ))
        {
            i++;
        }
        return i;
    }

    private boolean handleKeyPress(RailLogic raillogic)
    {
        if(isConnectedTo(raillogic))
        {
            return true;
        }
        if(connectedTracks.size() == 2)
        {
            return false;
        }
        if(connectedTracks.size() == 0)
        {
            return true;
        }
        ChunkPosition chunkposition = (ChunkPosition)connectedTracks.get(0);
        return raillogic.trackY != trackY || chunkposition.y != trackY ? true : true;
    }

    private void func_598_d(RailLogic raillogic)
    {
        connectedTracks.add(new ChunkPosition(raillogic.trackX, raillogic.trackY, raillogic.trackZ));
        boolean flag = func_599_b(trackX, trackY, trackZ - 1);
        boolean flag1 = func_599_b(trackX, trackY, trackZ + 1);
        boolean flag2 = func_599_b(trackX - 1, trackY, trackZ);
        boolean flag3 = func_599_b(trackX + 1, trackY, trackZ);
        byte byte0 = -1;
        if(flag || flag1)
        {
            byte0 = 0;
        }
        if(flag2 || flag3)
        {
            byte0 = 1;
        }
        if(!field_27084_f)
        {
            if(flag1 && flag3 && !flag && !flag2)
            {
                byte0 = 6;
            }
            if(flag1 && flag2 && !flag && !flag3)
            {
                byte0 = 7;
            }
            if(flag && flag2 && !flag1 && !flag3)
            {
                byte0 = 8;
            }
            if(flag && flag3 && !flag1 && !flag2)
            {
                byte0 = 9;
            }
        }
        if(byte0 == 0)
        {
            if(BlockRail.func_27029_g(worldObj, trackX, trackY + 1, trackZ - 1))
            {
                byte0 = 4;
            }
            if(BlockRail.func_27029_g(worldObj, trackX, trackY + 1, trackZ + 1))
            {
                byte0 = 5;
            }
        }
        if(byte0 == 1)
        {
            if(BlockRail.func_27029_g(worldObj, trackX + 1, trackY + 1, trackZ))
            {
                byte0 = 2;
            }
            if(BlockRail.func_27029_g(worldObj, trackX - 1, trackY + 1, trackZ))
            {
                byte0 = 3;
            }
        }
        if(byte0 < 0)
        {
            byte0 = 0;
        }
        int i = byte0;
        if(field_27084_f)
        {
            i = worldObj.getBlockMetadata(trackX, trackY, trackZ) & 8 | byte0;
        }
        worldObj.setBlockMetadataWithNotify(trackX, trackY, trackZ, i);
    }

    private boolean func_592_c(int i, int j, int k)
    {
        RailLogic raillogic = getMinecartTrackLogic(new ChunkPosition(i, j, k));
        if(raillogic == null)
        {
            return false;
        } else
        {
            raillogic.func_591_b();
            return raillogic.handleKeyPress(this);
        }
    }

    public void func_596_a(boolean flag, boolean flag1)
    {
        boolean flag2 = func_592_c(trackX, trackY, trackZ - 1);
        boolean flag3 = func_592_c(trackX, trackY, trackZ + 1);
        boolean flag4 = func_592_c(trackX - 1, trackY, trackZ);
        boolean flag5 = func_592_c(trackX + 1, trackY, trackZ);
        byte byte0 = -1;
        if((flag2 || flag3) && !flag4 && !flag5)
        {
            byte0 = 0;
        }
        if((flag4 || flag5) && !flag2 && !flag3)
        {
            byte0 = 1;
        }
        if(!field_27084_f)
        {
            if(flag3 && flag5 && !flag2 && !flag4)
            {
                byte0 = 6;
            }
            if(flag3 && flag4 && !flag2 && !flag5)
            {
                byte0 = 7;
            }
            if(flag2 && flag4 && !flag3 && !flag5)
            {
                byte0 = 8;
            }
            if(flag2 && flag5 && !flag3 && !flag4)
            {
                byte0 = 9;
            }
        }
        if(byte0 == -1)
        {
            if(flag2 || flag3)
            {
                byte0 = 0;
            }
            if(flag4 || flag5)
            {
                byte0 = 1;
            }
            if(!field_27084_f)
            {
                if(flag)
                {
                    if(flag3 && flag5)
                    {
                        byte0 = 6;
                    }
                    if(flag4 && flag3)
                    {
                        byte0 = 7;
                    }
                    if(flag5 && flag2)
                    {
                        byte0 = 9;
                    }
                    if(flag2 && flag4)
                    {
                        byte0 = 8;
                    }
                } else
                {
                    if(flag2 && flag4)
                    {
                        byte0 = 8;
                    }
                    if(flag5 && flag2)
                    {
                        byte0 = 9;
                    }
                    if(flag4 && flag3)
                    {
                        byte0 = 7;
                    }
                    if(flag3 && flag5)
                    {
                        byte0 = 6;
                    }
                }
            }
        }
        if(byte0 == 0)
        {
            if(BlockRail.func_27029_g(worldObj, trackX, trackY + 1, trackZ - 1))
            {
                byte0 = 4;
            }
            if(BlockRail.func_27029_g(worldObj, trackX, trackY + 1, trackZ + 1))
            {
                byte0 = 5;
            }
        }
        if(byte0 == 1)
        {
            if(BlockRail.func_27029_g(worldObj, trackX + 1, trackY + 1, trackZ))
            {
                byte0 = 2;
            }
            if(BlockRail.func_27029_g(worldObj, trackX - 1, trackY + 1, trackZ))
            {
                byte0 = 3;
            }
        }
        if(byte0 < 0)
        {
            byte0 = 0;
        }
        func_27083_a(byte0);
        int i = byte0;
        if(field_27084_f)
        {
            i = worldObj.getBlockMetadata(trackX, trackY, trackZ) & 8 | byte0;
        }
        if(flag1 || worldObj.getBlockMetadata(trackX, trackY, trackZ) != i)
        {
            worldObj.setBlockMetadataWithNotify(trackX, trackY, trackZ, i);
            for(int j = 0; j < connectedTracks.size(); j++)
            {
                RailLogic raillogic = getMinecartTrackLogic((ChunkPosition)connectedTracks.get(j));
                if(raillogic == null)
                {
                    continue;
                }
                raillogic.func_591_b();
                if(raillogic.handleKeyPress(this))
                {
                    raillogic.func_598_d(this);
                }
            }

        }
    }

    static int getNAdjacentTracks(RailLogic raillogic)
    {
        return raillogic.getAdjacentTracks();
    }

    private World worldObj;
    private int trackX;
    private int trackY;
    private int trackZ;
    private final boolean field_27084_f;
    private List connectedTracks;
    final BlockRail minecartTrack; /* synthetic field */
}
