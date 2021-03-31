package net.modificationstation.stationapi.api.common.config;

import net.modificationstation.stationapi.api.common.util.Named;
import net.modificationstation.stationapi.api.common.util.Writeable;

public interface Property extends Named, Writeable, Comparable<Property> {

    void setValue(Object o);

    String getStringValue();

    int getIntValue();

    boolean getBooleanValue();

    void setComment(String comment);
}
