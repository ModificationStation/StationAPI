package net.modificationstation.stationapi.impl.resource;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.resource.InputSupplier;
import net.modificationstation.stationapi.api.resource.ResourcePack;
import net.modificationstation.stationapi.api.resource.ResourceType;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;

public class ZipResourcePack extends AbstractFileResourcePack {
    public static final Splitter TYPE_NAMESPACE_SPLITTER = Splitter.on('/').omitEmptyStrings().limit(3);
    private final File backingZipFile;
    @Nullable
    private ZipFile file;
    private boolean failedToOpen;

    public ZipResourcePack(String name, File backingZipFile, boolean alwaysStable) {
        super(name, alwaysStable);
        this.backingZipFile = backingZipFile;
    }

    @Nullable
    private ZipFile getZipFile() {
        if (this.failedToOpen) {
            return null;
        }
        if (this.file == null) {
            try {
                this.file = new ZipFile(this.backingZipFile);
            } catch (IOException iOException) {
                LOGGER.error("Failed to open pack {}", this.backingZipFile, iOException);
                this.failedToOpen = true;
                return null;
            }
        }
        return this.file;
    }

    private static String toPath(ResourceType type, Identifier id) {
        return String.format(Locale.ROOT, "%s/%s/%s", type.getDirectory(), id.namespace, id.path);
    }

    @Override
    @Nullable
    public InputSupplier<InputStream> openRoot(String ... segments) {
        return this.openFile(String.join("/", segments));
    }

    @Override
    public InputSupplier<InputStream> open(ResourceType type, Identifier id) {
        return this.openFile(ZipResourcePack.toPath(type, id));
    }

    @Nullable
    private InputSupplier<InputStream> openFile(String path) {
        ZipFile zipFile = this.getZipFile();
        if (zipFile == null) {
            return null;
        }
        ZipEntry zipEntry = zipFile.getEntry(path);
        if (zipEntry == null) {
            return null;
        }
        return InputSupplier.create(zipFile, zipEntry);
    }

    @Override
    public Set<Namespace> getNamespaces(ResourceType type) {
        ZipFile zipFile = this.getZipFile();
        if (zipFile == null) {
            return Set.of();
        }
        Enumeration<? extends ZipEntry> enumeration = zipFile.entries();
        HashSet<Namespace> set = new HashSet<>();
        while (enumeration.hasMoreElements()) {
            ArrayList<String> list;
            ZipEntry zipEntry = enumeration.nextElement();
            String string = zipEntry.getName();
            if (!string.startsWith(type.getDirectory() + "/") || (list = Lists.newArrayList(TYPE_NAMESPACE_SPLITTER.split(string))).size() <= 1) continue;
            String string2 = list.get(1);
            if (string2.equals(string2.toLowerCase(Locale.ROOT))) {
                set.add(Namespace.of(string2));
                continue;
            }
            LOGGER.warn("Ignored non-lowercase namespace: {} in {}", string2, this.backingZipFile);
        }
        return set;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }

    @Override
    public void close() {
        if (this.file != null) {
            IOUtils.closeQuietly(this.file);
            this.file = null;
        }
    }

    @Override
    public void findResources(ResourceType type, Namespace namespace, String prefix, ResourcePack.ResultConsumer consumer) {
        ZipFile zipFile = this.getZipFile();
        if (zipFile == null) {
            return;
        }
        Enumeration<? extends ZipEntry> enumeration = zipFile.entries();
        String string = type.getDirectory() + "/" + namespace + "/";
        String string2 = string + prefix + "/";
        while (enumeration.hasMoreElements()) {
            String string3;
            ZipEntry zipEntry = enumeration.nextElement();
            if (zipEntry.isDirectory() || !(string3 = zipEntry.getName()).startsWith(string2)) continue;
            String string4 = string3.substring(string.length());
            Identifier identifier = Identifier.of(namespace, string4);
            consumer.accept(identifier, InputSupplier.create(zipFile, zipEntry));
        }
    }
}

