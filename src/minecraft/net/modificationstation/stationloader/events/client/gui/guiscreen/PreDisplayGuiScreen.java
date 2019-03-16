package net.modificationstation.stationloader.events.client.gui.guiscreen;

import net.minecraft.src.GuiScreen;
import net.modificationstation.stationloader.events.common.Event;
import net.modificationstation.stationloader.events.common.InstancedEvent;

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
