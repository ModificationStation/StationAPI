package net.modificationstation.stationapi.api.event.resource;

import com.google.gson.JsonElement;
import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.modificationstation.stationapi.api.resource.InputSupplier;
import net.modificationstation.stationapi.api.resource.ResourceFinder;
import net.modificationstation.stationapi.api.resource.ResourceType;
import net.modificationstation.stationapi.api.resource.RuntimeResourcePack;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;

import java.io.InputStream;
import java.util.Map;

/**
 * Fired once per reload, before packs are handed to the resource manager, to collect resources that
 * mods supply from code rather than from files.
 *
 * <p>Resources added here behave exactly like files shipped in the adding mod's JAR - see
 * {@link RuntimeResourcePack} for what that buys you. Because the event is fired per reload,
 * listeners may consult registry state freely and their output is regenerated when the player
 * reloads resources.
 *
 * <p>{@link #with(ResourceFinder)} names the loader that should pick the resources up, and no path
 * is spelled out by hand:
 *
 * <pre>{@code
 * @EventListener
 * public void assets(RuntimeResourcesEvent.Assets event) {
 *     event.with(BLOCK_STATES_FINDER)
 *             .add(Identifier.of("minecraft:sponge"), spongeState)
 *             .add(Identifier.of("minecraft:dirt"), dirtState);
 * }
 * }</pre>
 *
 * <p>The receiving pack is resolved from the calling class's mod. Callers generating resources from
 * a shared utility class, where that would resolve to the wrong mod, should name their pack
 * explicitly via {@link #pack(Namespace)} and populate it directly.
 */
@SuperBuilder
public abstract class RuntimeResourcesEvent extends Event {

    /**
     * The kind of resources being collected. Matches the concrete event type, and is the type the
     * packs handed out by this event serve.
     */
    public final ResourceType type;

    private final Map<Namespace, RuntimeResourcePack> packs;

    /**
     * {@return the runtime pack owned by {@code namespace}, creating it if this is its first use}
     *
     * <p>The namespace identifies the <em>mod supplying</em> the resources, which is unrelated to
     * the namespaces of the resources themselves - a mod's pack may serve {@code minecraft} paths.
     * It determines pack ordering against other mods' packs and the pack name shown in load errors.
     */
    public RuntimeResourcePack pack(Namespace namespace) {
        return packs.computeIfAbsent(namespace, ns -> new RuntimeResourcePack(ns, type));
    }

    /**
     * {@return the runtime pack owned by the mod that called this method}
     */
    public RuntimeResourcePack pack() {
        return pack(callerNamespace());
    }

    /**
     * {@return the calling mod's pack, scoped to the resources {@code finder} looks for}
     *
     * @see RuntimeResourcePack#with(ResourceFinder)
     */
    public RuntimeResourcePack.Scope with(ResourceFinder finder) {
        return pack(callerNamespace()).with(finder);
    }

    /**
     * Adds a resource at an explicit resource path, to the calling mod's pack.
     *
     * @see RuntimeResourcePack#add(Identifier, InputSupplier)
     */
    public RuntimeResourcePack add(Identifier path, InputSupplier<InputStream> content) {
        return pack(callerNamespace()).add(path, content);
    }

    public RuntimeResourcePack add(Identifier path, byte[] content) {
        return pack(callerNamespace()).add(path, content);
    }

    public RuntimeResourcePack add(Identifier path, String content) {
        return pack(callerNamespace()).add(path, content);
    }

    public RuntimeResourcePack add(Identifier path, JsonElement content) {
        return pack(callerNamespace()).add(path, content);
    }

    /**
     * Resolves the namespace of the first class on the stack that isn't part of this event, so that
     * callers don't have to restate their own namespace. Chaining from {@link #with(ResourceFinder)}
     * resolves once for the whole chain.
     */
    private static Namespace callerNamespace() {
        return Namespace.resolve(
                StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).walk(frames -> frames
                        .map(StackWalker.StackFrame::getDeclaringClass)
                        .filter(caller -> !RuntimeResourcesEvent.class.isAssignableFrom(caller))
                        .findFirst()
                        .orElseThrow()
                )
        );
    }

    /**
     * Collects {@link ResourceType#CLIENT_RESOURCES} - models, blockstates, textures, sounds.
     */
    @SuperBuilder
    public static final class Assets extends RuntimeResourcesEvent {}

    /**
     * Collects {@link ResourceType#SERVER_DATA} - recipes, tags.
     */
    @SuperBuilder
    public static final class Data extends RuntimeResourcesEvent {}
}
