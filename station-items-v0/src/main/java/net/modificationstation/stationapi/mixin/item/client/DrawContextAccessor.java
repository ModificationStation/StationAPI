package net.modificationstation.stationapi.mixin.item.client;

import net.minecraft.client.gui.DrawContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(DrawContext.class)
public interface DrawContextAccessor {
    @Invoker
    void invokeFill(int x1, int y1, int x2, int y2, int color);
}
