package net.modificationstation.stationapi.api.registry;

import net.minecraft.achievement.AchievementGUID;
import net.minecraft.block.BlockBase;
import net.minecraft.client.StatEntity;
import net.minecraft.item.ItemBase;
import net.minecraft.item.SecondaryBlock;
import net.minecraft.stat.Stats;
import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationapi.mixin.block.AchievementGUIDAccessor;
import net.modificationstation.stationapi.mixin.block.BlockAccessor;
import net.modificationstation.stationapi.mixin.block.BlockBaseAccessor;
import net.modificationstation.stationapi.mixin.block.FireAccessor;
import net.modificationstation.stationapi.mixin.block.ItemBaseAccessor;
import net.modificationstation.stationapi.mixin.block.SecondaryBlockAccessor;
import net.modificationstation.stationapi.mixin.block.StatAccessor;
import net.modificationstation.stationapi.mixin.block.StatEntityAccessor;
import net.modificationstation.stationapi.mixin.block.StatsAccessor;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static net.modificationstation.stationapi.api.StationAPI.MODID;

public final class BlockRegistry extends LevelSerialRegistry<BlockBase> {

    public static final BlockRegistry INSTANCE = new BlockRegistry();

    private BlockRegistry() {
        super(Identifier.of(MODID, "blocks"));
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        BlockBase[] oldBlocks = BlockBase.BY_ID.clone();
        super.load(tag);
        for (int i = getSize(); i < ItemBase.byId.length; i++) {
            ItemBase item = ItemBase.byId[i];
            if (item instanceof SecondaryBlock)
                ((SecondaryBlockAccessor) item).setTileId(getSerialID(oldBlocks[((SecondaryBlockAccessor) item).getTileId()]));
        }
    }

    @Override
    protected void remap(int newSerialID, BlockBase value) {
        if (value != null) {
            ItemBase blockItem = ItemBase.byId[value.id];
            boolean ticksRandomly = BlockBase.TICKS_RANDOMLY[value.id];
            boolean fullOpaque = BlockBase.FULL_OPAQUE[value.id];
            boolean hasTileEntity = BlockBase.HAS_TILE_ENTITY[value.id];
            int lightOpacity = BlockBase.LIGHT_OPACITY[value.id];
            boolean allowsGrassUnder = BlockBase.ALLOWS_GRASS_UNDER[value.id];
            int emittance = BlockBase.EMITTANCE[value.id];
            boolean noNotifyOnMetaChange = BlockBase.NO_NOTIFY_ON_META_CHANGE[value.id];
            boolean minedGUIDPresent = ((AchievementGUIDAccessor) AchievementGUID.INSTANCE).getGUIDByID().containsKey(16777216 + value.id);
            String minedGUID = null;
            if (minedGUIDPresent)
                minedGUID = ((AchievementGUIDAccessor) AchievementGUID.INSTANCE).getGUIDByID().get(16777216 + value.id);
            boolean minedMapPresent = StatsAccessor.getIdMap().containsKey(16777216 + value.id);
            StatEntity minedMap = null;
            if (minedMapPresent)
                minedMap = (StatEntity) StatsAccessor.getIdMap().get(16777216 + value.id);
            boolean minedArrayPresent = Stats.mineBlock[value.id] != null;
            StatEntity minedArray = null;
            if (minedArrayPresent)
                minedArray = (StatEntity) Stats.mineBlock[value.id];
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
            int field_2307 = ((FireAccessor) BlockBase.FIRE).getField_2307()[value.id];
            int spreadDelayChance = ((FireAccessor) BlockBase.FIRE).getSpreadDelayChance()[value.id];

            BlockBase.BY_ID[value.id] = null;
            ItemBase.byId[value.id] = null;
            BlockBase.TICKS_RANDOMLY[value.id] = false;
            BlockBase.FULL_OPAQUE[value.id] = false;
            BlockBase.HAS_TILE_ENTITY[value.id] = false;
            BlockBase.LIGHT_OPACITY[value.id] = 0;
            BlockBase.ALLOWS_GRASS_UNDER[value.id] = false;
            BlockBase.EMITTANCE[value.id] = 0;
            BlockBase.NO_NOTIFY_ON_META_CHANGE[value.id] = false;
            if (minedGUIDPresent)
                ((AchievementGUIDAccessor) AchievementGUID.INSTANCE).getGUIDByID().remove(16777216 + value.id);
            if (minedMapPresent)
                StatsAccessor.getIdMap().remove(16777216 + value.id);
            if (minedArrayPresent)
                Stats.mineBlock[value.id] = null;
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
            ((FireAccessor) BlockBase.FIRE).getField_2307()[value.id] = 0;
            ((FireAccessor) BlockBase.FIRE).getSpreadDelayChance()[value.id] = 0;

            if (BlockBase.BY_ID[newSerialID] != null)
                remap(getNextSerialID(), BlockBase.BY_ID[newSerialID]);

            ((BlockBaseAccessor) value).setId(newSerialID);
            ((ItemBaseAccessor) blockItem).setId(newSerialID);
            ((BlockAccessor) blockItem).setBlockId(newSerialID);
            if (minedMapPresent) {
                ((StatAccessor) minedMap).setID(16777216 + newSerialID);
                ((StatEntityAccessor) minedMap).setValue(newSerialID);
            }
            if (minedArrayPresent) {
                ((StatAccessor) minedArray).setID(16777216 + newSerialID);
                ((StatEntityAccessor) minedArray).setValue(newSerialID);
            }
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

            BlockBase.BY_ID[newSerialID] = value;
            ItemBase.byId[newSerialID] = blockItem;
            BlockBase.TICKS_RANDOMLY[newSerialID] = ticksRandomly;
            BlockBase.FULL_OPAQUE[newSerialID] = fullOpaque;
            BlockBase.HAS_TILE_ENTITY[newSerialID] = hasTileEntity;
            BlockBase.LIGHT_OPACITY[newSerialID] = lightOpacity;
            BlockBase.ALLOWS_GRASS_UNDER[newSerialID] = allowsGrassUnder;
            BlockBase.EMITTANCE[newSerialID] = emittance;
            BlockBase.NO_NOTIFY_ON_META_CHANGE[newSerialID] = noNotifyOnMetaChange;
            if (minedGUIDPresent)
                ((AchievementGUIDAccessor) AchievementGUID.INSTANCE).getGUIDByID().put(16777216 + newSerialID, minedGUID);
            if (minedMapPresent)
                StatsAccessor.getIdMap().put(16777216 + newSerialID, minedMap);
            if (minedArrayPresent)
                Stats.mineBlock[newSerialID] = minedArray;
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
            ((FireAccessor) BlockBase.FIRE).getField_2307()[newSerialID] = field_2307;
            ((FireAccessor) BlockBase.FIRE).getSpreadDelayChance()[newSerialID] = spreadDelayChance;
        }
    }

    @Override
    public int getSize() {
        return BlockBase.BY_ID.length;
    }

    @Override
    public int getSerialID(BlockBase value) {
        return value == null ? 0 : value.id;
    }

    @Override
    public @NotNull Optional<BlockBase> get(int serialID) {
        try {
            return Optional.ofNullable(BlockBase.BY_ID[serialID]);
        } catch (ArrayIndexOutOfBoundsException e) {
            return Optional.empty();
        }
    }

    @Override
    public int getSerialIDShift() {
        return 1;
    }
}
