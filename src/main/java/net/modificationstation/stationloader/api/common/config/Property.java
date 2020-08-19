package net.modificationstation.stationloader.api.common.config;

import net.modificationstation.stationloader.api.common.util.Named;
import net.modificationstation.stationloader.api.common.util.Writeable;

public interface Property extends Named, Writeable, Comparable<Property> {

    void setValue(Object o);

    String getStringValue();

    int getIntValue();

    boolean getBooleanValue();

    void setComment(String comment);
}
