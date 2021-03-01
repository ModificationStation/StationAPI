package net.modificationstation.stationapi.api.common.config;

import net.modificationstation.stationapi.api.common.util.Readable;
import net.modificationstation.stationapi.api.common.util.Writeable;
import net.modificationstation.stationapi.impl.common.config.ConfigurationImpl;

import java.io.*;
import java.util.*;

public interface Configuration extends Writeable, Readable {

    Category getCategory(String name);

    Collection<Category> getCategories();

    File getConfigFile();

    void save();

    void load();

    static Configuration of(File file) {
        return ConfigurationImpl.of(file);
    }
}
