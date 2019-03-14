package net.mine_diver.testmod.events;

import net.minecraft.src.GuiScreen;
import net.modificationstation.stationloader.events.client.gui.guiscreen.DrawScreen;

public class EventHandler implements DrawScreen{
	public boolean drawScreen(GuiScreen guiscreen, int x, int y, float partialTicks, String screenType) {
		return true;
	}
}
