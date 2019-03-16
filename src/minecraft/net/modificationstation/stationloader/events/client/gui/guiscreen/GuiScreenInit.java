package net.modificationstation.stationloader.events.client.gui.guiscreen;

import net.minecraft.src.GuiScreen;
import net.modificationstation.stationloader.events.common.Event;
import net.modificationstation.stationloader.events.common.InstancedEvent;

public interface GuiScreenInit {
	public static final Event<GuiScreenInit> EVENT = new InstancedEvent<>(GuiScreenInit.class, (listeners) -> 
	(guiscreen) -> {
		boolean pass = true;
		for (GuiScreenInit event : listeners) {
			if(!event.initGuiScreen(guiscreen) && pass)
				pass = false;
		}
		return pass;
	});
	boolean initGuiScreen(GuiScreen guiscreen);
}
