package net.modificationstation.stationapi.api.util;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileNameUtil {

    private static final char[] INVALID_CHARS_LEVEL_NAME = new char[] {'/', '\n', '\r', '\t', '\u0000', '\f', '`', '?', '*', '\\', '<', '>', '|', '\"', ':'};
    private static final Pattern FILE_NAME_WITH_COUNT = Pattern.compile("(<name>.*) \\((<count>\\d*)\\)", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    private static final int MAX_NAME_LENGTH = 255;
    private static final Pattern RESERVED_WINDOWS_NAMES = Pattern.compile(".*\\.|(?:COM|CLOCK\\$|CON|PRN|AUX|NUL|COM[1-9]|LPT[1-9])(?:\\..*)?", Pattern.CASE_INSENSITIVE);

    public static String getNextUniqueName(Path path, String name, String extension) throws IOException {
        for (char c : INVALID_CHARS_LEVEL_NAME) {
            name = name.replace(c, '_');
        }
        if (RESERVED_WINDOWS_NAMES.matcher(name = name.replaceAll("[./\"]", "_")).matches()) {
            name = "_" + name + "_";
        }
        Matcher matcher = FILE_NAME_WITH_COUNT.matcher(name);
        int i = 0;
        if (matcher.matches()) {
            name = matcher.group("name");
            i = Integer.parseInt(matcher.group("count"));
        }
        if (name.length() > MAX_NAME_LENGTH - extension.length()) {
            name = name.substring(0, MAX_NAME_LENGTH - extension.length());
        }
        while (true) {
            String string = name;
            if (i != 0) {
                String string2 = " (" + i + ")";
                int j = MAX_NAME_LENGTH - string2.length();
                if (string.length() > j) {
                    string = string.substring(0, j);
                }
                string = string + string2;
            }
            string = string + extension;
            Path path2 = path.resolve(string);
            try {
                Path path3 = Files.createDirectory(path2);
                Files.deleteIfExists(path3);
                return path.relativize(path3).toString();
            }
            catch (FileAlreadyExistsException fileAlreadyExistsException) {
                ++i;
            }
        }
    }

    public static boolean isNormal(Path path) {
        Path path2 = path.normalize();
        return path2.equals(path);
    }

    public static boolean isAllowedName(Path path) {
        for (Path path2 : path) {
            if (!RESERVED_WINDOWS_NAMES.matcher(path2.toString()).matches()) continue;
            return false;
        }
        return true;
    }

    public static Path getResourcePath(Path path, String resourceName, String extension) {
        String string = resourceName + extension;
        Path path2 = Paths.get(string);
        if (path2.endsWith(extension)) {
            throw new InvalidPathException(string, "empty resource name");
        }
        return path.resolve(path2);
    }

    public static String getPosixFullPath(String path) {
        return FilenameUtils.getFullPath(path).replace(File.separator, "/");
    }

    public static String normalizeToPosix(String path) {
        return FilenameUtils.normalize(path).replace(File.separator, "/");
    }
}

