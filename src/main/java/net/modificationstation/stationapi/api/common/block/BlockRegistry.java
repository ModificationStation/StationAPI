package net.modificationstation.stationapi.api.common.block;


import net.minecraft.block.BlockBase;
import net.minecraft.stat.Stats;
import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationapi.api.common.StationAPI;
import net.modificationstation.stationapi.api.common.registry.Identifier;
import net.modificationstation.stationapi.api.common.registry.LevelRegistry;
import net.modificationstation.stationapi.mixin.common.accessor.BlockBaseAccessor;

public final class BlockRegistry extends LevelRegistry<BlockBase> {

    public static final BlockRegistry INSTANCE = new BlockRegistry(Identifier.of(StationAPI.INSTANCE.getModID(), "blocks"));

    private BlockRegistry(Identifier registryId) {
        super(registryId);
    }

    @Override
    public void load(CompoundTag tag) {
        forEach((identifier, block) -> {
            if (tag.containsKey(identifier.toString())) {
                int newID = tag.getInt(identifier.toString());
                if (newID != block.id)
                    remap(block, newID);
            }
        });
        Stats.method_753();
    }

    private void remap(BlockBase block, int newID) {
        if (BlockBase.BY_ID[newID] != null)
            setID(BlockBase.BY_ID[newID], block.id);
        setID(block, newID);
    }

    private void setID(BlockBase block, int newID) {
        BlockBase.BY_ID[newID] = block;
        ((BlockBaseAccessor) block).setId(newID);
    }

    @Override
    public void save(CompoundTag tag) {
        forEach((identifier, block) -> tag.put(identifier.toString(), block.id));
    }

    @Override
    public void registerSerializedValue(Identifier identifier, BlockBase value, int serializedId) {
        registerValue(identifier, value);
        BlockBase[] BY_ID = BlockBase.BY_ID;
        if (BY_ID[serializedId] == null)
            BY_ID[serializedId] = value;
    }

    @Override
    public int getNextSerializedID() {
        BlockBase[] BY_ID = BlockBase.BY_ID;
        for (int i = 1; i < BY_ID.length; i++)
            if (BY_ID[i] == null)
                return i;
        throw new RuntimeException("No free space left!");
    }

    @Override
    protected void update() {
        for (BlockBase blockBase : BlockBase.BY_ID)
            if (blockBase != null && getIdentifier(blockBase) == null)
                registerValue(Identifier.of(blockBase.getName() + "_" + blockBase.id), blockBase);
    }

    @Override
    public int getRegistrySize() {
        return BlockBase.BY_ID.length;
    }
}
