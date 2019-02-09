// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.*;
import java.util.logging.Logger;
import net.minecraft.server.MinecraftServer;

// Referenced classes of package net.minecraft.src:
//            ServerCommand, ICommandListener, ServerConfigurationManager, WorldServer, 
//            EntityPlayerMP, NetServerHandler, Item, ItemStack, 
//            Packet3Chat, PropertyManager

public class ConsoleCommandHandler
{

    public ConsoleCommandHandler(MinecraftServer minecraftserver)
    {
        minecraftServer = minecraftserver;
    }

    public void handleCommand(ServerCommand servercommand)
    {
        String s = servercommand.command;
        ICommandListener icommandlistener = servercommand.commandListener;
        String s1 = icommandlistener.getUsername();
        ServerConfigurationManager serverconfigurationmanager = minecraftServer.configManager;
        if(s.toLowerCase().startsWith("help") || s.toLowerCase().startsWith("?"))
        {
            printHelp(icommandlistener);
        } else
        if(s.toLowerCase().startsWith("list"))
        {
            icommandlistener.log((new StringBuilder()).append("Connected players: ").append(serverconfigurationmanager.getPlayerList()).toString());
        } else
        if(s.toLowerCase().startsWith("stop"))
        {
            sendNoticeToOps(s1, "Stopping the server..");
            minecraftServer.initiateShutdown();
        } else
        if(s.toLowerCase().startsWith("save-all"))
        {
            sendNoticeToOps(s1, "Forcing save..");
            if(serverconfigurationmanager != null)
            {
                serverconfigurationmanager.savePlayerStates();
            }
            for(int i = 0; i < minecraftServer.worldMngr.length; i++)
            {
                WorldServer worldserver = minecraftServer.worldMngr[i];
                worldserver.saveWorld(true, null);
            }

            sendNoticeToOps(s1, "Save complete.");
        } else
        if(s.toLowerCase().startsWith("save-off"))
        {
            sendNoticeToOps(s1, "Disabling level saving..");
            for(int j = 0; j < minecraftServer.worldMngr.length; j++)
            {
                WorldServer worldserver1 = minecraftServer.worldMngr[j];
                worldserver1.levelSaving = true;
            }

        } else
        if(s.toLowerCase().startsWith("save-on"))
        {
            sendNoticeToOps(s1, "Enabling level saving..");
            for(int k = 0; k < minecraftServer.worldMngr.length; k++)
            {
                WorldServer worldserver2 = minecraftServer.worldMngr[k];
                worldserver2.levelSaving = false;
            }

        } else
        if(s.toLowerCase().startsWith("op "))
        {
            String s2 = s.substring(s.indexOf(" ")).trim();
            serverconfigurationmanager.opPlayer(s2);
            sendNoticeToOps(s1, (new StringBuilder()).append("Opping ").append(s2).toString());
            serverconfigurationmanager.sendChatMessageToPlayer(s2, "\247eYou are now op!");
        } else
        if(s.toLowerCase().startsWith("deop "))
        {
            String s3 = s.substring(s.indexOf(" ")).trim();
            serverconfigurationmanager.deopPlayer(s3);
            serverconfigurationmanager.sendChatMessageToPlayer(s3, "\247eYou are no longer op!");
            sendNoticeToOps(s1, (new StringBuilder()).append("De-opping ").append(s3).toString());
        } else
        if(s.toLowerCase().startsWith("ban-ip "))
        {
            String s4 = s.substring(s.indexOf(" ")).trim();
            serverconfigurationmanager.banIP(s4);
            sendNoticeToOps(s1, (new StringBuilder()).append("Banning ip ").append(s4).toString());
        } else
        if(s.toLowerCase().startsWith("pardon-ip "))
        {
            String s5 = s.substring(s.indexOf(" ")).trim();
            serverconfigurationmanager.pardonIP(s5);
            sendNoticeToOps(s1, (new StringBuilder()).append("Pardoning ip ").append(s5).toString());
        } else
        if(s.toLowerCase().startsWith("ban "))
        {
            String s6 = s.substring(s.indexOf(" ")).trim();
            serverconfigurationmanager.banPlayer(s6);
            sendNoticeToOps(s1, (new StringBuilder()).append("Banning ").append(s6).toString());
            EntityPlayerMP entityplayermp = serverconfigurationmanager.getPlayerEntity(s6);
            if(entityplayermp != null)
            {
                entityplayermp.playerNetServerHandler.kickPlayer("Banned by admin");
            }
        } else
        if(s.toLowerCase().startsWith("pardon "))
        {
            String s7 = s.substring(s.indexOf(" ")).trim();
            serverconfigurationmanager.pardonPlayer(s7);
            sendNoticeToOps(s1, (new StringBuilder()).append("Pardoning ").append(s7).toString());
        } else
        if(s.toLowerCase().startsWith("kick "))
        {
            String s8 = s.substring(s.indexOf(" ")).trim();
            EntityPlayerMP entityplayermp1 = null;
            for(int l = 0; l < serverconfigurationmanager.playerEntities.size(); l++)
            {
                EntityPlayerMP entityplayermp5 = (EntityPlayerMP)serverconfigurationmanager.playerEntities.get(l);
                if(entityplayermp5.username.equalsIgnoreCase(s8))
                {
                    entityplayermp1 = entityplayermp5;
                }
            }

            if(entityplayermp1 != null)
            {
                entityplayermp1.playerNetServerHandler.kickPlayer("Kicked by admin");
                sendNoticeToOps(s1, (new StringBuilder()).append("Kicking ").append(entityplayermp1.username).toString());
            } else
            {
                icommandlistener.log((new StringBuilder()).append("Can't find user ").append(s8).append(". No kick.").toString());
            }
        } else
        if(s.toLowerCase().startsWith("tp "))
        {
            String as[] = s.split(" ");
            if(as.length == 3)
            {
                EntityPlayerMP entityplayermp2 = serverconfigurationmanager.getPlayerEntity(as[1]);
                EntityPlayerMP entityplayermp3 = serverconfigurationmanager.getPlayerEntity(as[2]);
                if(entityplayermp2 == null)
                {
                    icommandlistener.log((new StringBuilder()).append("Can't find user ").append(as[1]).append(". No tp.").toString());
                } else
                if(entityplayermp3 == null)
                {
                    icommandlistener.log((new StringBuilder()).append("Can't find user ").append(as[2]).append(". No tp.").toString());
                } else
                if(entityplayermp2.dimension != entityplayermp3.dimension)
                {
                    icommandlistener.log((new StringBuilder()).append("User ").append(as[1]).append(" and ").append(as[2]).append(" are in different dimensions. No tp.").toString());
                } else
                {
                    entityplayermp2.playerNetServerHandler.teleportTo(entityplayermp3.posX, entityplayermp3.posY, entityplayermp3.posZ, entityplayermp3.rotationYaw, entityplayermp3.rotationPitch);
                    sendNoticeToOps(s1, (new StringBuilder()).append("Teleporting ").append(as[1]).append(" to ").append(as[2]).append(".").toString());
                }
            } else
            {
                icommandlistener.log("Syntax error, please provice a source and a target.");
            }
        } else
        if(s.toLowerCase().startsWith("give "))
        {
            String as1[] = s.split(" ");
            if(as1.length != 3 && as1.length != 4)
            {
                return;
            }
            String s9 = as1[1];
            EntityPlayerMP entityplayermp4 = serverconfigurationmanager.getPlayerEntity(s9);
            if(entityplayermp4 != null)
            {
                try
                {
                    int j1 = Integer.parseInt(as1[2]);
                    if(Item.itemsList[j1] != null)
                    {
                        sendNoticeToOps(s1, (new StringBuilder()).append("Giving ").append(entityplayermp4.username).append(" some ").append(j1).toString());
                        int i2 = 1;
                        if(as1.length > 3)
                        {
                            i2 = tryParse(as1[3], 1);
                        }
                        if(i2 < 1)
                        {
                            i2 = 1;
                        }
                        if(i2 > 64)
                        {
                            i2 = 64;
                        }
                        entityplayermp4.dropPlayerItem(new ItemStack(j1, i2, 0));
                    } else
                    {
                        icommandlistener.log((new StringBuilder()).append("There's no item with id ").append(j1).toString());
                    }
                }
                catch(NumberFormatException numberformatexception1)
                {
                    icommandlistener.log((new StringBuilder()).append("There's no item with id ").append(as1[2]).toString());
                }
            } else
            {
                icommandlistener.log((new StringBuilder()).append("Can't find user ").append(s9).toString());
            }
        } else
        if(s.toLowerCase().startsWith("time "))
        {
            String as2[] = s.split(" ");
            if(as2.length != 3)
            {
                return;
            }
            String s10 = as2[1];
            try
            {
                int i1 = Integer.parseInt(as2[2]);
                if("add".equalsIgnoreCase(s10))
                {
                    for(int k1 = 0; k1 < minecraftServer.worldMngr.length; k1++)
                    {
                        WorldServer worldserver3 = minecraftServer.worldMngr[k1];
                        worldserver3.func_32005_b(worldserver3.getWorldTime() + (long)i1);
                    }

                    sendNoticeToOps(s1, (new StringBuilder()).append("Added ").append(i1).append(" to time").toString());
                } else
                if("set".equalsIgnoreCase(s10))
                {
                    for(int l1 = 0; l1 < minecraftServer.worldMngr.length; l1++)
                    {
                        WorldServer worldserver4 = minecraftServer.worldMngr[l1];
                        worldserver4.func_32005_b(i1);
                    }

                    sendNoticeToOps(s1, (new StringBuilder()).append("Set time to ").append(i1).toString());
                } else
                {
                    icommandlistener.log("Unknown method, use either \"add\" or \"set\"");
                }
            }
            catch(NumberFormatException numberformatexception)
            {
                icommandlistener.log((new StringBuilder()).append("Unable to convert time value, ").append(as2[2]).toString());
            }
        } else
        if(s.toLowerCase().startsWith("say "))
        {
            s = s.substring(s.indexOf(" ")).trim();
            minecraftLogger.info((new StringBuilder()).append("[").append(s1).append("] ").append(s).toString());
            serverconfigurationmanager.sendPacketToAllPlayers(new Packet3Chat((new StringBuilder()).append("\247d[Server] ").append(s).toString()));
        } else
        if(s.toLowerCase().startsWith("tell "))
        {
            String as3[] = s.split(" ");
            if(as3.length >= 3)
            {
                s = s.substring(s.indexOf(" ")).trim();
                s = s.substring(s.indexOf(" ")).trim();
                minecraftLogger.info((new StringBuilder()).append("[").append(s1).append("->").append(as3[1]).append("] ").append(s).toString());
                s = (new StringBuilder()).append("\2477").append(s1).append(" whispers ").append(s).toString();
                minecraftLogger.info(s);
                if(!serverconfigurationmanager.sendPacketToPlayer(as3[1], new Packet3Chat(s)))
                {
                    icommandlistener.log("There's no player by that name online.");
                }
            }
        } else
        if(s.toLowerCase().startsWith("whitelist "))
        {
            handleWhitelist(s1, s, icommandlistener);
        } else
        {
            minecraftLogger.info("Unknown console command. Type \"help\" for help.");
        }
    }

    private void handleWhitelist(String s, String s1, ICommandListener icommandlistener)
    {
        String as[] = s1.split(" ");
        if(as.length < 2)
        {
            return;
        }
        String s2 = as[1].toLowerCase();
        if("on".equals(s2))
        {
            sendNoticeToOps(s, "Turned on white-listing");
            minecraftServer.propertyManagerObj.setProperty("white-list", true);
        } else
        if("off".equals(s2))
        {
            sendNoticeToOps(s, "Turned off white-listing");
            minecraftServer.propertyManagerObj.setProperty("white-list", false);
        } else
        if("list".equals(s2))
        {
            Set set = minecraftServer.configManager.getWhiteListedIPs();
            String s5 = "";
            for(Iterator iterator = set.iterator(); iterator.hasNext();)
            {
                String s6 = (String)iterator.next();
                s5 = (new StringBuilder()).append(s5).append(s6).append(" ").toString();
            }

            icommandlistener.log((new StringBuilder()).append("White-listed players: ").append(s5).toString());
        } else
        if("add".equals(s2) && as.length == 3)
        {
            String s3 = as[2].toLowerCase();
            minecraftServer.configManager.addToWhiteList(s3);
            sendNoticeToOps(s, (new StringBuilder()).append("Added ").append(s3).append(" to white-list").toString());
        } else
        if("remove".equals(s2) && as.length == 3)
        {
            String s4 = as[2].toLowerCase();
            minecraftServer.configManager.removeFromWhiteList(s4);
            sendNoticeToOps(s, (new StringBuilder()).append("Removed ").append(s4).append(" from white-list").toString());
        } else
        if("reload".equals(s2))
        {
            minecraftServer.configManager.reloadWhiteList();
            sendNoticeToOps(s, "Reloaded white-list from file");
        }
    }

    private void printHelp(ICommandListener icommandlistener)
    {
        icommandlistener.log("To run the server without a gui, start it like this:");
        icommandlistener.log("   java -Xmx1024M -Xms1024M -jar minecraft_server.jar nogui");
        icommandlistener.log("Console commands:");
        icommandlistener.log("   help  or  ?               shows this message");
        icommandlistener.log("   kick <player>             removes a player from the server");
        icommandlistener.log("   ban <player>              bans a player from the server");
        icommandlistener.log("   pardon <player>           pardons a banned player so that they can connect again");
        icommandlistener.log("   ban-ip <ip>               bans an IP address from the server");
        icommandlistener.log("   pardon-ip <ip>            pardons a banned IP address so that they can connect again");
        icommandlistener.log("   op <player>               turns a player into an op");
        icommandlistener.log("   deop <player>             removes op status from a player");
        icommandlistener.log("   tp <player1> <player2>    moves one player to the same location as another player");
        icommandlistener.log("   give <player> <id> [num]  gives a player a resource");
        icommandlistener.log("   tell <player> <message>   sends a private message to a player");
        icommandlistener.log("   stop                      gracefully stops the server");
        icommandlistener.log("   save-all                  forces a server-wide level save");
        icommandlistener.log("   save-off                  disables terrain saving (useful for backup scripts)");
        icommandlistener.log("   save-on                   re-enables terrain saving");
        icommandlistener.log("   list                      lists all currently connected players");
        icommandlistener.log("   say <message>             broadcasts a message to all players");
        icommandlistener.log("   time <add|set> <amount>   adds to or sets the world time (0-24000)");
    }

    private void sendNoticeToOps(String s, String s1)
    {
        String s2 = (new StringBuilder()).append(s).append(": ").append(s1).toString();
        minecraftServer.configManager.sendChatMessageToAllOps((new StringBuilder()).append("\2477(").append(s2).append(")").toString());
        minecraftLogger.info(s2);
    }

    private int tryParse(String s, int i)
    {
        try
        {
            return Integer.parseInt(s);
        }
        catch(NumberFormatException numberformatexception)
        {
            return i;
        }
    }

    private static Logger minecraftLogger = Logger.getLogger("Minecraft");
    private MinecraftServer minecraftServer;

}
