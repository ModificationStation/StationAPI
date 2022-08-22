package net.modificationstation.stationapi.mixin.item;

import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemBase;
import net.modificationstation.stationapi.api.block.StationItemsBlock;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BlockBase.class)
public class MixinBlockBase implements StationItemsBlock {

    @Shadow @Final public int id;

    @Override
    public ItemBase asItem() {
        return ItemBase.byId[id];
    }
}
