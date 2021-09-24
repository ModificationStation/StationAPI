package net.modificationstation.stationapi.mixin.render;

import net.minecraft.item.ItemBase;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.client.texture.atlas.CustomAtlasProvider;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemBase.class)
public class MixinItemBase implements CustomAtlasProvider {

    @Override
    public Atlas getAtlas() {
        return Atlases.getGuiItems();
    }
}
