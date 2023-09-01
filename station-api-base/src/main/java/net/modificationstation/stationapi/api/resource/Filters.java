package net.modificationstation.stationapi.api.resource;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @deprecated Use station-resource-loader-v0 instead.
 */
@Deprecated
public final class Filters {
    public static final Predicate<String>
            ALL = file -> true;

    public static final class FileType {
        public static final Function<String, Predicate<String>>
                FILE_TYPE_FACTORY = fileType -> file -> file.endsWith("." + fileType);

        public static final Predicate<String>
                JSON = FILE_TYPE_FACTORY.apply("json");
    }
}
