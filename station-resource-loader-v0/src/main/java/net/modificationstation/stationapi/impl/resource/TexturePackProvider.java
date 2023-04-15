package net.modificationstation.stationapi.impl.resource;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resource.TexturePack;
import net.minecraft.client.resource.ZippedTexturePack;
import net.modificationstation.stationapi.api.resource.ResourceType;
import net.modificationstation.stationapi.mixin.resourceloader.client.ZippedTexturePackAccessor;

import java.util.function.Consumer;

public class TexturePackProvider implements ResourcePackProvider {
    @Override
    public void register(Consumer<ResourcePackProfile> profileAdder) {
        //noinspection deprecation
        TexturePack texturePack = ((Minecraft) FabricLoader.getInstance().getGameInstance()).texturePackManager.texturePack;
        if (texturePack instanceof ZippedTexturePack zipTexturePack) {
            String fileName = ((ZippedTexturePackAccessor) zipTexturePack).getZipFile().getName();
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

