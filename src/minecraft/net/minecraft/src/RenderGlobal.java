// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.nio.IntBuffer;
import java.util.*;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.ARBOcclusionQuery;
import org.lwjgl.opengl.GL11;

// Referenced classes of package net.minecraft.src:
//            IWorldAccess, GLAllocation, RenderList, OpenGlCapsChecker, 
//            Tessellator, World, RenderManager, RenderBlocks, 
//            Block, GameSettings, BlockLeaves, WorldRenderer, 
//            Entity, MathHelper, EntitySorter, TileEntityRenderer, 
//            ICamera, EntityLiving, TileEntity, RenderHelper, 
//            EntityRenderer, WorldProvider, Vec3D, RenderEngine, 
//            RenderSorter, MovingObjectPosition, EntityPlayer, EnumMovingObjectType, 
//            AxisAlignedBB, GuiIngame, SoundManager, EntityBubbleFX, 
//            EffectRenderer, EntitySmokeFX, EntityNoteFX, EntityPortalFX, 
//            EntityExplodeFX, EntityFlameFX, EntityLavaFX, EntityFootStepFX, 
//            EntitySplashFX, EntityReddustFX, EntitySlimeFX, Item, 
//            EntitySnowShovelFX, EntityHeartFX, ImageBufferDownload, StepSound, 
//            ItemRecord, ItemStack

public class RenderGlobal
    implements IWorldAccess
{

    public RenderGlobal(Minecraft minecraft, RenderEngine renderengine)
    {
        tileEntities = new ArrayList();
        worldRenderersToUpdate = new ArrayList();
        occlusionEnabled = false;
        cloudOffsetX = 0;
        renderDistance = -1;
        renderEntitiesStartupCounter = 2;
        dummyBuf50k = new int[50000];
        occlusionResult = GLAllocation.createDirectIntBuffer(64);
        glRenderLists = new ArrayList();
        dummyInt0 = 0;
        glDummyList = GLAllocation.generateDisplayLists(1);
        prevSortX = -9999D;
        prevSortY = -9999D;
        prevSortZ = -9999D;
        frustrumCheckOffset = 0;
        mc = minecraft;
        renderEngine = renderengine;
        byte byte0 = 64;
        glRenderListBase = GLAllocation.generateDisplayLists(byte0 * byte0 * byte0 * 3);
        occlusionEnabled = minecraft.getOpenGlCapsChecker().checkARBOcclusion();
        if(occlusionEnabled)
        {
            occlusionResult.clear();
            glOcclusionQueryBase = GLAllocation.createDirectIntBuffer(byte0 * byte0 * byte0);
            glOcclusionQueryBase.clear();
            glOcclusionQueryBase.position(0);
            glOcclusionQueryBase.limit(byte0 * byte0 * byte0);
            ARBOcclusionQuery.glGenQueriesARB(glOcclusionQueryBase);
        }
        starGLCallList = GLAllocation.generateDisplayLists(3);
        GL11.glPushMatrix();
        GL11.glNewList(starGLCallList, 4864 /*GL_COMPILE*/);
        renderStars();
        GL11.glEndList();
        GL11.glPopMatrix();
        Tessellator tessellator = Tessellator.instance;
        glSkyList = starGLCallList + 1;
        GL11.glNewList(glSkyList, 4864 /*GL_COMPILE*/);
        byte byte1 = 64;
        int i = 256 / byte1 + 2;
        float f = 16F;
        for(int j = -byte1 * i; j <= byte1 * i; j += byte1)
        {
            for(int l = -byte1 * i; l <= byte1 * i; l += byte1)
            {
                tessellator.startDrawingQuads();
                tessellator.addVertex(j + 0, f, l + 0);
                tessellator.addVertex(j + byte1, f, l + 0);
                tessellator.addVertex(j + byte1, f, l + byte1);
                tessellator.addVertex(j + 0, f, l + byte1);
                tessellator.draw();
            }

        }

        GL11.glEndList();
        glSkyList2 = starGLCallList + 2;
        GL11.glNewList(glSkyList2, 4864 /*GL_COMPILE*/);
        f = -16F;
        tessellator.startDrawingQuads();
        for(int k = -byte1 * i; k <= byte1 * i; k += byte1)
        {
            for(int i1 = -byte1 * i; i1 <= byte1 * i; i1 += byte1)
            {
                tessellator.addVertex(k + byte1, f, i1 + 0);
                tessellator.addVertex(k + 0, f, i1 + 0);
                tessellator.addVertex(k + 0, f, i1 + byte1);
                tessellator.addVertex(k + byte1, f, i1 + byte1);
            }

        }

        tessellator.draw();
        GL11.glEndList();
    }

    private void renderStars()
    {
        Random random = new Random(10842L);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        for(int i = 0; i < 1500; i++)
        {
            double d = random.nextFloat() * 2.0F - 1.0F;
            double d1 = random.nextFloat() * 2.0F - 1.0F;
            double d2 = random.nextFloat() * 2.0F - 1.0F;
            double d3 = 0.25F + random.nextFloat() * 0.25F;
            double d4 = d * d + d1 * d1 + d2 * d2;
            if(d4 >= 1.0D || d4 <= 0.01D)
            {
                continue;
            }
            d4 = 1.0D / Math.sqrt(d4);
            d *= d4;
            d1 *= d4;
            d2 *= d4;
            double d5 = d * 100D;
            double d6 = d1 * 100D;
            double d7 = d2 * 100D;
            double d8 = Math.atan2(d, d2);
            double d9 = Math.sin(d8);
            double d10 = Math.cos(d8);
            double d11 = Math.atan2(Math.sqrt(d * d + d2 * d2), d1);
            double d12 = Math.sin(d11);
            double d13 = Math.cos(d11);
            double d14 = random.nextDouble() * 3.1415926535897931D * 2D;
            double d15 = Math.sin(d14);
            double d16 = Math.cos(d14);
            for(int j = 0; j < 4; j++)
            {
                double d17 = 0.0D;
                double d18 = (double)((j & 2) - 1) * d3;
                double d19 = (double)((j + 1 & 2) - 1) * d3;
                double d20 = d17;
                double d21 = d18 * d16 - d19 * d15;
                double d22 = d19 * d16 + d18 * d15;
                double d23 = d22;
                double d24 = d21 * d12 + d20 * d13;
                double d25 = d20 * d12 - d21 * d13;
                double d26 = d25 * d9 - d23 * d10;
                double d27 = d24;
                double d28 = d23 * d9 + d25 * d10;
                tessellator.addVertex(d5 + d26, d6 + d27, d7 + d28);
            }

        }

        tessellator.draw();
    }

    public void changeWorld(World world)
    {
        if(worldObj != null)
        {
            worldObj.removeWorldAccess(this);
        }
        prevSortX = -9999D;
        prevSortY = -9999D;
        prevSortZ = -9999D;
        RenderManager.instance.func_852_a(world);
        worldObj = world;
        globalRenderBlocks = new RenderBlocks(world);
        if(world != null)
        {
            world.addWorldAccess(this);
            loadRenderers();
        }
    }

    public void loadRenderers()
    {
        Block.leaves.setGraphicsLevel(mc.gameSettings.fancyGraphics);
        renderDistance = mc.gameSettings.renderDistance;
        if(worldRenderers != null)
        {
            for(int i = 0; i < worldRenderers.length; i++)
            {
                worldRenderers[i].func_1204_c();
            }

        }
        int j = 64 << 3 - renderDistance;
        if(j > 400)
        {
            j = 400;
        }
        renderChunksWide = j / 16 + 1;
        renderChunksTall = 8;
        renderChunksDeep = j / 16 + 1;
        worldRenderers = new WorldRenderer[renderChunksWide * renderChunksTall * renderChunksDeep];
        sortedWorldRenderers = new WorldRenderer[renderChunksWide * renderChunksTall * renderChunksDeep];
        int k = 0;
        int l = 0;
        minBlockX = 0;
        minBlockY = 0;
        minBlockZ = 0;
        maxBlockX = renderChunksWide;
        maxBlockY = renderChunksTall;
        maxBlockZ = renderChunksDeep;
        for(int i1 = 0; i1 < worldRenderersToUpdate.size(); i1++)
        {
            ((WorldRenderer)worldRenderersToUpdate.get(i1)).needsUpdate = false;
        }

        worldRenderersToUpdate.clear();
        tileEntities.clear();
        for(int j1 = 0; j1 < renderChunksWide; j1++)
        {
            for(int k1 = 0; k1 < renderChunksTall; k1++)
            {
                for(int l1 = 0; l1 < renderChunksDeep; l1++)
                {
                    worldRenderers[(l1 * renderChunksTall + k1) * renderChunksWide + j1] = new WorldRenderer(worldObj, tileEntities, j1 * 16, k1 * 16, l1 * 16, 16, glRenderListBase + k);
                    if(occlusionEnabled)
                    {
                        worldRenderers[(l1 * renderChunksTall + k1) * renderChunksWide + j1].glOcclusionQuery = glOcclusionQueryBase.get(l);
                    }
                    worldRenderers[(l1 * renderChunksTall + k1) * renderChunksWide + j1].isWaitingOnOcclusionQuery = false;
                    worldRenderers[(l1 * renderChunksTall + k1) * renderChunksWide + j1].isVisible = true;
                    worldRenderers[(l1 * renderChunksTall + k1) * renderChunksWide + j1].isInFrustum = true;
                    worldRenderers[(l1 * renderChunksTall + k1) * renderChunksWide + j1].chunkIndex = l++;
                    worldRenderers[(l1 * renderChunksTall + k1) * renderChunksWide + j1].markDirty();
                    sortedWorldRenderers[(l1 * renderChunksTall + k1) * renderChunksWide + j1] = worldRenderers[(l1 * renderChunksTall + k1) * renderChunksWide + j1];
                    worldRenderersToUpdate.add(worldRenderers[(l1 * renderChunksTall + k1) * renderChunksWide + j1]);
                    k += 3;
                }

            }

        }

        if(worldObj != null)
        {
            EntityLiving entityliving = mc.renderViewEntity;
            if(entityliving != null)
            {
                markRenderersForNewPosition(MathHelper.floor_double(((Entity) (entityliving)).posX), MathHelper.floor_double(((Entity) (entityliving)).posY), MathHelper.floor_double(((Entity) (entityliving)).posZ));
                Arrays.sort(sortedWorldRenderers, new EntitySorter(entityliving));
            }
        }
        renderEntitiesStartupCounter = 2;
    }

    public void renderEntities(Vec3D vec3d, ICamera icamera, float f)
    {
        if(renderEntitiesStartupCounter > 0)
        {
            renderEntitiesStartupCounter--;
            return;
        }
        TileEntityRenderer.instance.cacheActiveRenderInfo(worldObj, renderEngine, mc.fontRenderer, mc.renderViewEntity, f);
        RenderManager.instance.cacheActiveRenderInfo(worldObj, renderEngine, mc.fontRenderer, mc.renderViewEntity, mc.gameSettings, f);
        countEntitiesTotal = 0;
        countEntitiesRendered = 0;
        countEntitiesHidden = 0;
        EntityLiving entityliving = mc.renderViewEntity;
        RenderManager.renderPosX = ((Entity) (entityliving)).lastTickPosX + (((Entity) (entityliving)).posX - ((Entity) (entityliving)).lastTickPosX) * (double)f;
        RenderManager.renderPosY = ((Entity) (entityliving)).lastTickPosY + (((Entity) (entityliving)).posY - ((Entity) (entityliving)).lastTickPosY) * (double)f;
        RenderManager.renderPosZ = ((Entity) (entityliving)).lastTickPosZ + (((Entity) (entityliving)).posZ - ((Entity) (entityliving)).lastTickPosZ) * (double)f;
        TileEntityRenderer.staticPlayerX = ((Entity) (entityliving)).lastTickPosX + (((Entity) (entityliving)).posX - ((Entity) (entityliving)).lastTickPosX) * (double)f;
        TileEntityRenderer.staticPlayerY = ((Entity) (entityliving)).lastTickPosY + (((Entity) (entityliving)).posY - ((Entity) (entityliving)).lastTickPosY) * (double)f;
        TileEntityRenderer.staticPlayerZ = ((Entity) (entityliving)).lastTickPosZ + (((Entity) (entityliving)).posZ - ((Entity) (entityliving)).lastTickPosZ) * (double)f;
        List list = worldObj.getLoadedEntityList();
        countEntitiesTotal = list.size();
        for(int i = 0; i < worldObj.weatherEffects.size(); i++)
        {
            Entity entity = (Entity)worldObj.weatherEffects.get(i);
            countEntitiesRendered++;
            if(entity.isInRangeToRenderVec3D(vec3d))
            {
                RenderManager.instance.renderEntity(entity, f);
            }
        }

        for(int j = 0; j < list.size(); j++)
        {
            Entity entity1 = (Entity)list.get(j);
            if(!entity1.isInRangeToRenderVec3D(vec3d) || !entity1.ignoreFrustumCheck && !icamera.isBoundingBoxInFrustum(entity1.boundingBox) || entity1 == mc.renderViewEntity && !mc.gameSettings.thirdPersonView && !mc.renderViewEntity.isPlayerSleeping())
            {
                continue;
            }
            int l = MathHelper.floor_double(entity1.posY);
            if(l < 0)
            {
                l = 0;
            }
            if(l >= 128)
            {
                l = 127;
            }
            if(worldObj.blockExists(MathHelper.floor_double(entity1.posX), l, MathHelper.floor_double(entity1.posZ)))
            {
                countEntitiesRendered++;
                RenderManager.instance.renderEntity(entity1, f);
            }
        }

        for(int k = 0; k < tileEntities.size(); k++)
        {
            TileEntityRenderer.instance.renderTileEntity((TileEntity)tileEntities.get(k), f);
        }

    }

    public String getDebugInfoRenders()
    {
        return (new StringBuilder()).append("C: ").append(renderersBeingRendered).append("/").append(renderersLoaded).append(". F: ").append(renderersBeingClipped).append(", O: ").append(renderersBeingOccluded).append(", E: ").append(renderersSkippingRenderPass).toString();
    }

    public String getDebugInfoEntities()
    {
        return (new StringBuilder()).append("E: ").append(countEntitiesRendered).append("/").append(countEntitiesTotal).append(". B: ").append(countEntitiesHidden).append(", I: ").append(countEntitiesTotal - countEntitiesHidden - countEntitiesRendered).toString();
    }

    private void markRenderersForNewPosition(int i, int j, int k)
    {
        i -= 8;
        j -= 8;
        k -= 8;
        minBlockX = 0x7fffffff;
        minBlockY = 0x7fffffff;
        minBlockZ = 0x7fffffff;
        maxBlockX = 0x80000000;
        maxBlockY = 0x80000000;
        maxBlockZ = 0x80000000;
        int l = renderChunksWide * 16;
        int i1 = l / 2;
        for(int j1 = 0; j1 < renderChunksWide; j1++)
        {
            int k1 = j1 * 16;
            int l1 = (k1 + i1) - i;
            if(l1 < 0)
            {
                l1 -= l - 1;
            }
            l1 /= l;
            k1 -= l1 * l;
            if(k1 < minBlockX)
            {
                minBlockX = k1;
            }
            if(k1 > maxBlockX)
            {
                maxBlockX = k1;
            }
            for(int i2 = 0; i2 < renderChunksDeep; i2++)
            {
                int j2 = i2 * 16;
                int k2 = (j2 + i1) - k;
                if(k2 < 0)
                {
                    k2 -= l - 1;
                }
                k2 /= l;
                j2 -= k2 * l;
                if(j2 < minBlockZ)
                {
                    minBlockZ = j2;
                }
                if(j2 > maxBlockZ)
                {
                    maxBlockZ = j2;
                }
                for(int l2 = 0; l2 < renderChunksTall; l2++)
                {
                    int i3 = l2 * 16;
                    if(i3 < minBlockY)
                    {
                        minBlockY = i3;
                    }
                    if(i3 > maxBlockY)
                    {
                        maxBlockY = i3;
                    }
                    WorldRenderer worldrenderer = worldRenderers[(i2 * renderChunksTall + l2) * renderChunksWide + j1];
                    boolean flag = worldrenderer.needsUpdate;
                    worldrenderer.setPosition(k1, i3, j2);
                    if(!flag && worldrenderer.needsUpdate)
                    {
                        worldRenderersToUpdate.add(worldrenderer);
                    }
                }

            }

        }

    }

    public int sortAndRender(EntityLiving entityliving, int i, double d)
    {
        for(int j = 0; j < 10; j++)
        {
            worldRenderersCheckIndex = (worldRenderersCheckIndex + 1) % worldRenderers.length;
            WorldRenderer worldrenderer = worldRenderers[worldRenderersCheckIndex];
            if(worldrenderer.needsUpdate && !worldRenderersToUpdate.contains(worldrenderer))
            {
                worldRenderersToUpdate.add(worldrenderer);
            }
        }

        if(mc.gameSettings.renderDistance != renderDistance)
        {
            loadRenderers();
        }
        if(i == 0)
        {
            renderersLoaded = 0;
            renderersBeingClipped = 0;
            renderersBeingOccluded = 0;
            renderersBeingRendered = 0;
            renderersSkippingRenderPass = 0;
        }
        double d1 = entityliving.lastTickPosX + (entityliving.posX - entityliving.lastTickPosX) * d;
        double d2 = entityliving.lastTickPosY + (entityliving.posY - entityliving.lastTickPosY) * d;
        double d3 = entityliving.lastTickPosZ + (entityliving.posZ - entityliving.lastTickPosZ) * d;
        double d4 = entityliving.posX - prevSortX;
        double d5 = entityliving.posY - prevSortY;
        double d6 = entityliving.posZ - prevSortZ;
        if(d4 * d4 + d5 * d5 + d6 * d6 > 16D)
        {
            prevSortX = entityliving.posX;
            prevSortY = entityliving.posY;
            prevSortZ = entityliving.posZ;
            markRenderersForNewPosition(MathHelper.floor_double(entityliving.posX), MathHelper.floor_double(entityliving.posY), MathHelper.floor_double(entityliving.posZ));
            Arrays.sort(sortedWorldRenderers, new EntitySorter(entityliving));
        }
        RenderHelper.disableStandardItemLighting();
        int k = 0;
        if(occlusionEnabled && mc.gameSettings.advancedOpengl && !mc.gameSettings.anaglyph && i == 0)
        {
            int l = 0;
            int i1 = 16;
            checkOcclusionQueryResult(l, i1);
            for(int j1 = l; j1 < i1; j1++)
            {
                sortedWorldRenderers[j1].isVisible = true;
            }

            k += renderSortedRenderers(l, i1, i, d);
            do
            {
                int byte0 = i1;
                i1 *= 2;
                if(i1 > sortedWorldRenderers.length)
                {
                    i1 = sortedWorldRenderers.length;
                }
                GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
                GL11.glDisable(2896 /*GL_LIGHTING*/);
                GL11.glDisable(3008 /*GL_ALPHA_TEST*/);
                GL11.glDisable(2912 /*GL_FOG*/);
                GL11.glColorMask(false, false, false, false);
                GL11.glDepthMask(false);
                checkOcclusionQueryResult(byte0, i1);
                GL11.glPushMatrix();
                float f = 0.0F;
                float f1 = 0.0F;
                float f2 = 0.0F;
                for(int k1 = byte0; k1 < i1; k1++)
                {
                    if(sortedWorldRenderers[k1].skipAllRenderPasses())
                    {
                        sortedWorldRenderers[k1].isInFrustum = false;
                        continue;
                    }
                    if(!sortedWorldRenderers[k1].isInFrustum)
                    {
                        sortedWorldRenderers[k1].isVisible = true;
                    }
                    if(!sortedWorldRenderers[k1].isInFrustum || sortedWorldRenderers[k1].isWaitingOnOcclusionQuery)
                    {
                        continue;
                    }
                    float f3 = MathHelper.sqrt_float(sortedWorldRenderers[k1].distanceToEntitySquared(entityliving));
                    int l1 = (int)(1.0F + f3 / 128F);
                    if(cloudOffsetX % l1 != k1 % l1)
                    {
                        continue;
                    }
                    WorldRenderer worldrenderer1 = sortedWorldRenderers[k1];
                    float f4 = (float)((double)worldrenderer1.posXMinus - d1);
                    float f5 = (float)((double)worldrenderer1.posYMinus - d2);
                    float f6 = (float)((double)worldrenderer1.posZMinus - d3);
                    float f7 = f4 - f;
                    float f8 = f5 - f1;
                    float f9 = f6 - f2;
                    if(f7 != 0.0F || f8 != 0.0F || f9 != 0.0F)
                    {
                        GL11.glTranslatef(f7, f8, f9);
                        f += f7;
                        f1 += f8;
                        f2 += f9;
                    }
                    ARBOcclusionQuery.glBeginQueryARB(35092 /*GL_SAMPLES_PASSED_ARB*/, sortedWorldRenderers[k1].glOcclusionQuery);
                    sortedWorldRenderers[k1].callOcclusionQueryList();
                    ARBOcclusionQuery.glEndQueryARB(35092 /*GL_SAMPLES_PASSED_ARB*/);
                    sortedWorldRenderers[k1].isWaitingOnOcclusionQuery = true;
                }

                GL11.glPopMatrix();
                if(mc.gameSettings.anaglyph)
                {
                    if(EntityRenderer.anaglyphField == 0)
                    {
                        GL11.glColorMask(false, true, true, true);
                    } else
                    {
                        GL11.glColorMask(true, false, false, true);
                    }
                } else
                {
                    GL11.glColorMask(true, true, true, true);
                }
                GL11.glDepthMask(true);
                GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
                GL11.glEnable(3008 /*GL_ALPHA_TEST*/);
                GL11.glEnable(2912 /*GL_FOG*/);
                k += renderSortedRenderers(byte0, i1, i, d);
            } while(i1 < sortedWorldRenderers.length);
        } else
        {
            k += renderSortedRenderers(0, sortedWorldRenderers.length, i, d);
        }
        return k;
    }

    private void checkOcclusionQueryResult(int i, int j)
    {
        for(int k = i; k < j; k++)
        {
            if(!sortedWorldRenderers[k].isWaitingOnOcclusionQuery)
            {
                continue;
            }
            occlusionResult.clear();
            ARBOcclusionQuery.glGetQueryObjectuARB(sortedWorldRenderers[k].glOcclusionQuery, 34919 /*GL_QUERY_RESULT_AVAILABLE_ARB*/, occlusionResult);
            if(occlusionResult.get(0) != 0)
            {
                sortedWorldRenderers[k].isWaitingOnOcclusionQuery = false;
                occlusionResult.clear();
                ARBOcclusionQuery.glGetQueryObjectuARB(sortedWorldRenderers[k].glOcclusionQuery, 34918 /*GL_QUERY_RESULT_ARB*/, occlusionResult);
                sortedWorldRenderers[k].isVisible = occlusionResult.get(0) != 0;
            }
        }

    }

    private int renderSortedRenderers(int i, int j, int k, double d)
    {
        glRenderLists.clear();
        int l = 0;
        for(int i1 = i; i1 < j; i1++)
        {
            if(k == 0)
            {
                renderersLoaded++;
                if(sortedWorldRenderers[i1].skipRenderPass[k])
                {
                    renderersSkippingRenderPass++;
                } else
                if(!sortedWorldRenderers[i1].isInFrustum)
                {
                    renderersBeingClipped++;
                } else
                if(occlusionEnabled && !sortedWorldRenderers[i1].isVisible)
                {
                    renderersBeingOccluded++;
                } else
                {
                    renderersBeingRendered++;
                }
            }
            if(sortedWorldRenderers[i1].skipRenderPass[k] || !sortedWorldRenderers[i1].isInFrustum || occlusionEnabled && !sortedWorldRenderers[i1].isVisible)
            {
                continue;
            }
            int j1 = sortedWorldRenderers[i1].getGLCallListForPass(k);
            if(j1 >= 0)
            {
                glRenderLists.add(sortedWorldRenderers[i1]);
                l++;
            }
        }

        EntityLiving entityliving = mc.renderViewEntity;
        double d1 = entityliving.lastTickPosX + (entityliving.posX - entityliving.lastTickPosX) * d;
        double d2 = entityliving.lastTickPosY + (entityliving.posY - entityliving.lastTickPosY) * d;
        double d3 = entityliving.lastTickPosZ + (entityliving.posZ - entityliving.lastTickPosZ) * d;
        int k1 = 0;
        for(int l1 = 0; l1 < allRenderLists.length; l1++)
        {
            allRenderLists[l1].func_859_b();
        }

        for(int i2 = 0; i2 < glRenderLists.size(); i2++)
        {
            WorldRenderer worldrenderer = (WorldRenderer)glRenderLists.get(i2);
            int j2 = -1;
            for(int k2 = 0; k2 < k1; k2++)
            {
                if(allRenderLists[k2].func_862_a(worldrenderer.posXMinus, worldrenderer.posYMinus, worldrenderer.posZMinus))
                {
                    j2 = k2;
                }
            }

            if(j2 < 0)
            {
                j2 = k1++;
                allRenderLists[j2].func_861_a(worldrenderer.posXMinus, worldrenderer.posYMinus, worldrenderer.posZMinus, d1, d2, d3);
            }
            allRenderLists[j2].func_858_a(worldrenderer.getGLCallListForPass(k));
        }

        renderAllRenderLists(k, d);
        return l;
    }

    public void renderAllRenderLists(int i, double d)
    {
        for(int j = 0; j < allRenderLists.length; j++)
        {
            allRenderLists[j].func_860_a();
        }

    }

    public void updateClouds()
    {
        cloudOffsetX++;
    }

    public void renderSky(float f)
    {
        if(mc.theWorld.worldProvider.isNether)
        {
            return;
        }
        GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
        Vec3D vec3d = worldObj.func_4079_a(mc.renderViewEntity, f);
        float f1 = (float)vec3d.xCoord;
        float f2 = (float)vec3d.yCoord;
        float f3 = (float)vec3d.zCoord;
        if(mc.gameSettings.anaglyph)
        {
            float f4 = (f1 * 30F + f2 * 59F + f3 * 11F) / 100F;
            float f5 = (f1 * 30F + f2 * 70F) / 100F;
            float f7 = (f1 * 30F + f3 * 70F) / 100F;
            f1 = f4;
            f2 = f5;
            f3 = f7;
        }
        GL11.glColor3f(f1, f2, f3);
        Tessellator tessellator = Tessellator.instance;
        GL11.glDepthMask(false);
        GL11.glEnable(2912 /*GL_FOG*/);
        GL11.glColor3f(f1, f2, f3);
        GL11.glCallList(glSkyList);
        GL11.glDisable(2912 /*GL_FOG*/);
        GL11.glDisable(3008 /*GL_ALPHA_TEST*/);
        GL11.glEnable(3042 /*GL_BLEND*/);
        GL11.glBlendFunc(770, 771);
        RenderHelper.disableStandardItemLighting();
        float af[] = worldObj.worldProvider.calcSunriseSunsetColors(worldObj.getCelestialAngle(f), f);
        if(af != null)
        {
            GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
            GL11.glShadeModel(7425 /*GL_SMOOTH*/);
            GL11.glPushMatrix();
            GL11.glRotatef(90F, 1.0F, 0.0F, 0.0F);
            float f8 = worldObj.getCelestialAngle(f);
            GL11.glRotatef(f8 <= 0.5F ? 0.0F : 180F, 0.0F, 0.0F, 1.0F);
            float f10 = af[0];
            float f12 = af[1];
            float f14 = af[2];
            if(mc.gameSettings.anaglyph)
            {
                float f16 = (f10 * 30F + f12 * 59F + f14 * 11F) / 100F;
                float f18 = (f10 * 30F + f12 * 70F) / 100F;
                float f19 = (f10 * 30F + f14 * 70F) / 100F;
                f10 = f16;
                f12 = f18;
                f14 = f19;
            }
            tessellator.startDrawing(6);
            tessellator.setColorRGBA_F(f10, f12, f14, af[3]);
            tessellator.addVertex(0.0D, 100D, 0.0D);
            int i = 16;
            tessellator.setColorRGBA_F(af[0], af[1], af[2], 0.0F);
            for(int j = 0; j <= i; j++)
            {
                float f20 = ((float)j * 3.141593F * 2.0F) / (float)i;
                float f21 = MathHelper.sin(f20);
                float f22 = MathHelper.cos(f20);
                tessellator.addVertex(f21 * 120F, f22 * 120F, -f22 * 40F * af[3]);
            }

            tessellator.draw();
            GL11.glPopMatrix();
            GL11.glShadeModel(7424 /*GL_FLAT*/);
        }
        GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
        GL11.glBlendFunc(770, 1);
        GL11.glPushMatrix();
        float f6 = 1.0F - worldObj.func_27162_g(f);
        float f9 = 0.0F;
        float f11 = 0.0F;
        float f13 = 0.0F;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, f6);
        GL11.glTranslatef(f9, f11, f13);
        GL11.glRotatef(0.0F, 0.0F, 0.0F, 1.0F);
        GL11.glRotatef(worldObj.getCelestialAngle(f) * 360F, 1.0F, 0.0F, 0.0F);
        float f15 = 30F;
        GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, renderEngine.getTexture("/terrain/sun.png"));
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(-f15, 100D, -f15, 0.0D, 0.0D);
        tessellator.addVertexWithUV(f15, 100D, -f15, 1.0D, 0.0D);
        tessellator.addVertexWithUV(f15, 100D, f15, 1.0D, 1.0D);
        tessellator.addVertexWithUV(-f15, 100D, f15, 0.0D, 1.0D);
        tessellator.draw();
        f15 = 20F;
        GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, renderEngine.getTexture("/terrain/moon.png"));
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(-f15, -100D, f15, 1.0D, 1.0D);
        tessellator.addVertexWithUV(f15, -100D, f15, 0.0D, 1.0D);
        tessellator.addVertexWithUV(f15, -100D, -f15, 0.0D, 0.0D);
        tessellator.addVertexWithUV(-f15, -100D, -f15, 1.0D, 0.0D);
        tessellator.draw();
        GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
        float f17 = worldObj.getStarBrightness(f) * f6;
        if(f17 > 0.0F)
        {
            GL11.glColor4f(f17, f17, f17, f17);
            GL11.glCallList(starGLCallList);
        }
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(3042 /*GL_BLEND*/);
        GL11.glEnable(3008 /*GL_ALPHA_TEST*/);
        GL11.glEnable(2912 /*GL_FOG*/);
        GL11.glPopMatrix();
        if(worldObj.worldProvider.func_28112_c())
        {
            GL11.glColor3f(f1 * 0.2F + 0.04F, f2 * 0.2F + 0.04F, f3 * 0.6F + 0.1F);
        } else
        {
            GL11.glColor3f(f1, f2, f3);
        }
        GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
        GL11.glCallList(glSkyList2);
        GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
        GL11.glDepthMask(true);
    }

    public void renderClouds(float f)
    {
        if(mc.theWorld.worldProvider.isNether)
        {
            return;
        }
        if(mc.gameSettings.fancyGraphics)
        {
            renderCloudsFancy(f);
            return;
        }
        GL11.glDisable(2884 /*GL_CULL_FACE*/);
        float f1 = (float)(mc.renderViewEntity.lastTickPosY + (mc.renderViewEntity.posY - mc.renderViewEntity.lastTickPosY) * (double)f);
        byte byte0 = 32;
        int i = 256 / byte0;
        Tessellator tessellator = Tessellator.instance;
        GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, renderEngine.getTexture("/environment/clouds.png"));
        GL11.glEnable(3042 /*GL_BLEND*/);
        GL11.glBlendFunc(770, 771);
        Vec3D vec3d = worldObj.func_628_d(f);
        float f2 = (float)vec3d.xCoord;
        float f3 = (float)vec3d.yCoord;
        float f4 = (float)vec3d.zCoord;
        if(mc.gameSettings.anaglyph)
        {
            float f5 = (f2 * 30F + f3 * 59F + f4 * 11F) / 100F;
            float f7 = (f2 * 30F + f3 * 70F) / 100F;
            float f8 = (f2 * 30F + f4 * 70F) / 100F;
            f2 = f5;
            f3 = f7;
            f4 = f8;
        }
        float f6 = 0.0004882813F;
        double d = mc.renderViewEntity.prevPosX + (mc.renderViewEntity.posX - mc.renderViewEntity.prevPosX) * (double)f + (double)(((float)cloudOffsetX + f) * 0.03F);
        double d1 = mc.renderViewEntity.prevPosZ + (mc.renderViewEntity.posZ - mc.renderViewEntity.prevPosZ) * (double)f;
        int j = MathHelper.floor_double(d / 2048D);
        int k = MathHelper.floor_double(d1 / 2048D);
        d -= j * 2048 /*GL_EXP*/;
        d1 -= k * 2048 /*GL_EXP*/;
        float f9 = (worldObj.worldProvider.getCloudHeight() - f1) + 0.33F;
        float f10 = (float)(d * (double)f6);
        float f11 = (float)(d1 * (double)f6);
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(f2, f3, f4, 0.8F);
        for(int l = -byte0 * i; l < byte0 * i; l += byte0)
        {
            for(int i1 = -byte0 * i; i1 < byte0 * i; i1 += byte0)
            {
                tessellator.addVertexWithUV(l + 0, f9, i1 + byte0, (float)(l + 0) * f6 + f10, (float)(i1 + byte0) * f6 + f11);
                tessellator.addVertexWithUV(l + byte0, f9, i1 + byte0, (float)(l + byte0) * f6 + f10, (float)(i1 + byte0) * f6 + f11);
                tessellator.addVertexWithUV(l + byte0, f9, i1 + 0, (float)(l + byte0) * f6 + f10, (float)(i1 + 0) * f6 + f11);
                tessellator.addVertexWithUV(l + 0, f9, i1 + 0, (float)(l + 0) * f6 + f10, (float)(i1 + 0) * f6 + f11);
            }

        }

        tessellator.draw();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(3042 /*GL_BLEND*/);
        GL11.glEnable(2884 /*GL_CULL_FACE*/);
    }

    public boolean func_27307_a(double d, double d1, double d2, float f)
    {
        return false;
    }

    public void renderCloudsFancy(float f)
    {
        GL11.glDisable(2884 /*GL_CULL_FACE*/);
        float f1 = (float)(mc.renderViewEntity.lastTickPosY + (mc.renderViewEntity.posY - mc.renderViewEntity.lastTickPosY) * (double)f);
        Tessellator tessellator = Tessellator.instance;
        float f2 = 12F;
        float f3 = 4F;
        double d = (mc.renderViewEntity.prevPosX + (mc.renderViewEntity.posX - mc.renderViewEntity.prevPosX) * (double)f + (double)(((float)cloudOffsetX + f) * 0.03F)) / (double)f2;
        double d1 = (mc.renderViewEntity.prevPosZ + (mc.renderViewEntity.posZ - mc.renderViewEntity.prevPosZ) * (double)f) / (double)f2 + 0.33000001311302185D;
        float f4 = (worldObj.worldProvider.getCloudHeight() - f1) + 0.33F;
        int i = MathHelper.floor_double(d / 2048D);
        int j = MathHelper.floor_double(d1 / 2048D);
        d -= i * 2048 /*GL_EXP*/;
        d1 -= j * 2048 /*GL_EXP*/;
        GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, renderEngine.getTexture("/environment/clouds.png"));
        GL11.glEnable(3042 /*GL_BLEND*/);
        GL11.glBlendFunc(770, 771);
        Vec3D vec3d = worldObj.func_628_d(f);
        float f5 = (float)vec3d.xCoord;
        float f6 = (float)vec3d.yCoord;
        float f7 = (float)vec3d.zCoord;
        if(mc.gameSettings.anaglyph)
        {
            float f8 = (f5 * 30F + f6 * 59F + f7 * 11F) / 100F;
            float f10 = (f5 * 30F + f6 * 70F) / 100F;
            float f12 = (f5 * 30F + f7 * 70F) / 100F;
            f5 = f8;
            f6 = f10;
            f7 = f12;
        }
        float f9 = (float)(d * 0.0D);
        float f11 = (float)(d1 * 0.0D);
        float f13 = 0.00390625F;
        f9 = (float)MathHelper.floor_double(d) * f13;
        f11 = (float)MathHelper.floor_double(d1) * f13;
        float f14 = (float)(d - (double)MathHelper.floor_double(d));
        float f15 = (float)(d1 - (double)MathHelper.floor_double(d1));
        int k = 8;
        byte byte0 = 3;
        float f16 = 0.0009765625F;
        GL11.glScalef(f2, 1.0F, f2);
        for(int l = 0; l < 2; l++)
        {
            if(l == 0)
            {
                GL11.glColorMask(false, false, false, false);
            } else
            if(mc.gameSettings.anaglyph)
            {
                if(EntityRenderer.anaglyphField == 0)
                {
                    GL11.glColorMask(false, true, true, true);
                } else
                {
                    GL11.glColorMask(true, false, false, true);
                }
            } else
            {
                GL11.glColorMask(true, true, true, true);
            }
            for(int i1 = -byte0 + 1; i1 <= byte0; i1++)
            {
                for(int j1 = -byte0 + 1; j1 <= byte0; j1++)
                {
                    tessellator.startDrawingQuads();
                    float f17 = i1 * k;
                    float f18 = j1 * k;
                    float f19 = f17 - f14;
                    float f20 = f18 - f15;
                    if(f4 > -f3 - 1.0F)
                    {
                        tessellator.setColorRGBA_F(f5 * 0.7F, f6 * 0.7F, f7 * 0.7F, 0.8F);
                        tessellator.setNormal(0.0F, -1F, 0.0F);
                        tessellator.addVertexWithUV(f19 + 0.0F, f4 + 0.0F, f20 + (float)k, (f17 + 0.0F) * f13 + f9, (f18 + (float)k) * f13 + f11);
                        tessellator.addVertexWithUV(f19 + (float)k, f4 + 0.0F, f20 + (float)k, (f17 + (float)k) * f13 + f9, (f18 + (float)k) * f13 + f11);
                        tessellator.addVertexWithUV(f19 + (float)k, f4 + 0.0F, f20 + 0.0F, (f17 + (float)k) * f13 + f9, (f18 + 0.0F) * f13 + f11);
                        tessellator.addVertexWithUV(f19 + 0.0F, f4 + 0.0F, f20 + 0.0F, (f17 + 0.0F) * f13 + f9, (f18 + 0.0F) * f13 + f11);
                    }
                    if(f4 <= f3 + 1.0F)
                    {
                        tessellator.setColorRGBA_F(f5, f6, f7, 0.8F);
                        tessellator.setNormal(0.0F, 1.0F, 0.0F);
                        tessellator.addVertexWithUV(f19 + 0.0F, (f4 + f3) - f16, f20 + (float)k, (f17 + 0.0F) * f13 + f9, (f18 + (float)k) * f13 + f11);
                        tessellator.addVertexWithUV(f19 + (float)k, (f4 + f3) - f16, f20 + (float)k, (f17 + (float)k) * f13 + f9, (f18 + (float)k) * f13 + f11);
                        tessellator.addVertexWithUV(f19 + (float)k, (f4 + f3) - f16, f20 + 0.0F, (f17 + (float)k) * f13 + f9, (f18 + 0.0F) * f13 + f11);
                        tessellator.addVertexWithUV(f19 + 0.0F, (f4 + f3) - f16, f20 + 0.0F, (f17 + 0.0F) * f13 + f9, (f18 + 0.0F) * f13 + f11);
                    }
                    tessellator.setColorRGBA_F(f5 * 0.9F, f6 * 0.9F, f7 * 0.9F, 0.8F);
                    if(i1 > -1)
                    {
                        tessellator.setNormal(-1F, 0.0F, 0.0F);
                        for(int k1 = 0; k1 < k; k1++)
                        {
                            tessellator.addVertexWithUV(f19 + (float)k1 + 0.0F, f4 + 0.0F, f20 + (float)k, (f17 + (float)k1 + 0.5F) * f13 + f9, (f18 + (float)k) * f13 + f11);
                            tessellator.addVertexWithUV(f19 + (float)k1 + 0.0F, f4 + f3, f20 + (float)k, (f17 + (float)k1 + 0.5F) * f13 + f9, (f18 + (float)k) * f13 + f11);
                            tessellator.addVertexWithUV(f19 + (float)k1 + 0.0F, f4 + f3, f20 + 0.0F, (f17 + (float)k1 + 0.5F) * f13 + f9, (f18 + 0.0F) * f13 + f11);
                            tessellator.addVertexWithUV(f19 + (float)k1 + 0.0F, f4 + 0.0F, f20 + 0.0F, (f17 + (float)k1 + 0.5F) * f13 + f9, (f18 + 0.0F) * f13 + f11);
                        }

                    }
                    if(i1 <= 1)
                    {
                        tessellator.setNormal(1.0F, 0.0F, 0.0F);
                        for(int l1 = 0; l1 < k; l1++)
                        {
                            tessellator.addVertexWithUV((f19 + (float)l1 + 1.0F) - f16, f4 + 0.0F, f20 + (float)k, (f17 + (float)l1 + 0.5F) * f13 + f9, (f18 + (float)k) * f13 + f11);
                            tessellator.addVertexWithUV((f19 + (float)l1 + 1.0F) - f16, f4 + f3, f20 + (float)k, (f17 + (float)l1 + 0.5F) * f13 + f9, (f18 + (float)k) * f13 + f11);
                            tessellator.addVertexWithUV((f19 + (float)l1 + 1.0F) - f16, f4 + f3, f20 + 0.0F, (f17 + (float)l1 + 0.5F) * f13 + f9, (f18 + 0.0F) * f13 + f11);
                            tessellator.addVertexWithUV((f19 + (float)l1 + 1.0F) - f16, f4 + 0.0F, f20 + 0.0F, (f17 + (float)l1 + 0.5F) * f13 + f9, (f18 + 0.0F) * f13 + f11);
                        }

                    }
                    tessellator.setColorRGBA_F(f5 * 0.8F, f6 * 0.8F, f7 * 0.8F, 0.8F);
                    if(j1 > -1)
                    {
                        tessellator.setNormal(0.0F, 0.0F, -1F);
                        for(int i2 = 0; i2 < k; i2++)
                        {
                            tessellator.addVertexWithUV(f19 + 0.0F, f4 + f3, f20 + (float)i2 + 0.0F, (f17 + 0.0F) * f13 + f9, (f18 + (float)i2 + 0.5F) * f13 + f11);
                            tessellator.addVertexWithUV(f19 + (float)k, f4 + f3, f20 + (float)i2 + 0.0F, (f17 + (float)k) * f13 + f9, (f18 + (float)i2 + 0.5F) * f13 + f11);
                            tessellator.addVertexWithUV(f19 + (float)k, f4 + 0.0F, f20 + (float)i2 + 0.0F, (f17 + (float)k) * f13 + f9, (f18 + (float)i2 + 0.5F) * f13 + f11);
                            tessellator.addVertexWithUV(f19 + 0.0F, f4 + 0.0F, f20 + (float)i2 + 0.0F, (f17 + 0.0F) * f13 + f9, (f18 + (float)i2 + 0.5F) * f13 + f11);
                        }

                    }
                    if(j1 <= 1)
                    {
                        tessellator.setNormal(0.0F, 0.0F, 1.0F);
                        for(int j2 = 0; j2 < k; j2++)
                        {
                            tessellator.addVertexWithUV(f19 + 0.0F, f4 + f3, (f20 + (float)j2 + 1.0F) - f16, (f17 + 0.0F) * f13 + f9, (f18 + (float)j2 + 0.5F) * f13 + f11);
                            tessellator.addVertexWithUV(f19 + (float)k, f4 + f3, (f20 + (float)j2 + 1.0F) - f16, (f17 + (float)k) * f13 + f9, (f18 + (float)j2 + 0.5F) * f13 + f11);
                            tessellator.addVertexWithUV(f19 + (float)k, f4 + 0.0F, (f20 + (float)j2 + 1.0F) - f16, (f17 + (float)k) * f13 + f9, (f18 + (float)j2 + 0.5F) * f13 + f11);
                            tessellator.addVertexWithUV(f19 + 0.0F, f4 + 0.0F, (f20 + (float)j2 + 1.0F) - f16, (f17 + 0.0F) * f13 + f9, (f18 + (float)j2 + 0.5F) * f13 + f11);
                        }

                    }
                    tessellator.draw();
                }

            }

        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(3042 /*GL_BLEND*/);
        GL11.glEnable(2884 /*GL_CULL_FACE*/);
    }

    public boolean updateRenderers(EntityLiving entityliving, boolean flag)
    {
        boolean flag1 = false;
        if(flag1)
        {
            Collections.sort(worldRenderersToUpdate, new RenderSorter(entityliving));
            int i = worldRenderersToUpdate.size() - 1;
            int j = worldRenderersToUpdate.size();
            for(int k = 0; k < j; k++)
            {
                WorldRenderer worldrenderer = (WorldRenderer)worldRenderersToUpdate.get(i - k);
                if(!flag)
                {
                    if(worldrenderer.distanceToEntitySquared(entityliving) > 256F)
                    {
                        if(worldrenderer.isInFrustum)
                        {
                            if(k >= 3)
                            {
                                return false;
                            }
                        } else
                        if(k >= 1)
                        {
                            return false;
                        }
                    }
                } else
                if(!worldrenderer.isInFrustum)
                {
                    continue;
                }
                worldrenderer.updateRenderer();
                worldRenderersToUpdate.remove(worldrenderer);
                worldrenderer.needsUpdate = false;
            }

            return worldRenderersToUpdate.size() == 0;
        }
        byte byte0 = 2;
        RenderSorter rendersorter = new RenderSorter(entityliving);
        WorldRenderer aworldrenderer[] = new WorldRenderer[byte0];
        ArrayList arraylist = null;
        int l = worldRenderersToUpdate.size();
        int i1 = 0;
        for(int j1 = 0; j1 < l; j1++)
        {
            WorldRenderer worldrenderer1 = (WorldRenderer)worldRenderersToUpdate.get(j1);
            if(!flag)
            {
                if(worldrenderer1.distanceToEntitySquared(entityliving) > 256F)
                {
                    int k2;
                    for(k2 = 0; k2 < byte0 && (aworldrenderer[k2] == null || rendersorter.doCompare(aworldrenderer[k2], worldrenderer1) <= 0); k2++) { }
                    if(--k2 <= 0)
                    {
                        continue;
                    }
                    for(int i3 = k2; --i3 != 0;)
                    {
                        aworldrenderer[i3 - 1] = aworldrenderer[i3];
                    }

                    aworldrenderer[k2] = worldrenderer1;
                    continue;
                }
            } else
            if(!worldrenderer1.isInFrustum)
            {
                continue;
            }
            if(arraylist == null)
            {
                arraylist = new ArrayList();
            }
            i1++;
            arraylist.add(worldrenderer1);
            worldRenderersToUpdate.set(j1, null);
        }

        if(arraylist != null)
        {
            if(arraylist.size() > 1)
            {
                Collections.sort(arraylist, rendersorter);
            }
            for(int k1 = arraylist.size() - 1; k1 >= 0; k1--)
            {
                WorldRenderer worldrenderer2 = (WorldRenderer)arraylist.get(k1);
                worldrenderer2.updateRenderer();
                worldrenderer2.needsUpdate = false;
            }

        }
        int l1 = 0;
        for(int i2 = byte0 - 1; i2 >= 0; i2--)
        {
            WorldRenderer worldrenderer3 = aworldrenderer[i2];
            if(worldrenderer3 == null)
            {
                continue;
            }
            if(!worldrenderer3.isInFrustum && i2 != byte0 - 1)
            {
                aworldrenderer[i2] = null;
                aworldrenderer[0] = null;
                break;
            }
            aworldrenderer[i2].updateRenderer();
            aworldrenderer[i2].needsUpdate = false;
            l1++;
        }

        int j2 = 0;
        int l2 = 0;
        for(int j3 = worldRenderersToUpdate.size(); j2 != j3; j2++)
        {
            WorldRenderer worldrenderer4 = (WorldRenderer)worldRenderersToUpdate.get(j2);
            if(worldrenderer4 == null)
            {
                continue;
            }
            boolean flag2 = false;
            for(int k3 = 0; k3 < byte0 && !flag2; k3++)
            {
                if(worldrenderer4 == aworldrenderer[k3])
                {
                    flag2 = true;
                }
            }

            if(flag2)
            {
                continue;
            }
            if(l2 != j2)
            {
                worldRenderersToUpdate.set(l2, worldrenderer4);
            }
            l2++;
        }

        while(--j2 >= l2) 
        {
            worldRenderersToUpdate.remove(j2);
        }
        return l == i1 + l1;
    }

    public void drawBlockBreaking(EntityPlayer entityplayer, MovingObjectPosition movingobjectposition, int i, ItemStack itemstack, float f)
    {
        Tessellator tessellator = Tessellator.instance;
        GL11.glEnable(3042 /*GL_BLEND*/);
        GL11.glEnable(3008 /*GL_ALPHA_TEST*/);
        GL11.glBlendFunc(770, 1);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, (MathHelper.sin((float)System.currentTimeMillis() / 100F) * 0.2F + 0.4F) * 0.5F);
        if(i == 0)
        {
            if(damagePartialTime > 0.0F)
            {
                GL11.glBlendFunc(774, 768);
                int j = renderEngine.getTexture("/terrain.png");
                GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, j);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
                GL11.glPushMatrix();
                int k = worldObj.getBlockId(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ);
                Block block = k <= 0 ? null : Block.blocksList[k];
                GL11.glDisable(3008 /*GL_ALPHA_TEST*/);
                GL11.glPolygonOffset(-3F, -3F);
                GL11.glEnable(32823 /*GL_POLYGON_OFFSET_FILL*/);
                double d = entityplayer.lastTickPosX + (entityplayer.posX - entityplayer.lastTickPosX) * (double)f;
                double d1 = entityplayer.lastTickPosY + (entityplayer.posY - entityplayer.lastTickPosY) * (double)f;
                double d2 = entityplayer.lastTickPosZ + (entityplayer.posZ - entityplayer.lastTickPosZ) * (double)f;
                if(block == null)
                {
                    block = Block.stone;
                }
                GL11.glEnable(3008 /*GL_ALPHA_TEST*/);
                tessellator.startDrawingQuads();
                tessellator.setTranslationD(-d, -d1, -d2);
                tessellator.disableColor();
                globalRenderBlocks.renderBlockUsingTexture(block, movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ, 240 + (int)(damagePartialTime * 10F));
                tessellator.draw();
                tessellator.setTranslationD(0.0D, 0.0D, 0.0D);
                GL11.glDisable(3008 /*GL_ALPHA_TEST*/);
                GL11.glPolygonOffset(0.0F, 0.0F);
                GL11.glDisable(32823 /*GL_POLYGON_OFFSET_FILL*/);
                GL11.glEnable(3008 /*GL_ALPHA_TEST*/);
                GL11.glDepthMask(true);
                GL11.glPopMatrix();
            }
        } else
        if(itemstack != null)
        {
            GL11.glBlendFunc(770, 771);
            float f1 = MathHelper.sin((float)System.currentTimeMillis() / 100F) * 0.2F + 0.8F;
            GL11.glColor4f(f1, f1, f1, MathHelper.sin((float)System.currentTimeMillis() / 200F) * 0.2F + 0.5F);
            int l = renderEngine.getTexture("/terrain.png");
            GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, l);
            int i1 = movingobjectposition.blockX;
            int j1 = movingobjectposition.blockY;
            int k1 = movingobjectposition.blockZ;
            if(movingobjectposition.sideHit == 0)
            {
                j1--;
            }
            if(movingobjectposition.sideHit == 1)
            {
                j1++;
            }
            if(movingobjectposition.sideHit == 2)
            {
                k1--;
            }
            if(movingobjectposition.sideHit == 3)
            {
                k1++;
            }
            if(movingobjectposition.sideHit == 4)
            {
                i1--;
            }
            if(movingobjectposition.sideHit == 5)
            {
                i1++;
            }
        }
        GL11.glDisable(3042 /*GL_BLEND*/);
        GL11.glDisable(3008 /*GL_ALPHA_TEST*/);
    }

    public void drawSelectionBox(EntityPlayer entityplayer, MovingObjectPosition movingobjectposition, int i, ItemStack itemstack, float f)
    {
        if(i == 0 && movingobjectposition.typeOfHit == EnumMovingObjectType.TILE)
        {
            GL11.glEnable(3042 /*GL_BLEND*/);
            GL11.glBlendFunc(770, 771);
            GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.4F);
            GL11.glLineWidth(2.0F);
            GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
            GL11.glDepthMask(false);
            float f1 = 0.002F;
            int j = worldObj.getBlockId(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ);
            if(j > 0)
            {
                Block.blocksList[j].setBlockBoundsBasedOnState(worldObj, movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ);
                double d = entityplayer.lastTickPosX + (entityplayer.posX - entityplayer.lastTickPosX) * (double)f;
                double d1 = entityplayer.lastTickPosY + (entityplayer.posY - entityplayer.lastTickPosY) * (double)f;
                double d2 = entityplayer.lastTickPosZ + (entityplayer.posZ - entityplayer.lastTickPosZ) * (double)f;
                drawOutlinedBoundingBox(Block.blocksList[j].getSelectedBoundingBoxFromPool(worldObj, movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ).expand(f1, f1, f1).getOffsetBoundingBox(-d, -d1, -d2));
            }
            GL11.glDepthMask(true);
            GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
            GL11.glDisable(3042 /*GL_BLEND*/);
        }
    }

    private void drawOutlinedBoundingBox(AxisAlignedBB axisalignedbb)
    {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawing(3);
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
        tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
        tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
        tessellator.draw();
        tessellator.startDrawing(3);
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
        tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
        tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
        tessellator.draw();
        tessellator.startDrawing(1);
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
        tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
        tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
        tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
        tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
        tessellator.draw();
    }

    public void func_949_a(int i, int j, int k, int l, int i1, int j1)
    {
        int k1 = MathHelper.bucketInt(i, 16);
        int l1 = MathHelper.bucketInt(j, 16);
        int i2 = MathHelper.bucketInt(k, 16);
        int j2 = MathHelper.bucketInt(l, 16);
        int k2 = MathHelper.bucketInt(i1, 16);
        int l2 = MathHelper.bucketInt(j1, 16);
        for(int i3 = k1; i3 <= j2; i3++)
        {
            int j3 = i3 % renderChunksWide;
            if(j3 < 0)
            {
                j3 += renderChunksWide;
            }
            for(int k3 = l1; k3 <= k2; k3++)
            {
                int l3 = k3 % renderChunksTall;
                if(l3 < 0)
                {
                    l3 += renderChunksTall;
                }
                for(int i4 = i2; i4 <= l2; i4++)
                {
                    int j4 = i4 % renderChunksDeep;
                    if(j4 < 0)
                    {
                        j4 += renderChunksDeep;
                    }
                    int k4 = (j4 * renderChunksTall + l3) * renderChunksWide + j3;
                    WorldRenderer worldrenderer = worldRenderers[k4];
                    if(!worldrenderer.needsUpdate)
                    {
                        worldRenderersToUpdate.add(worldrenderer);
                        worldrenderer.markDirty();
                    }
                }

            }

        }

    }

    public void markBlockAndNeighborsNeedsUpdate(int i, int j, int k)
    {
        func_949_a(i - 1, j - 1, k - 1, i + 1, j + 1, k + 1);
    }

    public void markBlockRangeNeedsUpdate(int i, int j, int k, int l, int i1, int j1)
    {
        func_949_a(i - 1, j - 1, k - 1, l + 1, i1 + 1, j1 + 1);
    }

    public void clipRenderersByFrustrum(ICamera icamera, float f)
    {
        for(int i = 0; i < worldRenderers.length; i++)
        {
            if(!worldRenderers[i].skipAllRenderPasses() && (!worldRenderers[i].isInFrustum || (i + frustrumCheckOffset & 0xf) == 0))
            {
                worldRenderers[i].updateInFrustrum(icamera);
            }
        }

        frustrumCheckOffset++;
    }

    public void playRecord(String s, int i, int j, int k)
    {
        if(s != null)
        {
            mc.ingameGUI.setRecordPlayingMessage((new StringBuilder()).append("C418 - ").append(s).toString());
        }
        mc.sndManager.playStreaming(s, i, j, k, 1.0F, 1.0F);
    }

    public void playSound(String s, double d, double d1, double d2, 
            float f, float f1)
    {
        float f2 = 16F;
        if(f > 1.0F)
        {
            f2 *= f;
        }
        if(mc.renderViewEntity.getDistanceSq(d, d1, d2) < (double)(f2 * f2))
        {
            mc.sndManager.playSound(s, (float)d, (float)d1, (float)d2, f, f1);
        }
    }

    public void spawnParticle(String s, double d, double d1, double d2, 
            double d3, double d4, double d5)
    {
        if(mc == null || mc.renderViewEntity == null || mc.effectRenderer == null)
        {
            return;
        }
        double d6 = mc.renderViewEntity.posX - d;
        double d7 = mc.renderViewEntity.posY - d1;
        double d8 = mc.renderViewEntity.posZ - d2;
        double d9 = 16D;
        if(d6 * d6 + d7 * d7 + d8 * d8 > d9 * d9)
        {
            return;
        }
        if(s.equals("bubble"))
        {
            mc.effectRenderer.addEffect(new EntityBubbleFX(worldObj, d, d1, d2, d3, d4, d5));
        } else
        if(s.equals("smoke"))
        {
            mc.effectRenderer.addEffect(new EntitySmokeFX(worldObj, d, d1, d2, d3, d4, d5));
        } else
        if(s.equals("note"))
        {
            mc.effectRenderer.addEffect(new EntityNoteFX(worldObj, d, d1, d2, d3, d4, d5));
        } else
        if(s.equals("portal"))
        {
            mc.effectRenderer.addEffect(new EntityPortalFX(worldObj, d, d1, d2, d3, d4, d5));
        } else
        if(s.equals("explode"))
        {
            mc.effectRenderer.addEffect(new EntityExplodeFX(worldObj, d, d1, d2, d3, d4, d5));
        } else
        if(s.equals("flame"))
        {
            mc.effectRenderer.addEffect(new EntityFlameFX(worldObj, d, d1, d2, d3, d4, d5));
        } else
        if(s.equals("lava"))
        {
            mc.effectRenderer.addEffect(new EntityLavaFX(worldObj, d, d1, d2));
        } else
        if(s.equals("footstep"))
        {
            mc.effectRenderer.addEffect(new EntityFootStepFX(renderEngine, worldObj, d, d1, d2));
        } else
        if(s.equals("splash"))
        {
            mc.effectRenderer.addEffect(new EntitySplashFX(worldObj, d, d1, d2, d3, d4, d5));
        } else
        if(s.equals("largesmoke"))
        {
            mc.effectRenderer.addEffect(new EntitySmokeFX(worldObj, d, d1, d2, d3, d4, d5, 2.5F));
        } else
        if(s.equals("reddust"))
        {
            mc.effectRenderer.addEffect(new EntityReddustFX(worldObj, d, d1, d2, (float)d3, (float)d4, (float)d5));
        } else
        if(s.equals("snowballpoof"))
        {
            mc.effectRenderer.addEffect(new EntitySlimeFX(worldObj, d, d1, d2, Item.snowball));
        } else
        if(s.equals("snowshovel"))
        {
            mc.effectRenderer.addEffect(new EntitySnowShovelFX(worldObj, d, d1, d2, d3, d4, d5));
        } else
        if(s.equals("slime"))
        {
            mc.effectRenderer.addEffect(new EntitySlimeFX(worldObj, d, d1, d2, Item.slimeBall));
        } else
        if(s.equals("heart"))
        {
            mc.effectRenderer.addEffect(new EntityHeartFX(worldObj, d, d1, d2, d3, d4, d5));
        }
    }

    public void obtainEntitySkin(Entity entity)
    {
        entity.updateCloak();
        if(entity.skinUrl != null)
        {
            renderEngine.obtainImageData(entity.skinUrl, new ImageBufferDownload());
        }
        if(entity.cloakUrl != null)
        {
            renderEngine.obtainImageData(entity.cloakUrl, new ImageBufferDownload());
        }
    }

    public void releaseEntitySkin(Entity entity)
    {
        if(entity.skinUrl != null)
        {
            renderEngine.releaseImageData(entity.skinUrl);
        }
        if(entity.cloakUrl != null)
        {
            renderEngine.releaseImageData(entity.cloakUrl);
        }
    }

    public void updateAllRenderers()
    {
        for(int i = 0; i < worldRenderers.length; i++)
        {
            if(worldRenderers[i].isChunkLit && !worldRenderers[i].needsUpdate)
            {
                worldRenderersToUpdate.add(worldRenderers[i]);
                worldRenderers[i].markDirty();
            }
        }

    }

    public void doNothingWithTileEntity(int i, int j, int k, TileEntity tileentity)
    {
    }

    public void func_28137_f()
    {
        GLAllocation.func_28194_b(glRenderListBase);
    }

    public void func_28136_a(EntityPlayer entityplayer, int i, int j, int k, int l, int i1)
    {
        Random random = worldObj.rand;
        switch(i)
        {
        default:
            break;

        case 1001: 
            worldObj.playSoundEffect(j, k, l, "random.click", 1.0F, 1.2F);
            break;

        case 1000: 
            worldObj.playSoundEffect(j, k, l, "random.click", 1.0F, 1.0F);
            break;

        case 1002: 
            worldObj.playSoundEffect(j, k, l, "random.bow", 1.0F, 1.2F);
            break;

        case 2000: 
            int j1 = i1 % 3 - 1;
            int k1 = (i1 / 3) % 3 - 1;
            double d = (double)j + (double)j1 * 0.59999999999999998D + 0.5D;
            double d1 = (double)k + 0.5D;
            double d2 = (double)l + (double)k1 * 0.59999999999999998D + 0.5D;
            for(int l1 = 0; l1 < 10; l1++)
            {
                double d3 = random.nextDouble() * 0.20000000000000001D + 0.01D;
                double d4 = d + (double)j1 * 0.01D + (random.nextDouble() - 0.5D) * (double)k1 * 0.5D;
                double d5 = d1 + (random.nextDouble() - 0.5D) * 0.5D;
                double d6 = d2 + (double)k1 * 0.01D + (random.nextDouble() - 0.5D) * (double)j1 * 0.5D;
                double d7 = (double)j1 * d3 + random.nextGaussian() * 0.01D;
                double d8 = -0.029999999999999999D + random.nextGaussian() * 0.01D;
                double d9 = (double)k1 * d3 + random.nextGaussian() * 0.01D;
                spawnParticle("smoke", d4, d5, d6, d7, d8, d9);
            }

            break;

        case 2001: 
            int i2 = i1 & 0xff;
            if(i2 > 0)
            {
                Block block = Block.blocksList[i2];
                mc.sndManager.playSound(block.stepSound.stepSoundDir(), (float)j + 0.5F, (float)k + 0.5F, (float)l + 0.5F, (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
            }
            mc.effectRenderer.addBlockDestroyEffects(j, k, l, i1 & 0xff, i1 >> 8 & 0xff);
            break;

        case 1003: 
            if(Math.random() < 0.5D)
            {
                worldObj.playSoundEffect((double)j + 0.5D, (double)k + 0.5D, (double)l + 0.5D, "random.door_open", 1.0F, worldObj.rand.nextFloat() * 0.1F + 0.9F);
            } else
            {
                worldObj.playSoundEffect((double)j + 0.5D, (double)k + 0.5D, (double)l + 0.5D, "random.door_close", 1.0F, worldObj.rand.nextFloat() * 0.1F + 0.9F);
            }
            break;

        case 1004: 
            worldObj.playSoundEffect((float)j + 0.5F, (float)k + 0.5F, (float)l + 0.5F, "random.fizz", 0.5F, 2.6F + (random.nextFloat() - random.nextFloat()) * 0.8F);
            break;

        case 1005: 
            if(Item.itemsList[i1] instanceof ItemRecord)
            {
                worldObj.playRecord(((ItemRecord)Item.itemsList[i1]).recordName, j, k, l);
            } else
            {
                worldObj.playRecord(null, j, k, l);
            }
            break;
        }
    }

    public List tileEntities;
    private World worldObj;
    private RenderEngine renderEngine;
    private List worldRenderersToUpdate;
    private WorldRenderer sortedWorldRenderers[];
    private WorldRenderer worldRenderers[];
    private int renderChunksWide;
    private int renderChunksTall;
    private int renderChunksDeep;
    private int glRenderListBase;
    private Minecraft mc;
    private RenderBlocks globalRenderBlocks;
    private IntBuffer glOcclusionQueryBase;
    private boolean occlusionEnabled;
    private int cloudOffsetX;
    private int starGLCallList;
    private int glSkyList;
    private int glSkyList2;
    private int minBlockX;
    private int minBlockY;
    private int minBlockZ;
    private int maxBlockX;
    private int maxBlockY;
    private int maxBlockZ;
    private int renderDistance;
    private int renderEntitiesStartupCounter;
    private int countEntitiesTotal;
    private int countEntitiesRendered;
    private int countEntitiesHidden;
    int dummyBuf50k[];
    IntBuffer occlusionResult;
    private int renderersLoaded;
    private int renderersBeingClipped;
    private int renderersBeingOccluded;
    private int renderersBeingRendered;
    private int renderersSkippingRenderPass;
    private int worldRenderersCheckIndex;
    private List glRenderLists;
    private RenderList allRenderLists[] = {
        new RenderList(), new RenderList(), new RenderList(), new RenderList()
    };
    int dummyInt0;
    int glDummyList;
    double prevSortX;
    double prevSortY;
    double prevSortZ;
    public float damagePartialTime;
    int frustrumCheckOffset;
}
