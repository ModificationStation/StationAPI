package net.modificationstation.stationloader.events.client.gui.guiscreen;

import net.minecraft.src.GuiScreen;
import net.modificationstation.stationloader.events.common.Event;
import net.modificationstation.stationloader.events.common.InstancedEvent;

/**
 * Event that is called after GuiScreen's init (some GuiScreens clear buttons on init, so everything added on GuiScreenInit event
 * can be removed, but on this event it can't be)
 * 
 * args: guiscreen (the GuiScreen that is initialized)
 * return: void
 * 
 * @author mine_diver
 *
 */
public interface PostGuiScreenInit {
	public static final Event<PostGuiScreenInit> EVENT = new InstancedEvent<>(PostGuiScreenInit.class, (listeners) -> 
	(guiscreen) -> {
		for (PostGuiScreenInit event : listeners) {
			event.postInitGuiScreen(guiscreen);
		}
	});
	void postInitGuiScreen(GuiScreen guiscreen);
 }
