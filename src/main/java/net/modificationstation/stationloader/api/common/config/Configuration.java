package net.modificationstation.stationloader.api.common.config;

import net.modificationstation.stationloader.api.common.util.Readable;
import net.modificationstation.stationloader.api.common.util.Writeable;

import java.io.File;
import java.util.Collection;

public interface Configuration extends Writeable, Readable {

    Category getCategory(String name);

    Collection<Category> getCategories();

    File getConfigFile();

    void save();

    void load();
}
