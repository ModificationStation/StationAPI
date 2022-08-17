package net.modificationstation.stationapi.mixin.resourceloader.client;

import com.google.common.collect.Lists;
import net.minecraft.client.resource.ZippedTexturePack;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.resource.*;
import net.modificationstation.stationapi.api.resource.metadata.ResourceMetadataReader;
import net.modificationstation.stationapi.api.util.exception.MissingModException;
import net.modificationstation.stationapi.impl.client.resource.SuperResourcePack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Predicate;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;

@Mixin(ZippedTexturePack.class)
public abstract class MixinZippedTexturePack implements ResourcePack, SuperResourcePack {

    @Shadow private ZipFile zipFile;

    @Shadow private File file;

    @Shadow public abstract InputStream getResourceAsStream(String string);

    @Inject(
            method = "getResourceAsStream(Ljava/lang/String;)Ljava/io/InputStream;",
            at = @At("RETURN"),
            cancellable = true
    )
    private void fakeResource(String path, CallbackInfoReturnable<InputStream> cir) {
        if (cir.getReturnValue() == null)
            cir.setReturnValue(FakeResourceManager.get(path));
    }

    @Unique
    @Override
    public @Nullable InputStream openRoot(String fileName) {
        if (fileName.contains("/") || fileName.contains("\\"))
            throw new IllegalArgumentException("Root resources can only be filenames, not paths (no / allowed!)");
        return this.getResourceAsStream(fileName);
    }

    @Unique
    @Override
    public InputStream open(ResourceType type, Identifier id) {
        return this.getResourceAsStream(AbstractFileResourcePack.getFilename(type, id));
    }

    @Unique
    @Override
    public Collection<Identifier> findResources(ResourceType type, String namespace, String prefix, Predicate<Identifier> allowedPathPredicate) {
        Enumeration<? extends ZipEntry> enumeration = zipFile.entries();
        ArrayList<Identifier> list = Lists.newArrayList();
        String string = type.getDirectory() + "/" + namespace + "/";
        String string2 = string + prefix + "/";
        while (enumeration.hasMoreElements()) {
            String string3;
            ZipEntry zipEntry = enumeration.nextElement();
            if (zipEntry.isDirectory() || (string3 = zipEntry.getName()).endsWith(".mcmeta") || !string3.startsWith(string2)) continue;
            String string4 = string3.substring(string.length());
            Identifier identifier;
            try {
                identifier = Identifier.of(ModID.of(namespace), string4);
            } catch (MissingModException e) {
                LOGGER.warn("Invalid path in datapack: {}:{}, ignoring", namespace, string4);
                continue;
            }
            if (!allowedPathPredicate.test(identifier)) continue;
            list.add(identifier);
        }
        return list;
    }

    @Unique
    public boolean containsFile(String name) {
        return zipFile.getEntry(name) != null;
    }

    @Unique
    @Override
    public boolean contains(ResourceType type, Identifier id) {
        return this.containsFile(AbstractFileResourcePack.getFilename(type, id)) || stationapi_superContains(type, id);
    }

    @Unique
    @Override
    public Set<String> getNamespaces(ResourceType type) {
        Enumeration<? extends ZipEntry> enumeration = zipFile.entries();
        HashSet<String> set = new HashSet<>();
        while (enumeration.hasMoreElements()) {
            ArrayList<String> list;
            ZipEntry zipEntry = enumeration.nextElement();
            String string = zipEntry.getName();
            if (!string.startsWith(type.getDirectory() + "/") || (list = Lists.newArrayList(ZipResourcePack.TYPE_NAMESPACE_SPLITTER.split(string))).size() <= 1) continue;
            String string2 = list.get(1);
            if (string2.equals(string2.toLowerCase(Locale.ROOT))) {
                set.add(string2);
                continue;
            }
            this.warnNonLowerCaseNamespace(string2);
        }
        set.addAll(stationapi_getSuperNamespaces());
        return set;
    }

    @Unique
    protected void warnNonLowerCaseNamespace(String namespace) {
        LOGGER.warn("ResourcePack: ignored non-lowercase namespace: {} in {}", namespace, file);
    }

    @Unique
    @Override
    @Nullable
    public <T> T parseMetadata(ResourceMetadataReader<T> metaReader) throws IOException {
        try (InputStream inputStream = this.getResourceAsStream("pack.mcmeta")) {
            return AbstractFileResourcePack.parseMetadata(metaReader, inputStream);
        }
    }

    @Unique
    @Override
    public String getName() {
        return file.getName();
    }
}
