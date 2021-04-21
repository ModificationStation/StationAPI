package net.modificationstation.stationapi.api.client.texture;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.mixin.sortme.client.accessor.TextureManagerAccessor;

public class ExpandableTextureAtlas extends TextureAtlas {

    private static final Map<String, ExpandableTextureAtlas> PATH_TO_ATLAS = new HashMap<>();

    private BufferedImage spritesheetImage;

    public ExpandableTextureAtlas(Identifier identifier) {
        super("/assets/stationapi/atlases/" + identifier, 0);
        PATH_TO_ATLAS.put(spritesheet, this);
    }

    public ExpandableTextureAtlas(Identifier identifier, TextureAtlas parent) {
        super("/assets/stationapi/atlases/" + identifier, 0, parent);
        PATH_TO_ATLAS.put(spritesheet, this);
    }

    public int addTexture(String texture) {
        //noinspection deprecation
        Minecraft minecraft = (Minecraft) FabricLoader.getInstance().getGameInstance();
        TextureManagerAccessor textureManager = (TextureManagerAccessor) minecraft.textureManager;
        BufferedImage image = textureManager.invokeMethod_1091(minecraft.texturePackManager.texturePack.getResourceAsStream(texture));
        if (spritesheetImage == null)
            spritesheetImage = image;
        else {
            int previousWidth = spritesheetImage.getWidth();
            resizeSpritesheet(spritesheetImage.getWidth() + image.getWidth(), Math.max(image.getHeight(), spritesheetImage.getHeight()));
            Graphics2D graphics = spritesheetImage.createGraphics();
            graphics.drawImage(image, previousWidth, 0, null);
            graphics.dispose();
        }
        textureManager.invokeMethod_1089(spritesheetImage, minecraft.textureManager.getTextureId(spritesheet));
        int lastIndex = size;
        size++;
        return lastIndex;
    }

    private void resizeSpritesheet(int targetWidth, int targetHeight) {
        BufferedImage previousSpriteSheet = spritesheetImage;
        spritesheetImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = spritesheetImage.createGraphics();
        graphics.drawImage(previousSpriteSheet, 0, 0, null);
        graphics.dispose();
    }

    public InputStream getAsStream() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(spritesheetImage, "png", outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    public static ExpandableTextureAtlas getByPath(String spritesheet) {
        return PATH_TO_ATLAS.get(spritesheet);
    }
}
