package net.modificationstation.sltest.network;

import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.modificationstation.stationapi.api.network.PayloadHandler;
import net.modificationstation.stationapi.mixin.player.server.ServerPlayNetworkHandlerAccessor;

public class ExamplePayloadHandler implements PayloadHandler {

    private final ServerPlayNetworkHandler handler;

    public ExamplePayloadHandler(ServerPlayNetworkHandler handler) {
        this.handler = handler;
    }

    public void handleExamplePayload(ExamplePayload payload) {
        ((ServerPlayNetworkHandlerAccessor) this.handler).getField_920().inventory.method_671(payload.stack());
    }
}
