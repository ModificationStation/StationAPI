package net.modificationstation.stationloader.client.gui;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiMainMenu;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiSmallButton;
import net.minecraft.src.GuiTexturePacks;
import net.modificationstation.classloader.ReflectionHelper;
import net.modificationstation.stationloader.StationLoader;
import net.modificationstation.stationloader.events.client.gui.guiscreen.DrawScreen;
import net.modificationstation.stationloader.events.client.gui.guiscreen.GuiScreenInit;
import net.modificationstation.stationmodloader.StationModLoader;
import net.modificationstation.stationmodloader.util.Mod;

public class GuiHandler implements DrawScreen, GuiScreenInit{
	public GuiHandler() {
		DrawScreen.EVENT.register(this::drawScreen);
		GuiScreenInit.EVENT.register(this::initGuiScreen);
	}

	@Override
	public boolean drawScreen(GuiScreen guiscreen, int x, int y, float partialTicks, String screenType) {
		if (guiscreen instanceof GuiMainMenu && screenType.equals("GuiScreen")) {
			guiscreen.drawString(Minecraft.theMinecraft.fontRenderer, "StationLoader version " + StationLoader.class.getAnnotation(Mod.class).version(), 2, 12, 0x505050);
			guiscreen.drawString(Minecraft.theMinecraft.fontRenderer, "(" + (StationModLoader.loadedMods.size() - 1 > 1 ? (StationModLoader.loadedMods.size() - 1) + " mods are" : StationModLoader.loadedMods.size() - 1 == 1 ? "1 mod is" : "No mods are") + " loaded)", 2, 22, 0x505050);
        }
		return true;
	}

	@Override
	public boolean initGuiScreen(GuiScreen guiscreen) {
		if (guiscreen instanceof GuiTexturePacks) {
            List<GuiSmallButton> controlList = ReflectionHelper.getPrivateValue(GuiScreen.class, guiscreen, new String[] {"e", "controlList"});
            controlList.add(new GuiSmallButton(7, guiscreen.width / 2 + 4, 0, "Texture Packs"));
        }
		return true;
	}
}
