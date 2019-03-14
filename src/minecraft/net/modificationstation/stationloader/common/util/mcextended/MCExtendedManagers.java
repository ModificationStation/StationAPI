// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.modificationstation.stationloader.common.util.mcextended;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;
import net.minecraft.src.CompressedStreamTools;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityRenderer;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemRenderer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.RenderEngine;
import net.minecraft.src.RenderItem;
import net.minecraft.src.RenderManager;
import net.minecraft.src.SaveHandler;
import net.minecraft.src.Tessellator;
import net.minecraft.src.TexturePackCustom;
import net.minecraft.src.TexturePackDefault;
import net.minecraft.src.World;
import net.modificationstation.stationloader.common.util.ReflectionHelper;

import org.lwjgl.opengl.GL11;

// Referenced classes of package net.minecraft.src:
//            ModLoader, TexturePackDefault, TexturePackCustom, TexturePackBase, 
//            Block, BlockFire, World, NBTTagCompound, 
//            CompressedStreamTools, IModNBT, SaveHandler, RenderEngine, 
//            EntityItem, ItemStack, MathHelper, Tessellator, 
//            RenderItem, RenderManager, Item, FontRenderer, 
//            BaseRender, ItemRenderer, EntityRenderer

public class MCExtendedManagers
{
    public static class TexturePackD extends TexturePackDefault
    {

        public InputStream getResourceAsStream(String s)
        {
            InputStream inputstream = TextureManager.getOutsideTexture(s);
            if(inputstream != null)
            {
                return inputstream;
            } else
            {
                return super.getResourceAsStream(s);
            }
        }

        public TexturePackD()
        {
        }
    }

    public static class TexturePackC extends TexturePackCustom
    {

        private void resetFields(Field afield[], TexturePackCustom texturepackcustom)
        {
            for(int i = 0; i < afield.length; i++)
            {
                try
                {
                    afield[i].set(this, afield[i].get(texturepackcustom));
                }
                catch(Exception exception)
                {
                    exception.printStackTrace();
                }
            }

        }

        public InputStream getResourceAsStream(String s)
        {
            InputStream inputstream = TextureManager.getOutsideTexture(s);
            if(inputstream != null)
            {
                return inputstream;
            } else
            {
                return super.getResourceAsStream(s);
            }
        }

        static Class _mthclass$(String s)
        {
            try
            {
                return Class.forName(s);
            }
            catch(ClassNotFoundException classnotfoundexception)
            {
                throw new NoClassDefFoundError(classnotfoundexception.getMessage());
            }
        }

        private static Field TexturePackCustomFields[];
        private static Field TexturePackBaseFields[];

        static 
        {
            TexturePackCustomFields = (net.minecraft.src.TexturePackCustom.class).getDeclaredFields();
            Field.setAccessible(TexturePackCustomFields, true);
            TexturePackBaseFields = (net.minecraft.src.TexturePackBase.class).getDeclaredFields();
            Field.setAccessible(TexturePackBaseFields, true);
        }

        public TexturePackC(File file, TexturePackCustom texturepackcustom)
        {
            super(file);
            resetFields(TexturePackCustomFields, texturepackcustom);
            resetFields(TexturePackBaseFields, texturepackcustom);
        }
    }

    public static class TextureManager
    {

        public static InputStream getOutsideTexture(String s)
        {
            try
            {
                if(registeredOutsideTextures.get(s) != null)
                {
                    return new BufferedInputStream(new FileInputStream((File)registeredOutsideTextures.get(s)));
                }
            }
            catch(Exception exception)
            {
                throw new RuntimeException(exception);
            }
            return null;
        }

        public static void registerOutsideTexture(String s, File file)
        {
            registeredOutsideTextures.put(s, file);
        }

        private static HashMap registeredOutsideTextures = new HashMap();


        public TextureManager()
        {
        }
    }

    public static class BlockManager
    {

        public static void addBlockForFire(int i, int j, int k)
        {
            try
            {
                BlockFire_setBurnRate.invoke(Block.fire, new Object[] {
                    Integer.valueOf(i), Integer.valueOf(j), Integer.valueOf(k)
                });
            }
            catch(Exception exception) { }
        }

        static Class _mthclass$(String s)
        {
            try
            {
                return Class.forName(s);
            }
            catch(ClassNotFoundException classnotfoundexception)
            {
                throw new NoClassDefFoundError(classnotfoundexception.getMessage());
            }
        }

        private static Method BlockFire_setBurnRate;

        static 
        {
            try
            {
                BlockFire_setBurnRate = (net.minecraft.src.BlockFire.class).getDeclaredMethod("a", new Class[] {
                    Integer.TYPE, Integer.TYPE, Integer.TYPE
                });
            }
            catch(NoSuchMethodException nosuchmethodexception)
            {
                try
                {
                    BlockFire_setBurnRate = (net.minecraft.src.BlockFire.class).getDeclaredMethod("setBurnRate", new Class[] {
                        Integer.TYPE, Integer.TYPE, Integer.TYPE
                    });
                }
                catch(Exception exception)
                {
                    exception.printStackTrace();
                }
            }
            BlockFire_setBurnRate.setAccessible(true);
        }

        public BlockManager()
        {
        }
    }

    public static class NBTManager
    {

        public static void saveModsToCompound(World world)
        {
            if(ModsForNBT.size() == 0)
            {
                return;
            }
            try
            {
                File file = (File)saveDir.get((SaveHandler)ReflectionHelper.getPrivateValue(World.class, world, new String[] {"p", "saveHandler"}));
                File file1 = new File(file, "ModData.dat");
                if(!file1.exists())
                {
                    CompressedStreamTools.writeGzippedCompoundToOutputStream(new NBTTagCompound(), new FileOutputStream(file1));
                }
                NBTTagCompound nbttagcompound = CompressedStreamTools.func_1138_a(new FileInputStream(file1));
                for(int i = 0; i < ModsForNBT.size(); i++)
                {
                    IModNBT imodnbt = (IModNBT)ModsForNBT.get(i);
                    NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag(imodnbt.getClass().getSimpleName());
                    nbttagcompound.setCompoundTag(imodnbt.getClass().getSimpleName(), nbttagcompound1);
                    imodnbt.saveModToNBT(nbttagcompound1);
                }

                CompressedStreamTools.writeGzippedCompoundToOutputStream(nbttagcompound, new FileOutputStream(file1));
            }
            catch(Exception exception)
            {
                exception.printStackTrace();
            }
        }

        public static void loadModsFromCompound(World world)
        {
            if(ModsForNBT.size() == 0)
            {
                return;
            }
            try
            {
                File file = (File)saveDir.get((SaveHandler)ReflectionHelper.getPrivateValue(World.class, world, new String[] {"p", "saveHandler"}));
                File file1 = new File(file, "ModData.dat");
                if(!file1.exists())
                {
                    CompressedStreamTools.writeGzippedCompoundToOutputStream(new NBTTagCompound(), new FileOutputStream(file1));
                }
                NBTTagCompound nbttagcompound = CompressedStreamTools.func_1138_a(new FileInputStream(file1));
                for(int i = 0; i < ModsForNBT.size(); i++)
                {
                    IModNBT imodnbt = (IModNBT)ModsForNBT.get(i);
                    imodnbt.readModFromNBT(nbttagcompound.getCompoundTag(imodnbt.getClass().getSimpleName()));
                }

            }
            catch(Exception exception)
            {
                exception.printStackTrace();
            }
        }

        public static void addModForNBT(IModNBT imodnbt)
        {
            ModsForNBT.add(imodnbt);
        }

        static Class _mthclass$(String s)
        {
            try
            {
                return Class.forName(s);
            }
            catch(ClassNotFoundException classnotfoundexception)
            {
                throw new NoClassDefFoundError(classnotfoundexception.getMessage());
            }
        }

        private static ArrayList ModsForNBT = new ArrayList();
        private static Field saveDir;

        static 
        {
            try
            {
                saveDir = (net.minecraft.src.SaveHandler.class).getDeclaredField("b");
            }
            catch(NoSuchFieldException nosuchfieldexception)
            {
                try
                {
                    saveDir = (net.minecraft.src.SaveHandler.class).getDeclaredField("saveDirectory");
                }
                catch(Exception exception)
                {
                    exception.printStackTrace();
                }
            }
            saveDir.setAccessible(true);
        }

        public NBTManager()
        {
        }
    }

    public static class ItemManager
    {

        public static String getItemName(int i)
        {
            return (String)items.get(i);
        }

        public static int getItem(int i)
        {
            return MCExtendedManagers.mc.renderEngine.getTexture((String)items.get(i));
        }

        public static int getItemNumBasedonPath(String s)
        {
            return ((Integer)pathToIdMap.get(s)).intValue();
        }

        public static int registerItem(String s)
        {
            items.add(currentIndex, s);
            pathToIdMap.put(s, Integer.valueOf(currentIndex));
            return currentIndex++;
        }

        public static int getTotal()
        {
            return items.size();
        }

        private static int currentIndex = 0;
        private static HashMap pathToIdMap = new HashMap();
        private static ArrayList items = new ArrayList();

        static 
        {
            registerItem("/gui/items.png");
        }

        public ItemManager()
        {
        }
    }

    public static class TerrainManager
    {

        public static int getTerrain(int i)
        {
            return MCExtendedManagers.mc.renderEngine.getTexture((String)terrains.get(i));
        }

        public static String getTerrainName(int i)
        {
            return (String)terrains.get(i);
        }

        public static int getTerrainNumBasedOnPath(String s)
        {
            return ((Integer)pathToIdMap.get(s)).intValue();
        }

        public static int registerTerrain(String s)
        {
            terrains.add(currentIndex, s);
            pathToIdMap.put(s, Integer.valueOf(currentIndex));
            return currentIndex++;
        }

        public static int getTotal()
        {
            return terrains.size();
        }

        private static int currentIndex = 0;
        private static HashMap pathToIdMap = new HashMap();
        private static ArrayList terrains = new ArrayList();

        static 
        {
            registerTerrain("/terrain.png");
        }

        public TerrainManager()
        {
        }
    }

    public static class RenderItemOverrides
    {

        public static boolean renderOverrideGround(RenderItem renderitem, EntityItem entityitem, double d, double d1, double d2, 
                float f, float f1)
        {
            ItemStack itemstack = entityitem.item;
            if(itemstack != null && itemstack.getIconIndex() >= 256 && itemstack.itemID >= Block.blocksList.length)
            {
                random.setSeed(187L);
                GL11.glPushMatrix();
                float f2 = MathHelper.sin(((float)entityitem.age + f1) / 10F + entityitem.field_804_d) * 0.1F + 0.1F;
                byte byte0 = 1;
                if(entityitem.item.stackSize > 1)
                {
                    byte0 = 2;
                }
                if(entityitem.item.stackSize > 5)
                {
                    byte0 = 3;
                }
                if(entityitem.item.stackSize > 20)
                {
                    byte0 = 4;
                }
                GL11.glTranslatef((float)d, (float)d1 + f2, (float)d2);
                GL11.glEnable(32826 /*GL_RESCALE_NORMAL_EXT*/);
                GL11.glScalef(0.5F, 0.5F, 0.5F);
                int i = itemstack.getIconIndex();
                int j = i / 256;
                i -= j * 256;
                MCExtendedManagers.mc.renderEngine.bindTexture(ItemManager.getItem(j));
                Tessellator tessellator = Tessellator.instance;
                float f3 = (float)((i % 16) * 16 + 0) / 256F;
                float f4 = (float)((i % 16) * 16 + 16) / 256F;
                float f5 = (float)((i / 16) * 16 + 0) / 256F;
                float f6 = (float)((i / 16) * 16 + 16) / 256F;
                float f7 = 1.0F;
                float f8 = 0.5F;
                float f9 = 0.25F;
                for(int k = 0; k < byte0; k++)
                {
                    GL11.glPushMatrix();
                    if(k > 0)
                    {
                        float f10 = (random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                        float f11 = (random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                        float f12 = (random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                        GL11.glTranslatef(f10, f11, f12);
                    }
                    GL11.glRotatef(180F - ((RenderManager)ReflectionHelper.getPrivateValue(RenderItem.class, renderitem, new String[] {"a", "renderManager"})).playerViewY, 0.0F, 1.0F, 0.0F);
                    tessellator.startDrawingQuads();
                    tessellator.setNormal(0.0F, 1.0F, 0.0F);
                    tessellator.addVertexWithUV(0.0F - f8, 0.0F - f9, 0.0D, f3, f6);
                    tessellator.addVertexWithUV(f7 - f8, 0.0F - f9, 0.0D, f4, f6);
                    tessellator.addVertexWithUV(f7 - f8, 1.0F - f9, 0.0D, f4, f5);
                    tessellator.addVertexWithUV(0.0F - f8, 1.0F - f9, 0.0D, f3, f5);
                    tessellator.draw();
                    GL11.glPopMatrix();
                }

                GL11.glDisable(32826 /*GL_RESCALE_NORMAL_EXT*/);
                GL11.glPopMatrix();
                return true;
            } else
            {
                return false;
            }
        }

        public static boolean renderOverrideGUI(RenderItem renderitem, FontRenderer fontrenderer, RenderEngine renderengine, ItemStack itemstack, int i, int j)
        {
            if(itemstack != null && itemstack.getIconIndex() >= 256 && itemstack.itemID >= Block.blocksList.length)
            {
                GL11.glDisable(2896 /*GL_LIGHTING*/);
                int k = itemstack.getIconIndex();
                int l = k / 256;
                k -= 256 * l;
                GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, ItemManager.getItem(l));
                int i1 = Item.itemsList[itemstack.itemID].getColorFromDamage(itemstack.getItemDamage());
                float f = (float)(i1 >> 16 & 0xff) / 255F;
                float f1 = (float)(i1 >> 8 & 0xff) / 255F;
                float f2 = (float)(i1 & 0xff) / 255F;
                if(renderitem.field_27004_a)
                {
                    GL11.glColor4f(f, f1, f2, 1.0F);
                }
                renderTexturedQuad(i, j, (k % 16) * 16, (k / 16) * 16, 16, 16);
                GL11.glEnable(2896 /*GL_LIGHTING*/);
                return true;
            } else
            {
                return false;
            }
        }

        public static boolean renderOverrideHand(ItemStack itemstack)
        {
            if(itemstack != null && itemstack.getIconIndex() >= 256 && itemstack.itemID >= Block.blocksList.length)
            {
                GL11.glPushMatrix();
                Tessellator tessellator = Tessellator.instance;
                int i = itemstack.getIconIndex();
                int j = i / 256;
                i -= j * 256;
                GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, ItemManager.getItem(j));
                float f = ((float)((i % 16) * 16) + 0.0F) / 256F;
                float f1 = ((float)((i % 16) * 16) + 15.99F) / 256F;
                float f2 = ((float)((i / 16) * 16) + 0.0F) / 256F;
                float f3 = ((float)((i / 16) * 16) + 15.99F) / 256F;
                float f4 = 1.0F;
                float f5 = 0.0F;
                float f6 = 0.3F;
                GL11.glEnable(32826 /*GL_RESCALE_NORMAL_EXT*/);
                GL11.glTranslatef(-f5, -f6, 0.0F);
                float f7 = 1.5F;
                GL11.glScalef(f7, f7, f7);
                GL11.glRotatef(50F, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(335F, 0.0F, 0.0F, 1.0F);
                GL11.glTranslatef(-0.9375F, -0.0625F, 0.0F);
                float f8 = 0.0625F;
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, 0.0F, 1.0F);
                tessellator.addVertexWithUV(0.0D, 0.0D, 0.0D, f1, f3);
                tessellator.addVertexWithUV(f4, 0.0D, 0.0D, f, f3);
                tessellator.addVertexWithUV(f4, 1.0D, 0.0D, f, f2);
                tessellator.addVertexWithUV(0.0D, 1.0D, 0.0D, f1, f2);
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, 0.0F, -1F);
                tessellator.addVertexWithUV(0.0D, 1.0D, 0.0F - f8, f1, f2);
                tessellator.addVertexWithUV(f4, 1.0D, 0.0F - f8, f, f2);
                tessellator.addVertexWithUV(f4, 0.0D, 0.0F - f8, f, f3);
                tessellator.addVertexWithUV(0.0D, 0.0D, 0.0F - f8, f1, f3);
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(-1F, 0.0F, 0.0F);
                for(int k = 0; k < 16; k++)
                {
                    float f9 = (float)k / 16F;
                    float f13 = (f1 + (f - f1) * f9) - 0.001953125F;
                    float f17 = f4 * f9;
                    tessellator.addVertexWithUV(f17, 0.0D, 0.0F - f8, f13, f3);
                    tessellator.addVertexWithUV(f17, 0.0D, 0.0D, f13, f3);
                    tessellator.addVertexWithUV(f17, 1.0D, 0.0D, f13, f2);
                    tessellator.addVertexWithUV(f17, 1.0D, 0.0F - f8, f13, f2);
                }

                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(1.0F, 0.0F, 0.0F);
                for(int l = 0; l < 16; l++)
                {
                    float f10 = (float)l / 16F;
                    float f14 = (f1 + (f - f1) * f10) - 0.001953125F;
                    float f18 = f4 * f10 + 0.0625F;
                    tessellator.addVertexWithUV(f18, 1.0D, 0.0F - f8, f14, f2);
                    tessellator.addVertexWithUV(f18, 1.0D, 0.0D, f14, f2);
                    tessellator.addVertexWithUV(f18, 0.0D, 0.0D, f14, f3);
                    tessellator.addVertexWithUV(f18, 0.0D, 0.0F - f8, f14, f3);
                }

                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, 1.0F, 0.0F);
                for(int i1 = 0; i1 < 16; i1++)
                {
                    float f11 = (float)i1 / 16F;
                    float f15 = (f3 + (f2 - f3) * f11) - 0.001953125F;
                    float f19 = f4 * f11 + 0.0625F;
                    tessellator.addVertexWithUV(0.0D, f19, 0.0D, f1, f15);
                    tessellator.addVertexWithUV(f4, f19, 0.0D, f, f15);
                    tessellator.addVertexWithUV(f4, f19, 0.0F - f8, f, f15);
                    tessellator.addVertexWithUV(0.0D, f19, 0.0F - f8, f1, f15);
                }

                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, -1F, 0.0F);
                for(int j1 = 0; j1 < 16; j1++)
                {
                    float f12 = (float)j1 / 16F;
                    float f16 = (f3 + (f2 - f3) * f12) - 0.001953125F;
                    float f20 = f4 * f12;
                    tessellator.addVertexWithUV(f4, f20, 0.0D, f, f16);
                    tessellator.addVertexWithUV(0.0D, f20, 0.0D, f1, f16);
                    tessellator.addVertexWithUV(0.0D, f20, 0.0F - f8, f1, f16);
                    tessellator.addVertexWithUV(f4, f20, 0.0F - f8, f, f16);
                }

                tessellator.draw();
                GL11.glDisable(32826 /*GL_RESCALE_NORMAL_EXT*/);
                GL11.glPopMatrix();
                return true;
            } else
            {
                return false;
            }
        }

        public static void renderTexturedQuad(int i, int j, int k, int l, int i1, int j1)
        {
            float f = 0.0F;
            float f1 = 0.00390625F;
            float f2 = 0.00390625F;
            Tessellator tessellator = Tessellator.instance;
            tessellator.startDrawingQuads();
            tessellator.addVertexWithUV(i + 0, j + j1, f, (float)(k + 0) * f1, (float)(l + j1) * f2);
            tessellator.addVertexWithUV(i + i1, j + j1, f, (float)(k + i1) * f1, (float)(l + j1) * f2);
            tessellator.addVertexWithUV(i + i1, j + 0, f, (float)(k + i1) * f1, (float)(l + 0) * f2);
            tessellator.addVertexWithUV(i + 0, j + 0, f, (float)(k + 0) * f1, (float)(l + 0) * f2);
            tessellator.draw();
        }

        private static Random random = new Random();


        public RenderItemOverrides()
        {
        }
    }

    public static class PlayerManager
    {

        public static boolean playerOverlayHook(ItemRenderer itemrenderer, float f)
        {
            for(int i = 0; i < registeredPlayerOverlayHooks.size(); i++)
            {
                if(((BaseRender)registeredPlayerOverlayHooks.get(i)).renderPlayerOverlay(itemrenderer, f))
                {
                    return true;
                }
            }

            return false;
        }

        public static boolean fogColorHook(EntityRenderer entityrenderer, float f)
        {
            for(int i = 0; i < registeredFogColorHooks.size(); i++)
            {
                if(((BaseRender)registeredFogColorHooks.get(i)).updateFogColor(entityrenderer, f))
                {
                    return true;
                }
            }

            return false;
        }

        public static boolean setupFogHook(EntityRenderer entityrenderer, int i, float f)
        {
            for(int j = 0; j < registeredSetupFogHooks.size(); j++)
            {
                if(((BaseRender)registeredSetupFogHooks.get(j)).setupFog(entityrenderer, i, f))
                {
                    return true;
                }
            }

            return false;
        }

        public static void registerPlayerOverlayHook(BaseRender baserender)
        {
            registeredPlayerOverlayHooks.add(baserender);
        }

        public static void registerFogColorHook(BaseRender baserender)
        {
            registeredFogColorHooks.add(baserender);
        }

        public static void registerSetupFogHook(BaseRender baserender)
        {
            registeredSetupFogHooks.add(baserender);
        }

        private static ArrayList registeredPlayerOverlayHooks = new ArrayList();
        private static ArrayList registeredFogColorHooks = new ArrayList();
        private static ArrayList registeredSetupFogHooks = new ArrayList();


        public PlayerManager()
        {
        }
    }

    public static class ItemRenderManager
    {

        public static boolean renderItemOnGroundHook(RenderItem renderitem, EntityItem entityitem, double d, double d1, double d2, 
                float f, float f1)
        {
            ItemStack itemstack = entityitem.item;
            if(itemstack != null && registeredItemOnGroundHooks.get(Integer.valueOf(itemstack.itemID)) != null)
            {
                ((BaseRender)registeredItemOnGroundHooks.get(Integer.valueOf(itemstack.itemID))).renderItemOnGround(renderitem, entityitem, d, d2, f, f1);
                return true;
            } else
            {
                return false;
            }
        }

        public static boolean renderItemIntoGUIHook(RenderItem renderitem, FontRenderer fontrenderer, RenderEngine renderengine, ItemStack itemstack, int i, int j)
        {
            if(itemstack != null && registeredItemGuiHooks.get(Integer.valueOf(itemstack.itemID)) != null)
            {
                ((BaseRender)registeredItemGuiHooks.get(Integer.valueOf(itemstack.itemID))).renderItemIntoGUI(renderitem, fontrenderer, renderengine, itemstack, i, j);
                return true;
            } else
            {
                return false;
            }
        }

        public static boolean renderItemOverlayHook(RenderItem renderitem, FontRenderer fontrenderer, RenderEngine renderengine, ItemStack itemstack, int i, int j)
        {
            if(itemstack != null && registeredItemOverlayHooks.get(Integer.valueOf(itemstack.itemID)) != null)
            {
                ((BaseRender)registeredItemOverlayHooks.get(Integer.valueOf(itemstack.itemID))).renderItemOverlayIntoGUI(renderitem, fontrenderer, renderengine, itemstack, i, j);
                return true;
            } else
            {
                return false;
            }
        }

        public static boolean renderItemInHandHook(ItemRenderer itemrenderer, ItemStack itemstack)
        {
            if(itemstack != null && registeredItemInHandHooks.get(Integer.valueOf(itemstack.itemID)) != null)
            {
                ((BaseRender)registeredItemInHandHooks.get(Integer.valueOf(itemstack.itemID))).renderItemInHand(itemrenderer, itemstack);
                return true;
            } else
            {
                return false;
            }
        }

        public static void registerItemInHandHook(int i, BaseRender baserender)
        {
            registeredItemInHandHooks.put(Integer.valueOf(i), baserender);
        }

        public static void registerItemOnGroundHook(int i, BaseRender baserender)
        {
            registeredItemOnGroundHooks.put(Integer.valueOf(i), baserender);
        }

        public static void registerItemOverlayHook(int i, BaseRender baserender)
        {
            registeredItemOverlayHooks.put(Integer.valueOf(i), baserender);
        }

        public static void registerItemGuiHook(int i, BaseRender baserender)
        {
            registeredItemGuiHooks.put(Integer.valueOf(i), baserender);
        }

        public static void registerAllHooks(int i, BaseRender baserender)
        {
            registerItemInHandHook(i, baserender);
            registerItemOnGroundHook(i, baserender);
            registerItemOverlayHook(i, baserender);
            registerItemGuiHook(i, baserender);
        }

        private static HashMap registeredItemInHandHooks = new HashMap();
        private static HashMap registeredItemOnGroundHooks = new HashMap();
        private static HashMap registeredItemOverlayHooks = new HashMap();
        private static HashMap registeredItemGuiHooks = new HashMap();


        public ItemRenderManager()
        {
        }
    }


    public MCExtendedManagers()
    {
    }

    public static final Minecraft mc = Minecraft.theMinecraft;

}
