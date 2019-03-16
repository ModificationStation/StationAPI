package net.modificationstation.stationloader.events.client.gui.guiscreen;

import net.minecraft.src.GuiScreen;
import net.modificationstation.stationloader.events.common.Event;
import net.modificationstation.stationloader.events.common.InstancedEvent;

/**
 * Event that is called when Minecraft is about to display another GuiScreen
 * 
 * args: guiscreen (the GuiScreen that is about to be displayed)
 * return: GuiScreen (you can return another GuiScreen and it'll displayed instead of given one)
 * 
 * @author mine_diver
 *
 */
public interface PreDisplayGuiScreen {
	public static final Event<PreDisplayGuiScreen> EVENT = new InstancedEvent<>(PreDisplayGuiScreen.class, (listeners) -> 
	(guiscreen) -> {
		GuiScreen finalGuiScreen = guiscreen;
		for (PreDisplayGuiScreen event : listeners) {
			finalGuiScreen = event.preDisplayGuiScreen(finalGuiScreen);
		}
		return finalGuiScreen;
	});
	GuiScreen preDisplayGuiScreen(GuiScreen guiscreen);
}
