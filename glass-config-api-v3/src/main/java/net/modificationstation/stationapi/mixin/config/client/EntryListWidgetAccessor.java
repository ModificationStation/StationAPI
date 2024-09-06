package net.modificationstation.stationapi.mixin.config.client;

import net.minecraft.client.gui.widget.EntryListWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntryListWidget.class)
public interface EntryListWidgetAccessor {

    @Accessor("scrollAmount")
    float getScrollAmount();

    @Accessor("scrollAmount")
    void setScrollAmount(float value);
}
