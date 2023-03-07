package net.modificationstation.stationapi.api.resource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@FunctionalInterface
public interface InputSupplier<T> {

    static InputSupplier<InputStream> create(Path path) {
        return () -> Files.newInputStream(path);
    }

    static InputSupplier<InputStream> create(ZipFile zipFile, ZipEntry zipEntry) {
        return () -> zipFile.getInputStream(zipEntry);
    }

    T get() throws IOException;
}

