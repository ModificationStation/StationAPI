package net.modificationstation.stationapi.api.common.util;

import java.io.*;

public interface Writeable {

    void save(BufferedWriter buffer);
}
