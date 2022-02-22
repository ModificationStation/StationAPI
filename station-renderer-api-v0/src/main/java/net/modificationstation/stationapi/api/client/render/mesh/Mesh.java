package net.modificationstation.stationapi.api.client.render.mesh;

import net.modificationstation.stationapi.api.client.render.Renderer;

import java.util.function.*;

/**
 * A bundle of one or more {@link QuadView} instances encoded by the renderer,
 * typically via {@link Renderer#meshBuilder()}.
 *
 * <p>Similar in purpose to the {@code List<BakedQuad>} instances returned by BakedModel, but
 * affords the renderer the ability to optimize the format for performance
 * and memory allocation.
 *
 * <p>Only the renderer should implement or extend this interface.
 */
public interface Mesh {
	/**
	 * Use to access all of the quads encoded in this mesh. The quad instances
	 * sent to the consumer will likely be threadlocal/reused and should never
	 * be retained by the consumer.
	 */
	void forEach(Consumer<QuadView> consumer);
}