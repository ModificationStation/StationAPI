package net.modificationstation.stationapi.api.client.texture.atlas;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonParser;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import net.modificationstation.stationapi.api.client.texture.MissingSprite;
import net.modificationstation.stationapi.api.client.texture.SpriteContents;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.resource.Resource;
import net.modificationstation.stationapi.api.resource.ResourceFinder;
import net.modificationstation.stationapi.api.resource.ResourceManager;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;
import static net.modificationstation.stationapi.impl.client.texture.StationRenderImpl.LOGGER;

public class AtlasLoader {
    private static final ResourceFinder FINDER = new ResourceFinder(NAMESPACE + "/atlases", ".json");
    private final List<AtlasSource> sources;

    private AtlasLoader(List<AtlasSource> sources) {
        this.sources = sources;
    }

    public List<Supplier<SpriteContents>> loadSources(ResourceManager resourceManager) {
        final Reference2ReferenceMap<Identifier, AtlasSource.SpriteRegion> map = new Reference2ReferenceOpenHashMap<>();
        AtlasSource.SpriteRegions spriteRegions = new AtlasSource.SpriteRegions() {
            @Override
            public void add(Identifier arg, AtlasSource.SpriteRegion region) {
                AtlasSource.SpriteRegion spriteRegion = map.put(arg, region);
                if (spriteRegion != null) spriteRegion.close();
            }

            @Override
            public void removeIf(Predicate<Identifier> predicate) {
                Iterator<Map.Entry<Identifier, AtlasSource.SpriteRegion>> iterator = map.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<Identifier, AtlasSource.SpriteRegion> entry = iterator.next();
                    if (!predicate.test(entry.getKey())) continue;
                    entry.getValue().close();
                    iterator.remove();
                }
            }
        };
        this.sources.forEach(source -> source.load(resourceManager, spriteRegions));
        ImmutableList.Builder<Supplier<SpriteContents>> builder = ImmutableList.builder();
        builder.add(MissingSprite::createSpriteContents);
        builder.addAll(map.values());
        return builder.build();
    }

    public static AtlasLoader of(ResourceManager resourceManager, Identifier id) {
        Identifier identifier = FINDER.toResourcePath(id);
        ArrayList<AtlasSource> list = new ArrayList<>();
        for (Resource resource : resourceManager.getAllResources(identifier))
            try (BufferedReader bufferedReader = resource.getReader()) {
                list.addAll(AtlasSourceManager.LIST_CODEC.parse(new Dynamic<>(JsonOps.INSTANCE, JsonParser.parseReader(bufferedReader))).getOrThrow(false, LOGGER::error));
            } catch (Exception exception) {
                LOGGER.warn("Failed to parse atlas definition {} in pack {}", identifier, "Default", exception);
            }
        return new AtlasLoader(list);
    }
}

