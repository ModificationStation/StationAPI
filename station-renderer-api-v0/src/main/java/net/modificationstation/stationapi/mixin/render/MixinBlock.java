package net.modificationstation.stationapi.mixin.render;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.CustomAtlasProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BlockItem.class)
public class MixinBlock implements CustomAtlasProvider {

    @Shadow private int blockId;

    @Override
    public Atlas getAtlas() {
        return Block.BLOCKS[blockId].getAtlas();
    }
}
