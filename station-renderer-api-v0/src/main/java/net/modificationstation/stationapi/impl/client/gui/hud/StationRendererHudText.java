package net.modificationstation.stationapi.impl.client.gui.hud;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.event.gui.hud.HudTextRenderEvent;
import net.modificationstation.stationapi.api.client.event.gui.hud.HudTextLine;
import net.modificationstation.stationapi.api.client.render.RendererAccess;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;

import java.util.Objects;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class StationRendererHudText {
    @EventListener(phase = StationAPI.INTERNAL_PHASE)
    private static void renderHudText(HudTextRenderEvent event) {
        if (!event.debug) return;
        event.left.add(new HudTextLine("Active renderer: " + (RendererAccess.INSTANCE.hasRenderer() ?
                Objects.requireNonNull(RendererAccess.INSTANCE.getRenderer()).getClass().getSimpleName() : "none (vanilla)")));
    }
}