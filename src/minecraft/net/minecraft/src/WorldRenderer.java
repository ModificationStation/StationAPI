// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.*;
import org.lwjgl.opengl.GL11;

// Referenced classes of package net.minecraft.src:
//            MathHelper, AxisAlignedBB, RenderItem, Chunk, 
//            ChunkCache, RenderBlocks, IBlockAccess, Tessellator, 
//            Block, TileEntityRenderer, Entity, ICamera, 
//            World

public class WorldRenderer
{

    public WorldRenderer(World world, List list, int i, int j, int k, int l, int i1)
    {
        glRenderList = -1;
        isInFrustum = false;
        skipRenderPass = new boolean[2];
        isVisible = true;
        isInitialized = false;
        tileEntityRenderers = new ArrayList();
        worldObj = world;
        tileEntities = list;
        sizeWidth = sizeHeight = sizeDepth = l;
        rendererRadius = MathHelper.sqrt_float(sizeWidth * sizeWidth + sizeHeight * sizeHeight + sizeDepth * sizeDepth) / 2.0F;
        glRenderList = i1;
        posX = -999;
        setPosition(i, j, k);
        needsUpdate = false;
    }

    public void setPosition(int i, int j, int k)
    {
        if(i == posX && j == posY && k == posZ)
        {
            return;
        } else
        {
            setDontDraw();
            posX = i;
            posY = j;
            posZ = k;
            posXPlus = i + sizeWidth / 2;
            posYPlus = j + sizeHeight / 2;
            posZPlus = k + sizeDepth / 2;
            posXClip = i & 0x3ff;
            posYClip = j;
            posZClip = k & 0x3ff;
            posXMinus = i - posXClip;
            posYMinus = j - posYClip;
            posZMinus = k - posZClip;
            float f = 6F;
            rendererBoundingBox = AxisAlignedBB.getBoundingBox((float)i - f, (float)j - f, (float)k - f, (float)(i + sizeWidth) + f, (float)(j + sizeHeight) + f, (float)(k + sizeDepth) + f);
            GL11.glNewList(glRenderList + 2, 4864 /*GL_COMPILE*/);
            RenderItem.renderAABB(AxisAlignedBB.getBoundingBoxFromPool((float)posXClip - f, (float)posYClip - f, (float)posZClip - f, (float)(posXClip + sizeWidth) + f, (float)(posYClip + sizeHeight) + f, (float)(posZClip + sizeDepth) + f));
            GL11.glEndList();
            markDirty();
            return;
        }
    }

    private void setupGLTranslation()
    {
        GL11.glTranslatef(posXClip, posYClip, posZClip);
    }

    public void updateRenderer()
    {
        if(!needsUpdate)
        {
            return;
        }
        chunksUpdated++;
        int i = posX;
        int j = posY;
        int k = posZ;
        int l = posX + sizeWidth;
        int i1 = posY + sizeHeight;
        int j1 = posZ + sizeDepth;
        for(int k1 = 0; k1 < 2; k1++)
        {
            skipRenderPass[k1] = true;
        }

        Chunk.isLit = false;
        HashSet hashset = new HashSet();
        hashset.addAll(tileEntityRenderers);
        tileEntityRenderers.clear();
        int l1 = 1;
        ChunkCache chunkcache = new ChunkCache(worldObj, i - l1, j - l1, k - l1, l + l1, i1 + l1, j1 + l1);
        RenderBlocks renderblocks = new RenderBlocks(chunkcache);
        int i2 = 0;
        do
        {
            if(i2 >= 2)
            {
                break;
            }
            boolean flag = false;
            boolean flag1 = false;
            boolean flag2 = false;
            for(int j2 = j; j2 < i1; j2++)
            {
                for(int k2 = k; k2 < j1; k2++)
                {
                    for(int l2 = i; l2 < l; l2++)
                    {
                        int i3 = chunkcache.getBlockId(l2, j2, k2);
                        if(i3 <= 0)
                        {
                            continue;
                        }
                        if(!flag2)
                        {
                            flag2 = true;
                            GL11.glNewList(glRenderList + i2, 4864 /*GL_COMPILE*/);
                            GL11.glPushMatrix();
                            setupGLTranslation();
                            float f = 1.000001F;
                            GL11.glTranslatef((float)(-sizeDepth) / 2.0F, (float)(-sizeHeight) / 2.0F, (float)(-sizeDepth) / 2.0F);
                            GL11.glScalef(f, f, f);
                            GL11.glTranslatef((float)sizeDepth / 2.0F, (float)sizeHeight / 2.0F, (float)sizeDepth / 2.0F);
                            tessellator.startDrawingQuads();
                            tessellator.setTranslationD(-posX, -posY, -posZ);
                        }
                        if(i2 == 0 && Block.isBlockContainer[i3])
                        {
                            TileEntity tileentity = chunkcache.getBlockTileEntity(l2, j2, k2);
                            if(TileEntityRenderer.instance.hasSpecialRenderer(tileentity))
                            {
                                tileEntityRenderers.add(tileentity);
                            }
                        }
                        Block block = Block.blocksList[i3];
                        int j3 = block.getRenderBlockPass();
                        if(j3 != i2)
                        {
                            flag = true;
                            continue;
                        }
                        if(j3 == i2)
                        {
                            flag1 |= renderblocks.renderBlockByRenderType(block, l2, j2, k2);
                        }
                    }

                }

            }

            if(flag2)
            {
                tessellator.draw();
                GL11.glPopMatrix();
                GL11.glEndList();
                tessellator.setTranslationD(0.0D, 0.0D, 0.0D);
            } else
            {
                flag1 = false;
            }
            if(flag1)
            {
                skipRenderPass[i2] = false;
            }
            if(!flag)
            {
                break;
            }
            i2++;
        } while(true);
        HashSet hashset1 = new HashSet();
        hashset1.addAll(tileEntityRenderers);
        hashset1.removeAll(hashset);
        tileEntities.addAll(hashset1);
        hashset.removeAll(tileEntityRenderers);
        tileEntities.removeAll(hashset);
        isChunkLit = Chunk.isLit;
        isInitialized = true;
    }

    public float distanceToEntitySquared(Entity entity)
    {
        float f = (float)(entity.posX - (double)posXPlus);
        float f1 = (float)(entity.posY - (double)posYPlus);
        float f2 = (float)(entity.posZ - (double)posZPlus);
        return f * f + f1 * f1 + f2 * f2;
    }

    public void setDontDraw()
    {
        for(int i = 0; i < 2; i++)
        {
            skipRenderPass[i] = true;
        }

        isInFrustum = false;
        isInitialized = false;
    }

    public void func_1204_c()
    {
        setDontDraw();
        worldObj = null;
    }

    public int getGLCallListForPass(int i)
    {
        if(!isInFrustum)
        {
            return -1;
        }
        if(!skipRenderPass[i])
        {
            return glRenderList + i;
        } else
        {
            return -1;
        }
    }

    public void updateInFrustrum(ICamera icamera)
    {
        isInFrustum = icamera.isBoundingBoxInFrustum(rendererBoundingBox);
    }

    public void callOcclusionQueryList()
    {
        GL11.glCallList(glRenderList + 2);
    }

    public boolean skipAllRenderPasses()
    {
        if(!isInitialized)
        {
            return false;
        } else
        {
            return skipRenderPass[0] && skipRenderPass[1];
        }
    }

    public void markDirty()
    {
        needsUpdate = true;
    }

    public World worldObj;
    private int glRenderList;
    private static Tessellator tessellator;
    public static int chunksUpdated = 0;
    public int posX;
    public int posY;
    public int posZ;
    public int sizeWidth;
    public int sizeHeight;
    public int sizeDepth;
    public int posXMinus;
    public int posYMinus;
    public int posZMinus;
    public int posXClip;
    public int posYClip;
    public int posZClip;
    public boolean isInFrustum;
    public boolean skipRenderPass[];
    public int posXPlus;
    public int posYPlus;
    public int posZPlus;
    public float rendererRadius;
    public boolean needsUpdate;
    public AxisAlignedBB rendererBoundingBox;
    public int chunkIndex;
    public boolean isVisible;
    public boolean isWaitingOnOcclusionQuery;
    public int glOcclusionQuery;
    public boolean isChunkLit;
    private boolean isInitialized;
    public List tileEntityRenderers;
    private List tileEntities;

    static 
    {
        tessellator = Tessellator.instance;
    }
}
