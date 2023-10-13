package net.modificationstation.stationapi.mixin.resourceloader.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import cyclops.function.FluentFunctions;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.TextRenderer;
import net.modificationstation.stationapi.api.client.resource.ReloadScreenManager;
import net.modificationstation.stationapi.impl.client.resource.ReloadScreenTessellatorHolder;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Objects;

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
    private Tessellator stationapi_changeTessellatorIfNecessary(Tessellator instance) {
        return ReloadScreenManager.getThread().map(FluentFunctions.of(Objects::equals).partiallyApply(Thread.currentThread())).orElse(false) ? ReloadScreenTessellatorHolder.reloadScreenTessellator : instance;
    }
}
