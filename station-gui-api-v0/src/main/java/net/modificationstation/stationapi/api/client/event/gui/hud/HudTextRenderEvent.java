package net.modificationstation.stationapi.api.client.event.gui.hud;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.mine_diver.unsafeevents.event.EventPhases;
import net.minecraft.client.Minecraft;
import net.modificationstation.stationapi.api.StationAPI;

import java.util.ArrayList;
import java.util.List;

/**
 * Fires while rendering in-game (debug) HUD text.
 */
@SuperBuilder
@EventPhases({StationAPI.INTERNAL_PHASE, EventPhases.DEFAULT_PHASE})
public class HudTextRenderEvent extends Event {
    /**
     * Whether the F3 debug HUD is enabled.
     * @see net.minecraft.client.option.GameOptions#debugHud
     */
    public final boolean debug;
    public final Minecraft minecraft;
    public final List<HudTextLine> left = new ArrayList<>(), right = new ArrayList<>();
    @Getter private HudTextLine version;

    /**
     * Set by highest priority listener.
     */
    public void setVersion(HudTextLine line) {
        if (version == null) version = line;
    }
}
