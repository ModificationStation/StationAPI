package net.rek.oldlogo.events;

import net.minecraft.src.GuiScreen;
import net.modificationstation.stationloader.events.client.gui.guiscreen.PreDisplayGuiScreen;
import net.rek.oldlogo.gui.GuiMainMenu;

public class GuiHandler implements PreDisplayGuiScreen {
	public GuiHandler() {
		PreDisplayGuiScreen.EVENT.register(this::preDisplayGuiScreen);
	}
	@Override
	public GuiScreen preDisplayGuiScreen(GuiScreen guiscreen) {
		return guiscreen instanceof net.minecraft.src.GuiMainMenu ? new GuiMainMenu() : guiscreen;
	}
	
}
