package net.modificationstation.sltest.item;

import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationapi.api.item.nbt.StationNBT;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.item.TemplateItemBase;

import java.util.Random;

import static net.modificationstation.sltest.SLTest.MODID;
import static net.modificationstation.stationapi.api.registry.Identifier.of;

public class NBTItem extends TemplateItemBase {

    protected NBTItem(Identifier id) {
        super(id);
    }

    @Override
    public boolean useOnTile(ItemInstance item, PlayerBase player, Level level, int x, int y, int z, int facing) {
        CompoundTag nbt = StationNBT.cast(item).getStationNBT();
        if (!nbt.containsKey(of(MODID, "rand_num").toString()))
            nbt.put(of(MODID, "rand_num").toString(), new Random().nextInt(3));
        player.sendMessage("Woah: " + nbt.getInt(of(MODID, "rand_num").toString()));
        return true;
    }
}
