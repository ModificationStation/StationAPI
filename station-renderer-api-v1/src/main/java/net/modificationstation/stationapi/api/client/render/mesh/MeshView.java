package net.modificationstation.stationapi.api.client.render.mesh;

import org.jetbrains.annotations.Range;

import java.util.function.Consumer;

/**
 * A bundle of {@linkplain QuadView quads} encoded by the renderer. It may be {@linkplain MutableMesh mutable} or
 * {@linkplain Mesh immutable}.
 *
 * <p>Meshes are similar in purpose to {@code List<BakedQuad>} instances passed around in vanilla pipelines, but allow
 * the renderer to optimize their format for performance and memory allocation.
 *
 * <p>All declared methods in this interface are <b>not</b> thread-safe and must not be used concurrently. Subclasses
 * may override this contract.
 *
 * <p>Only the renderer should implement or extend this interface.
 */
public interface MeshView {
    /**
     * Returns the number of quads encoded in this mesh.
     */
    @Range(from = 0, to = Integer.MAX_VALUE)
    int size();

    /**
     * Access all quads encoded in this mesh. The quad instance sent to the consumer should never be retained outside
     * the current call to the consumer.
     *
     * <p>Nesting calls to this method on the same mesh is allowed.
     */
    void forEach(Consumer<? super QuadView> action);

    /**
     * Outputs all quads in this mesh to the given quad emitter.
     */
    void outputTo(QuadEmitter emitter);
}
