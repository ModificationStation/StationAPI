package net.modificationstation.stationapi.mixin.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvironmentInterface;
import net.minecraft.item.ItemBase;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.client.texture.atlas.CustomAtlasProvider;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemBase.class)
@EnvironmentInterface(value = EnvType.CLIENT, itf = CustomAtlasProvider.class)
public class MixinItemBase implements CustomAtlasProvider {

    @Override
    @Environment(EnvType.CLIENT)
    public Atlas getAtlas() {
        return Atlases.getGuiItems();
    }
}
