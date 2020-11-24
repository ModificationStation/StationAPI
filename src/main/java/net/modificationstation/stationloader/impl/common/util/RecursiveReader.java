package net.modificationstation.stationloader.impl.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class RecursiveReader {

    public RecursiveReader(String path) {
        this(path, null);
    }

    public RecursiveReader(String path, Function<String, Boolean> formatter) {
        this.path = path.startsWith("/") ? path.substring(1) : path;
        this.formatter = formatter;
    }

    public Set<URL> read() throws IOException, URISyntaxException {
        Set<URL> files = new HashSet<>();
        ClassLoader classLoader = getClass().getClassLoader().getParent();
        Enumeration<URL> urls = classLoader.getResources(path);
        URL rPath;
        while (urls.hasMoreElements()) {
            rPath = urls.nextElement();
            BufferedReader reader;
            String filePath;
            reader = new BufferedReader(new InputStreamReader(rPath.openStream()));
            filePath = reader.readLine();
            URL file;
            String actualFilePath;
            while (filePath != null) {
                actualFilePath = path + "/" + filePath;
                file = classLoader.getResource(actualFilePath);
                if (file != null) {
                    if (new File(file.toURI()).isFile()) {
                        if (formatter == null || formatter.apply(actualFilePath))
                            files.add(file);
                    } else
                        files.addAll(new RecursiveReader(actualFilePath, formatter).read());
                }
                filePath = reader.readLine();
            }
            reader.close();
        }
        return files;
    }

    private final String path;
    private final Function<String, Boolean> formatter;
}
