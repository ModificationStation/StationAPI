package net.modificationstation.stationapi.mixin.render.client;

import net.minecraft.client.render.Tessellator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Tessellator.class)
public interface TessellatorAccessor {

    @Accessor
    boolean getHasColour();

    @Accessor
    void setHasColour(boolean hasColour);
}
