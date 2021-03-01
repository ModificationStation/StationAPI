package net.modificationstation.stationapi.impl.common.config;

import net.modificationstation.stationapi.api.common.config.Property;
import net.modificationstation.stationapi.api.common.factory.GeneralFactory;

import java.io.*;
import java.util.*;

public class CategoryImpl implements net.modificationstation.stationapi.api.common.config.Category {

    private final String name;
    private final Set<Property> properties = new TreeSet<>();

    public CategoryImpl(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Property getProperty(String name) {
        for (Property property : properties)
            if (name.equals(property.getName()))
                return property;
        Property property = GeneralFactory.INSTANCE.newInst(Property.class, name);
        properties.add(property);
        return property;
    }

    @Override
    public Property getProperty(String name, String defaultValue) {
        Property property = getProperty(name);
        if (property.getStringValue() == null) {
            property.setValue(defaultValue);
        }
        return property;
    }

    @Override
    public Property getProperty(String name, int defaultValue) {
        Property property = getProperty(name);
        if (property.getStringValue() == null) {
            property.setValue(defaultValue);
        }
        return property;
    }

    @Override
    public Property getProperty(String name, boolean defaultValue) {
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
        for (Property property : properties)
            property.save(buffer);
        try {
            buffer.write("}\r\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int compareTo(net.modificationstation.stationapi.api.common.config.Category o) {
        return getName().compareTo(o.getName());
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof net.modificationstation.stationapi.api.common.config.Category && getName().equals(((net.modificationstation.stationapi.api.common.config.Category) o).getName());
    }
}
