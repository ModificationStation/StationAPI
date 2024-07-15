package net.modificationstation.stationapi.api.client.event.gui.hud;

import lombok.AllArgsConstructor;

/**
 * Text and info for rendering custom HUD text lines.
 */
@AllArgsConstructor
public final class HudTextLine {
    public static final int WHITE = 0xFFFFFF;
    public static final int GRAY = 0xE0E0E0;

    public String text;
    public int color;
    public int offset;

    public HudTextLine(String text) {
        this(text, WHITE);
    }
    public HudTextLine(String text, int color) {
        this(text, color, 10);
    }
}
