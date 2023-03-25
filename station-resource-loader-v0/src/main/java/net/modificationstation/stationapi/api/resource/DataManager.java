package net.modificationstation.stationapi.api.resource;

import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ModID;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class DataManager implements ResourceManager {

    @Override
    public Optional<Resource> getResource(Identifier id) {
//        String path = ResourceHelper.ASSETS.toPath(id);
//        try (InputStream stream = activePack.getResourceAsStream(path)) {
//            if (stream != null) {
//                String metaPath = path + ".mcmeta";
//                try (InputStream metaStream = activePack.getResourceAsStream(metaPath)) {
//                    return Optional.of(new Resource(() -> activePack.getResourceAsStream(path), metaStream == null ? () -> ResourceMetadata.NONE : () -> {
//                        InputStream newMetaStream = activePack.getResourceAsStream(metaPath);
//                        ResourceMetadata meta;
//                        try {
//                            meta = ResourceMetadata.create(newMetaStream);
//                        } catch (IOException e) {
//                            throw new RuntimeException(e);
//                        }
//                        return meta;
//                    }));
//                }
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        return Optional.empty();
    }

    @Override
    public Set<ModID> getAllNamespaces() {
        return null;
    }

    @Override
    public List<Resource> getAllResources(Identifier id) {
        return null;
    }

    @Override
    public Map<Identifier, Resource> findResources(String startingPath, Predicate<Identifier> allowedPathPredicate) {
        return null;
    }

    @Override
    public Map<Identifier, List<Resource>> findAllResources(String startingPath, Predicate<Identifier> allowedPathPredicate) {
        return null;
    }

    @Override
    public Stream<ResourcePack> streamResourcePacks() {
        return null;
    }
}
