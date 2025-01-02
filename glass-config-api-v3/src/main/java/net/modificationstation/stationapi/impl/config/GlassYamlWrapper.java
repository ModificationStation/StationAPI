package net.modificationstation.stationapi.impl.config;

import org.simpleyaml.configuration.file.YamlFile;
import org.simpleyaml.configuration.file.YamlFileWrapper;
import org.simpleyaml.configuration.implementation.api.YamlImplementation;

import java.util.*;

// And so the code crimes continue.
public class GlassYamlWrapper extends YamlFileWrapper {
    public GlassYamlWrapper(YamlFile configuration, String path) {
        super(configuration, path);
    }

    protected GlassYamlWrapper(YamlFile configuration, String path, YamlFileWrapper parent) {
        super(configuration, path, parent);
    }

    public <T> Object get(Class<T> type) {
        return configuration.get(path);
    }

    public <T> Object getChild(String key, Class<T> type) {
        return configuration.get(childPath(key));
    }

    @Override
    public GlassYamlWrapper path(String path) {
        // Fixes some fuckery with comments.
        return new GlassYamlWrapper(this.configuration, path, Objects.equals(this.path, "") ? null : this);
    }
}
