package net.modificationstation.stationapi.api.client.event.texture.plugin;

import net.mine_diver.unsafeevents.Event;
import net.modificationstation.stationapi.api.client.texture.plugin.RenderPlugin;

import java.util.function.*;

public class ProvideRenderPluginEvent extends Event {

    public Supplier<RenderPlugin> pluginProvider = RenderPlugin::new;

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
