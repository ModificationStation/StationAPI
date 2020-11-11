package net.modificationstation.stationloader.api.common.registry;

import net.minecraft.entity.player.PlayerBase;
import net.minecraft.inventory.InventoryBase;
import net.modificationstation.stationloader.api.common.packet.CustomData;
import uk.co.benjiweber.expressions.functions.TriConsumer;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public interface ModIDRegistry {

    Map<String, Map<String, Integer>> item = new HashMap<>();

    Map<String, Map<String, BiConsumer<PlayerBase, CustomData>>> packet = new HashMap<>();

    Map<String, Map<Short, TriConsumer<PlayerBase, InventoryBase, CustomData>>> gui = new HashMap<>();
}
