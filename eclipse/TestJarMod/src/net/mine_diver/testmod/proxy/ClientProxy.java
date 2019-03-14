package net.mine_diver.testmod.proxy;

import net.mine_diver.testmod.TestMod;
import net.mine_diver.testmod.events.EventHandler;
import net.modificationstation.stationloader.events.client.gui.guiscreen.DrawScreen;

public class ClientProxy extends CommonProxy{
    @Override
    public void onInit() {
    	DrawScreen.EVENT.register(eventHandler::drawScreen);
        TestMod.INSTANCE.LOGGER.info("ClientProxy initialiazed!");
        super.onInit();
    }
    public static final EventHandler eventHandler = new EventHandler();
}
