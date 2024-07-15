package net.modificationstation.sltest.gui.hud;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.client.event.gui.hud.HudTextLine;
import net.modificationstation.stationapi.api.client.event.gui.hud.HudTextRenderEvent;

import java.util.Random;

public class TestHudText {
    private static final int[] COLORS = {0x5BCEFA, 0xF5A9B8, 0xFFFFFF, 0xF5A9B8};
    private final Random random = new Random();
    private int frames;
    private int color;
    private int verColor;

    @EventListener
    public void renderHudText(HudTextRenderEvent event) {
        if (frames % 7 == 0) verColor = random.nextInt(0xFFFFFF + 1);
        event.setVersion(new HudTextLine(
                "Statint api  2rd ediction ;)" + (event.debug ? "(" + event.minecraft.debugText + ")" : ""),
                verColor)
        );
        if (frames > 0 && frames % 15 == 0) {
            color = color + 1 < COLORS.length? color + 1 : 0;
        }
        event.right.add(new HudTextLine("gaming", COLORS[color]));
        if (event.debug) {
            event.left.add(new HudTextLine("This Texts only Shows In de Bugge ?!??! :0"));
            event.right.add(new HudTextLine("this text has a rly big offsets", 0xFF0000, 40));
        }
        frames++;
    }
}
