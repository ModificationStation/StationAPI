package net.modificationstation.stationapi.api.client.texture.plugin;

import net.minecraft.class_66;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class MeshRendererPlugin {

    protected final class_66 meshRenderer;

    public MeshRendererPlugin(class_66 meshRenderer) {
        this.meshRenderer = meshRenderer;
    }

    public void buildMesh(CallbackInfo ci) {}
}
