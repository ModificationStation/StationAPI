package net.modificationstation.stationloader.impl.common.config;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

public class Category implements Comparable<Category> {

    public Category(String name) {
        this.name = name;
    }

    public Property getProperty(String name) {
        for (Property property : properties)
            if (name.equals(property.name))
                return property;
        Property property = new Property(name);
        properties.add(property);
        return property;
    }

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
    public int compareTo(Category o) {
        return name.compareTo(o.name);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Category && name.equals(((Category) o).name);
    }

    public final String name;
    public final Set<Property> properties = new TreeSet<>();
}
