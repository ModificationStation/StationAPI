package net.modificationstation.sltest.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.swing.*;
import net.minecraft.class_153;
import net.minecraft.class_519;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionData;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

@Mixin(World.class)
public abstract class MixinLevel {
    @Shadow public abstract class_519 method_1781();

    /*@Inject(
        method = "<init>(Lnet/minecraft/level/dimension/DimensionData;Ljava/lang/String;J)V",
        at = @At("TAIL")
    )*/
    private void onInit(DimensionData string, String l, long par3, CallbackInfo ci) {
        int side = 800;

        BufferedImage buffer = new BufferedImage(side, side, BufferedImage.TYPE_INT_ARGB);
        int[] pixels = ((DataBufferInt) buffer.getRaster().getDataBuffer()).getData();

        int start = -(side >> 1);
        class_519 biomeSource = method_1781();
        class_153[] biomes = biomeSource.method_1791(new class_153[side * side], start, start, side, side);

        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = biomes[i].field_889 | 0xFF000000;
        }

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new JLabel(new ImageIcon(buffer)));
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
