package net.modificationstation.stationapi.mixin.render.client;

import net.minecraft.client.render.Tessellator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Tessellator.class)
public interface TessellatorAccessor {

    @Accessor
    boolean getDisableColour();

    @Accessor
    void setDisableColour(boolean disableColour);

    @Accessor
    boolean getHasColour();

    @Accessor
    void setHasColour(boolean hasColour);

    @Accessor
    boolean getHasNormals();

    @Accessor
    void setHasNormals(boolean hasNormals);

    @Accessor
    boolean getHasTexture();

    @Accessor
    void setHasTexture(boolean hasTexture);

    @Invoker("<init>")
    static Tessellator newInst(int bufferSize) {
        throw new AssertionError("Mixin!");
    }

    @Accessor
    boolean getDrawing();

    @Accessor
    double getXOffset();

    @Accessor
    double getYOffset();

    @Accessor
    double getZOffset();

    @Accessor
    int getColour();

    @Accessor
    int getNormal();

    @Accessor
    double getTextureX();

    @Accessor
    double getTextureY();
}
