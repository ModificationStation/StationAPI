package net.modificationstation.stationapi.api.client.render.mesh;

/**
 * Decouples models from the vertex format(s) used by
 * ModelRenderer to allow compatibility across diverse implementations.
 */
public interface MeshBuilder {

	/**
	 * Returns the {@link QuadEmitter} used to append quad to this mesh.
	 * Calling this method a second time invalidates any prior result.
	 * Do not retain references outside the context of building the mesh.
	 */
	QuadEmitter getEmitter();

	/**
	 * Returns a new {@link Mesh} instance containing all
	 * quads added to this builder and resets the builder to an empty state.
	 */
	Mesh build();
}