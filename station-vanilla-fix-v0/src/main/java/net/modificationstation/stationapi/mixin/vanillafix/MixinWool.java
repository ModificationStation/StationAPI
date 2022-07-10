package net.modificationstation.stationapi.mixin.vanillafix;

import net.minecraft.block.BlockBase;
import net.minecraft.block.Wool;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.BlockStateHolder;
import net.modificationstation.stationapi.api.block.DropListProvider;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.vanillafix.block.VanillaBlockProperties;
import net.modificationstation.stationapi.impl.vanillafix.block.WoolDropImpl;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

// TODO: make this use separate blocks rather than blockstates
@Mixin(Wool.class)
public abstract class MixinWool implements DropListProvider, BlockStateHolder {

    @Override
    public void appendProperties(StateManager.Builder<BlockBase, BlockState> builder) {
        builder.add(VanillaBlockProperties.COLOR);
    }

    @Override
    public List<ItemInstance> getDropList(Level level, int x, int y, int z, BlockState state, int meta) {
        return WoolDropImpl.drop(level, x, y, z, state, meta);
    }
}
