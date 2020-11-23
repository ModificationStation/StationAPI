package net.modificationstation.stationloader.impl.common.util;

import java.io.IOException;
import java.net.*;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class RecursiveReader {

    public RecursiveReader(String path) {
        this(path, null);
    }

    public RecursiveReader(String path, Function<String, Boolean> formatter) {
        this.path = path;
        this.formatter = formatter;
    }

    public Set<URL> read() throws IOException, URISyntaxException {
        Set<URL> files = new HashSet<>();
        URL[] urls = ((URLClassLoader) getClass().getClassLoader().getParent()).getURLs();
        for (URL file : urls) {
            try {
                file = new URL(file.toString().replace("file:", "jar:file:") + "!/");
                System.out.println(file);
            } catch (Exception e) {
            }
            try {
                URLConnection urlConnection = file.openConnection();
                JarURLConnection jarConnection = (JarURLConnection) urlConnection;
                JarFile jarFile = jarConnection.getJarFile();
                Enumeration<JarEntry> entries = jarFile.entries();
                System.out.println(entries);

                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    if (formatter == null || (formatter.apply(entry.getName()) && ("/" + entry.getName()).startsWith(path))) {
                        String fileName = file + entry.getName();
                        files.add(new URL(fileName));
                    }
                }
            } catch (Exception e) {
            }
        }
//        System.out.println(path);
//        Enumeration<URL> urls = getClass().getClassLoader().getResources(path);
//        URL rPath;
//        while (urls.hasMoreElements()) {
//            rPath = urls.nextElement();
//            BufferedReader reader;
//            String filePath;
//            reader = new BufferedReader(new InputStreamReader(rPath.openStream()));
//            filePath = reader.readLine();
//            URL file;
//            String actualFilePath;
//            System.out.println(filePath);
//            while (filePath != null) {
//                actualFilePath = path + "/" + filePath;
//                file = getClass().getResource(actualFilePath);
//                if (new File(file.toURI()).isFile()) {
//                    if (formatter == null || formatter.apply(actualFilePath))
//                        files.add(actualFilePath);
//                } else
//                    files.addAll(new RecursiveReader(actualFilePath, formatter).read());
//                filePath = reader.readLine();
//            }
//            reader.close();
//
//        }
//        System.out.println(Arrays.toString(files.toArray()));
        return files;
    }

    private final String path;
    private final Function<String, Boolean> formatter;
}
