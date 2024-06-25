package net.modificationstation.stationapi.mixin.config.client;

import net.minecraft.client.gui.DrawContext;
import net.modificationstation.stationapi.impl.config.DrawContextAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(DrawContext.class)
public abstract class DrawContextMixin implements DrawContextAccessor {


    @Shadow protected abstract void fill(int x1, int y1, int x2, int y2, int colour);

    @Override
    public void invokeFill(int x1, int y1, int x2, int y2, int colour) {
        fill(x1, y1, x2, y2, colour);
    }
}
