package net.modificationstation.stationapi.impl.worldgen;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.minecraft.class_153;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.world.biome.BiomeRegisterEvent;
import net.modificationstation.stationapi.api.event.worldgen.biome.BiomeProviderRegisterEvent;
import net.modificationstation.stationapi.api.worldgen.BiomeAPI;

public class WorldgenListener {
    private boolean initiated;

    @EventListener(priority = ListenerPriority.LOWEST)
    public void afterInit(BiomeRegisterEvent event) {
        if (initiated) return;
        StationAPI.EVENT_BUS.post(BiomeProviderRegisterEvent.builder().build());
        initiated = true;
    }

    @EventListener(phase = StationAPI.INTERNAL_PHASE)
    public void registerBiomes(BiomeProviderRegisterEvent event) {
        BiomeAPI.addOverworldBiomeProvider(
                StationAPI.NAMESPACE.id("overworld_biome_provider"),
                OverworldBiomeProviderImpl.getInstance()
        );
        BiomeAPI.addNetherBiomeProvider(
                StationAPI.NAMESPACE.id("nether_biome_provider"),
                NetherBiomeProviderImpl.getInstance()
        );
        class_153.field_886.setFogColor(0xFF330707);
    }
}
