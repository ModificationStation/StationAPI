package net.modificationstation.stationapi.api.resource;

import com.google.common.base.CharMatcher;
import net.modificationstation.stationapi.api.util.Util;

import java.io.File;
import java.io.IOException;

public class DirectoryResourcePack {
    private static final boolean IS_WINDOWS = Util.getOperatingSystem() == Util.OperatingSystem.WINDOWS;
    private static final CharMatcher BACKSLASH_MATCHER = CharMatcher.is('\\');

    public static boolean isValidPath(File file, String filename) throws IOException {
        String string = file.getCanonicalPath();
        if (IS_WINDOWS) {
            string = BACKSLASH_MATCHER.replaceFrom(string, '/');
        }
        return string.endsWith(filename);
    }
}
