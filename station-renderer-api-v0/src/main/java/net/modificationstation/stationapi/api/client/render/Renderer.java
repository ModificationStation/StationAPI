package net.modificationstation.stationapi.api.client.render;

import net.modificationstation.stationapi.api.client.render.material.MaterialFinder;
import net.modificationstation.stationapi.api.client.render.material.RenderMaterial;
import net.modificationstation.stationapi.api.client.render.mesh.MeshBuilder;
import net.modificationstation.stationapi.api.client.render.model.BakedModelRenderer;
import net.modificationstation.stationapi.api.registry.Identifier;
import org.jetbrains.annotations.Nullable;

/**
 * Interface for rendering plug-ins that provide enhanced capabilities
 * for model lighting, buffering and rendering. Such plug-ins implement the
 * enhanced model rendering interfaces specified by the Fabric API.
 */
public interface Renderer {
	/**
	 * Obtain a new {@link MeshBuilder} instance used to create
	 * baked models with enhanced features.
	 *
	 * <p>Renderer does not retain a reference to returned instances and they should be re-used for
	 * multiple models when possible to avoid memory allocation overhead.
	 */
	MeshBuilder meshBuilder();

	/**
	 * Obtain a new {@link MaterialFinder} instance used to retrieve
	 * standard {@link RenderMaterial} instances.
	 *
	 * <p>Renderer does not retain a reference to returned instances and they should be re-used for
	 * multiple materials when possible to avoid memory allocation overhead.
	 */
	MaterialFinder materialFinder();

	/**
	 * Return a material previously registered via {@link #registerMaterial(Identifier, RenderMaterial)}.
	 * Will return null if no material was found matching the given identifier.
	 */
	@Nullable
	RenderMaterial materialById(Identifier id);

	/**
	 * Register a material for re-use by other mods or models within a mod.
	 * The registry does not persist registrations - mods must create and register
	 * all materials at game initialization.
	 *
	 * <p>Returns false if a material with the given identifier is already present,
	 * leaving the existing material intact.
	 */
	boolean registerMaterial(Identifier id, RenderMaterial material);

	/**
	 * Obtain a new {@link BakedModelRenderer} instance used to render
	 * baked models.
	 *
	 *
	 */
	BakedModelRenderer bakedModelRenderer();
}