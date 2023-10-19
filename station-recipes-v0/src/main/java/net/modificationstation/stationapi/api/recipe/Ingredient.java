package net.modificationstation.stationapi.api.recipe;

import com.google.common.collect.Lists;
import com.google.gson.*;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.item.ItemConvertible;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.registry.RegistryEntry;
import net.modificationstation.stationapi.api.tag.TagKey;
import net.modificationstation.stationapi.api.util.JsonHelper;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class Ingredient implements Predicate<ItemInstance>{
    public static final Ingredient EMPTY = new Ingredient(Stream.empty());
    private final Entry[] entries;

    private Ingredient(Stream<? extends Entry> entries) {
        this.entries = entries.toArray(Entry[]::new);
    }

    public ItemInstance getRandom(Random random) {
        return isEmpty() ? null : entries[random.nextInt(entries.length)].getRandom(random);
    }

    @Override
    public boolean test(@Nullable ItemInstance itemStack) {
        if (isEmpty() && itemStack == null) return true;
        if (isEmpty() || itemStack == null) return false;
        for (int i = 0; i < entries.length; i++)
            if (!entries[i].test(itemStack))
                return false;
        return true;
    }

    public boolean isEmpty() {
        return this.entries.length == 0;
    }

    private static Ingredient ofEntries(Stream<? extends Entry> entries) {
        Ingredient ingredient = new Ingredient(entries);
        return ingredient.isEmpty() ? EMPTY : ingredient;
    }

    public static Ingredient empty() {
        return EMPTY;
    }

    public static Ingredient ofItems(ItemConvertible... items) {
        return Ingredient.ofStacks(Arrays.stream(items).map(ItemConvertible::asItem).map(ItemInstance::new));
    }

    public static Ingredient ofStacks(ItemInstance ... stacks) {
        return Ingredient.ofStacks(Arrays.stream(stacks));
    }

    public static Ingredient ofStacks(Stream<ItemInstance> stacks) {
        return Ingredient.ofEntries(stacks.filter(stack -> stack != null && stack.itemId != 0 && stack.count != 0).map(StackEntry::new));
    }

    public static Ingredient fromTag(TagKey<ItemBase> tag) {
        return Ingredient.ofEntries(Stream.of(new TagEntry(tag)));
    }

    public static Ingredient fromJson(@Nullable JsonElement json) {
        return Ingredient.fromJson(json, true);
    }

    public static Ingredient fromJson(@Nullable JsonElement json, boolean allowAir) {
        if (json == null || json.isJsonNull()) throw new JsonSyntaxException("Item cannot be null");
        if (json.isJsonObject())
            return Ingredient.ofEntries(Stream.of(Ingredient.entryFromJson(json.getAsJsonObject())));
        if (json.isJsonArray()) {
            JsonArray jsonArray = json.getAsJsonArray();
            if (jsonArray.size() == 0 && !allowAir)
                throw new JsonSyntaxException("Item array cannot be empty, at least one item must be defined");
            return Ingredient.ofEntries(StreamSupport.stream(jsonArray.spliterator(), false).map(jsonElement -> Ingredient.entryFromJson(JsonHelper.asObject(jsonElement, "item"))));
        }
        throw new JsonSyntaxException("Expected item to be object or array of objects");
    }

    private static Entry entryFromJson(JsonObject json) {
        if (json.has("item") && json.has("tag"))
            throw new JsonParseException("An ingredient entry is either a tag or an item, not both");
        if (json.has("item")) {
            ItemBase item = StationShapedRecipe.getItem(json);
            return new StackEntry(new ItemInstance(item));
        }
        if (json.has("tag")) {
            Identifier identifier = Identifier.of(JsonHelper.getString(json, "tag"));
            TagKey<ItemBase> tagKey = TagKey.of(ItemRegistry.KEY, identifier);
            return new TagEntry(tagKey);
        }
        throw new JsonParseException("An ingredient entry needs either a tag or an item");
    }

    interface Entry extends Predicate<ItemInstance> {
        Collection<ItemInstance> getStacks();

        JsonObject toJson();

        ItemInstance getRandom(Random random);
    }

    static class TagEntry
    implements Entry {
        private final TagKey<ItemBase> tag;

        TagEntry(TagKey<ItemBase> tag) {
            this.tag = tag;
        }

        @Override
        public Collection<ItemInstance> getStacks() {
            ArrayList<ItemInstance> list = Lists.newArrayList();
            for (RegistryEntry<ItemBase> registryEntry : ItemRegistry.INSTANCE.iterateEntries(this.tag))
                list.add(new ItemInstance(registryEntry.value()));
            return list;
        }

        @Override
        public JsonObject toJson() {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("tag", this.tag.id().toString());
            return jsonObject;
        }

        @Override
        public ItemInstance getRandom(Random random) {
            return new ItemInstance(ItemRegistry.INSTANCE.getEntryList(tag).orElseThrow().getRandom(random).orElseThrow().value());
        }

        @Override
        public boolean test(ItemInstance itemInstance) {
            return itemInstance.isIn(tag);
        }
    }

    static class StackEntry
    implements Entry {
        private final ItemInstance stack;

        StackEntry(ItemInstance stack) {
            this.stack = stack;
        }

        @Override
        public Collection<ItemInstance> getStacks() {
            return Collections.singleton(this.stack);
        }

        @Override
        public JsonObject toJson() {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("item", ItemRegistry.INSTANCE.getId(this.stack.getType()).toString());
            return jsonObject;
        }

        @Override
        public ItemInstance getRandom(Random random) {
            return stack;
        }

        @Override
        public boolean test(ItemInstance itemInstance) {
            boolean ignoreDamage = stack.getDamage() == -1;
            if (ignoreDamage) stack.setDamage(itemInstance.getDamage());
            boolean equals = stack.isDamageAndIDIdentical(itemInstance);
            if (ignoreDamage) stack.setDamage(-1);
            return equals;
        }
    }
}

