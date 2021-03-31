package net.modificationstation.sltest.mixin;

import net.minecraft.client.gui.widgets.ScrollableBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ScrollableBase.class)
public class MixinScrollableBase {

    @ModifyArg(method = "render(IIF)V", index = 4, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Tessellator;vertex(DDDDD)V", ordinal = 0))
    private double modArg1(double arg) {
        return offset(arg);
    }

    @ModifyArg(method = "render(IIF)V", index = 4, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Tessellator;vertex(DDDDD)V", ordinal = 1))
    private double modArg2(double arg) {
        return offset(arg);
    }

    @ModifyArg(method = "render(IIF)V", index = 4, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Tessellator;vertex(DDDDD)V", ordinal = 2))
    private double modArg3(double arg) {
        return offset(arg);
    }

    @ModifyArg(method = "render(IIF)V", index = 4, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Tessellator;vertex(DDDDD)V", ordinal = 3))
    private double modArg4(double arg) {
        return offset(arg);
    }

    @ModifyArg(method = "renderBackground(IIII)V", index = 4, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Tessellator;vertex(DDDDD)V"))
    private double modArg5(double arg) {
        return offset(arg);
    }

    private double offset(double arg) {
        return arg + (System.currentTimeMillis() % 5000) / 5000D;
    }
}
