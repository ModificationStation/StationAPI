// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.nio.FloatBuffer;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;
import org.lwjgl.util.glu.GLU;

// Referenced classes of package net.minecraft.src:
//            MouseFilter, GLAllocation, ItemRenderer, EntityLiving, 
//            MathHelper, World, GameSettings, PlayerController, 
//            MovingObjectPosition, Vec3D, PlayerControllerTest, AxisAlignedBB, 
//            Entity, Material, EntityPlayer, Block, 
//            RenderGlobal, EntityPlayerSP, MouseHelper, ScaledResolution, 
//            GuiIngame, GuiScreen, GuiParticle, ChunkProviderLoadOrGenerate, 
//            ClippingHelperImpl, Frustrum, ICamera, RenderEngine, 
//            RenderHelper, EffectRenderer, InventoryPlayer, WorldChunkManager, 
//            BiomeGenBase, EntitySmokeFX, EntityRainFX, Tessellator, 
//            WorldProvider

public class EntityRenderer
{

    public EntityRenderer(Minecraft minecraft)
    {
        farPlaneDistance = 0.0F;
        pointedEntity = null;
        mouseFilterXAxis = new MouseFilter();
        mouseFilterYAxis = new MouseFilter();
        mouseFilterDummy1 = new MouseFilter();
        mouseFilterDummy2 = new MouseFilter();
        mouseFilterDummy3 = new MouseFilter();
        mouseFilterDummy4 = new MouseFilter();
        field_22228_r = 4F;
        field_22227_s = 4F;
        field_22226_t = 0.0F;
        field_22225_u = 0.0F;
        field_22224_v = 0.0F;
        field_22223_w = 0.0F;
        field_22222_x = 0.0F;
        field_22221_y = 0.0F;
        field_22220_z = 0.0F;
        field_22230_A = 0.0F;
        cloudFog = false;
        cameraZoom = 1.0D;
        cameraYaw = 0.0D;
        cameraPitch = 0.0D;
        prevFrameTime = System.currentTimeMillis();
        field_28133_I = 0L;
        random = new Random();
        rainSoundCounter = 0;
        field_1394_b = 0;
        field_1393_c = 0;
        fogColorBuffer = GLAllocation.createDirectFloatBuffer(16);
        mc = minecraft;
        itemRenderer = new ItemRenderer(minecraft);
    }

    public void updateRenderer()
    {
        fogColor2 = fogColor1;
        field_22227_s = field_22228_r;
        field_22225_u = field_22226_t;
        field_22223_w = field_22224_v;
        field_22221_y = field_22222_x;
        field_22230_A = field_22220_z;
        if(mc.renderViewEntity == null)
        {
            mc.renderViewEntity = mc.thePlayer;
        }
        float f = mc.theWorld.getLightBrightness(MathHelper.floor_double(mc.renderViewEntity.posX), MathHelper.floor_double(mc.renderViewEntity.posY), MathHelper.floor_double(mc.renderViewEntity.posZ));
        float f1 = (float)(3 - mc.gameSettings.renderDistance) / 3F;
        float f2 = f * (1.0F - f1) + f1;
        fogColor1 += (f2 - fogColor1) * 0.1F;
        rendererUpdateCount++;
        itemRenderer.updateEquippedItem();
        addRainParticles();
    }

    public void getMouseOver(float f)
    {
        if(mc.renderViewEntity == null)
        {
            return;
        }
        if(mc.theWorld == null)
        {
            return;
        }
        double d = mc.playerController.getBlockReachDistance();
        mc.objectMouseOver = mc.renderViewEntity.rayTrace(d, f);
        double d1 = d;
        Vec3D vec3d = mc.renderViewEntity.getPosition(f);
        if(mc.objectMouseOver != null)
        {
            d1 = mc.objectMouseOver.hitVec.distanceTo(vec3d);
        }
        if(mc.playerController instanceof PlayerControllerTest)
        {
            d1 = d = 32D;
        } else
        {
            if(d1 > 3D)
            {
                d1 = 3D;
            }
            d = d1;
        }
        Vec3D vec3d1 = mc.renderViewEntity.getLook(f);
        Vec3D vec3d2 = vec3d.addVector(vec3d1.xCoord * d, vec3d1.yCoord * d, vec3d1.zCoord * d);
        pointedEntity = null;
        float f1 = 1.0F;
        List list = mc.theWorld.getEntitiesWithinAABBExcludingEntity(mc.renderViewEntity, mc.renderViewEntity.boundingBox.addCoord(vec3d1.xCoord * d, vec3d1.yCoord * d, vec3d1.zCoord * d).expand(f1, f1, f1));
        double d2 = 0.0D;
        for(int i = 0; i < list.size(); i++)
        {
            Entity entity = (Entity)list.get(i);
            if(!entity.canBeCollidedWith())
            {
                continue;
            }
            float f2 = entity.getCollisionBorderSize();
            AxisAlignedBB axisalignedbb = entity.boundingBox.expand(f2, f2, f2);
            MovingObjectPosition movingobjectposition = axisalignedbb.func_1169_a(vec3d, vec3d2);
            if(axisalignedbb.isVecInside(vec3d))
            {
                if(0.0D < d2 || d2 == 0.0D)
                {
                    pointedEntity = entity;
                    d2 = 0.0D;
                }
                continue;
            }
            if(movingobjectposition == null)
            {
                continue;
            }
            double d3 = vec3d.distanceTo(movingobjectposition.hitVec);
            if(d3 < d2 || d2 == 0.0D)
            {
                pointedEntity = entity;
                d2 = d3;
            }
        }

        if(pointedEntity != null && !(mc.playerController instanceof PlayerControllerTest))
        {
            mc.objectMouseOver = new MovingObjectPosition(pointedEntity);
        }
    }

    private float getFOVModifier(float f)
    {
        EntityLiving entityliving = mc.renderViewEntity;
        float f1 = 70F;
        if(entityliving.isInsideOfMaterial(Material.water))
        {
            f1 = 60F;
        }
        if(entityliving.health <= 0)
        {
            float f2 = (float)entityliving.deathTime + f;
            f1 /= (1.0F - 500F / (f2 + 500F)) * 2.0F + 1.0F;
        }
        return f1 + field_22221_y + (field_22222_x - field_22221_y) * f;
    }

    private void hurtCameraEffect(float f)
    {
        EntityLiving entityliving = mc.renderViewEntity;
        float f1 = (float)entityliving.hurtTime - f;
        if(entityliving.health <= 0)
        {
            float f2 = (float)entityliving.deathTime + f;
            GL11.glRotatef(40F - 8000F / (f2 + 200F), 0.0F, 0.0F, 1.0F);
        }
        if(f1 < 0.0F)
        {
            return;
        } else
        {
            f1 /= entityliving.maxHurtTime;
            f1 = MathHelper.sin(f1 * f1 * f1 * f1 * 3.141593F);
            float f3 = entityliving.attackedAtYaw;
            GL11.glRotatef(-f3, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-f1 * 14F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(f3, 0.0F, 1.0F, 0.0F);
            return;
        }
    }

    private void setupViewBobbing(float f)
    {
        if(!(mc.renderViewEntity instanceof EntityPlayer))
        {
            return;
        } else
        {
            EntityPlayer entityplayer = (EntityPlayer)mc.renderViewEntity;
            float f1 = entityplayer.distanceWalkedModified - entityplayer.prevDistanceWalkedModified;
            float f2 = -(entityplayer.distanceWalkedModified + f1 * f);
            float f3 = entityplayer.field_775_e + (entityplayer.field_774_f - entityplayer.field_775_e) * f;
            float f4 = entityplayer.cameraPitch + (entityplayer.field_9328_R - entityplayer.cameraPitch) * f;
            GL11.glTranslatef(MathHelper.sin(f2 * 3.141593F) * f3 * 0.5F, -Math.abs(MathHelper.cos(f2 * 3.141593F) * f3), 0.0F);
            GL11.glRotatef(MathHelper.sin(f2 * 3.141593F) * f3 * 3F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(Math.abs(MathHelper.cos(f2 * 3.141593F - 0.2F) * f3) * 5F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(f4, 1.0F, 0.0F, 0.0F);
            return;
        }
    }

    private void orientCamera(float f)
    {
        EntityLiving entityliving = mc.renderViewEntity;
        float f1 = entityliving.yOffset - 1.62F;
        double d = entityliving.prevPosX + (entityliving.posX - entityliving.prevPosX) * (double)f;
        double d1 = (entityliving.prevPosY + (entityliving.posY - entityliving.prevPosY) * (double)f) - (double)f1;
        double d2 = entityliving.prevPosZ + (entityliving.posZ - entityliving.prevPosZ) * (double)f;
        GL11.glRotatef(field_22230_A + (field_22220_z - field_22230_A) * f, 0.0F, 0.0F, 1.0F);
        if(entityliving.isPlayerSleeping())
        {
            f1 = (float)((double)f1 + 1.0D);
            GL11.glTranslatef(0.0F, 0.3F, 0.0F);
            if(!mc.gameSettings.field_22273_E)
            {
                int i = mc.theWorld.getBlockId(MathHelper.floor_double(entityliving.posX), MathHelper.floor_double(entityliving.posY), MathHelper.floor_double(entityliving.posZ));
                if(i == Block.blockBed.blockID)
                {
                    int j = mc.theWorld.getBlockMetadata(MathHelper.floor_double(entityliving.posX), MathHelper.floor_double(entityliving.posY), MathHelper.floor_double(entityliving.posZ));
                    int k = j & 3;
                    GL11.glRotatef(k * 90, 0.0F, 1.0F, 0.0F);
                }
                GL11.glRotatef(entityliving.prevRotationYaw + (entityliving.rotationYaw - entityliving.prevRotationYaw) * f + 180F, 0.0F, -1F, 0.0F);
                GL11.glRotatef(entityliving.prevRotationPitch + (entityliving.rotationPitch - entityliving.prevRotationPitch) * f, -1F, 0.0F, 0.0F);
            }
        } else
        if(mc.gameSettings.thirdPersonView)
        {
            double d3 = field_22227_s + (field_22228_r - field_22227_s) * f;
            if(mc.gameSettings.field_22273_E)
            {
                float f2 = field_22225_u + (field_22226_t - field_22225_u) * f;
                float f4 = field_22223_w + (field_22224_v - field_22223_w) * f;
                GL11.glTranslatef(0.0F, 0.0F, (float)(-d3));
                GL11.glRotatef(f4, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(f2, 0.0F, 1.0F, 0.0F);
            } else
            {
                float f3 = entityliving.rotationYaw;
                float f5 = entityliving.rotationPitch;
                double d4 = (double)(-MathHelper.sin((f3 / 180F) * 3.141593F) * MathHelper.cos((f5 / 180F) * 3.141593F)) * d3;
                double d5 = (double)(MathHelper.cos((f3 / 180F) * 3.141593F) * MathHelper.cos((f5 / 180F) * 3.141593F)) * d3;
                double d6 = (double)(-MathHelper.sin((f5 / 180F) * 3.141593F)) * d3;
                for(int l = 0; l < 8; l++)
                {
                    float f6 = (l & 1) * 2 - 1;
                    float f7 = (l >> 1 & 1) * 2 - 1;
                    float f8 = (l >> 2 & 1) * 2 - 1;
                    f6 *= 0.1F;
                    f7 *= 0.1F;
                    f8 *= 0.1F;
                    MovingObjectPosition movingobjectposition = mc.theWorld.rayTraceBlocks(Vec3D.createVector(d + (double)f6, d1 + (double)f7, d2 + (double)f8), Vec3D.createVector((d - d4) + (double)f6 + (double)f8, (d1 - d6) + (double)f7, (d2 - d5) + (double)f8));
                    if(movingobjectposition == null)
                    {
                        continue;
                    }
                    double d7 = movingobjectposition.hitVec.distanceTo(Vec3D.createVector(d, d1, d2));
                    if(d7 < d3)
                    {
                        d3 = d7;
                    }
                }

                GL11.glRotatef(entityliving.rotationPitch - f5, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(entityliving.rotationYaw - f3, 0.0F, 1.0F, 0.0F);
                GL11.glTranslatef(0.0F, 0.0F, (float)(-d3));
                GL11.glRotatef(f3 - entityliving.rotationYaw, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(f5 - entityliving.rotationPitch, 1.0F, 0.0F, 0.0F);
            }
        } else
        {
            GL11.glTranslatef(0.0F, 0.0F, -0.1F);
        }
        if(!mc.gameSettings.field_22273_E)
        {
            GL11.glRotatef(entityliving.prevRotationPitch + (entityliving.rotationPitch - entityliving.prevRotationPitch) * f, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(entityliving.prevRotationYaw + (entityliving.rotationYaw - entityliving.prevRotationYaw) * f + 180F, 0.0F, 1.0F, 0.0F);
        }
        GL11.glTranslatef(0.0F, f1, 0.0F);
        d = entityliving.prevPosX + (entityliving.posX - entityliving.prevPosX) * (double)f;
        d1 = (entityliving.prevPosY + (entityliving.posY - entityliving.prevPosY) * (double)f) - (double)f1;
        d2 = entityliving.prevPosZ + (entityliving.posZ - entityliving.prevPosZ) * (double)f;
        cloudFog = mc.renderGlobal.func_27307_a(d, d1, d2, f);
    }

    private void setupCameraTransform(float f, int i)
    {
        farPlaneDistance = 256 >> mc.gameSettings.renderDistance;
        GL11.glMatrixMode(5889 /*GL_PROJECTION*/);
        GL11.glLoadIdentity();
        float f1 = 0.07F;
        if(mc.gameSettings.anaglyph)
        {
            GL11.glTranslatef((float)(-(i * 2 - 1)) * f1, 0.0F, 0.0F);
        }
        if(cameraZoom != 1.0D)
        {
            GL11.glTranslatef((float)cameraYaw, (float)(-cameraPitch), 0.0F);
            GL11.glScaled(cameraZoom, cameraZoom, 1.0D);
            GLU.gluPerspective(getFOVModifier(f), (float)mc.displayWidth / (float)mc.displayHeight, 0.05F, farPlaneDistance * 2.0F);
        } else
        {
            GLU.gluPerspective(getFOVModifier(f), (float)mc.displayWidth / (float)mc.displayHeight, 0.05F, farPlaneDistance * 2.0F);
        }
        GL11.glMatrixMode(5888 /*GL_MODELVIEW0_ARB*/);
        GL11.glLoadIdentity();
        if(mc.gameSettings.anaglyph)
        {
            GL11.glTranslatef((float)(i * 2 - 1) * 0.1F, 0.0F, 0.0F);
        }
        hurtCameraEffect(f);
        if(mc.gameSettings.viewBobbing)
        {
            setupViewBobbing(f);
        }
        float f2 = mc.thePlayer.prevTimeInPortal + (mc.thePlayer.timeInPortal - mc.thePlayer.prevTimeInPortal) * f;
        if(f2 > 0.0F)
        {
            float f3 = 5F / (f2 * f2 + 5F) - f2 * 0.04F;
            f3 *= f3;
            GL11.glRotatef(((float)rendererUpdateCount + f) * 20F, 0.0F, 1.0F, 1.0F);
            GL11.glScalef(1.0F / f3, 1.0F, 1.0F);
            GL11.glRotatef(-((float)rendererUpdateCount + f) * 20F, 0.0F, 1.0F, 1.0F);
        }
        orientCamera(f);
    }

    private void func_4135_b(float f, int i)
    {
        GL11.glLoadIdentity();
        if(mc.gameSettings.anaglyph)
        {
            GL11.glTranslatef((float)(i * 2 - 1) * 0.1F, 0.0F, 0.0F);
        }
        GL11.glPushMatrix();
        hurtCameraEffect(f);
        if(mc.gameSettings.viewBobbing)
        {
            setupViewBobbing(f);
        }
        if(!mc.gameSettings.thirdPersonView && !mc.renderViewEntity.isPlayerSleeping() && !mc.gameSettings.hideGUI)
        {
            itemRenderer.renderItemInFirstPerson(f);
        }
        GL11.glPopMatrix();
        if(!mc.gameSettings.thirdPersonView && !mc.renderViewEntity.isPlayerSleeping())
        {
            itemRenderer.renderOverlays(f);
            hurtCameraEffect(f);
        }
        if(mc.gameSettings.viewBobbing)
        {
            setupViewBobbing(f);
        }
    }

    public void updateCameraAndRender(float f)
    {
        if(!Display.isActive())
        {
            if(System.currentTimeMillis() - prevFrameTime > 500L)
            {
                mc.displayInGameMenu();
            }
        } else
        {
            prevFrameTime = System.currentTimeMillis();
        }
        if(mc.inGameHasFocus)
        {
            mc.mouseHelper.mouseXYChange();
            float f1 = mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
            float f2 = f1 * f1 * f1 * 8F;
            float f3 = (float)mc.mouseHelper.deltaX * f2;
            float f4 = (float)mc.mouseHelper.deltaY * f2;
            int l = 1;
            if(mc.gameSettings.invertMouse)
            {
                l = -1;
            }
            if(mc.gameSettings.smoothCamera)
            {
                f3 = mouseFilterXAxis.func_22386_a(f3, 0.05F * f2);
                f4 = mouseFilterYAxis.func_22386_a(f4, 0.05F * f2);
            }
            mc.thePlayer.func_346_d(f3, f4 * (float)l);
        }
        if(mc.skipRenderWorld)
        {
            return;
        }
        field_28135_a = mc.gameSettings.anaglyph;
        ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        int i = scaledresolution.getScaledWidth();
        int j = scaledresolution.getScaledHeight();
        int k = (Mouse.getX() * i) / mc.displayWidth;
        int i1 = j - (Mouse.getY() * j) / mc.displayHeight - 1;
        char c = '\310';
        if(mc.gameSettings.limitFramerate == 1)
        {
            c = 'x';
        }
        if(mc.gameSettings.limitFramerate == 2)
        {
            c = '(';
        }
        if(mc.theWorld != null)
        {
            if(mc.gameSettings.limitFramerate == 0)
            {
                renderWorld(f, 0L);
            } else
            {
                renderWorld(f, field_28133_I + (long)(0x3b9aca00 / c));
            }
            if(mc.gameSettings.limitFramerate == 2)
            {
                long l1 = ((field_28133_I + (long)(0x3b9aca00 / c)) - System.nanoTime()) / 0xf4240L;
                if(l1 > 0L && l1 < 500L)
                {
                    try
                    {
                        Thread.sleep(l1);
                    }
                    catch(InterruptedException interruptedexception)
                    {
                        interruptedexception.printStackTrace();
                    }
                }
            }
            field_28133_I = System.nanoTime();
            if(!mc.gameSettings.hideGUI || mc.currentScreen != null)
            {
                mc.ingameGUI.renderGameOverlay(f, mc.currentScreen != null, k, i1);
            }
        } else
        {
            GL11.glViewport(0, 0, mc.displayWidth, mc.displayHeight);
            GL11.glMatrixMode(5889 /*GL_PROJECTION*/);
            GL11.glLoadIdentity();
            GL11.glMatrixMode(5888 /*GL_MODELVIEW0_ARB*/);
            GL11.glLoadIdentity();
            func_905_b();
            if(mc.gameSettings.limitFramerate == 2)
            {
                long l2 = ((field_28133_I + (long)(0x3b9aca00 / c)) - System.nanoTime()) / 0xf4240L;
                if(l2 < 0L)
                {
                    l2 += 10L;
                }
                if(l2 > 0L && l2 < 500L)
                {
                    try
                    {
                        Thread.sleep(l2);
                    }
                    catch(InterruptedException interruptedexception1)
                    {
                        interruptedexception1.printStackTrace();
                    }
                }
            }
            field_28133_I = System.nanoTime();
        }
        if(mc.currentScreen != null)
        {
            GL11.glClear(256);
            mc.currentScreen.drawScreen(k, i1, f);
            if(mc.currentScreen != null && mc.currentScreen.field_25091_h != null)
            {
                mc.currentScreen.field_25091_h.func_25087_a(f);
            }
        }
    }

    public void renderWorld(float f, long l)
    {
        GL11.glEnable(2884 /*GL_CULL_FACE*/);
        GL11.glEnable(2929 /*GL_DEPTH_TEST*/);
        if(mc.renderViewEntity == null)
        {
            mc.renderViewEntity = mc.thePlayer;
        }
        getMouseOver(f);
        EntityLiving entityliving = mc.renderViewEntity;
        RenderGlobal renderglobal = mc.renderGlobal;
        EffectRenderer effectrenderer = mc.effectRenderer;
        double d = entityliving.lastTickPosX + (entityliving.posX - entityliving.lastTickPosX) * (double)f;
        double d1 = entityliving.lastTickPosY + (entityliving.posY - entityliving.lastTickPosY) * (double)f;
        double d2 = entityliving.lastTickPosZ + (entityliving.posZ - entityliving.lastTickPosZ) * (double)f;
        IChunkProvider ichunkprovider = mc.theWorld.getIChunkProvider();
        if(ichunkprovider instanceof ChunkProviderLoadOrGenerate)
        {
            ChunkProviderLoadOrGenerate chunkproviderloadorgenerate = (ChunkProviderLoadOrGenerate)ichunkprovider;
            int j = MathHelper.floor_float((int)d) >> 4;
            int k = MathHelper.floor_float((int)d2) >> 4;
            chunkproviderloadorgenerate.setCurrentChunkOver(j, k);
        }
        for(int i = 0; i < 2; i++)
        {
            if(mc.gameSettings.anaglyph)
            {
                anaglyphField = i;
                if(anaglyphField == 0)
                {
                    GL11.glColorMask(false, true, true, false);
                } else
                {
                    GL11.glColorMask(true, false, false, false);
                }
            }
            GL11.glViewport(0, 0, mc.displayWidth, mc.displayHeight);
            updateFogColor(f);
            GL11.glClear(16640);
            GL11.glEnable(2884 /*GL_CULL_FACE*/);
            setupCameraTransform(f, i);
            ClippingHelperImpl.getInstance();
            if(mc.gameSettings.renderDistance < 2)
            {
                setupFog(-1, f);
                renderglobal.renderSky(f);
            }
            GL11.glEnable(2912 /*GL_FOG*/);
            setupFog(1, f);
            if(mc.gameSettings.ambientOcclusion)
            {
                GL11.glShadeModel(7425 /*GL_SMOOTH*/);
            }
            Frustrum frustrum = new Frustrum();
            frustrum.setPosition(d, d1, d2);
            mc.renderGlobal.clipRenderersByFrustrum(frustrum, f);
            long l1;
            if(i == 0)
            {
                do
                {
                    if(mc.renderGlobal.updateRenderers(entityliving, false) || l == 0L)
                    {
                        break;
                    }
                    l1 = l - System.nanoTime();
                } while(l1 >= 0L && l1 <= 0x3b9aca00L);
            }
            setupFog(0, f);
            GL11.glEnable(2912 /*GL_FOG*/);
            GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, mc.renderEngine.getTexture("/terrain.png"));
            RenderHelper.disableStandardItemLighting();
            renderglobal.sortAndRender(entityliving, 0, f);
            GL11.glShadeModel(7424 /*GL_FLAT*/);
            RenderHelper.enableStandardItemLighting();
            renderglobal.renderEntities(entityliving.getPosition(f), frustrum, f);
            effectrenderer.func_1187_b(entityliving, f);
            RenderHelper.disableStandardItemLighting();
            setupFog(0, f);
            effectrenderer.renderParticles(entityliving, f);
            if(mc.objectMouseOver != null && entityliving.isInsideOfMaterial(Material.water) && (entityliving instanceof EntityPlayer))
            {
                EntityPlayer entityplayer = (EntityPlayer)entityliving;
                GL11.glDisable(3008 /*GL_ALPHA_TEST*/);
                renderglobal.drawBlockBreaking(entityplayer, mc.objectMouseOver, 0, entityplayer.inventory.getCurrentItem(), f);
                renderglobal.drawSelectionBox(entityplayer, mc.objectMouseOver, 0, entityplayer.inventory.getCurrentItem(), f);
                GL11.glEnable(3008 /*GL_ALPHA_TEST*/);
            }
            GL11.glBlendFunc(770, 771);
            setupFog(0, f);
            GL11.glEnable(3042 /*GL_BLEND*/);
            GL11.glDisable(2884 /*GL_CULL_FACE*/);
            GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, mc.renderEngine.getTexture("/terrain.png"));
            if(mc.gameSettings.fancyGraphics)
            {
                if(mc.gameSettings.ambientOcclusion)
                {
                    GL11.glShadeModel(7425 /*GL_SMOOTH*/);
                }
                GL11.glColorMask(false, false, false, false);
                int i1 = renderglobal.sortAndRender(entityliving, 1, f);
                if(mc.gameSettings.anaglyph)
                {
                    if(anaglyphField == 0)
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
                if(i1 > 0)
                {
                    renderglobal.renderAllRenderLists(1, f);
                }
                GL11.glShadeModel(7424 /*GL_FLAT*/);
            } else
            {
                renderglobal.sortAndRender(entityliving, 1, f);
            }
            GL11.glDepthMask(true);
            GL11.glEnable(2884 /*GL_CULL_FACE*/);
            GL11.glDisable(3042 /*GL_BLEND*/);
            if(cameraZoom == 1.0D && (entityliving instanceof EntityPlayer) && mc.objectMouseOver != null && !entityliving.isInsideOfMaterial(Material.water))
            {
                EntityPlayer entityplayer1 = (EntityPlayer)entityliving;
                GL11.glDisable(3008 /*GL_ALPHA_TEST*/);
                renderglobal.drawBlockBreaking(entityplayer1, mc.objectMouseOver, 0, entityplayer1.inventory.getCurrentItem(), f);
                renderglobal.drawSelectionBox(entityplayer1, mc.objectMouseOver, 0, entityplayer1.inventory.getCurrentItem(), f);
                GL11.glEnable(3008 /*GL_ALPHA_TEST*/);
            }
            renderRainSnow(f);
            GL11.glDisable(2912 /*GL_FOG*/);
            if(pointedEntity == null);
            setupFog(0, f);
            GL11.glEnable(2912 /*GL_FOG*/);
            renderglobal.renderClouds(f);
            GL11.glDisable(2912 /*GL_FOG*/);
            setupFog(1, f);
            if(cameraZoom == 1.0D)
            {
                GL11.glClear(256);
                func_4135_b(f, i);
            }
            if(!mc.gameSettings.anaglyph)
            {
                return;
            }
        }

        GL11.glColorMask(true, true, true, false);
    }

    private void addRainParticles()
    {
        float f = mc.theWorld.func_27162_g(1.0F);
        if(!mc.gameSettings.fancyGraphics)
        {
            f /= 2.0F;
        }
        if(f == 0.0F)
        {
            return;
        }
        random.setSeed((long)rendererUpdateCount * 0x12a7ce5fL);
        EntityLiving entityliving = mc.renderViewEntity;
        World world = mc.theWorld;
        int i = MathHelper.floor_double(entityliving.posX);
        int j = MathHelper.floor_double(entityliving.posY);
        int k = MathHelper.floor_double(entityliving.posZ);
        byte byte0 = 10;
        double d = 0.0D;
        double d1 = 0.0D;
        double d2 = 0.0D;
        int l = 0;
        for(int i1 = 0; i1 < (int)(100F * f * f); i1++)
        {
            int j1 = (i + random.nextInt(byte0)) - random.nextInt(byte0);
            int k1 = (k + random.nextInt(byte0)) - random.nextInt(byte0);
            int l1 = world.findTopSolidBlock(j1, k1);
            int i2 = world.getBlockId(j1, l1 - 1, k1);
            if(l1 > j + byte0 || l1 < j - byte0 || !world.getWorldChunkManager().getBiomeGenAt(j1, k1).canSpawnLightningBolt())
            {
                continue;
            }
            float f1 = random.nextFloat();
            float f2 = random.nextFloat();
            if(i2 <= 0)
            {
                continue;
            }
            if(Block.blocksList[i2].blockMaterial == Material.lava)
            {
                mc.effectRenderer.addEffect(new EntitySmokeFX(world, (float)j1 + f1, (double)((float)l1 + 0.1F) - Block.blocksList[i2].minY, (float)k1 + f2, 0.0D, 0.0D, 0.0D));
                continue;
            }
            if(random.nextInt(++l) == 0)
            {
                d = (float)j1 + f1;
                d1 = (double)((float)l1 + 0.1F) - Block.blocksList[i2].minY;
                d2 = (float)k1 + f2;
            }
            mc.effectRenderer.addEffect(new EntityRainFX(world, (float)j1 + f1, (double)((float)l1 + 0.1F) - Block.blocksList[i2].minY, (float)k1 + f2));
        }

        if(l > 0 && random.nextInt(3) < rainSoundCounter++)
        {
            rainSoundCounter = 0;
            if(d1 > entityliving.posY + 1.0D && world.findTopSolidBlock(MathHelper.floor_double(entityliving.posX), MathHelper.floor_double(entityliving.posZ)) > MathHelper.floor_double(entityliving.posY))
            {
                mc.theWorld.playSoundEffect(d, d1, d2, "ambient.weather.rain", 0.1F, 0.5F);
            } else
            {
                mc.theWorld.playSoundEffect(d, d1, d2, "ambient.weather.rain", 0.2F, 1.0F);
            }
        }
    }

    protected void renderRainSnow(float f)
    {
        float f1 = mc.theWorld.func_27162_g(f);
        if(f1 <= 0.0F)
        {
            return;
        }
        EntityLiving entityliving = mc.renderViewEntity;
        World world = mc.theWorld;
        int i = MathHelper.floor_double(entityliving.posX);
        int j = MathHelper.floor_double(entityliving.posY);
        int k = MathHelper.floor_double(entityliving.posZ);
        Tessellator tessellator = Tessellator.instance;
        GL11.glDisable(2884 /*GL_CULL_FACE*/);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GL11.glEnable(3042 /*GL_BLEND*/);
        GL11.glBlendFunc(770, 771);
        GL11.glAlphaFunc(516, 0.01F);
        GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, mc.renderEngine.getTexture("/environment/snow.png"));
        double d = entityliving.lastTickPosX + (entityliving.posX - entityliving.lastTickPosX) * (double)f;
        double d1 = entityliving.lastTickPosY + (entityliving.posY - entityliving.lastTickPosY) * (double)f;
        double d2 = entityliving.lastTickPosZ + (entityliving.posZ - entityliving.lastTickPosZ) * (double)f;
        int l = MathHelper.floor_double(d1);
        int i1 = 5;
        if(mc.gameSettings.fancyGraphics)
        {
            i1 = 10;
        }
        BiomeGenBase abiomegenbase[] = world.getWorldChunkManager().func_4069_a(i - i1, k - i1, i1 * 2 + 1, i1 * 2 + 1);
        int j1 = 0;
        for(int k1 = i - i1; k1 <= i + i1; k1++)
        {
            for(int i2 = k - i1; i2 <= k + i1; i2++)
            {
                BiomeGenBase biomegenbase = abiomegenbase[j1++];
                if(!biomegenbase.getEnableSnow())
                {
                    continue;
                }
                int k2 = world.findTopSolidBlock(k1, i2);
                if(k2 < 0)
                {
                    k2 = 0;
                }
                int i3 = k2;
                if(i3 < l)
                {
                    i3 = l;
                }
                int k3 = j - i1;
                int i4 = j + i1;
                if(k3 < k2)
                {
                    k3 = k2;
                }
                if(i4 < k2)
                {
                    i4 = k2;
                }
                float f3 = 1.0F;
                if(k3 != i4)
                {
                    random.setSeed(k1 * k1 * 3121 /*GL_RGBA_MODE*/ + k1 * 0x2b24abb + i2 * i2 * 0x66397 + i2 * 13761);
                    float f5 = (float)rendererUpdateCount + f;
                    float f6 = ((float)(rendererUpdateCount & 0x1ff) + f) / 512F;
                    float f7 = random.nextFloat() + f5 * 0.01F * (float)random.nextGaussian();
                    float f8 = random.nextFloat() + f5 * (float)random.nextGaussian() * 0.001F;
                    double d5 = (double)((float)k1 + 0.5F) - entityliving.posX;
                    double d6 = (double)((float)i2 + 0.5F) - entityliving.posZ;
                    float f11 = MathHelper.sqrt_double(d5 * d5 + d6 * d6) / (float)i1;
                    tessellator.startDrawingQuads();
                    float f12 = world.getLightBrightness(k1, i3, i2);
                    GL11.glColor4f(f12, f12, f12, ((1.0F - f11 * f11) * 0.3F + 0.5F) * f1);
                    tessellator.setTranslationD(-d * 1.0D, -d1 * 1.0D, -d2 * 1.0D);
                    tessellator.addVertexWithUV(k1 + 0, k3, (double)i2 + 0.5D, 0.0F * f3 + f7, ((float)k3 * f3) / 4F + f6 * f3 + f8);
                    tessellator.addVertexWithUV(k1 + 1, k3, (double)i2 + 0.5D, 1.0F * f3 + f7, ((float)k3 * f3) / 4F + f6 * f3 + f8);
                    tessellator.addVertexWithUV(k1 + 1, i4, (double)i2 + 0.5D, 1.0F * f3 + f7, ((float)i4 * f3) / 4F + f6 * f3 + f8);
                    tessellator.addVertexWithUV(k1 + 0, i4, (double)i2 + 0.5D, 0.0F * f3 + f7, ((float)i4 * f3) / 4F + f6 * f3 + f8);
                    tessellator.addVertexWithUV((double)k1 + 0.5D, k3, i2 + 0, 0.0F * f3 + f7, ((float)k3 * f3) / 4F + f6 * f3 + f8);
                    tessellator.addVertexWithUV((double)k1 + 0.5D, k3, i2 + 1, 1.0F * f3 + f7, ((float)k3 * f3) / 4F + f6 * f3 + f8);
                    tessellator.addVertexWithUV((double)k1 + 0.5D, i4, i2 + 1, 1.0F * f3 + f7, ((float)i4 * f3) / 4F + f6 * f3 + f8);
                    tessellator.addVertexWithUV((double)k1 + 0.5D, i4, i2 + 0, 0.0F * f3 + f7, ((float)i4 * f3) / 4F + f6 * f3 + f8);
                    tessellator.setTranslationD(0.0D, 0.0D, 0.0D);
                    tessellator.draw();
                }
            }

        }

        GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, mc.renderEngine.getTexture("/environment/rain.png"));
        if(mc.gameSettings.fancyGraphics)
        {
            i1 = 10;
        }
        j1 = 0;
        for(int l1 = i - i1; l1 <= i + i1; l1++)
        {
            for(int j2 = k - i1; j2 <= k + i1; j2++)
            {
                BiomeGenBase biomegenbase1 = abiomegenbase[j1++];
                if(!biomegenbase1.canSpawnLightningBolt())
                {
                    continue;
                }
                int l2 = world.findTopSolidBlock(l1, j2);
                int j3 = j - i1;
                int l3 = j + i1;
                if(j3 < l2)
                {
                    j3 = l2;
                }
                if(l3 < l2)
                {
                    l3 = l2;
                }
                float f2 = 1.0F;
                if(j3 != l3)
                {
                    random.setSeed(l1 * l1 * 3121 /*GL_RGBA_MODE*/ + l1 * 0x2b24abb + j2 * j2 * 0x66397 + j2 * 13761);
                    float f4 = (((float)(rendererUpdateCount + l1 * l1 * 3121 /*GL_RGBA_MODE*/ + l1 * 0x2b24abb + j2 * j2 * 0x66397 + j2 * 13761 & 0x1f) + f) / 32F) * (3F + random.nextFloat());
                    double d3 = (double)((float)l1 + 0.5F) - entityliving.posX;
                    double d4 = (double)((float)j2 + 0.5F) - entityliving.posZ;
                    float f9 = MathHelper.sqrt_double(d3 * d3 + d4 * d4) / (float)i1;
                    tessellator.startDrawingQuads();
                    float f10 = world.getLightBrightness(l1, 128, j2) * 0.85F + 0.15F;
                    GL11.glColor4f(f10, f10, f10, ((1.0F - f9 * f9) * 0.5F + 0.5F) * f1);
                    tessellator.setTranslationD(-d * 1.0D, -d1 * 1.0D, -d2 * 1.0D);
                    tessellator.addVertexWithUV(l1 + 0, j3, (double)j2 + 0.5D, 0.0F * f2, ((float)j3 * f2) / 4F + f4 * f2);
                    tessellator.addVertexWithUV(l1 + 1, j3, (double)j2 + 0.5D, 1.0F * f2, ((float)j3 * f2) / 4F + f4 * f2);
                    tessellator.addVertexWithUV(l1 + 1, l3, (double)j2 + 0.5D, 1.0F * f2, ((float)l3 * f2) / 4F + f4 * f2);
                    tessellator.addVertexWithUV(l1 + 0, l3, (double)j2 + 0.5D, 0.0F * f2, ((float)l3 * f2) / 4F + f4 * f2);
                    tessellator.addVertexWithUV((double)l1 + 0.5D, j3, j2 + 0, 0.0F * f2, ((float)j3 * f2) / 4F + f4 * f2);
                    tessellator.addVertexWithUV((double)l1 + 0.5D, j3, j2 + 1, 1.0F * f2, ((float)j3 * f2) / 4F + f4 * f2);
                    tessellator.addVertexWithUV((double)l1 + 0.5D, l3, j2 + 1, 1.0F * f2, ((float)l3 * f2) / 4F + f4 * f2);
                    tessellator.addVertexWithUV((double)l1 + 0.5D, l3, j2 + 0, 0.0F * f2, ((float)l3 * f2) / 4F + f4 * f2);
                    tessellator.setTranslationD(0.0D, 0.0D, 0.0D);
                    tessellator.draw();
                }
            }

        }

        GL11.glEnable(2884 /*GL_CULL_FACE*/);
        GL11.glDisable(3042 /*GL_BLEND*/);
        GL11.glAlphaFunc(516, 0.1F);
    }

    public void func_905_b()
    {
        ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        GL11.glClear(256);
        GL11.glMatrixMode(5889 /*GL_PROJECTION*/);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0D, scaledresolution.field_25121_a, scaledresolution.field_25120_b, 0.0D, 1000D, 3000D);
        GL11.glMatrixMode(5888 /*GL_MODELVIEW0_ARB*/);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0F, 0.0F, -2000F);
    }

    private void updateFogColor(float f)
    {
        World world = mc.theWorld;
        EntityLiving entityliving = mc.renderViewEntity;
        float f1 = 1.0F / (float)(4 - mc.gameSettings.renderDistance);
        f1 = 1.0F - (float)Math.pow(f1, 0.25D);
        Vec3D vec3d = world.func_4079_a(mc.renderViewEntity, f);
        float f2 = (float)vec3d.xCoord;
        float f3 = (float)vec3d.yCoord;
        float f4 = (float)vec3d.zCoord;
        Vec3D vec3d1 = world.getFogColor(f);
        fogColorRed = (float)vec3d1.xCoord;
        fogColorGreen = (float)vec3d1.yCoord;
        fogColorBlue = (float)vec3d1.zCoord;
        fogColorRed += (f2 - fogColorRed) * f1;
        fogColorGreen += (f3 - fogColorGreen) * f1;
        fogColorBlue += (f4 - fogColorBlue) * f1;
        float f5 = world.func_27162_g(f);
        if(f5 > 0.0F)
        {
            float f6 = 1.0F - f5 * 0.5F;
            float f8 = 1.0F - f5 * 0.4F;
            fogColorRed *= f6;
            fogColorGreen *= f6;
            fogColorBlue *= f8;
        }
        float f7 = world.func_27166_f(f);
        if(f7 > 0.0F)
        {
            float f9 = 1.0F - f7 * 0.5F;
            fogColorRed *= f9;
            fogColorGreen *= f9;
            fogColorBlue *= f9;
        }
        if(cloudFog)
        {
            Vec3D vec3d2 = world.func_628_d(f);
            fogColorRed = (float)vec3d2.xCoord;
            fogColorGreen = (float)vec3d2.yCoord;
            fogColorBlue = (float)vec3d2.zCoord;
        } else
        if(entityliving.isInsideOfMaterial(Material.water))
        {
            fogColorRed = 0.02F;
            fogColorGreen = 0.02F;
            fogColorBlue = 0.2F;
        } else
        if(entityliving.isInsideOfMaterial(Material.lava))
        {
            fogColorRed = 0.6F;
            fogColorGreen = 0.1F;
            fogColorBlue = 0.0F;
        }
        float f10 = fogColor2 + (fogColor1 - fogColor2) * f;
        fogColorRed *= f10;
        fogColorGreen *= f10;
        fogColorBlue *= f10;
        if(mc.gameSettings.anaglyph)
        {
            float f11 = (fogColorRed * 30F + fogColorGreen * 59F + fogColorBlue * 11F) / 100F;
            float f12 = (fogColorRed * 30F + fogColorGreen * 70F) / 100F;
            float f13 = (fogColorRed * 30F + fogColorBlue * 70F) / 100F;
            fogColorRed = f11;
            fogColorGreen = f12;
            fogColorBlue = f13;
        }
        GL11.glClearColor(fogColorRed, fogColorGreen, fogColorBlue, 0.0F);
    }

    private void setupFog(int i, float f)
    {
        EntityLiving entityliving = mc.renderViewEntity;
        GL11.glFog(2918 /*GL_FOG_COLOR*/, func_908_a(fogColorRed, fogColorGreen, fogColorBlue, 1.0F));
        GL11.glNormal3f(0.0F, -1F, 0.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        if(cloudFog)
        {
            GL11.glFogi(2917 /*GL_FOG_MODE*/, 2048 /*GL_EXP*/);
            GL11.glFogf(2914 /*GL_FOG_DENSITY*/, 0.1F);
            float f1 = 1.0F;
            float f4 = 1.0F;
            float f7 = 1.0F;
            if(mc.gameSettings.anaglyph)
            {
                float f10 = (f1 * 30F + f4 * 59F + f7 * 11F) / 100F;
                float f13 = (f1 * 30F + f4 * 70F) / 100F;
                float f16 = (f1 * 30F + f7 * 70F) / 100F;
                f1 = f10;
                f4 = f13;
                f7 = f16;
            }
        } else
        if(entityliving.isInsideOfMaterial(Material.water))
        {
            GL11.glFogi(2917 /*GL_FOG_MODE*/, 2048 /*GL_EXP*/);
            GL11.glFogf(2914 /*GL_FOG_DENSITY*/, 0.1F);
            float f2 = 0.4F;
            float f5 = 0.4F;
            float f8 = 0.9F;
            if(mc.gameSettings.anaglyph)
            {
                float f11 = (f2 * 30F + f5 * 59F + f8 * 11F) / 100F;
                float f14 = (f2 * 30F + f5 * 70F) / 100F;
                float f17 = (f2 * 30F + f8 * 70F) / 100F;
                f2 = f11;
                f5 = f14;
                f8 = f17;
            }
        } else
        if(entityliving.isInsideOfMaterial(Material.lava))
        {
            GL11.glFogi(2917 /*GL_FOG_MODE*/, 2048 /*GL_EXP*/);
            GL11.glFogf(2914 /*GL_FOG_DENSITY*/, 2.0F);
            float f3 = 0.4F;
            float f6 = 0.3F;
            float f9 = 0.3F;
            if(mc.gameSettings.anaglyph)
            {
                float f12 = (f3 * 30F + f6 * 59F + f9 * 11F) / 100F;
                float f15 = (f3 * 30F + f6 * 70F) / 100F;
                float f18 = (f3 * 30F + f9 * 70F) / 100F;
                f3 = f12;
                f6 = f15;
                f9 = f18;
            }
        } else
        {
            GL11.glFogi(2917 /*GL_FOG_MODE*/, 9729 /*GL_LINEAR*/);
            GL11.glFogf(2915 /*GL_FOG_START*/, farPlaneDistance * 0.25F);
            GL11.glFogf(2916 /*GL_FOG_END*/, farPlaneDistance);
            if(i < 0)
            {
                GL11.glFogf(2915 /*GL_FOG_START*/, 0.0F);
                GL11.glFogf(2916 /*GL_FOG_END*/, farPlaneDistance * 0.8F);
            }
            if(GLContext.getCapabilities().GL_NV_fog_distance)
            {
                GL11.glFogi(34138, 34139);
            }
            if(mc.theWorld.worldProvider.isNether)
            {
                GL11.glFogf(2915 /*GL_FOG_START*/, 0.0F);
            }
        }
        GL11.glEnable(2903 /*GL_COLOR_MATERIAL*/);
        GL11.glColorMaterial(1028 /*GL_FRONT*/, 4608 /*GL_AMBIENT*/);
    }

    private FloatBuffer func_908_a(float f, float f1, float f2, float f3)
    {
        fogColorBuffer.clear();
        fogColorBuffer.put(f).put(f1).put(f2).put(f3);
        fogColorBuffer.flip();
        return fogColorBuffer;
    }

    public static boolean field_28135_a = false;
    public static int anaglyphField;
    private Minecraft mc;
    private float farPlaneDistance;
    public ItemRenderer itemRenderer;
    private int rendererUpdateCount;
    private Entity pointedEntity;
    private MouseFilter mouseFilterXAxis;
    private MouseFilter mouseFilterYAxis;
    private MouseFilter mouseFilterDummy1;
    private MouseFilter mouseFilterDummy2;
    private MouseFilter mouseFilterDummy3;
    private MouseFilter mouseFilterDummy4;
    private float field_22228_r;
    private float field_22227_s;
    private float field_22226_t;
    private float field_22225_u;
    private float field_22224_v;
    private float field_22223_w;
    private float field_22222_x;
    private float field_22221_y;
    private float field_22220_z;
    private float field_22230_A;
    private boolean cloudFog;
    private double cameraZoom;
    private double cameraYaw;
    private double cameraPitch;
    private long prevFrameTime;
    private long field_28133_I;
    private Random random;
    private int rainSoundCounter;
    volatile int field_1394_b;
    volatile int field_1393_c;
    FloatBuffer fogColorBuffer;
    float fogColorRed;
    float fogColorGreen;
    float fogColorBlue;
    private float fogColor2;
    private float fogColor1;

}
