package net.modificationstation.stationapi.mixin.client.accessor;

import net.minecraft.client.render.Tessellator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Tessellator.class)
public interface TessellatorAccessor {

    @Accessor
    boolean getField_2065();

    @Accessor
    void setField_2065(boolean field_2065);
}
