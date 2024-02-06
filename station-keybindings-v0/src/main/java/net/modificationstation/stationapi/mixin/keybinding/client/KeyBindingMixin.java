package net.modificationstation.stationapi.mixin.keybinding.client;

import net.minecraft.client.option.KeyBinding;
import net.modificationstation.stationapi.impl.client.option.StationKeyBindingImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(KeyBinding.class)
class KeyBindingMixin implements StationKeyBindingImpl {
    @Unique
    private boolean stationapi_useCustomFile;

    @Override
    @Unique
    public boolean stationapi_useCustomFile() {
        return stationapi_useCustomFile;
    }

    @Override
    @Unique
    public void stationapi_useCustomFile(boolean useCustomFile) {
        this.stationapi_useCustomFile = useCustomFile;
    }
}
