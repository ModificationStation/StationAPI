package net.modificationstation.sltest.datafixer;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.event.datafixer.DataFixerRegisterEvent;
import net.modificationstation.stationapi.api.util.Util;
import net.modificationstation.stationapi.api.vanillafix.datafixer.schema.Schema69420;

public class DataFixerListener {

    public static final Int2ObjectMap<String> ITEM_RENAMES = Util.make(new Int2ObjectOpenHashMap<>(), m -> {
        m.defaultReturnValue("minecraft:air");
        m.put(97, "sltest:test_animated_block");
        m.put(98, "sltest:test_block");
        m.put(99, "sltest:farlands_block");
        m.put(100, "sltest:freezer");
        m.put(101, "sltest:altar");
        m.put(360, "sltest:test_item");
        m.put(361, "sltest:test_pickaxe");
        m.put(362, "sltest:nbt_item");
        m.put(363, "sltest:model_item");
        m.put(364, "sltest:ironOre");
    });

    @EventListener
    private static void registerFixer(DataFixerRegisterEvent event) {
        Schema69420.ITEM_RENAMES.putAll(ITEM_RENAMES);
    }
}
