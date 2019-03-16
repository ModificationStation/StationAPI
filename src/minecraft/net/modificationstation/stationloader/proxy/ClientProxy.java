package net.modificationstation.stationloader.proxy;
import net.minecraft.client.Minecraft;
import net.modificationstation.stationloader.client.gui.GuiHandler;
import net.modificationstation.stationloader.client.proxy.EntityRenderer;
import net.modificationstation.stationmodloader.events.MCPreInitializationEvent;

/**
 * ClientProxy for StationLoader that registers all client-side-only stuff, like GuiHandler to customize GuiMainMenu
 * 
 * @author mine_diver
 *
 */
public class ClientProxy extends CommonProxy{
	@Override
	public void preInit(MCPreInitializationEvent event) {
		new GuiHandler();
		super.preInit(event);
	}
	
	@Override
	public void postInit() {
		Minecraft.theMinecraft.entityRenderer = new EntityRenderer(Minecraft.theMinecraft);
		super.postInit();
	}
}
