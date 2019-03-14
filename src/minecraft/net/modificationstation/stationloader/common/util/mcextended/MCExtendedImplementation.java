package net.modificationstation.stationloader.common.util.mcextended;

import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.World;
import net.minecraft.src.WorldInfo;
import net.modificationstation.stationloader.common.util.ReflectionHelper;

public class MCExtendedImplementation {
    public static boolean OnTickInGame(Minecraft minecraft)
    {
        if(!mc.theWorld.multiplayerWorld)
        {
            if(mc.theWorld != worldObj)
            {
                if(worldObj != null)
                {
                    MCExtendedManagers.NBTManager.saveModsToCompound(worldObj);
                }
                if(mc.theWorld != null)
                {
                    MCExtendedManagers.NBTManager.loadModsFromCompound(mc.theWorld);
                }
                worldObj = mc.theWorld;
            }
            if(worldObj != null && ((WorldInfo)ReflectionHelper.getPrivateValue(World.class, worldObj, new String[] {"q", "worldInfo"})).getWorldTime() % ((long)(Integer)ReflectionHelper.getPrivateValue(World.class, worldObj, new String[] {"j", "autosavePeriod"})) == 0L)
            {
                MCExtendedManagers.NBTManager.saveModsToCompound(worldObj);
            }
        }
        return true;
    }
    public static boolean OnTickInGUI(Minecraft minecraft, GuiScreen guiscreen)
    {
        mc = minecraft;
        if(minecraft.theWorld == null && worldObj != null && !worldObj.multiplayerWorld)
        {
            MCExtendedManagers.NBTManager.saveModsToCompound(worldObj);
            worldObj = null;
        }
        return true;
    }
    private static World worldObj;
    private static Minecraft mc;
}
