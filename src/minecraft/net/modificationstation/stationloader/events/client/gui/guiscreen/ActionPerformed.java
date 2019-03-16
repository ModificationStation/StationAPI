package net.modificationstation.stationloader.events.client.gui.guiscreen;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.modificationstation.stationloader.events.common.Event;
import net.modificationstation.stationloader.events.common.InstancedEvent;

/**
 * Event that is called when button is pressed in GuiScreen
 * 
 * args: guiscreen (the GuiScreen which button is pressed), button (the GuiButton that is pressed)
 * return: boolean (if false, Minecraft won't execute actionPerformed of this GuiScreen)
 * 
 * @author mine_diver
 *
 */
public interface ActionPerformed {
	public static final Event<ActionPerformed> EVENT = new InstancedEvent<>(ActionPerformed.class, (listeners) -> 
	(guiscreen, button) -> {
		boolean pass = true;
		for (ActionPerformed event : listeners) {
			if (!event.actionPerformed(guiscreen, button) && pass)
				pass = false;
		}
		return pass;
	});
	boolean actionPerformed(GuiScreen guiscreen, GuiButton button);
}
