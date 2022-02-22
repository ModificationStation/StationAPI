package net.modificationstation.stationapi.api.client.render.material;

/**
 * Defines how sprite pixels will be blended with the scene.
 */
public enum BlendMode {

	/**
	 * Emulate blending behavior of {@code BlockRenderLayer} associated with the block.
	 */
	DEFAULT(-1),

	/**
	 * Fully opaque with depth test, no blending. Used for most normal blocks.
	 */
	SOLID(0),

	/**
	 * Pixels are blended with the background according to alpha colour values. Some performance cost,
	 * use in moderation. Texture mip-map enabled.  Used for ice.
	 */
	TRANSLUCENT(1);

	public final int blockRenderPass;

	BlendMode(int blockRenderPass) {
		this.blockRenderPass = blockRenderPass;
	}

	public static BlendMode fromRenderPass(int renderPass) {
		return switch (renderPass) {
			case 0 -> SOLID;
			case 1 -> TRANSLUCENT;
			default -> DEFAULT;
		};
	}
}