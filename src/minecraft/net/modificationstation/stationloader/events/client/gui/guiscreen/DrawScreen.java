package net.modificationstation.stationloader.events.client.gui.guiscreen;
import net.minecraft.src.GuiScreen;
import net.modificationstation.stationloader.events.common.Event;
import net.modificationstation.stationloader.events.common.InstancedEvent;

public interface DrawScreen {
	public static final Event<DrawScreen> EVENT = new InstancedEvent<>(DrawScreen.class, (listeners) -> 
	(guiscreen, x, y, partialTicks, screenType) -> {
		boolean pass = true;
		for (DrawScreen event : listeners) {
			if (!event.drawScreen(guiscreen, x, y, partialTicks, screenType) && pass)
				pass = false;
		}
		return pass;
	});
	boolean drawScreen(GuiScreen guiscreen, int x, int y, float partialTicks, String screenType);
}
