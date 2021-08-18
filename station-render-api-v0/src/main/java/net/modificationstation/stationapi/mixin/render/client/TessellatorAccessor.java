package net.modificationstation.stationapi.mixin.render.client;

import net.minecraft.client.render.Tessellator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Tessellator.class)
public interface TessellatorAccessor {

    @Accessor
    boolean getHasColour();

    @Accessor
    void setHasColour(boolean hasColour);

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
}
