package net.modificationstation.stationapi.api.resource;

import com.google.gson.JsonElement;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import it.unimi.dsi.fastutil.objects.ReferenceSet;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.modificationstation.stationapi.api.event.resource.RuntimeResourcesEvent;
import net.modificationstation.stationapi.api.resource.metadata.ResourceMetadataReader;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.impl.resource.ModResourcePack;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Set;

/**
 * A resource pack whose contents are supplied by code instead of by files in a mod JAR.
 *
 * <p>Runtime packs are indistinguishable from file-backed packs to everything downstream of
 * {@link ResourceManager}: they take part in pack layering, can be overridden by resource packs the
 * player enables, honor the {@code filter} section of other packs' {@code pack.mcmeta}, and are
 * ordered against other mods' packs by the usual {@code station-resource-loader-v0:<type>_priority}
 * custom values. Consequently there is no need for a loader to grow its own injection hook - if a
 * loader reads a resource, a runtime pack can supply it.
 *
 * <p>Packs are populated per reload, through {@link RuntimeResourcesEvent},
 * so generated content is free to depend on registry state and is regenerated when the player
 * reloads resources.
 *
 * @see RuntimeResourcesEvent
 */
public class RuntimeResourcePack implements ModResourcePack {

    private final Namespace owner;
    private final ResourceType type;
    private final Reference2ReferenceMap<Identifier, InputSupplier<InputStream>> resources = new Reference2ReferenceOpenHashMap<>();
    private final ReferenceSet<Namespace> namespaces = new ReferenceOpenHashSet<>();

    public RuntimeResourcePack(Namespace owner, ResourceType type) {
        this.owner = owner;
        this.type = type;
    }

    /**
     * {@return this pack, scoped to the resources {@code finder} looks for}
     *
     * <p>Within a scope, resources are named by the IDs their loader knows them by, and the
     * directory and extension are left to the finder:
     *
     * <pre>{@code
     * pack.with(BLOCK_STATES_FINDER)
     *         .add(Identifier.of("minecraft:sponge"), spongeState)
     *         .add(Identifier.of("minecraft:dirt"), dirtState);
     * }</pre>
     *
     * @param finder the finder of the loader that should pick these resources up
     */
    public Scope with(ResourceFinder finder) {
        return new Scope(finder);
    }

    /**
     * Adds a resource at an explicit resource path.
     *
     * <p>The path is the full in-pack path, i.e. what a file under
     * {@code assets/<namespace>/<path>} would resolve to. Prefer {@link #with(ResourceFinder)} when
     * the target loader exposes its finder, so that the directory and extension aren't restated
     * here.
     *
     * @param path    the resource path
     * @param content supplier of the resource's bytes, invoked every time the resource is read
     */
    public RuntimeResourcePack add(Identifier path, InputSupplier<InputStream> content) {
        resources.put(path, content);
        namespaces.add(path.namespace);
        return this;
    }

    public RuntimeResourcePack add(Identifier path, byte[] content) {
        return add(path, () -> new ByteArrayInputStream(content));
    }

    public RuntimeResourcePack add(Identifier path, String content) {
        return add(path, content.getBytes(StandardCharsets.UTF_8));
    }

    public RuntimeResourcePack add(Identifier path, JsonElement content) {
        return add(path, content.toString());
    }

    public boolean isEmpty() {
        return resources.isEmpty();
    }

    @Nullable
    @Override
    public InputSupplier<InputStream> openRoot(String... segments) {
        // Root files (pack.mcmeta, pack.png) are supplied by the enclosing mod pack.
        return null;
    }

    @Nullable
    @Override
    public InputSupplier<InputStream> open(ResourceType type, Identifier id) {
        return this.type == type ? resources.get(id) : null;
    }

    @Override
    public void findResources(ResourceType type, Namespace namespace, String prefix, ResultConsumer consumer) {
        if (this.type != type || prefix.startsWith("/")) return;
        resources.forEach((id, content) -> {
            if (id.namespace == namespace && isUnder(id.path, prefix)) consumer.accept(id, content);
        });
    }

    /**
     * Matches whole path segments only, so that a search for {@code stationapi/block} doesn't
     * report resources living in {@code stationapi/blockstates}.
     */
    private static boolean isUnder(String path, String prefix) {
        return path.startsWith(prefix) && (path.length() == prefix.length() || path.charAt(prefix.length()) == '/');
    }

    @Override
    public Set<Namespace> getNamespaces(ResourceType type) {
        return this.type == type ? namespaces : Collections.emptySet();
    }

    @Nullable
    @Override
    public <T> T parseMetadata(ResourceMetadataReader<T> metaReader) {
        return null;
    }

    @Override
    public String getName() {
        return owner.getName() + " (generated)";
    }

    @Override
    public void close() {}

    @Override
    public ModMetadata getFabricModMetadata() {
        return owner.getMetadata();
    }

    /**
     * A {@link RuntimeResourcePack} bound to one {@link ResourceFinder}, so that a run of related
     * resources can be added without restating where they go.
     *
     * @see RuntimeResourcePack#with(ResourceFinder)
     */
    public final class Scope {

        private final ResourceFinder finder;

        private Scope(ResourceFinder finder) {
            this.finder = finder;
        }

        /**
         * Adds a resource for the given ID, deriving its path from this scope's finder.
         *
         * @param id      the ID the loader knows the resource by
         * @param content supplier of the resource's bytes, invoked every time the resource is read
         */
        public Scope add(Identifier id, InputSupplier<InputStream> content) {
            RuntimeResourcePack.this.add(finder.toResourcePath(id), content);
            return this;
        }

        public Scope add(Identifier id, byte[] content) {
            RuntimeResourcePack.this.add(finder.toResourcePath(id), content);
            return this;
        }

        public Scope add(Identifier id, String content) {
            RuntimeResourcePack.this.add(finder.toResourcePath(id), content);
            return this;
        }

        public Scope add(Identifier id, JsonElement content) {
            RuntimeResourcePack.this.add(finder.toResourcePath(id), content);
            return this;
        }

        /**
         * {@return the same pack, scoped to {@code finder} instead}
         *
         * <p>Lets one chain cover several kinds of resource.
         */
        public Scope with(ResourceFinder finder) {
            return RuntimeResourcePack.this.with(finder);
        }

        /**
         * {@return the pack this scope adds to}
         *
         * <p>For leaving a chain to add a resource by explicit path.
         */
        public RuntimeResourcePack pack() {
            return RuntimeResourcePack.this;
        }
    }
}
