package net.modificationstation.stationapi.api.util.math;

public class ColourHelper {

    public static class Argb {

        public static int getAlpha(int argb) {
            return argb >>> 24;
        }

        public static int getRed(int argb) {
            return argb >> 16 & 0xFF;
        }

        public static int getGreen(int argb) {
            return argb >> 8 & 0xFF;
        }

        public static int getBlue(int argb) {
            return argb & 0xFF;
        }

        public static int getArgb(int alpha, int red, int green, int blue) {
            return alpha << 24 | red << 16 | green << 8 | blue;
        }

        public static int mixColor(int first, int second) {
            return Argb.getArgb(Argb.getAlpha(first) * Argb.getAlpha(second) / 255, Argb.getRed(first) * Argb.getRed(second) / 255, Argb.getGreen(first) * Argb.getGreen(second) / 255, Argb.getBlue(first) * Argb.getBlue(second) / 255);
        }
    }
}

