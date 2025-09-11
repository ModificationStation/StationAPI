package net.modificationstation.sltest.network;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.modificationstation.stationapi.api.event.network.payload.PayloadHandlerRegisterEvent;

public class NetworkListener {
    @EventListener
    public void registerHandler(PayloadHandlerRegisterEvent event) {
        if (event.networkHandler instanceof ServerPlayNetworkHandler handler)
            event.register(ExamplePayload.TYPE, new ExamplePayloadHandler(handler));
    }
}
