package net.modificationstation.stationloader.api.common.block;


import net.minecraft.block.BlockBase;
import net.modificationstation.stationloader.api.common.StationLoader;
import net.modificationstation.stationloader.api.common.registry.Identifier;
import net.modificationstation.stationloader.api.common.registry.SerializedRegistry;

public final class BlockRegistry extends SerializedRegistry<BlockBase> {

    public static final BlockRegistry INSTANCE = new BlockRegistry(Identifier.of(StationLoader.INSTANCE.getModID(), "blocks"));

    private BlockRegistry(Identifier registryId) {
        super(registryId);
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
