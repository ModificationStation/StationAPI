package net.modificationstation.stationapi.mixin.arsenic.client;

import net.minecraft.client.render.Tessellator;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.render.ArsenicTessellator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Tessellator.class)
public class MixinTessellator {

    @Unique
    private final ArsenicTessellator arsenic_plugin = new ArsenicTessellator((Tessellator) (Object) this);

    /**
     * @reason there's no saving notch code
     * @author mine_diver
     */
    @Overwrite
    public void addVertex(double d1, double d2, double par3) {
        arsenic_plugin.addVertex(d1, d2, par3);
    }
}
