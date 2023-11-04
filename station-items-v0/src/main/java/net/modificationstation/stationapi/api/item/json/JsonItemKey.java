package net.modificationstation.stationapi.api.item.json;

import com.mojang.datafixers.util.Either;
import lombok.Data;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.tag.TagKey;

import static net.modificationstation.stationapi.api.util.Identifier.of;

@Data
public class JsonItemKey {

    private String item;
    private int count = 1;
    private int damage = 0;
    private String tag;

    public ItemStack getItemInstance() {
        Item itemBase = ItemRegistry.INSTANCE.get(of(item));
        return itemBase == null ? null : new ItemStack(itemBase, count, damage);
    }

    public TagKey<Item> getTag() {
        return TagKey.of(ItemRegistry.KEY, of(tag));
    }

    public Either<ItemStack, TagKey<Item>> get() {
        if (item == null && tag != null)
            return Either.right(getTag());
        else if (item != null && tag == null)
            return Either.left(getItemInstance());
        else throw new IllegalStateException("Neither item nor tag, or both are specified in the JsonItemKey!");
    }
}