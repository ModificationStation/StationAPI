package net.modificationstation.stationapi.api.client.render.mesh;

/**
 * An immutable bundle of {@linkplain QuadView quads} encoded by the renderer, typically via
 * {@link MutableMesh#immutableCopy()}.
 *
 * <p>All declared methods in this interface and inherited methods from {@link MeshView} are thread-safe and may be used
 * concurrently.
 *
 * <p>Only the renderer should implement or extend this interface.
 *
 * @see MeshView
 * @see MutableMesh
 */
public interface Mesh extends MeshView {
}
