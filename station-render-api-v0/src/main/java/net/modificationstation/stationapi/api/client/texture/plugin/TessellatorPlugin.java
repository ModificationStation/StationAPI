package net.modificationstation.stationapi.api.client.texture.plugin;

import net.minecraft.client.render.Tessellator;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class TessellatorPlugin {

    protected final Tessellator tessellator;

    public TessellatorPlugin(Tessellator tessellator) {
        this.tessellator = tessellator;
    }

    public void addVertex(double x, double y, double z, CallbackInfo ci) {}

    public interface Provider {

        <T extends TessellatorPlugin> T getPlugin();
    }
}
