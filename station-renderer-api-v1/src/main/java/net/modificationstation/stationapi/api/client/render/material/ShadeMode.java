package net.modificationstation.stationapi.api.client.render.material;

/**
 * A hint to the renderer about how the quad is intended to be shaded, for example through ambient occlusion and
 * diffuse shading. The renderer is free to ignore this hint.
 */
public enum ShadeMode {
    /**
     * Conveys the intent that shading should be generally consistent, lack edge cases, and produce visually pleasing
     * results, even for quads that are not used by vanilla or are not possible to create through resource packs in
     * vanilla.
     */
    ENHANCED,

    /**
     * Conveys the intent that shading should mimic vanilla results, potentially to preserve certain visuals produced
     * by resource packs that modify models.
     */
    VANILLA;
}
