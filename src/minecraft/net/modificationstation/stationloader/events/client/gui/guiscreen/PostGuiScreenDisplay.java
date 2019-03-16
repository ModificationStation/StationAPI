package net.modificationstation.stationloader.events.client.gui.guiscreen;

import net.minecraft.src.GuiScreen;
import net.minecraft.src.ScaledResolution;
import net.modificationstation.stationloader.events.common.Event;
import net.modificationstation.stationloader.events.common.InstancedEvent;

public interface PostGuiScreenDisplay {
	public static final Event<PostGuiScreenDisplay> EVENT = new InstancedEvent<>(PostGuiScreenDisplay.class, (listeners) -> 
	(guiscreen, scaledresolution, width, height) -> {
		for (PostGuiScreenDisplay event : listeners) {
			event.postGuiScreenDisplay(guiscreen, scaledresolution, width, height);
		}
	});
	void postGuiScreenDisplay(GuiScreen guiscreen, ScaledResolution scaledresolution, int width, int height);
}
