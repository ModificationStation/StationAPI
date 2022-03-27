package net.modificationstation.stationapi.api.resource;

import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.util.API;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.jar.JarEntry;

public class RecursiveReader {

    private final String path;
    private final Predicate<String> filter;

    @API
    public RecursiveReader(String path) {
        this(path, null);
    }

    @API
    public RecursiveReader(String path, Predicate<String> filter) {
        this.path = path.startsWith("/") ? path.substring(1) : path;
        this.filter = filter;
    }

    @API
    public Set<URL> read() throws IOException, URISyntaxException {
        Set<URL> files = new HashSet<>();
        ClassLoader classLoader = getClass().getClassLoader().getParent();
        Enumeration<URL> urls = classLoader.getResources(path);
        while (urls.hasMoreElements()) {
            URL rPath = urls.nextElement();
            URLConnection connection = rPath.openConnection();
            try {
                if (connection instanceof JarURLConnection jarConnection) {
                    Enumeration<JarEntry> entries = jarConnection.getJarFile().entries();
                    for (JarEntry jarEntry = entries.nextElement(); entries.hasMoreElements(); jarEntry = entries.nextElement()) {
                        String name = jarEntry.getName();
                        if (!jarEntry.isDirectory() && name.startsWith(path) && filter.test(name)) {
                            files.add(classLoader.getResource(jarEntry.getName()));
                        }
                    }
                } else {
                    String path = rPath.toURI().getPath();
                    if (path != null) {
                        File basePath = new File(path.split("!/")[0].replace("\\", "/").replace("file:/", ""));
                        if (basePath.isDirectory())
                            Files.walk(Paths.get(String.valueOf(basePath)))
                                    .filter((pathpath) -> filter.test(pathpath.toString()))
                                    .forEach((pathUrl) -> {
                                        try {
                                            files.add(pathUrl.toUri().toURL());
                                        } catch (MalformedURLException e) {
                                            e.printStackTrace();
                                        }
                                    });
                    }
                }
            } catch (Exception e) {
                StationAPI.LOGGER.error("RecursiveReader was given an invalid URL or a corrupt JAR/ZIP!");
                e.printStackTrace();
            }
        }
        return files;
    }
}
