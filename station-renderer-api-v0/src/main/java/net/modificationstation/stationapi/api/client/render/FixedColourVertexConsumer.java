package net.modificationstation.stationapi.api.client.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public abstract class FixedColourVertexConsumer
implements VertexConsumer {
    protected boolean colourFixed;
    protected int fixedRed = 255;
    protected int fixedGreen = 255;
    protected int fixedBlue = 255;
    protected int fixedAlpha = 255;

    @Override
    public void fixedColour(int red, int green, int blue, int alpha) {
        this.fixedRed = red;
        this.fixedGreen = green;
        this.fixedBlue = blue;
        this.fixedAlpha = alpha;
        this.colourFixed = true;
    }

    @Override
    public void unfixColour() {
        this.colourFixed = false;
    }
}

