package net.modificationstation.stationapi.mixin.config.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.modificationstation.stationapi.api.config.HasDrawable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TextFieldWidget.class)
public abstract class TextFieldWidgetMixin extends DrawContext implements HasDrawable {
    @Mutable
    @Shadow @Final private int x;

    @Mutable
    @Shadow @Final private int y;

    @Mutable
    @Shadow @Final private int width;

    @Mutable
    @Shadow @Final private int height;

    @Override
    public void setXYWH(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}
