package net.modificationstation.stationapi.api.resource;

import com.google.common.base.Splitter;

public class ZipResourcePack {

    public static final Splitter TYPE_NAMESPACE_SPLITTER = Splitter.on('/').omitEmptyStrings().limit(3);
}
