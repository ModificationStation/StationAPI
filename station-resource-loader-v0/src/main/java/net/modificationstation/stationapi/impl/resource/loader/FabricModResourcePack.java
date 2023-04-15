package net.modificationstation.stationapi.impl.resource.loader;

import com.google.common.base.Charsets;
import net.fabricmc.loader.api.FabricLoader;
import net.modificationstation.stationapi.api.resource.InputSupplier;
import net.modificationstation.stationapi.api.resource.ResourceType;
import net.modificationstation.stationapi.api.resource.metadata.ResourceMetadataReader;
import net.modificationstation.stationapi.impl.resource.AbstractFileResourcePack;
import net.modificationstation.stationapi.impl.resource.GroupResourcePack;
import net.modificationstation.stationapi.impl.resource.ModResourcePack;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

/**
 * The Fabric mods resource pack, holds all the mod resource packs as one pack.
 */
public class FabricModResourcePack extends GroupResourcePack {
	public FabricModResourcePack(ResourceType type, List<ModResourcePack> packs) {
		super(type, packs);
	}

	@Override
	public InputSupplier<InputStream> openRoot(String... pathSegments) {
		String fileName = String.join("/", pathSegments);

		return switch (fileName) {
			case "pack.mcmeta" -> {
				String description = "pack.description.modResources";
				String fallback = "Mod resources.";
				String pack = String.format("{\"pack\":{\"pack_format\":" + switch (type) {
					case CLIENT_RESOURCES -> 13;
					case SERVER_DATA -> 12;
				} + ",\"description\":{\"translate\":\"%s\",\"fallback\":\"%s.\"}}}", description, fallback);
				yield () -> IOUtils.toInputStream(pack, Charsets.UTF_8);
			}
			case "pack.png" -> FabricLoader.getInstance().getModContainer("station-resource-loader-v0")
					.flatMap(container -> container.getMetadata().getIconPath(512).flatMap(container::findPath))
					.<InputSupplier<InputStream>>map(path -> () -> Files.newInputStream(path))
					.orElse(null);
			default -> null;
		};
	}

	@Override
	public <T> @Nullable T parseMetadata(ResourceMetadataReader<T> metaReader) throws IOException {
		InputSupplier<InputStream> inputSupplier = this.openRoot("pack.mcmeta");

		if (inputSupplier == null) return null;
		try (InputStream input = inputSupplier.get()) {
			return AbstractFileResourcePack.parseMetadata(metaReader, input);
		}
	}

	@Override
	public String getName() {
		return "Fabric Mods";
	}

	@Override
	public boolean isAlwaysStable() {
		return true;
	}
}
