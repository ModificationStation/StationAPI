package net.modificationstation.stationapi.api.registry;

import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.SecondaryBlock;
import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationapi.mixin.block.BlockAccessor;
import net.modificationstation.stationapi.mixin.block.BlockBaseAccessor;
import net.modificationstation.stationapi.mixin.block.ItemBaseAccessor;
import net.modificationstation.stationapi.mixin.block.SecondaryBlockAccessor;

import java.util.Optional;

import static net.modificationstation.stationapi.api.StationAPI.MODID;

public final class BlockRegistry extends LevelSerialRegistry<BlockBase> {

    public static final BlockRegistry INSTANCE = new BlockRegistry();

    private BlockRegistry() {
        super(Identifier.of(MODID, "blocks"));
    }

    @Override
    public void load(CompoundTag tag) {
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
        ItemBase blockItem = ItemBase.byId[value.id];
        boolean ticksRandomly = BlockBase.TICKS_RANDOMLY[value.id];
        boolean fullOpaque = BlockBase.FULL_OPAQUE[value.id];
        boolean hasTileEntity = BlockBase.HAS_TILE_ENTITY[value.id];
        int lightOpacity = BlockBase.LIGHT_OPACITY[value.id];
        boolean allowsGrassUnder = BlockBase.ALLOWS_GRASS_UNDER[value.id];
        int emittance = BlockBase.EMITTANCE[value.id];
        boolean noNotifyOnMetaChange = BlockBase.NO_NOTIFY_ON_META_CHANGE[value.id];
        BlockBase.BY_ID[value.id] = null;
        ItemBase.byId[value.id] = null;
        BlockBase.TICKS_RANDOMLY[value.id] = false;
        BlockBase.FULL_OPAQUE[value.id] = false;
        BlockBase.HAS_TILE_ENTITY[value.id] = false;
        BlockBase.LIGHT_OPACITY[value.id] = 0;
        BlockBase.ALLOWS_GRASS_UNDER[value.id] = false;
        BlockBase.EMITTANCE[value.id] = 0;
        BlockBase.NO_NOTIFY_ON_META_CHANGE[value.id] = false;
        if (BlockBase.BY_ID[newSerialID] != null)
            remap(getNextSerialID(), BlockBase.BY_ID[newSerialID]);
        ((BlockBaseAccessor) value).setId(newSerialID);
        BlockBase.BY_ID[newSerialID] = value;
        ((ItemBaseAccessor) blockItem).setId(newSerialID);
        ((BlockAccessor) blockItem).setBlockId(newSerialID);
        ItemBase.byId[newSerialID] = blockItem;
        BlockBase.TICKS_RANDOMLY[newSerialID] = ticksRandomly;
        BlockBase.FULL_OPAQUE[newSerialID] = fullOpaque;
        BlockBase.HAS_TILE_ENTITY[newSerialID] = hasTileEntity;
        BlockBase.LIGHT_OPACITY[newSerialID] = lightOpacity;
        BlockBase.ALLOWS_GRASS_UNDER[newSerialID] = allowsGrassUnder;
        BlockBase.EMITTANCE[newSerialID] = emittance;
        BlockBase.NO_NOTIFY_ON_META_CHANGE[newSerialID] = noNotifyOnMetaChange;
    }

    @Override
    public int getSize() {
        return BlockBase.BY_ID.length;
    }

    @Override
    public int getSerialID(BlockBase value) {
        return value.id;
    }

    @Override
    public Optional<BlockBase> get(int serialID) {
        try {
            return Optional.ofNullable(BlockBase.BY_ID[serialID]);
        } catch (ArrayIndexOutOfBoundsException e) {
            return Optional.empty();
        }
    }

    @Override
    public int getFirstSerialID() {
        return 1;
    }
}
