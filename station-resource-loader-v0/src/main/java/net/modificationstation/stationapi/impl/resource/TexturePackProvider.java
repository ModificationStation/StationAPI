package net.modificationstation.stationapi.impl.resource;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_285;
import net.minecraft.class_592;
import net.minecraft.client.Minecraft;
import net.modificationstation.stationapi.api.resource.ResourceType;
import net.modificationstation.stationapi.mixin.resourceloader.client.ZipTexturePackAccessor;

import java.util.function.Consumer;

public class TexturePackProvider implements ResourcePackProvider {
    @Override
    public void register(Consumer<ResourcePackProfile> profileAdder) {
        //noinspection deprecation
        class_285 texturePack = ((Minecraft) FabricLoader.getInstance().getGameInstance()).field_2768.field_1175;
        if (texturePack instanceof class_592 zipTexturePack) {
            String fileName = ((ZipTexturePackAccessor) zipTexturePack).getField_2562().getName();
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

