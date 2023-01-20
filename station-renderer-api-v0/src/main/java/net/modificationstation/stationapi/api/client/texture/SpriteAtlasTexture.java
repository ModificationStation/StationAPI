package net.modificationstation.stationapi.api.client.texture;

import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import it.unimi.dsi.fastutil.objects.ReferenceSet;
import net.minecraft.client.texture.TextureManager;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.resource.FakeResourceManager;
import net.modificationstation.stationapi.api.resource.ResourceHelper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Set;

public class SpriteAtlasTexture {

    private final Identifier id;
    private final String path;
    private BufferedImage image;
    private final ReferenceSet<Identifier> spritesToLoad = new ReferenceOpenHashSet<>();

    public SpriteAtlasTexture(TextureManager manager, Identifier id) {
        this.id = id;
        path = ResourceHelper.ASSETS.toPath(id, "textures/atlas", "png");
        FakeResourceManager.registerProvider(s -> {
            if (path.equals(s)) {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                try {
                    ImageIO.write(image, "png", outputStream);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return new ByteArrayInputStream(outputStream.toByteArray());
            } else
                return null;
        });
    }

    public
}
