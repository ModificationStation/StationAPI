package net.modificationstation.stationapi.mixin.render;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.CustomAtlasProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BlockItem.class)
class BlockItemMixin implements CustomAtlasProvider {
    @Shadow private int blockId;

    @Override
    @Unique
    public Atlas getAtlas() {
        return Block.BLOCKS[blockId].getAtlas();
    }
}
