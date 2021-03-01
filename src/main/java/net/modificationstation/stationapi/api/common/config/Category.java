package net.modificationstation.stationapi.api.common.config;

import net.modificationstation.stationapi.api.common.util.Named;
import net.modificationstation.stationapi.api.common.util.Writeable;

import java.util.*;

public interface Category extends Named, Writeable, Comparable<Category> {

    Property getProperty(String name);

    Property getProperty(String name, String defaultValue);

    Property getProperty(String name, int defaultValue);

    Property getProperty(String name, boolean defaultValue);

    Collection<Property> getProperties();
}
