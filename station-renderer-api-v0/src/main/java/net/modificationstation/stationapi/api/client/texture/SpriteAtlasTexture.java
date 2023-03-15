package net.modificationstation.stationapi.api.client.texture;

import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.resource.ResourceManager;
import net.modificationstation.stationapi.api.util.exception.CrashException;
import net.modificationstation.stationapi.api.util.exception.CrashReport;
import net.modificationstation.stationapi.api.util.exception.CrashReportSection;
import net.modificationstation.stationapi.impl.client.render.SpriteFinderImpl;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static net.modificationstation.stationapi.impl.client.texture.StationRenderImpl.LOGGER;

public class SpriteAtlasTexture extends AbstractTexture implements DynamicTexture, TextureTickListener {
    private List<SpriteContents> spritesToLoad = List.of();
    private List<Sprite.TickableAnimation> animatedSprites = List.of();
    private Map<Identifier, Sprite> sprites = Map.of();
    private final Identifier id;
    private final int maxTextureSize;
    private int width;
    private int height;
    private SpriteFinderImpl spriteFinder = null;

    public SpriteAtlasTexture(Identifier id) {
        this.id = id;
        this.maxTextureSize = TextureUtil.maxSupportedTextureSize();
    }

    @Override
    public void load(ResourceManager manager) {}

    public void upload(SpriteLoader.StitchResult stitchResult) {
        LOGGER.info("Created: {}x{} {}-atlas", stitchResult.width(), stitchResult.height(), this.id);
        TextureUtil.prepareImage(this.getGlId(), stitchResult.width(), stitchResult.height());
        this.width = stitchResult.width();
        this.height = stitchResult.height();
        this.clear();
        this.sprites = Map.copyOf(stitchResult.regions());
        ArrayList<SpriteContents> list = new ArrayList<>();
        ArrayList<Sprite.TickableAnimation> list2 = new ArrayList<>();
        for (Sprite sprite : stitchResult.regions().values()) {
            list.add(sprite.getContents());
            try {
                sprite.upload();
            } catch (Throwable throwable) {
                CrashReport crashReport = CrashReport.create(throwable, "Stitching texture atlas");
                CrashReportSection crashReportSection = crashReport.addElement("Texture being stitched together");
                crashReportSection.add("Atlas path", this.id);
                crashReportSection.add("Sprite", sprite);
                throw new CrashException(crashReport);
            }
            Sprite.TickableAnimation tickableAnimation = sprite.createAnimation();
            if (tickableAnimation == null) continue;
            list2.add(tickableAnimation);
        }
        this.spritesToLoad = List.copyOf(list);
        this.animatedSprites = List.copyOf(list2);
        spriteFinder = null;
    }

    @Override
    public void save(Identifier id, Path path) {
        String string = id.toString().replace('/', '_').replace(':', '_');
        TextureUtil.writeAsPNG(path, string, this.getGlId(), 0, this.width, this.height);
        SpriteAtlasTexture.dumpAtlasInfos(path, string, this.sprites);
    }

    private static void dumpAtlasInfos(Path path, String id, Map<Identifier, Sprite> sprites) {
        Path path2 = path.resolve(id + ".txt");
        try (BufferedWriter writer = Files.newBufferedWriter(path2)){
            for (Map.Entry<Identifier, Sprite> entry : sprites.entrySet().stream().sorted(Map.Entry.comparingByKey()).toList()) {
                Sprite sprite = entry.getValue();
                writer.write(String.format(Locale.ROOT, "%s\tx=%d\ty=%d\tw=%d\th=%d%n", entry.getKey(), sprite.getX(), sprite.getY(), sprite.getContents().getWidth(), sprite.getContents().getHeight()));
            }
        } catch (IOException iOException) {
            LOGGER.warn("Failed to write file {}", path2, iOException);
        }
    }

    @Override
    public void tick() {
        this.bindTexture();
        for (Sprite.TickableAnimation tickableAnimation : this.animatedSprites) tickableAnimation.tick();
    }

    public Sprite getSprite(Identifier id) {
        Sprite sprite = this.sprites.get(id);
        if (sprite == null) return this.sprites.get(MissingSprite.getMissingSpriteId());
        return sprite;
    }

    public void clear() {
        this.spritesToLoad.forEach(SpriteContents::close);
        this.animatedSprites.forEach(Sprite.TickableAnimation::close);
        this.spritesToLoad = List.of();
        this.animatedSprites = List.of();
        this.sprites = Map.of();
    }

    public Identifier getId() {
        return this.id;
    }

    public int getMaxTextureSize() {
        return this.maxTextureSize;
    }

    int getWidth() {
        return this.width;
    }

    int getHeight() {
        return this.height;
    }

    public void applyTextureFilter(SpriteLoader.StitchResult data) {
        this.setFilter(false, false);
    }

    public SpriteFinderImpl spriteFinder() {
        SpriteFinderImpl result = spriteFinder;
        if (result == null) {
            result = new SpriteFinderImpl(sprites, this);
            spriteFinder = result;
        }
        return result;
    }
}

