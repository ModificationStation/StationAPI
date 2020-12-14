package net.modificationstation.stationloader.impl.common.config;

import net.modificationstation.stationloader.api.common.config.Property;
import net.modificationstation.stationloader.api.common.factory.GeneralFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

public class Category implements net.modificationstation.stationloader.api.common.config.Category {

    private final String name;
    private final Set<net.modificationstation.stationloader.api.common.config.Property> properties = new TreeSet<>();

    public Category(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public net.modificationstation.stationloader.api.common.config.Property getProperty(String name) {
        for (net.modificationstation.stationloader.api.common.config.Property property : properties)
            if (name.equals(property.getName()))
                return property;
        net.modificationstation.stationloader.api.common.config.Property property = GeneralFactory.INSTANCE.newInst(net.modificationstation.stationloader.api.common.config.Property.class, name);
        properties.add(property);
        return property;
    }

    @Override
    public net.modificationstation.stationloader.api.common.config.Property getProperty(String name, String defaultValue) {
        Property property = getProperty(name);
        if (property.getStringValue() == null) {
            property.setValue(defaultValue);
        }
        return property;
    }

    @Override
    public net.modificationstation.stationloader.api.common.config.Property getProperty(String name, int defaultValue) {
        Property property = getProperty(name);
        if (property.getStringValue() == null) {
            property.setValue(defaultValue);
        }
        return property;
    }

    @Override
    public net.modificationstation.stationloader.api.common.config.Property getProperty(String name, boolean defaultValue) {
        Property property = getProperty(name);
        if (property.getStringValue() == null) {
            property.setValue(defaultValue);
        }
        return property;
    }

    @Override
    public Collection<Property> getProperties() {
        return Collections.unmodifiableCollection(properties);
    }

    @Override
    public void save(BufferedWriter buffer) {
        try {
            buffer.write(name + " {\r\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (net.modificationstation.stationloader.api.common.config.Property property : properties)
            property.save(buffer);
        try {
            buffer.write("}\r\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int compareTo(net.modificationstation.stationloader.api.common.config.Category o) {
        return getName().compareTo(o.getName());
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof net.modificationstation.stationloader.api.common.config.Category && getName().equals(((net.modificationstation.stationloader.api.common.config.Category) o).getName());
    }
}
