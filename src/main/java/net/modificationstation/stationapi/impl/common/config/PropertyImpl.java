package net.modificationstation.stationapi.impl.common.config;

import net.modificationstation.stationapi.api.common.config.Property;

import java.io.BufferedWriter;
import java.io.IOException;

public class PropertyImpl implements Property {

    private final String name;
    private String value;
    private String comment;

    public PropertyImpl(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setValue(Object o) {
        value = o instanceof String ? (String) o : o.toString();
    }

    @Override
    public String getStringValue() {
        return value;
    }

    @Override
    public int getIntValue() {
        return Integer.parseInt(value);
    }

    @Override
    public boolean getBooleanValue() {
        return Boolean.parseBoolean(value);
    }

    @Override
    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public void save(BufferedWriter buffer) {
        if (comment != null)
            try {
                buffer.write("    # " + comment + "\r\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        try {
            buffer.write("    " + getName() + "=" + getStringValue() + "\r\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int compareTo(Property o) {
        return getName().compareTo(o.getName());
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Property && getName().equals(((Property) o).getName());
    }
}
