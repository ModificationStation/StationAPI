// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.logging.*;

// Referenced classes of package net.minecraft.src:
//            ConsoleLogFormatter

public class ConsoleLogManager
{

    public ConsoleLogManager()
    {
    }

    public static void init()
    {
        ConsoleLogFormatter consolelogformatter = new ConsoleLogFormatter();
        logger.setUseParentHandlers(false);
        ConsoleHandler consolehandler = new ConsoleHandler();
        consolehandler.setFormatter(consolelogformatter);
        logger.addHandler(consolehandler);
        try
        {
            FileHandler filehandler = new FileHandler("server.log", true);
            filehandler.setFormatter(consolelogformatter);
            logger.addHandler(filehandler);
        }
        catch(Exception exception)
        {
            logger.log(Level.WARNING, "Failed to log to server.log", exception);
        }
    }

    public static Logger logger = Logger.getLogger("Minecraft");

}
