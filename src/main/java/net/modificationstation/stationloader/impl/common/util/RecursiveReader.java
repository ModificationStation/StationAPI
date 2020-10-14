package net.modificationstation.stationloader.impl.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class RecursiveReader {

    public RecursiveReader(String path) {
        this(path, null);
    }

    public RecursiveReader(String path, Function<String, Boolean> formatter) {
        this.path = path;
        this.formatter = formatter;
    }

    public Set<String> read() throws IOException, URISyntaxException {
        Set<String> files = new HashSet<>();
        URL rPath = getClass().getResource(path);
        BufferedReader reader;
        String filePath;
        reader = new BufferedReader(new InputStreamReader(rPath.openStream()));
        filePath = reader.readLine();
        URL file;
        String actualFilePath;
        while (filePath != null) {
            actualFilePath = path + "/" + filePath;
            file = getClass().getResource(actualFilePath);
            if (new File(file.toURI()).isFile()) {
                if (formatter == null || formatter.apply(actualFilePath))
                    files.add(actualFilePath);
            } else
                files.addAll(new RecursiveReader(actualFilePath, formatter).read());
            filePath = reader.readLine();
        }
        reader.close();
        return files;
    }

    private final String path;
    private final Function<String, Boolean> formatter;
}
