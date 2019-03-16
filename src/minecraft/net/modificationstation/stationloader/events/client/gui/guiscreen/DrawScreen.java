package net.modificationstation.stationloader.events.client.gui.guiscreen;
import net.minecraft.src.GuiScreen;
import net.modificationstation.stationloader.events.common.Event;
import net.modificationstation.stationloader.events.common.InstancedEvent;

/**
 * Event that is called when EntityRenderer executes current GuiScreen's drawScreen that does render
 * 
 * args: guiscreen (the GuiScreen that is about to be rendered), x (mouse's X coord), y (mouse's Y coord),
 * partialTicks (afaik, it's current ticks that are used for making animated stuff), screenType (the name of the class that event
 * is called from. Pretty much is used to identify super class GuiScreen call, because guiscreen arg will always return
 * subclass's name)
 * return: boolean (if false, Minecraft won't render this guiscreen)
 * 
 * @author mine_diver
 *
 */
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
