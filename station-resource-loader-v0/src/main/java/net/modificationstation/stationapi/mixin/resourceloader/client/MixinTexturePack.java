package net.modificationstation.stationapi.mixin.resourceloader.client;

import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import it.unimi.dsi.fastutil.objects.ReferenceSet;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.resource.TexturePack;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.resource.*;
import net.modificationstation.stationapi.api.resource.metadata.ResourceMetadataReader;
import net.modificationstation.stationapi.impl.client.resource.SuperResourcePack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;

@Mixin(TexturePack.class)
public abstract class MixinTexturePack implements ResourcePack, SuperResourcePack {

    @Shadow
    public abstract InputStream getResourceAsStream(String string);

    @Shadow public String name;

    @Unique
    private Set<String> stationapi_namespaces;

    @Inject(
            method = "<init>()V",
            at = @At("RETURN")
    )
    private void genNamespaces(CallbackInfo ci) {
        stationapi_namespaces = FabricLoader.getInstance().getAllMods().stream().flatMap(mod -> {
            List<Path> rootPaths = mod.getRootPaths();

            if (rootPaths.isEmpty()) return Stream.of();

            Set<String> namespaces = null;

            for (Path path : rootPaths) {
                Path dir = path.resolve(ResourceType.CLIENT_RESOURCES.getDirectory());
                if (!Files.isDirectory(dir)) continue;

                String separator = path.getFileSystem().getSeparator();

                try (DirectoryStream<Path> ds = Files.newDirectoryStream(dir)) {
                    for (Path p : ds) {
                        if (!Files.isDirectory(p)) continue;

                        String s = p.getFileName().toString();
                        // s may contain trailing slashes, remove them
                        s = s.replace(separator, "");

                        if (!DefaultResourcePack.RESOURCE_PACK_PATH.matcher(s).matches()) {
                            LOGGER.warn("Ignored invalid namespace: {} in mod ID {}", s, mod.getMetadata().getId());
                            continue;
                        }

                        if (namespaces == null) namespaces = new HashSet<>();

                        namespaces.add(s);
                    }
                } catch (IOException e) {
                    LOGGER.warn("getNamespaces in mod " + mod.getMetadata().getId() + " failed!", e);
                }
            }

            return namespaces == null ? Stream.of() : namespaces.stream();
        }).collect(Collectors.toUnmodifiableSet());
    }

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
    public InputStream openRoot(String fileName) {
        if (fileName.contains("/") || fileName.contains("\\"))
            throw new IllegalArgumentException("Root resources can only be filenames, not paths (no / allowed!)");
        return this.getResourceAsStream(fileName);
    }

    @Unique
    @Override
    public InputStream open(ResourceType type, Identifier id) throws IOException {
        InputStream inputStream = this.findInputStream(type, id);
        if (inputStream != null)
            return inputStream;
        throw new FileNotFoundException(id.id);
    }

    @Unique
    @Override
    public Collection<Identifier> findResources(ResourceType type, String namespace, String prefix, Predicate<Identifier> allowedPathPredicate) {
        ReferenceSet<Identifier> set = new ReferenceOpenHashSet<>();
        try {
            Path path = DefaultResourcePack.TYPE_TO_FILE_SYSTEM.get(type);
            if (path != null)
                DefaultResourcePack.collectIdentifiers(set, namespace, path, prefix, allowedPathPredicate);
            else
                LOGGER.error("Can't access assets root for type: {}", type);
        } catch (FileNotFoundException | NoSuchFileException ignored) {
        } catch (IOException iOException) {
            LOGGER.error("Couldn't get a list of all vanilla resources", iOException);
        }
        return set;
    }

    @Unique
    @Nullable
    protected InputStream findInputStream(ResourceType type, Identifier id) {
        String string = DefaultResourcePack.getPath(type, id);
        InputStream stream = null;
        try {
            URL uRL = TexturePack.class.getResource(string);
            if (DefaultResourcePack.isValidUrl(string, uRL))
                stream = uRL.openStream();
        } catch (IOException iOException) {
            stream = getResourceAsStream(string);
        }
        return stream == null ? FakeResourceManager.get(string) : stream;
    }

    @Override
    public boolean stationapi_superContains(ResourceType type, Identifier id) {
        String string = DefaultResourcePack.getPath(type, id);
        boolean present = false;
        try {
            URL uRL = TexturePack.class.getResource(string);
            present = DefaultResourcePack.isValidUrl(string, uRL);
        } catch (IOException ignored) {}
        return present || FakeResourceManager.get(string) != null;
    }

    @Unique
    @Override
    public boolean contains(ResourceType type, Identifier id) {
        return stationapi_superContains(type, id);
    }

    @Override
    public Set<String> stationapi_getSuperNamespaces() {
        return stationapi_namespaces;
    }

    @Unique
    @Override
    public Set<String> getNamespaces(ResourceType type) {
        return stationapi_getSuperNamespaces();
    }

    @Unique
    @Override
    public <T> @Nullable T parseMetadata(ResourceMetadataReader<T> metaReader) throws IOException {
        try (InputStream inputStream = this.openRoot("pack.mcmeta")){
            T object;
            if (inputStream != null && (object = AbstractFileResourcePack.parseMetadata(metaReader, inputStream)) != null) {
                return object;
            }
        } catch (FileNotFoundException | RuntimeException ignored) {}
        return null;
    }

    @Unique
    @Override
    public String getName() {
        return name;
    }

    @Unique
    @Override
    public void close() {}
}
