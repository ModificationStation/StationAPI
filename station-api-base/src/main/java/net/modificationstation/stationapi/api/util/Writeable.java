package net.modificationstation.stationapi.api.util;

import java.io.*;

public interface Writeable {

    void save(BufferedWriter buffer);
}
