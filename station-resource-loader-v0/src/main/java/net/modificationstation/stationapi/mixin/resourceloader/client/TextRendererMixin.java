package net.modificationstation.stationapi.mixin.resourceloader.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.Tessellator;
import net.modificationstation.stationapi.api.client.resource.ReloadScreenManager;
import net.modificationstation.stationapi.impl.client.resource.ReloadScreenTessellatorHolder;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Objects;

@Mixin(TextRenderer.class)
class TextRendererMixin {
    @ModifyExpressionValue(
            method = "<init>",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/render/Tessellator;INSTANCE:Lnet/minecraft/client/render/Tessellator;",
                    opcode = Opcodes.GETSTATIC
            )
    )
    private Tessellator stationapi_changeTessellatorIfNecessary(Tessellator instance) {
        return ReloadScreenManager.getThread().map(thread -> Objects.equals(thread, Thread.currentThread())).orElse(false) ? ReloadScreenTessellatorHolder.reloadScreenTessellator : instance;
    }
}
