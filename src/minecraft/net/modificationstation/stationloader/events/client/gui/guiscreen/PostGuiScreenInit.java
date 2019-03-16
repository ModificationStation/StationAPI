package net.modificationstation.stationloader.events.client.gui.guiscreen;

import net.minecraft.src.GuiScreen;
import net.modificationstation.stationloader.events.common.Event;
import net.modificationstation.stationloader.events.common.InstancedEvent;

public interface PostGuiScreenInit {
	public static final Event<PostGuiScreenInit> EVENT = new InstancedEvent<>(PostGuiScreenInit.class, (listeners) -> 
	(guiscreen) -> {
		for (PostGuiScreenInit event : listeners) {
			event.postInitGuiScreen(guiscreen);
		}
	});
	void postInitGuiScreen(GuiScreen guiscreen);
 }
