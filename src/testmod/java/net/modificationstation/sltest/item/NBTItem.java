package net.modificationstation.sltest.item;

import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.minecraft.util.io.CompoundTag;
import net.modificationstation.sltest.item.entity.NBTItemEntity;
import net.modificationstation.stationapi.api.common.item.HasItemEntity;
import net.modificationstation.stationapi.api.common.item.ItemEntity;
import net.modificationstation.stationapi.api.common.item.ItemWithEntity;
import net.modificationstation.stationapi.api.common.registry.Identifier;
import net.modificationstation.stationapi.template.common.item.TemplateItemBase;

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
