package net.modificationstation.stationloader.common.config;

import java.io.BufferedWriter;
import java.io.IOException;

public class Property implements Comparable<Property> {

    public Property(String name) {
        this.name = name;
    }

    public void setValue(Object o) {
        value = o instanceof String ? (String) o : o.toString();
    }

    public String getStringValue() {
        return value;
    }

    public int getIntegerValue() {
        return Integer.parseInt(value);
    }

    public boolean getBooleanValue() {
        return Boolean.parseBoolean(value);
    }

    public void save(BufferedWriter buffer) {
        if (comment != null)
            try {
                buffer.write("    # " + comment + "\r\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        try {
            buffer.write("    " + name + "=" + value + "\r\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int compareTo(Property o) {
        return name.compareTo(o.name);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Property && name.equals(((Property) o).name);
    }

    public final String name;
    private String value;
    public String comment;
}
