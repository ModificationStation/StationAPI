package net.modificationstation.sltest.datafixer;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.nbt.NbtCompound;
import net.modificationstation.stationapi.api.event.datafixer.DataFixerRegisterEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.EntrypointManager;
import net.modificationstation.stationapi.api.util.Util;

import java.lang.invoke.MethodHandles;

import static net.modificationstation.stationapi.api.vanillafix.datafixer.schema.StationFlatteningItemStackSchema.putItem;
import static net.modificationstation.stationapi.api.vanillafix.datafixer.schema.StationFlatteningItemStackSchema.putState;

public class DataFixerListener {
    static {
        EntrypointManager.registerLookup(MethodHandles.lookup());
    }

    @EventListener
    private static void registerFixer(DataFixerRegisterEvent event) {
        putState(97, "sltest:test_animated_block");
        putState(98, "sltest:test_block");
        putState(99, "sltest:farlands_block", Util.make(new NbtCompound(), tag -> tag.putString("facing", "north")));
        putState(100, "sltest:freezer");
        putState(101, "sltest:altar");
        putItem(360, "sltest:test_item");
        putItem(361, "sltest:test_pickaxe");
        putItem(362, "sltest:nbt_item");
        putItem(363, "sltest:model_item");
        putItem(364, "sltest:ironOre");
    }
}
