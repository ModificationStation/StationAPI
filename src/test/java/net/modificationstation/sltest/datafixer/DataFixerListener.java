package net.modificationstation.sltest.datafixer;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationapi.api.event.datafixer.DataFixerRegisterEvent;
import net.modificationstation.stationapi.api.util.Util;
import net.modificationstation.stationapi.api.vanillafix.datafixer.schema.Schema69420;

import static net.modificationstation.stationapi.api.vanillafix.datafixer.schema.Schema69420.putState;

public class DataFixerListener {

    public static final Int2ObjectMap<String> ITEM_RENAMES = Util.make(new Int2ObjectOpenHashMap<>(), m -> {
        m.put(360, "sltest:test_item");
        m.put(361, "sltest:test_pickaxe");
        m.put(362, "sltest:nbt_item");
        m.put(363, "sltest:model_item");
        m.put(364, "sltest:ironOre");
    });

    @EventListener
    private static void registerFixer(DataFixerRegisterEvent event) {
        putState(97, "sltest:test_animated_block");
        putState(98, "sltest:test_block");
        putState(99, "sltest:farlands_block", Util.make(new CompoundTag(), tag -> tag.put("facing", "north")));
        putState(100, "sltest:freezer");
        putState(101, "sltest:altar");
        Schema69420.ITEM_RENAMES.putAll(ITEM_RENAMES);
    }
}
