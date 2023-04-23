package net.modificationstation.stationapi.mixin.item;

import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.block.StationItemsBlock;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockBase.class)
public class MixinBlockBase implements StationItemsBlock {

}
