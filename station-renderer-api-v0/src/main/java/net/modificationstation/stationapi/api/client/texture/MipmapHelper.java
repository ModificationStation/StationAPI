package net.modificationstation.stationapi.api.client.texture;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.util.Util;

@Environment(value=EnvType.CLIENT)
public class MipmapHelper {
    private static final float[] COLOR_FRACTIONS = Util.make(new float[256], fs -> {
        for (int i = 0; i < fs.length; ++i) {
            fs[i] = (float)Math.pow((float)i / 255.0f, 2.2);
        }
    });

    public static NativeImage[] getMipmapLevelsImages(NativeImage image, int mipmap) {
        NativeImage[] nativeImages = new NativeImage[mipmap + 1];
        nativeImages[0] = image;
        if (mipmap > 0) {
            boolean bl = false;
            block0: for (int i = 0; i < image.getWidth(); ++i) {
                for (int j = 0; j < image.getHeight(); ++j) {
                    if (image.getColour(i, j) >> 24 != 0) continue;
                    bl = true;
                    break block0;
                }
            }
            for (int k = 1; k <= mipmap; ++k) {
                NativeImage nativeImage = nativeImages[k - 1];
                NativeImage nativeImage2 = new NativeImage(nativeImage.getWidth() >> 1, nativeImage.getHeight() >> 1, false);
                int l = nativeImage2.getWidth();
                int m = nativeImage2.getHeight();
                for (int n = 0; n < l; ++n) {
                    for (int o = 0; o < m; ++o) {
                        nativeImage2.setColour(n, o, MipmapHelper.blend(nativeImage.getColour(n * 2, o * 2), nativeImage.getColour(n * 2 + 1, o * 2), nativeImage.getColour(n * 2, o * 2 + 1), nativeImage.getColour(n * 2 + 1, o * 2 + 1), bl));
                    }
                }
                nativeImages[k] = nativeImage2;
            }
        }
        return nativeImages;
    }

    private static int blend(int one, int two, int three, int four, boolean checkAlpha) {
        if (checkAlpha) {
            float f = 0.0f;
            float g = 0.0f;
            float h = 0.0f;
            float i = 0.0f;
            if (one >> 24 != 0) {
                f += MipmapHelper.getColorFraction(one >> 24);
                g += MipmapHelper.getColorFraction(one >> 16);
                h += MipmapHelper.getColorFraction(one >> 8);
                i += MipmapHelper.getColorFraction(one);
            }
            if (two >> 24 != 0) {
                f += MipmapHelper.getColorFraction(two >> 24);
                g += MipmapHelper.getColorFraction(two >> 16);
                h += MipmapHelper.getColorFraction(two >> 8);
                i += MipmapHelper.getColorFraction(two);
            }
            if (three >> 24 != 0) {
                f += MipmapHelper.getColorFraction(three >> 24);
                g += MipmapHelper.getColorFraction(three >> 16);
                h += MipmapHelper.getColorFraction(three >> 8);
                i += MipmapHelper.getColorFraction(three);
            }
            if (four >> 24 != 0) {
                f += MipmapHelper.getColorFraction(four >> 24);
                g += MipmapHelper.getColorFraction(four >> 16);
                h += MipmapHelper.getColorFraction(four >> 8);
                i += MipmapHelper.getColorFraction(four);
            }
            int j = (int)(Math.pow(f / 4.0f, 0.45454545454545453) * 255.0);
            int k = (int)(Math.pow(g / 4.0f, 0.45454545454545453) * 255.0);
            int l = (int)(Math.pow(h / 4.0f, 0.45454545454545453) * 255.0);
            int m = (int)(Math.pow(i / 4.0f, 0.45454545454545453) * 255.0);
            if (j < 96) {
                j = 0;
            }
            return j << 24 | k << 16 | l << 8 | m;
        }
        int n = MipmapHelper.getColorComponent(one, two, three, four, 24);
        int o = MipmapHelper.getColorComponent(one, two, three, four, 16);
        int p = MipmapHelper.getColorComponent(one, two, three, four, 8);
        int q = MipmapHelper.getColorComponent(one, two, three, four, 0);
        return n << 24 | o << 16 | p << 8 | q;
    }

    private static int getColorComponent(int one, int two, int three, int four, int bits) {
        float f = MipmapHelper.getColorFraction(one >> bits);
        float g = MipmapHelper.getColorFraction(two >> bits);
        float h = MipmapHelper.getColorFraction(three >> bits);
        float i = MipmapHelper.getColorFraction(four >> bits);
        float j = (float)((double)((float)Math.pow((double)(f + g + h + i) * 0.25, 0.45454545454545453)));
        return (int)((double)j * 255.0);
    }

    private static float getColorFraction(int value) {
        return COLOR_FRACTIONS[value & 0xFF];
    }
}
