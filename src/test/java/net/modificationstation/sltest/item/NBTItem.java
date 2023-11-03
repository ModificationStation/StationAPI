package net.modificationstation.sltest.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
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
    public boolean useOnBlock(ItemStack item, PlayerEntity player, World level, int x, int y, int z, int facing) {
        NbtCompound nbt = StationNBT.class.cast(item).getStationNBT();
        if (!nbt.contains(of(MODID, "rand_num").toString()))
            nbt.putInt(of(MODID, "rand_num").toString(), new Random().nextInt(3));
        player.method_490("Woah: " + nbt.getInt(of(MODID, "rand_num").toString()));
        return true;
    }

    @Override
    public ItemStack use(ItemStack arg, World arg2, PlayerEntity arg3) {
//        CompoundTag chunkTag = new CompoundTag();
//        CompoundTag levelTag = new CompoundTag();
//        ListTag entities = new ListTag();
//        CompoundTag itemEntity = new CompoundTag();
//        itemEntity.put("id", "Item");
//        CompoundTag itemEntityStack = new CompoundTag();
//        itemEntityStack.put("id", (short) 1);
//        itemEntity.put("Item", itemEntityStack);
//        entities.add(itemEntity);
//        levelTag.put("Entities", entities);
//        chunkTag.put("Level", levelTag);
//        CompoundTag newChunk = NbtHelper.update(TypeReferences.CHUNK, chunkTag);
//        System.out.println(((CompoundTag) newChunk.getCompoundTag("Level").getListTag("Entities").get(0)).getCompoundTag("Item").getString(STATION_ID));
        return super.use(arg, arg2, arg3);
    }
}
