package net.modificationstation.stationapi.api.client.render.model;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.client.render.dimension.LightmapDimension;
import net.modificationstation.stationapi.api.client.texture.NativeImage;
import net.modificationstation.stationapi.api.client.texture.NativeImageBackedTexture;
import net.modificationstation.stationapi.api.client.texture.StationTextureManager;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import org.joml.Vector3f;

public class LightmapTextureManager {
    /**
     * Represents the maximum lightmap coordinate, where both sky light and block light equals {@code 15}.
     * The value of this maximum lightmap coordinate is {@value}.
     */
    public static final int MAX_LIGHT_COORDINATE = 15728880;
    /**
     * Represents the maximum sky-light-wise lightmap coordinate whose value is {@value}.
     * This is equivalent to a {@code 15} sky light and {@code 0} block light.
     */
    public static final int MAX_SKY_LIGHT_COORDINATE = 15728640;
    /**
     * Represents the maximum block-light-wise lightmap coordinate whose value is {@value}.
     * This is equivalent to a {@code 0} sky light and {@code 15} block light.
     */
    public static final int MAX_BLOCK_LIGHT_COORDINATE = 240;

    private final NativeImageBackedTexture texture;
    private final NativeImage image;
    private final Identifier textureIdentifier;
    private final Minecraft client;

    public LightmapTextureManager(Minecraft client) {
        StationTextureManager textureManager = StationTextureManager.get(client.textureManager);

        this.client = client;

        this.texture = new NativeImageBackedTexture(16, 16, false);
        this.textureIdentifier = textureManager.registerDynamicTexture("light_map", this.texture);
        this.image = this.texture.getImage();

        for (int y = 0; y < 16; y++) {
            for (int x = 0; x < 16; x++) {
                this.image.setColor(x, y, -1);
            }
        }

        this.texture.upload();
    }

    public void update(float delta) {
        World world = this.client.world;

        if (world != null) {
            float skyDarken = world.method_151(1);

            for (int yPixel = 0; yPixel < 16; yPixel++) {
                for (int xPixel = 0; xPixel < 16; xPixel++) {
                    Vector3f color = new Vector3f();

                    int x = (int) color.x();
                    int y = (int) color.y();
                    int z = (int) color.z();

                    this.image.setColor(xPixel, yPixel, 0xFF000000 | z << 16 | y << 8 | x);
                }
            }
        }
    }

    public static float getBrightness(LightmapDimension type, int lightLevel) {
        return getBrightness(type.ambientLight(), lightLevel);
    }

    public static float getBrightness(float ambientLight, int lightLevel) {
        float level = lightLevel / 15.0F;
        float curved_level = level / (4.0F - 3.0F * level);
        return MathHelper.lerp(ambientLight, curved_level, 1.0F);
    }

    public static int pack(int block, int sky) {
        return block << 4 | sky << 20;
    }

    public static int getBlockLightCoordinates(int light) {
        return light >> 4 & (MAX_BLOCK_LIGHT_COORDINATE | 65295);
    }

    public static int getSkyLightCoordinates(int light) {
        return light >> 20 & (MAX_BLOCK_LIGHT_COORDINATE | 65295);
    }
}
