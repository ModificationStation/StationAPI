package net.modificationstation.sltest.item;

import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.minecraft.util.io.CompoundTag;
import net.modificationstation.sltest.item.entity.NBTItemEntity;
import net.modificationstation.stationapi.api.item.nbt.HasItemEntity;
import net.modificationstation.stationapi.api.item.nbt.ItemEntity;
import net.modificationstation.stationapi.api.item.nbt.ItemWithEntity;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.item.TemplateItemBase;

import java.util.function.*;

public class NBTItem extends TemplateItemBase implements ItemWithEntity {

    protected NBTItem(Identifier id) {
        super(id);
    }

    @Override
    public Supplier<ItemEntity> getItemEntityFactory() {
        return NBTItemEntity::new;
    }

    @Override
    public Function<CompoundTag, ItemEntity> getItemEntityNBTFactory() {
        return NBTItemEntity::new;
    }

    @Override
    public boolean useOnTile(ItemInstance item, PlayerBase player, Level level, int x, int y, int z, int facing) {
        player.sendMessage("Woah: " + ((NBTItemEntity) HasItemEntity.cast(item).getItemEntity()).getDisplayNumber());
        return true;
    }
}
