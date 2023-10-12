package net.modificationstation.stationapi.api.util.math;

public class ColorHelper {
    public static class Abgr {
        public static int getAlpha(int abgr) {
            return abgr >>> 24;
        }

        public static int getBlue(int abgr) {
            return abgr & 255;
        }

        public static int getGreen(int abgr) {
            return abgr >> 8 & 255;
        }

        public static int getRed(int abgr) {
            return abgr >> 16 & 255;
        }

        public static int getBgr(int abgr) {
            return abgr & 16777215;
        }

        public static int toOpaque(int abgr) {
            return abgr | -16777216;
        }

        public static int getAbgr(int a, int b, int g, int r) {
            return a << 24 | b << 16 | g << 8 | r;
        }

        public static int withAlpha(int alpha, int bgr) {
            return alpha << 24 | bgr & 16777215;
        }
    }

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

