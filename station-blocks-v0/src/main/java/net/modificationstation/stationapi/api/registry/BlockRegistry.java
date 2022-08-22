package net.modificationstation.stationapi.api.registry;

import com.mojang.serialization.Lifecycle;
import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemBase;
import net.modificationstation.stationapi.api.registry.legacy.AbstractArrayBackedLegacyRegistry;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import net.modificationstation.stationapi.mixin.block.BlockBaseAccessor;
import net.modificationstation.stationapi.mixin.block.FireAccessor;
import net.modificationstation.stationapi.mixin.block.ItemBaseAccessor;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

import static net.modificationstation.stationapi.api.StationAPI.MODID;

public final class BlockRegistry extends AbstractArrayBackedLegacyRegistry<BlockBase> {

    public static final RegistryKey<Registry<BlockBase>> KEY = RegistryKey.ofRegistry(MODID.id("blocks"));
    public static final BlockRegistry INSTANCE = Registry.create(KEY, new BlockRegistry(), Lifecycle.experimental());

    private BlockRegistry() {
        super(KEY, BlockBase::getRegistryEntry);
    }

    @Override
    protected BlockBase[] getBackingArray() {
        return BlockBase.BY_ID;
    }

    @Override
    public int getLegacyIdShift() {
        return 1;
    }

    @Override
    public int getLegacyId(@NotNull BlockBase value) {
        return Objects.requireNonNull(value).id;
    }

    @Override
    protected boolean setSize(int newSize) {
        if (!super.setSize(newSize)) {
            int oldSize = getSize();
            BlockBaseAccessor.stationapi$setById(Arrays.copyOf(BlockBase.BY_ID, newSize));
            BlockBaseAccessor.stationapi$setTicksRandomly(Arrays.copyOf(BlockBase.TICKS_RANDOMLY, newSize));
            BlockBaseAccessor.stationapi$setFullOpaque(Arrays.copyOf(BlockBase.FULL_OPAQUE, newSize));
            BlockBaseAccessor.stationapi$setHasTileEntity(Arrays.copyOf(BlockBase.HAS_TILE_ENTITY, newSize));
            BlockBaseAccessor.stationapi$setLightOpacity(Arrays.copyOf(BlockBase.LIGHT_OPACITY, newSize));
            BlockBaseAccessor.stationapi$setAllowsGrassUnder(Arrays.copyOf(BlockBase.ALLOWS_GRASS_UNDER, newSize));
            BlockBaseAccessor.stationapi$setEmittance(Arrays.copyOf(BlockBase.EMITTANCE, newSize));
            BlockBaseAccessor.stationapi$setNoNotifyOnMetaChange(Arrays.copyOf(BlockBase.NO_NOTIFY_ON_META_CHANGE, newSize));
            ItemBase[] oldItems = ItemBase.byId.clone();
            for (int i = oldSize; i < oldItems.length; i++) {
                ItemBase item = oldItems[i];
                if (item != null) {
                    ItemBase.byId[i] = null;
                    int newId = i - oldSize + newSize;
                    ((ItemBaseAccessor) item).setId(newId);
                    (newId < ItemBase.byId.length ? ItemBase.byId : (ItemBase.byId = Arrays.copyOf(ItemBase.byId, MathHelper.isPowerOfTwo(newId + 1) ? newId + 1 : MathHelper.smallestEncompassingPowerOfTwo(newId))))[newId] = item;
                }
            }
            FireAccessor fireAccessor = (FireAccessor) BlockBase.FIRE;
            fireAccessor.setField_2307(Arrays.copyOf(fireAccessor.getField_2307(), newSize));
            fireAccessor.setSpreadDelayChance(Arrays.copyOf(fireAccessor.getSpreadDelayChance(), newSize));
        }
        return true;
    }
}
