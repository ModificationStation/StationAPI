package net.logan.texturepacksingame.events;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiIngameMenu;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiTexturePacks;
import net.modificationstation.classloader.ReflectionHelper;
import net.modificationstation.stationloader.events.client.gui.guiscreen.ActionPerformed;
import net.modificationstation.stationloader.events.client.gui.guiscreen.PostGuiScreenInit;

public class GuiHandler implements PostGuiScreenInit, ActionPerformed{
	public GuiHandler() {
		PostGuiScreenInit.EVENT.register(this::postInitGuiScreen);
		ActionPerformed.EVENT.register(this::actionPerformed);
	}

	@Override
	public void postInitGuiScreen(GuiScreen guiscreen) {
		if (guiscreen instanceof GuiIngameMenu) {
            List controlList = ReflectionHelper.getPrivateValue(GuiScreen.class, guiscreen, new String[] {"e", "controlList"});
            controlList.add(new GuiButton(3, guiscreen.width / 2 - 100, guiscreen.height / 4 + 72 + ((byte)-16), "Mods and Texture Packs"));
        }
	}
	
	@Override
	public boolean actionPerformed(GuiScreen guiscreen, GuiButton button) {
		if (guiscreen instanceof GuiIngameMenu && button.id == 3) {
			Minecraft.theMinecraft.displayGuiScreen(new GuiTexturePacks(guiscreen));
			return false;
		} else
			return true;
	}

	
}
