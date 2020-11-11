package net.modificationstation.stationloader.api.client.event.gui;

import net.minecraft.entity.player.PlayerBase;
import net.minecraft.inventory.InventoryBase;
import net.modificationstation.stationloader.api.common.event.ModIDEvent;
import net.modificationstation.stationloader.api.common.factory.EventFactory;
import net.modificationstation.stationloader.api.common.registry.ModIDRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public interface GuiRegister {

    ModIDEvent<GuiRegister> EVENT = EventFactory.INSTANCE.newModIDEvent(GuiRegister.class, listeners ->
            modGuis -> {
                Map<String, Map<Integer, BiConsumer<PlayerBase, InventoryBase>>> guis = ModIDRegistry.gui;
                String modid;
                for (GuiRegister listener : listeners) {
                    modid = GuiRegister.EVENT.getListenerModID(listener);
                    if (!guis.containsKey(modid))
                        guis.put(modid, new HashMap<>());
                    listener.registerGUIs(guis.get(modid));
                }
            });

    void registerGUIs(Map<Integer, BiConsumer<PlayerBase, InventoryBase>> guis);
}
