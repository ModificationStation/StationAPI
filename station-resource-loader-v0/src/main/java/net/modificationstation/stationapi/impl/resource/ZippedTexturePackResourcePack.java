package net.modificationstation.stationapi.impl.resource;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import net.minecraft.client.resource.ZippedTexturePack;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.resource.InputSupplier;
import net.modificationstation.stationapi.api.resource.ResourceType;
import net.modificationstation.stationapi.mixin.resourceloader.client.ZippedTexturePackAccessor;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;
import static net.modificationstation.stationapi.impl.resource.ZipResourcePack.TYPE_NAMESPACE_SPLITTER;

public class ZippedTexturePackResourcePack extends AbstractFileResourcePack {

    private final ZippedTexturePack texturePack;
    private final ZippedTexturePackAccessor texturePackAccessor;

    public ZippedTexturePackResourcePack(ZippedTexturePack texturePack, boolean alwaysStable) {
        super(((ZippedTexturePackAccessor) texturePack).getZipFile().getName(), alwaysStable);
        this.texturePack = texturePack;
        this.texturePackAccessor = (ZippedTexturePackAccessor) texturePack;
    }

    private static String toPath(ResourceType type, Identifier id) {
        return id.modID == ModID.MINECRAFT && id.id.startsWith("/") ? id.id.substring(1) : String.format(Locale.ROOT, "%s/%s/%s", type.getDirectory(), id.modID, id.id);
    }

    @Override
    public @Nullable InputSupplier<InputStream> openRoot(String... segments) {
        return openFile(String.join("/", segments));
    }

    @Override
    public @Nullable InputSupplier<InputStream> open(ResourceType type, Identifier id) {
        return openFile(toPath(type, id));
    }

    @Nullable
    private InputSupplier<InputStream> openFile(String path) {
        ZipFile zipFile = texturePackAccessor.getZipFile();
        if (zipFile == null) return null;
        ZipEntry zipEntry = zipFile.getEntry(path);
        if (zipEntry == null) return switch (path) {
            case "pack.mcmeta" -> () -> {
                String metadata = ModResourcePackUtil.serializeMetadata(13, texturePack.description);
                return IOUtils.toInputStream(metadata, Charsets.UTF_8);
            };
            default -> null;
        };
        return InputSupplier.create(zipFile, zipEntry);
    }

    @Override
    public void findResources(ResourceType type, ModID namespace, String prefix, ResultConsumer consumer) {
        ZipFile zipFile = texturePackAccessor.getZipFile();
        if (zipFile == null) return;
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

    @Override
    public Set<ModID> getNamespaces(ResourceType type) {
        ZipFile zipFile = texturePackAccessor.getZipFile();
        if (zipFile == null) return Set.of();
        Enumeration<? extends ZipEntry> enumeration = zipFile.entries();
        HashSet<ModID> set = new HashSet<>();
        set.add(ModID.MINECRAFT); // root
        while (enumeration.hasMoreElements()) {
            ArrayList<String> list;
            ZipEntry zipEntry = enumeration.nextElement();
            String string = zipEntry.getName();
            if (!string.startsWith(type.getDirectory() + "/") || (list = Lists.newArrayList(TYPE_NAMESPACE_SPLITTER.split(string))).size() <= 1) continue;
            String string2 = list.get(1);
            if (string2.equals(string2.toLowerCase(Locale.ROOT))) {
                set.add(ModID.of(string2));
                continue;
            }
            LOGGER.warn("Ignored non-lowercase namespace: {} in {}", string2, zipFile.getName());
        }
        return set;
    }

    @Override
    public void close() {
        // done by texture pack
    }
}
