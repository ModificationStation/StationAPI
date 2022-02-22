package net.modificationstation.stationapi.api.client.render.model;

import net.modificationstation.stationapi.api.client.render.mesh.Mesh;
import net.modificationstation.stationapi.api.client.render.mesh.MutableQuadView;
import net.modificationstation.stationapi.api.client.render.mesh.QuadView;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.client.texture.SpriteAtlasTexture;
import net.modificationstation.stationapi.impl.client.render.SpriteFinderImpl;

/**
 * Indexes a texture atlas to allow fast lookup of Sprites from
 * baked vertex coordinates.  Main use is for {@link Mesh}-based models
 * to generate vanilla quads on demand without tracking and retaining
 * the sprites that were baked into the mesh. In other words, this class
 * supplies the sprite parameter for {@link QuadView#toBakedQuad(int, Sprite, boolean)}.
 */
public interface SpriteFinder {
	/**
	 * Retrieves or creates the finder for the given atlas.
	 * Instances should not be retained as fields or they must be
	 * refreshed whenever there is a resource reload or other event
	 * that causes atlas textures to be re-stitched.
	 */
	static SpriteFinder get(SpriteAtlasTexture atlas) {
		return SpriteFinderImpl.get(atlas);
	}

	/**
	 * Finds the atlas sprite containing the vertex centroid of the quad.
	 * Vertex centroid is essentially the mean u,v coordinate - the intent being
	 * to find a point that is unambiguously inside the sprite (vs on an edge.)
	 *
	 * <p>Should be reliable for any convex quad or triangle. May fail for non-convex quads.
	 * Note that all the above refers to u,v coordinates. Geometric vertex does not matter,
	 * except to the extent it was used to determine u,v.
	 */
	Sprite find(QuadView quad, int textureIndex);

	/**
	 * Alternative to {@link #find(QuadView, int)} when vertex centroid is already
	 * known or unsuitable.  Expects normalized (0-1) coordinates on the atlas texture,
	 * which should already be the case for u,v values in vanilla baked quads and in
	 * {@link QuadView} after calling {@link MutableQuadView#spriteBake(int, Sprite, int)}.
	 *
	 * <p>Coordinates must be in the sprite interior for reliable results. Generally will
	 * be easier to use {@link #find(QuadView, int)} unless you know the vertex
	 * centroid will somehow not be in the quad interior. This method will be slightly
	 * faster if you already have the centroid or another appropriate value.
	 */
	Sprite find(float u, float v);
}