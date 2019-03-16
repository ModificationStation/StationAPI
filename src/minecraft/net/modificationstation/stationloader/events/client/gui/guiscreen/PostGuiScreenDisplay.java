package net.modificationstation.stationloader.events.client.gui.guiscreen;

import net.minecraft.src.GuiScreen;
import net.minecraft.src.ScaledResolution;
import net.modificationstation.stationloader.events.common.Event;
import net.modificationstation.stationloader.events.common.InstancedEvent;

/**
 * Event that is called after Minecraft displayed GuiScreen
 * 
 * args: guiscreen (the GuiScreen that is displayed), scaledresolution (guiscreen's resolution), width (guiscreen's width),
 * height (guiscreen's height)
 * return: void
 * 
 * @author mine_diver
 *
 */
public interface PostGuiScreenDisplay {
	public static final Event<PostGuiScreenDisplay> EVENT = new InstancedEvent<>(PostGuiScreenDisplay.class, (listeners) -> 
	(guiscreen, scaledresolution, width, height) -> {
		for (PostGuiScreenDisplay event : listeners) {
			event.postGuiScreenDisplay(guiscreen, scaledresolution, width, height);
		}
	});
	void postGuiScreenDisplay(GuiScreen guiscreen, ScaledResolution scaledresolution, int width, int height);
}
