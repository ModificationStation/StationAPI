package net.modificationstation.stationapi.impl.resource.loader;

import net.modificationstation.stationapi.api.resource.ResourceType;
import net.modificationstation.stationapi.impl.resource.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Represents a resource pack provider for mods and built-in mods resource packs.
 */
public class ModResourcePackCreator implements ResourcePackProvider {
	public static final ResourcePackSource RESOURCE_PACK_SOURCE = new ResourcePackSource() {
		@Override
		public String decorate(String packName) {
//			return Text.translatable("pack.nameAndSource", packName, Text.translatable("pack.source.fabricmod"));
			return "fixText";
		}

		@Override
		public boolean canBeEnabledLater() {
			return true;
		}
	};
	public static final ModResourcePackCreator CLIENT_RESOURCE_PACK_PROVIDER = new ModResourcePackCreator(ResourceType.CLIENT_RESOURCES);
	private final ResourceType type;

	public ModResourcePackCreator(ResourceType type) {
		this.type = type;
	}

	/**
	 * Registers the resource packs.
	 *
	 * @param consumer The resource pack profile consumer.
	 */
	@Override
	public void register(Consumer<ResourcePackProfile> consumer) {
		/*
			Register order rule in this provider:
			1. Mod resource packs
			2. Mod built-in resource packs

			Register order rule globally:
			1. Default and Vanilla built-in resource packs
			2. Mod resource packs
			3. Mod built-in resource packs
			4. User resource packs
		 */

		// Build a list of mod resource packs.
		List<ModResourcePack> packs = new ArrayList<>();
		ModResourcePackUtil.appendModResourcePacks(packs, type, null);

		if (!packs.isEmpty()) {
			// Make the resource pack profile for mod resource packs.
			// Mod resource packs must always be enabled to avoid issues, and they are inserted
			// on top to ensure that they are applied after vanilla built-in resource packs.
			String title = "pack.name.fabricMods";
			ResourcePackProfile resourcePackProfile = ResourcePackProfile.create("fabric", title,
					true, factory -> new FabricModResourcePack(this.type, packs), type, ResourcePackProfile.InsertionPosition.TOP,
					RESOURCE_PACK_SOURCE);

			if (resourcePackProfile != null) consumer.accept(resourcePackProfile);
		}

		// Register all built-in resource packs provided by mods.
		ResourceManagerHelperImpl.registerBuiltinResourcePacks(this.type, consumer);
	}
}
