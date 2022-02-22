package net.modificationstation.stationapi.api.client.render;

import net.modificationstation.stationapi.api.client.render.mesh.Mesh;
import net.modificationstation.stationapi.api.client.render.mesh.MeshBuilder;
import net.modificationstation.stationapi.api.client.render.mesh.MutableQuadView;
import net.modificationstation.stationapi.api.client.render.mesh.QuadEmitter;
import net.modificationstation.stationapi.api.client.render.model.BakedModel;

import java.util.function.*;

/**
 * This defines the instance made available to models for buffering vertex data at render time.
 *
 * <p>Only the renderer should implement or extend this interface.
 */
public interface RenderContext {
	/**
	 * Used by models to send vertex data previously baked via {@link MeshBuilder}.
	 * The fastest option and preferred whenever feasible.
	 */
	Consumer<Mesh> meshConsumer();

	/**
	 * Fabric causes vanilla baked models to send themselves
	 * via this interface. Can also be used by compound models that contain a mix
	 * of vanilla baked models, packaged quads and/or dynamic elements.
	 */
	Consumer<BakedModel> fallbackConsumer();

	/**
	 * Returns a {@link QuadEmitter} instance that emits directly to the render buffer.
	 * It remains necessary to call {@link QuadEmitter#emit()} to output the quad.
	 *
	 * <p>This method will always be less performant than passing pre-baked meshes
	 * via {@link #meshConsumer()}. It should be used sparingly for model components that
	 * demand it - text, icons, dynamic indicators, or other elements that vary too
	 * much for static baking to be feasible.
	 *
	 * <p>Calling this method invalidates any {@link QuadEmitter} returned earlier.
	 * Will be threadlocal/re-used - do not retain references.
	 */
	QuadEmitter getEmitter();

	/**
	 * Causes all models/quads/meshes sent to this consumer to be transformed by the provided
	 * {@link QuadTransform} that edits each quad before buffering. Quads in the mesh will
	 * be passed to the {@link QuadTransform} for modification before offsets, face culling or lighting are applied.
	 * Meant for animation and mesh customization.
	 *
	 * <p>You MUST call {@link #popTransform()} after model is done outputting quads.
	 *
	 * <p>More than one transformer can be added to the context.  Transformers are applied in reverse order.
	 * (Last pushed is applied first.)
	 *
	 * <p>Meshes are never mutated by the transformer - only buffered quads. This ensures thread-safe
	 * use of meshes/models across multiple chunk builders.
	 */
	void pushTransform(QuadTransform transform);

	/**
	 * Removes the transformation added by the last call to {@link #pushTransform(QuadTransform)}.
	 * MUST be called before exiting from {@link BakedModel} .emit... methods.
	 */
	void popTransform();

	@FunctionalInterface
	interface QuadTransform {

		/**
		 * Return false to filter out quads from rendering. When more than one transform
		 * is in effect, returning false means unapplied transforms will not receive the quad.
		 */
		boolean transform(MutableQuadView quad);
	}
}