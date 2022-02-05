package org.lwjgl.input;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.modificationstation.stationapi.api.client.WindowProvider;
import org.lwjgl.glfw.GLFW;

public class Keyboard {

    private static int LATEST_PRESSED_KEY;

    public static void create() {
        //noinspection deprecation
        GLFW.glfwSetCharCallback(((WindowProvider) FabricLoader.getInstance().getGameInstance()).getWindow(), (window, codepoint) -> {

        });
    }

    public static int getEventKey() {
        return 0;
    }

    public static boolean getEventKeyState() {
        return false;
    }
}
