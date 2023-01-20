package net.modificationstation.stationapi.impl.client.render;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.event.resource.ClientResourceReloaderRegisterEvent;
import net.modificationstation.stationapi.api.client.event.resource.ClientResourcesReloadEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.resource.FakeResourceManager;
import net.modificationstation.stationapi.api.resource.ResourceHelper;
import net.modificationstation.stationapi.api.util.Null;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public final class StationRendererImpl {

    @Entrypoint.Logger("StationRenderer|Impl")
    public static final Logger LOGGER = Null.get();

    @EventListener(numPriority = Integer.MAX_VALUE / 2 + Integer.MAX_VALUE / 4)
    private static void resourceReload(ClientResourcesReloadEvent event) {

    }

    @EventListener(numPriority = Integer.MAX_VALUE / 2 + Integer.MAX_VALUE / 4)
    private static void registerResourceReloaders(ClientResourceReloaderRegisterEvent event) {
        FakeResourceManager.registerProvider(path -> {
            try {
                Identifier identifier = ResourceHelper.ASSETS.toId(path, StationAPI.MODID + "/textures", "png");
                if (TERRAIN.slicedSpritesheetView.containsKey(identifier)) {
                    BufferedImage image = TERRAIN.slicedSpritesheetView.get(identifier);
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    try {
                        ImageIO.write(image, "png", outputStream);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    return new ByteArrayInputStream(outputStream.toByteArray());
                }
            } catch (IllegalArgumentException ignored) {}
            return null;
        });
        FakeResourceManager.registerProvider(path -> {
            try {
                Identifier identifier = ResourceHelper.ASSETS.toId(path, StationAPI.MODID + "/textures", "png");
                if (GUI_ITEMS.slicedSpritesheetView.containsKey(identifier)) {
                    BufferedImage image = GUI_ITEMS.slicedSpritesheetView.get(identifier);
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    try {
                        ImageIO.write(image, "png", outputStream);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    return new ByteArrayInputStream(outputStream.toByteArray());
                }
            } catch (IllegalArgumentException ignored) {}
            return null;
        });
    }
}
