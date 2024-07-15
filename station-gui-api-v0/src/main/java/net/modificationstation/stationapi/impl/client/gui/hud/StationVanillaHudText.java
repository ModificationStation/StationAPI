package net.modificationstation.stationapi.impl.client.gui.hud;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.minecraft.util.math.MathHelper;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.event.gui.hud.HudTextRenderEvent;
import net.modificationstation.stationapi.api.client.event.gui.hud.HudTextLine;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;

import java.util.Collections;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class StationVanillaHudText {
    @EventListener(phase = StationAPI.INTERNAL_PHASE, priority = ListenerPriority.HIGHEST)
    private static void renderVanillaDebugText(HudTextRenderEvent event) {
        if (!event.debug) return;
        Collections.addAll(event.left,
                new HudTextLine(event.minecraft.method_2141()),
                new HudTextLine(event.minecraft.method_2142()),
                new HudTextLine(event.minecraft.method_2144()),
                new HudTextLine(event.minecraft.method_2143()),
                new HudTextLine("x: " + event.minecraft.player.x, 14737632, 22),
                new HudTextLine("y: " + event.minecraft.player.y, 14737632, 8),
                new HudTextLine("z: " + event.minecraft.player.y, 14737632, 8),
                new HudTextLine("f: " + (MathHelper.floor((double)(event.minecraft.player.yaw * 4.0F / 360.0F) + 0.5) & 3), 14737632, 8)
        );
        long max = Runtime.getRuntime().maxMemory();
        long total = Runtime.getRuntime().totalMemory();
        long free = Runtime.getRuntime().freeMemory();
        long used = total - free;
        Collections.addAll(event.right,
                new HudTextLine("Used memory: " + used * 100L / max + "% (" + used / 1024L / 1024L + "MB) of " + max / 1024L / 1024L + "MB", 14737632),
                new HudTextLine("Allocated memory: " + total * 100L / max + "% (" + total / 1024L / 1024L + "MB)", 14737632)
        );
    }

    @EventListener(priority = ListenerPriority.LOWEST)
    private static void renderVanillaVersionText(HudTextRenderEvent event) {
        if (!event.debug) return;
        event.setVersion(new HudTextLine("Minecraft Beta 1.7.3 (" + event.minecraft.debugText + ")"));
    }
}