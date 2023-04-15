package net.modificationstation.stationapi.impl.resource;

import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.resource.InputSupplier;
import net.modificationstation.stationapi.api.resource.Resource;
import net.modificationstation.stationapi.api.resource.ResourcePack;
import net.modificationstation.stationapi.api.resource.ResourceType;
import net.modificationstation.stationapi.api.resource.metadata.ResourceMetadata;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents a group resource pack, holds multiple resource packs as one.
 */
public abstract class GroupResourcePack implements ResourcePack {
	protected final ResourceType type;
	protected final List<ModResourcePack> packs;
	protected final Map<ModID, List<ModResourcePack>> namespacedPacks = new Reference2ReferenceOpenHashMap<>();

	public GroupResourcePack(ResourceType type, List<ModResourcePack> packs) {
		this.type = type;
		this.packs = packs;
		this.packs.forEach(pack -> pack.getNamespaces(this.type)
				.forEach(namespace -> this.namespacedPacks.computeIfAbsent(namespace, value -> new ArrayList<>())
						.add(pack)));
	}

	@Override
	public @Nullable InputSupplier<InputStream> openRoot(String... segments) {
		List<ModResourcePack> packs = this.namespacedPacks.get(ModID.MINECRAFT);

		if (packs != null) for (int i = packs.size() - 1; i >= 0; i--) {
			ResourcePack pack = packs.get(i);
			InputSupplier<InputStream> supplier = pack.openRoot(segments);

			if (supplier != null) return supplier;
		}

		return null;
	}

	@Override
	public InputSupplier<InputStream> open(ResourceType type, Identifier id) {
		List<ModResourcePack> packs = this.namespacedPacks.get(id.modID);

		if (packs != null) for (int i = packs.size() - 1; i >= 0; i--) {
			ResourcePack pack = packs.get(i);
			InputSupplier<InputStream> supplier = pack.open(type, id);

			if (supplier != null) return supplier;
		}

		return null;
	}

	@Override
	public void findResources(ResourceType type, ModID namespace, String prefix, ResultConsumer consumer) {
		List<ModResourcePack> packs = this.namespacedPacks.get(namespace);

		if (packs == null) return;

		for (int i = packs.size() - 1; i >= 0; i--) {
			ResourcePack pack = packs.get(i);

			pack.findResources(type, namespace, prefix, consumer);
		}
	}

	@Override
	public Set<ModID> getNamespaces(ResourceType type) {
		return this.namespacedPacks.keySet();
	}

	public void appendResources(ResourceType type, Identifier id, List<Resource> resources) {
		List<ModResourcePack> packs = this.namespacedPacks.get(id.modID);

		if (packs == null) return;

		Identifier metadataId = NamespaceResourceManager.getMetadataPath(id);

		for (ModResourcePack pack : packs) {
			InputSupplier<InputStream> supplier = pack.open(type, id);

			if (supplier != null) {
				InputSupplier<ResourceMetadata> metadataSupplier = () -> {
					InputSupplier<InputStream> rawMetadataSupplier = pack.open(this.type, metadataId);
					return rawMetadataSupplier != null ? NamespaceResourceManager.loadMetadata(rawMetadataSupplier) : ResourceMetadata.NONE;
				};

				resources.add(new Resource(pack, supplier, metadataSupplier));
			}
		}
	}

	public String getFullName() {
		return this.getName() + " (" + this.packs.stream().map(ResourcePack::getName).collect(Collectors.joining(", ")) + ")";
	}

	@Override
	public void close() {
		this.packs.forEach(ResourcePack::close);
	}
}
