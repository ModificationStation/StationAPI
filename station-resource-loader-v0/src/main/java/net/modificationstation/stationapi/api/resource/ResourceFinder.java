package net.modificationstation.stationapi.api.resource;

import net.modificationstation.stationapi.api.registry.Identifier;

import java.util.List;
import java.util.Map;

public class ResourceFinder {
    private final String directoryName;
    private final String fileExtension;

    public ResourceFinder(String directoryName, String fileExtension) {
        this.directoryName = directoryName;
        this.fileExtension = fileExtension;
    }

    public static ResourceFinder json(String directoryName) {
        return new ResourceFinder(directoryName, ".json");
    }

    public Identifier toResourcePath(Identifier id) {
        return id.id.startsWith("/") ? id : id.prepend(this.directoryName + "/").append(this.fileExtension);
    }

    public Identifier toResourceId(Identifier path) {
        String string = path.id;
        return path.modID.id(string.substring(this.directoryName.length() + 1, string.length() - this.fileExtension.length()));
    }

    public Map<Identifier, Resource> findResources(ResourceManager resourceManager) {
        return resourceManager.findResources(this.directoryName, path -> path.id.endsWith(this.fileExtension));
    }

    public Map<Identifier, List<Resource>> findAllResources(ResourceManager resourceManager) {
        return resourceManager.findAllResources(this.directoryName, path -> path.id.endsWith(this.fileExtension));
    }
}
