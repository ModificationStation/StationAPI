package net.modificationstation.stationloader.api.common.config;

import net.modificationstation.stationloader.api.common.util.Named;
import net.modificationstation.stationloader.api.common.util.Writeable;

import java.util.Collection;

public interface Category extends Named, Writeable, Comparable<Category> {

    Property getProperty(String name);

    Property getProperty(String name, String defaultValue);
    Property getProperty(String name, int defaultValue);
    Property getProperty(String name, boolean defaultValue);

    Collection<Property> getProperties();
}
