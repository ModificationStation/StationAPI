package net.modificationstation.sltest.gui.hud;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.client.event.gui.hud.HudTextLine;
import net.modificationstation.stationapi.api.client.event.gui.hud.HudTextRenderEvent;

import java.util.Collections;
import java.util.Random;

public class TestHudText {
    private final Random random = new Random();

    @EventListener
    public void renderHudText(HudTextRenderEvent event) {
        event.setVersion(new HudTextLine(
                "Statint api  2rd version ;)" + (event.debug ? "(" + event.minecraft.debugText + ")" : ""),
                random.nextInt(0xFFFFFF + 1))
        );
        Collections.addAll(event.left,
                new HudTextLine("yippee :3"),
                new HudTextLine("wahoo")
        );
        Collections.addAll(event.right,
                new HudTextLine("gaming", 0x5BCEFA),
                new HudTextLine("god has forsaken me", 0xF5A9B8,8),
                new HudTextLine("waow :o", HudTextLine.WHITE, 8),
                new HudTextLine("this is not a test", 0xF5A9B8, 8),
                new HudTextLine("ok nvm now it is", 0x5BCEFA, 8)
        );
        if (event.debug) {
            event.left.add(new HudTextLine("This Texts only Shows In de Bugge ?!??! :0"));
            event.right.add(new HudTextLine("this text has a rly big offsets", 0xFF0000, 47));
        }
    }
}