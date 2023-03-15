package net.modificationstation.stationapi.api.util;

import com.mojang.serialization.DataResult;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PathUtil {
    private static final Pattern FILE_NAME_WITH_COUNT = Pattern.compile("(<name>.*) \\((<count>\\d*)\\)", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    private static final int MAX_NAME_LENGTH = 255;
    private static final Pattern RESERVED_WINDOWS_NAMES = Pattern.compile(".*\\.|(?:COM|CLOCK\\$|CON|PRN|AUX|NUL|COM[1-9]|LPT[1-9])(?:\\..*)?", Pattern.CASE_INSENSITIVE);
    private static final Pattern VALID_FILE_NAME = Pattern.compile("[-._a-z0-9]+");
    private static final char[] INVALID_CHARS_LEVEL_NAME = new char[] {'/', '\n', '\r', '\t', '\u0000', '\f', '`', '?', '*', '\\', '<', '>', '|', '"', ':'};

    public static String getNextUniqueName(Path path, String name, String extension) throws IOException {
        char[] var3 = INVALID_CHARS_LEVEL_NAME;
        int i = var3.length;
        for(int var5 = 0; var5 < i; ++var5) {
            char c = var3[var5];
            name = name.replace(c, '_');
        }
        name = name.replaceAll("[./\"]", "_");
        if (RESERVED_WINDOWS_NAMES.matcher(name).matches()) name = "_" + name + "_";
        Matcher matcher = FILE_NAME_WITH_COUNT.matcher(name);
        i = 0;
        if (matcher.matches()) {
            name = matcher.group("name");
            i = Integer.parseInt(matcher.group("count"));
        }
        if (name.length() > MAX_NAME_LENGTH - extension.length())
            name = name.substring(0, MAX_NAME_LENGTH - extension.length());
        while(true) {
            String string = name;
            if (i != 0) {
                String string2 = " (" + i + ")";
                int j = MAX_NAME_LENGTH - string2.length();
                if (name.length() > j) string = name.substring(0, j);

                string = string + string2;
            }
            string = string + extension;
            Path path2 = path.resolve(string);
            try {
                Path path3 = Files.createDirectory(path2);
                Files.deleteIfExists(path3);
                return path.relativize(path3).toString();
            } catch (FileAlreadyExistsException var8) {
                ++i;
            }
        }
    }

    public static boolean isNormal(Path path) {
        Path path2 = path.normalize();
        return path2.equals(path);
    }

    public static boolean isAllowedName(Path path) {
        Iterator<Path> var1 = path.iterator();
        Path path2;
        do {
            if (!var1.hasNext()) return true;
            path2 = var1.next();
        } while(!RESERVED_WINDOWS_NAMES.matcher(path2.toString()).matches());
        return false;
    }

    public static Path getResourcePath(Path path, String resourceName, String extension) {
        String string = resourceName + extension;
        Path path2 = Paths.get(string);
        if (path2.endsWith(extension)) throw new InvalidPathException(string, "empty resource name");
        else return path.resolve(path2);
    }

    public static String getPosixFullPath(String path) {
        return FilenameUtils.getFullPath(path).replace(File.separator, "/");
    }

    public static String normalizeToPosix(String path) {
        return FilenameUtils.normalize(path).replace(File.separator, "/");
    }

    public static DataResult<List<String>> split(String path) {
        int i = path.indexOf(47);
        if (i == -1) {
            return switch (path) {
                case "", ".", ".." -> DataResult.error(() -> "Invalid path '" + path + "'");
                default ->
                        !isFileNameValid(path) ? DataResult.error(() -> "Invalid path '" + path + "'") : DataResult.success(List.of(path));
            };
        } else {
            List<String> list = new ArrayList<>();
            int j = 0;
            boolean bl = false;
            while(true) {
                String string;
                switch (string = path.substring(j, i)) {
                    case "", ".", ".." -> {
                        return DataResult.error(() -> "Invalid segment '" + string + "' in path '" + path + "'");
                    }
                }
                if (!isFileNameValid(string))
                    return DataResult.error(() -> "Invalid segment '" + string + "' in path '" + path + "'");
                list.add(string);
                if (bl) return DataResult.success(list);
                j = i + 1;
                i = path.indexOf(47, j);
                if (i == -1) {
                    i = path.length();
                    bl = true;
                }
            }
        }
    }

    public static Path getPath(Path root, List<String> paths) {
        int i = paths.size();
        Path var10000;
        switch (i) {
            case 0 -> var10000 = root;
            case 1 -> var10000 = root.resolve(paths.get(0));
            default -> {
                String[] strings = new String[i - 1];
                for (int j = 1; j < i; ++j) strings[j - 1] = paths.get(j);
                var10000 = root.resolve(root.getFileSystem().getPath(paths.get(0), strings));
            }
        }
        return var10000;
    }

    public static boolean isFileNameValid(String name) {
        return VALID_FILE_NAME.matcher(name).matches();
    }

    public static void validatePath(String... paths) {
        if (paths.length == 0) throw new IllegalArgumentException("Path must have at least one element");
        else {
            int var2 = paths.length;
            for(int var3 = 0; var3 < var2; ++var3) {
                String string = paths[var3];
                if (string.equals("..") || string.equals(".") || !isFileNameValid(string))
                    throw new IllegalArgumentException("Illegal segment " + string + " in path " + Arrays.toString(paths));
            }

        }
    }

    public static void createDirectories(Path path) throws IOException {
        Files.createDirectories(Files.exists(path) ? path.toRealPath() : path);
    }
}
