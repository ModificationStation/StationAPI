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
 * Fires while rendering the game HUD.
 */
@SuperBuilder
@EventPhases({StationAPI.INTERNAL_PHASE, EventPhases.DEFAULT_PHASE})
public class HudTextRenderEvent extends Event {
    public final List<HudTextLine> left = new ArrayList<>(), right = new ArrayList<>();
    @Getter private HudTextLine version;
    public final boolean debug;
    public final Minecraft minecraft;

    /**
     * Set by highest priority listener.
     */
    public void setVersion(HudTextLine line) {
        if (version == null) version = line;
    }
}
