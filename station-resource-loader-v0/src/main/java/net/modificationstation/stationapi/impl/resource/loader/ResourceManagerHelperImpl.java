package net.modificationstation.stationapi.impl.resource.loader;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.loader.api.ModContainer;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.resource.*;
import net.modificationstation.stationapi.impl.resource.ModNioResourcePack;
import net.modificationstation.stationapi.impl.resource.ResourcePackProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;

public class ResourceManagerHelperImpl implements ResourceManagerHelper {
	private static final Map<ResourceType, ResourceManagerHelperImpl> registryMap = new HashMap<>();
	private static final Set<Pair<String, ModNioResourcePack>> builtinResourcePacks = new HashSet<>();
	private static final Logger LOGGER = LoggerFactory.getLogger(ResourceManagerHelperImpl.class);

	private final Set<Identifier> addedListenerIds = new HashSet<>();
	private final Set<IdentifiableResourceReloadListener> addedListeners = new LinkedHashSet<>();

	public static ResourceManagerHelperImpl get(ResourceType type) {
		return registryMap.computeIfAbsent(type, (t) -> new ResourceManagerHelperImpl());
	}

	/**
	 * Registers a built-in resource pack. Internal implementation.
	 *
	 * @param id             the identifier of the resource pack
	 * @param subPath        the sub path in the mod resources
	 * @param container      the mod container
	 * @param displayName    the display name of the resource pack
	 * @param activationType the activation type of the resource pack
	 * @return {@code true} if successfully registered the resource pack, else {@code false}
	 * @see ResourceManagerHelper#registerBuiltinResourcePack(Identifier, ModContainer, String, ResourcePackActivationType)
	 * @see ResourceManagerHelper#registerBuiltinResourcePack(Identifier, ModContainer, ResourcePackActivationType)
	 */
	public static boolean registerBuiltinResourcePack(Identifier id, String subPath, ModContainer container, String displayName, ResourcePackActivationType activationType) {
		// Assuming the mod has multiple paths, we simply "hope" that the  file separator is *not* different across them
		List<Path> paths = container.getRootPaths();
		String separator = paths.get(0).getFileSystem().getSeparator();
		subPath = subPath.replace("/", separator);
		ModNioResourcePack resourcePack = ModNioResourcePack.create(id, displayName, container, subPath, ResourceType.CLIENT_RESOURCES, activationType);
		ModNioResourcePack dataPack = ModNioResourcePack.create(id, displayName, container, subPath, ResourceType.SERVER_DATA, activationType);
		if (resourcePack == null && dataPack == null) return false;

		if (resourcePack != null) builtinResourcePacks.add(new Pair<>(displayName, resourcePack));

		if (dataPack != null) builtinResourcePacks.add(new Pair<>(displayName, dataPack));

		return true;
	}

	/**
	 * Registers a built-in resource pack. Internal implementation.
	 *
	 * @param id             the identifier of the resource pack
	 * @param subPath        the sub path in the mod resources
	 * @param container      the mod container
	 * @param activationType the activation type of the resource pack
	 * @return {@code true} if successfully registered the resource pack, else {@code false}
	 * @see ResourceManagerHelper#registerBuiltinResourcePack(Identifier, ModContainer, ResourcePackActivationType)
	 * @see ResourceManagerHelper#registerBuiltinResourcePack(Identifier, ModContainer, String, ResourcePackActivationType)
	 */
	public static boolean registerBuiltinResourcePack(Identifier id, String subPath, ModContainer container, ResourcePackActivationType activationType) {
		return registerBuiltinResourcePack(id, subPath, container, id.modID + "/" + id.id, activationType);
	}

	public static void registerBuiltinResourcePacks(ResourceType resourceType, Consumer<ResourcePackProfile> consumer) {
		// Loop through each registered built-in resource packs and add them if valid.
		for (Pair<String, ModNioResourcePack> entry : builtinResourcePacks) {
			ModNioResourcePack pack = entry.getSecond();

			// Add the built-in pack only if namespaces for the specified resource type are present.
			if (!pack.getNamespaces(resourceType).isEmpty()) {
				// Make the resource pack profile for built-in pack, should never be always enabled.
				ResourcePackProfile profile = ResourcePackProfile.create(
						entry.getSecond().getId().toString(),
						entry.getFirst(),
						pack.getActivationType() == ResourcePackActivationType.ALWAYS_ENABLED,
						ignored -> entry.getSecond(),
						resourceType,
						ResourcePackProfile.InsertionPosition.TOP,
						new BuiltinModResourcePackSource(pack.getFabricModMetadata().getName())
				);
				consumer.accept(profile);
			}
		}
	}

	public static List<ResourceReloader> sort(ResourceType type, List<ResourceReloader> listeners) {
		ResourceManagerHelperImpl instance = get(type);

		if (instance != null) {
			List<ResourceReloader> mutable = new ArrayList<>(listeners);
			instance.sort(mutable);
			return Collections.unmodifiableList(mutable);
		}

		return listeners;
	}

	protected void sort(List<ResourceReloader> listeners) {
		listeners.removeAll(addedListeners);

		// General rules:
		// - We *do not* touch the ordering of vanilla listeners. Ever.
		//   While dependency values are provided where possible, we cannot
		//   trust them 100%. Only code doesn't lie.
		// - We addReloadListener all custom listeners after vanilla listeners. Same reasons.

		List<IdentifiableResourceReloadListener> listenersToAdd = Lists.newArrayList(addedListeners);
		Set<Identifier> resolvedIds = new HashSet<>();

		for (ResourceReloader listener : listeners)
			if (listener instanceof IdentifiableResourceReloadListener)
				resolvedIds.add(((IdentifiableResourceReloadListener) listener).getId());

		int lastSize = -1;

		while (listeners.size() != lastSize) {
			lastSize = listeners.size();

			Iterator<IdentifiableResourceReloadListener> it = listenersToAdd.iterator();

			while (it.hasNext()) {
				IdentifiableResourceReloadListener listener = it.next();

				if (resolvedIds.containsAll(listener.getDependencies())) {
					resolvedIds.add(listener.getId());
					listeners.add(listener);
					it.remove();
				}
			}
		}

		for (IdentifiableResourceReloadListener listener : listenersToAdd)
			LOGGER.warn("Could not resolve dependencies for listener: " + listener.getId() + "!");
	}

	@Override
	public void registerReloadListener(IdentifiableResourceReloadListener listener) {
		if (!addedListenerIds.add(listener.getId())) {
			LOGGER.warn("Tried to register resource reload listener " + listener.getId() + " twice!");
			return;
		}

		if (!addedListeners.add(listener))
			throw new RuntimeException("Listener with previously unknown ID " + listener.getId() + " already in listener set!");
	}
}
