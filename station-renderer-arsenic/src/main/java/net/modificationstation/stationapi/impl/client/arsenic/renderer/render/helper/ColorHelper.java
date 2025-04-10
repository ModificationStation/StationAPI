package net.modificationstation.stationapi.impl.client.arsenic.renderer.render.helper;

import java.nio.ByteOrder;

/**
 * Static routines of general utility for renderer implementations.
 * Renderers are not required to use these helpers, but they were
 * designed to be usable without the default renderer.
 */
public final class ColorHelper {
    private ColorHelper() { }

    private static final boolean BIG_ENDIAN = ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN;

    /**
     * Component-wise max.
     */
    public static int maxLight(int l0, int l1) {
        if (l0 == 0) return l1;
        if (l1 == 0) return l0;

        return Math.max(l0 & 0xFFFF, l1 & 0xFFFF) | Math.max(l0 & 0xFFFF0000, l1 & 0xFFFF0000);
    }

	/*
	Renderer color format: ARGB (0xAARRGGBB)
	Vanilla color format (little endian): ABGR (0xAABBGGRR)
	Vanilla color format (big endian): RGBA (0xRRGGBBAA)

	Why does the vanilla color format change based on endianness?
	See VertexConsumer#quad. Quad data is loaded as integers into
	a native byte order buffer. Color is read directly from bytes
	12, 13, 14 of each vertex. A different byte order will yield
	different results.

	The renderer always uses ARGB because the API color methods
	always consume and return ARGB. Vanilla block and item colors
	also use ARGB.
	 */

    /**
     * Converts from ARGB color to ABGR color if little endian or RGBA color if big endian.
     */
    public static int toVanillaColor(int color) {
        if (color == -1) {
            return -1;
        }

        if (BIG_ENDIAN) {
            // ARGB to RGBA
            return ((color & 0x00FFFFFF) << 8) | ((color & 0xFF000000) >>> 24);
        } else {
            // ARGB to ABGR
            return (color & 0xFF00FF00) | ((color & 0x00FF0000) >>> 16) | ((color & 0x000000FF) << 16);
        }
    }

    /**
     * Converts to ARGB color from ABGR color if little endian or RGBA color if big endian.
     */
    public static int fromVanillaColor(int color) {
        if (color == -1) {
            return -1;
        }

        if (BIG_ENDIAN) {
            // RGBA to ARGB
            return ((color & 0xFFFFFF00) >>> 8) | ((color & 0x000000FF) << 24);
        } else {
            // ABGR to ARGB
            return (color & 0xFF00FF00) | ((color & 0x00FF0000) >>> 16) | ((color & 0x000000FF) << 16);
        }
    }
}
