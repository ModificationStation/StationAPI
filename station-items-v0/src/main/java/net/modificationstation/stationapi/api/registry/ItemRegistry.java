package net.modificationstation.stationapi.api.registry;

import net.minecraft.achievement.AchievementGUID;
import net.minecraft.client.StatEntity;
import net.minecraft.item.ItemBase;
import net.minecraft.stat.Stats;
import net.modificationstation.stationapi.mixin.block.AchievementGUIDAccessor;
import net.modificationstation.stationapi.mixin.block.StatAccessor;
import net.modificationstation.stationapi.mixin.block.StatEntityAccessor;
import net.modificationstation.stationapi.mixin.block.StatsAccessor;
import net.modificationstation.stationapi.mixin.item.ItemBaseAccessor;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static net.modificationstation.stationapi.api.StationAPI.MODID;

public final class ItemRegistry extends LevelSerialRegistry<ItemBase> {

    public static final ItemRegistry INSTANCE = new ItemRegistry(Identifier.of(MODID, "items"));

    private ItemRegistry(@NotNull Identifier identifier) {
        super(identifier, true);
    }

    @Override
    protected void remap(int newSerialID, @NotNull ItemBase value) {
        boolean craftedGUIDPresent = ((AchievementGUIDAccessor) AchievementGUID.INSTANCE).getGUIDByID().containsKey(16842752 + value.id);
        String craftedGUID = null;
        if (craftedGUIDPresent)
            craftedGUID = ((AchievementGUIDAccessor) AchievementGUID.INSTANCE).getGUIDByID().get(16842752 + value.id);
        boolean craftedMapPresent = StatsAccessor.getIdMap().containsKey(16842752 + value.id);
        StatEntity craftedMap = null;
        if (craftedMapPresent)
            craftedMap = (StatEntity) StatsAccessor.getIdMap().get(16842752 + value.id);
        boolean craftedArrayPresent = Stats.timesCrafted[value.id] != null;
        StatEntity craftedArray = null;
        if (craftedArrayPresent)
            craftedArray = (StatEntity) Stats.timesCrafted[value.id];
        boolean usedGUIDPresent = ((AchievementGUIDAccessor) AchievementGUID.INSTANCE).getGUIDByID().containsKey(16908288 + value.id);
        String usedGUID = null;
        if (usedGUIDPresent)
            usedGUID = ((AchievementGUIDAccessor) AchievementGUID.INSTANCE).getGUIDByID().get(16908288 + value.id);
        boolean usedMapPresent = StatsAccessor.getIdMap().containsKey(16908288 + value.id);
        StatEntity usedMap = null;
        if (usedMapPresent)
            usedMap = (StatEntity) StatsAccessor.getIdMap().get(16908288 + value.id);
        boolean usedArrayPresent = Stats.useItem[value.id] != null;
        StatEntity usedArray = null;
        if (usedArrayPresent)
            usedArray = (StatEntity) Stats.useItem[value.id];
        boolean brokenGUIDPresent = ((AchievementGUIDAccessor) AchievementGUID.INSTANCE).getGUIDByID().containsKey(16973824 + value.id);
        String brokenGUID = null;
        if (brokenGUIDPresent)
            brokenGUID = ((AchievementGUIDAccessor) AchievementGUID.INSTANCE).getGUIDByID().get(16973824 + value.id);
        boolean brokenMapPresent = StatsAccessor.getIdMap().containsKey(16973824 + value.id);
        StatEntity brokenMap = null;
        if (brokenMapPresent)
            brokenMap = (StatEntity) StatsAccessor.getIdMap().get(16973824 + value.id);
        boolean brokenArrayPresent = Stats.breakItem[value.id] != null;
        StatEntity brokenArray = null;
        if (brokenArrayPresent)
            brokenArray = (StatEntity) Stats.breakItem[value.id];

        ItemBase.byId[value.id] = null;
        if (craftedGUIDPresent)
            ((AchievementGUIDAccessor) AchievementGUID.INSTANCE).getGUIDByID().remove(16842752 + value.id);
        if (craftedMapPresent)
            StatsAccessor.getIdMap().remove(16842752 + value.id);
        if (craftedArrayPresent)
            Stats.timesCrafted[value.id] = null;
        if (usedGUIDPresent)
            ((AchievementGUIDAccessor) AchievementGUID.INSTANCE).getGUIDByID().remove(16908288 + value.id);
        if (usedMapPresent)
            StatsAccessor.getIdMap().remove(16908288 + value.id);
        if (usedArrayPresent)
            Stats.useItem[value.id] = null;
        if (brokenGUIDPresent)
            ((AchievementGUIDAccessor) AchievementGUID.INSTANCE).getGUIDByID().remove(16973824 + value.id);
        if (brokenMapPresent)
            StatsAccessor.getIdMap().remove(16973824 + value.id);
        if (brokenArrayPresent)
            Stats.breakItem[value.id] = null;

        if (ItemBase.byId[newSerialID] != null)
            remap(getNextSerialID(), ItemBase.byId[newSerialID]);

        ((ItemBaseAccessor) value).setId(newSerialID);
        if (craftedMapPresent) {
            ((StatAccessor) craftedMap).setID(16842752 + newSerialID);
            ((StatEntityAccessor) craftedMap).setValue(newSerialID);
        }
        if (craftedArrayPresent) {
            ((StatAccessor) craftedArray).setID(16842752 + newSerialID);
            ((StatEntityAccessor) craftedArray).setValue(newSerialID);
        }
        if (usedMapPresent) {
            ((StatAccessor) usedMap).setID(16908288 + newSerialID);
            ((StatEntityAccessor) usedMap).setValue(newSerialID);
        }
        if (usedArrayPresent) {
            ((StatAccessor) usedArray).setID(16908288 + newSerialID);
            ((StatEntityAccessor) usedArray).setValue(newSerialID);
        }
        if (brokenMapPresent) {
            ((StatAccessor) brokenMap).setID(16973824 + newSerialID);
            ((StatEntityAccessor) brokenMap).setValue(newSerialID);
        }
        if (brokenArrayPresent) {
            ((StatAccessor) brokenArray).setID(16973824 + newSerialID);
            ((StatEntityAccessor) brokenArray).setValue(newSerialID);
        }

        ItemBase.byId[newSerialID] = value;
        if (craftedGUIDPresent)
            ((AchievementGUIDAccessor) AchievementGUID.INSTANCE).getGUIDByID().put(16842752 + newSerialID, craftedGUID);
        if (craftedMapPresent)
            StatsAccessor.getIdMap().put(16842752 + newSerialID, craftedMap);
        if (craftedArrayPresent)
            Stats.timesCrafted[newSerialID] = craftedArray;
        if (usedGUIDPresent)
            ((AchievementGUIDAccessor) AchievementGUID.INSTANCE).getGUIDByID().put(16908288 + newSerialID, usedGUID);
        if (usedMapPresent)
            StatsAccessor.getIdMap().put(16908288 + newSerialID, usedMap);
        if (usedArrayPresent)
            Stats.useItem[newSerialID] = usedArray;
        if (brokenGUIDPresent)
            ((AchievementGUIDAccessor) AchievementGUID.INSTANCE).getGUIDByID().put(16973824 + newSerialID, brokenGUID);
        if (brokenMapPresent)
            StatsAccessor.getIdMap().put(16973824 + newSerialID, brokenMap);
        if (brokenArrayPresent)
            Stats.breakItem[newSerialID] = brokenArray;
    }

    @Override
    public int getSize() {
        return ItemBase.byId.length;
    }

    @Override
    public int getSerialID(@NotNull ItemBase value) {
        return value.id;
    }

    @Override
    public @NotNull Optional<ItemBase> get(int serialID) {
        try {
            return Optional.ofNullable(ItemBase.byId[serialID]);
        } catch (ArrayIndexOutOfBoundsException e) {
            return Optional.empty();
        }
    }

    @Override
    public int getSerialIDShift() {
        return BlockRegistry.INSTANCE.getSize();
    }

    @Override
    public @NotNull Optional<ItemBase> get(@NotNull Identifier identifier) {
        Optional<ItemBase> item = super.get(identifier);
        if (item.isPresent())
            return item;
        else {
            OptionalInt serialID = BlockRegistry.INSTANCE.getSerialID(identifier);
            return serialID.isPresent() ? get(serialID.getAsInt()) : Optional.empty();
        }
    }

    @Override
    public @NotNull Identifier getIdentifier(@NotNull ItemBase value) {
        Identifier identifier = super.getIdentifier(value);
        //noinspection ConstantConditions
        return identifier == null ? BlockRegistry.INSTANCE.get(getSerialID(value)).map(BlockRegistry.INSTANCE::getIdentifier).orElse(null) : identifier;
    }
}
