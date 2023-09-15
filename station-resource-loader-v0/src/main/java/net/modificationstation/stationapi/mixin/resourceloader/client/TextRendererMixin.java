package net.modificationstation.stationapi.mixin.resourceloader.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.TextRenderer;
import net.modificationstation.stationapi.impl.client.resource.AssetsReloaderImpl;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TextRenderer.class)
public class TextRendererMixin {
    @ModifyExpressionValue(
            method = "<init>",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/render/Tessellator;INSTANCE:Lnet/minecraft/client/render/Tessellator;",
                    opcode = Opcodes.GETSTATIC
            )
    )
    private Tessellator stationapi_changeTessellateIfNecessary(Tessellator instance) {
        return Thread.currentThread() == AssetsReloaderImpl.reloadScreenThread ? AssetsReloaderImpl.reloadScreenTessellator : instance;
    }
}
