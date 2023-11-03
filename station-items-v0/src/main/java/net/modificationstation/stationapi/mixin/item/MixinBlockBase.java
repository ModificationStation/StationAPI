package net.modificationstation.stationapi.mixin.item;

import net.minecraft.block.Block;
import net.modificationstation.stationapi.api.block.StationItemsBlock;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Block.class)
public class MixinBlockBase implements StationItemsBlock {

}
