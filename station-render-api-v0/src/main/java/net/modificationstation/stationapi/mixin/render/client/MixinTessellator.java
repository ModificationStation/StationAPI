package net.modificationstation.stationapi.mixin.render.client;

import lombok.Getter;
import net.minecraft.client.render.Tessellator;
import net.modificationstation.stationapi.api.client.texture.plugin.RenderPlugin;
import net.modificationstation.stationapi.api.client.texture.plugin.TessellatorPlugin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Tessellator.class)
public class MixinTessellator implements TessellatorPlugin.Provider {

    @Unique
    @Getter
    private final TessellatorPlugin plugin = RenderPlugin.PLUGIN.createTessellator((Tessellator) (Object) this);

    @Inject(
            method = "addVertex(DDD)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void addVertex(double d1, double d2, double par3, CallbackInfo ci) {
        plugin.addVertex(d1, d2, par3, ci);
    }
}
