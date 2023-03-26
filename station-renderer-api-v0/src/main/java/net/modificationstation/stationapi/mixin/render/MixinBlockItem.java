package net.modificationstation.stationapi.mixin.render;

import net.minecraft.block.BlockBase;
import net.minecraft.item.Block;
import net.minecraft.item.ItemBase;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.CustomAtlasProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Block.class)
public class MixinBlockItem extends ItemBase implements CustomAtlasProvider {
    @Shadow private int blockId;
    
    protected MixinBlockItem(int i) {
        super(i);
    }
    
    @Override
    public Atlas getAtlas() {
        return ((CustomAtlasProvider) BlockBase.BY_ID[blockId]).getAtlas();
    }
}
