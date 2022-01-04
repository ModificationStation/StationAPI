package net.modificationstation.stationapi.mixin.render.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_66;
import net.modificationstation.stationapi.api.client.texture.plugin.MeshRendererPlugin;
import net.modificationstation.stationapi.api.client.texture.plugin.RenderPlugin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(class_66.class)
public class Mixinclass_66 {

    private final MeshRendererPlugin plugin = RenderPlugin.PLUGIN.createMeshRenderer((class_66) (Object) this);

    @Inject(
            method = "method_296()V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void buildMesh(CallbackInfo ci) {
        plugin.buildMesh(ci);
    }
}
