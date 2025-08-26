package net.modificationstation.stationapi.impl.resource;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resource.pack.TexturePack;
import net.minecraft.client.resource.pack.ZippedTexturePack;
import net.modificationstation.stationapi.api.resource.ResourceType;
import net.modificationstation.stationapi.mixin.resourceloader.client.ZipTexturePackAccessor;

import java.util.function.Consumer;

public class TexturePackProvider implements ResourcePackProvider {
    @Override
    public void register(Consumer<ResourcePackProfile> profileAdder) {
        //noinspection deprecation
        TexturePack texturePack = ((Minecraft) FabricLoader.getInstance().getGameInstance()).texturePacks.selected;
        if (texturePack instanceof ZippedTexturePack zipTexturePack) {
            String fileName = ((ZipTexturePackAccessor) zipTexturePack).getZip().getName();
            ResourcePackProfile resourcePackProfile = ResourcePackProfile.create(
                    "file/" + fileName,
                    /*Text.literal(string)*/fileName,
                    true,
                    name -> new ZippedTexturePackResourcePack(zipTexturePack, false),
                    ResourceType.CLIENT_RESOURCES,
                    ResourcePackProfile.InsertionPosition.TOP,
                    ResourcePackSource.NONE
            );
            if (resourcePackProfile != null) profileAdder.accept(resourcePackProfile);
        }
    }
}

