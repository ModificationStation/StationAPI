package net.modificationstation.stationapi.api.common.util;

import java.io.BufferedWriter;

public interface Writeable {

    void save(BufferedWriter buffer);
}
