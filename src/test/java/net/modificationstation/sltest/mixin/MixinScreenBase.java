package net.modificationstation.sltest.mixin;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(Screen.class)
public class MixinScreenBase {

    @ModifyConstant(method = "renderDirtBackground(I)V", constant = @Constant(intValue = 4210752))
    private int modCon(int constant) {
        int col = 48 + (int) (MathHelper.abs(MathHelper.sin((float)(System.currentTimeMillis() % 50000L) / 50000.0F * (float)Math.PI * 2.0F)/* * 0.1F*/) * 32);//(int) ((System.currentTimeMillis() % 1024) / 4);
        return col << 16 | col << 8 | col;
    }

    @ModifyArg(method = "renderDirtBackground(I)V", index = 4, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Tessellator;vertex(DDDDD)V"))
    private double modArg1(double arg) {
        return arg + (System.currentTimeMillis() % 5000) / 5000D;
    }
}
