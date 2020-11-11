package net.modificationstation.stationloader.api.client.event.gui;

import net.minecraft.entity.player.PlayerBase;
import net.minecraft.inventory.InventoryBase;
import net.modificationstation.stationloader.api.common.event.ModIDEvent;
import net.modificationstation.stationloader.api.common.factory.EventFactory;
import net.modificationstation.stationloader.api.common.packet.CustomData;
import net.modificationstation.stationloader.api.common.registry.ModIDRegistry;
import uk.co.benjiweber.expressions.functions.TriConsumer;

import java.util.HashMap;
import java.util.Map;

public interface GuiRegister {

    ModIDEvent<GuiRegister> EVENT = EventFactory.INSTANCE.newModIDEvent(GuiRegister.class, listeners ->
            modGuis -> {
                Map<String, Map<Short, TriConsumer<PlayerBase, InventoryBase, CustomData>>> guis = ModIDRegistry.gui;
                String modid;
                for (GuiRegister listener : listeners) {
                    modid = GuiRegister.EVENT.getListenerModID(listener);
                    if (!guis.containsKey(modid))
                        guis.put(modid, new HashMap<>());
                    listener.registerGUIs(guis.get(modid));
                }
            });

    void registerGUIs(Map<Short, TriConsumer<PlayerBase, InventoryBase, CustomData>> guis);
}
