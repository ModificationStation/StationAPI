package net.modificationstation.stationapi.api.common.event.tileentity;

import lombok.RequiredArgsConstructor;
import net.minecraft.tileentity.TileEntityBase;
import net.modificationstation.stationapi.api.common.event.Event;

import java.util.function.*;

@RequiredArgsConstructor
public class TileEntityRegisterEvent extends Event {

    public final BiConsumer<Class<? extends TileEntityBase>, String> register;

    public final void register(Class<? extends TileEntityBase> teClass, String teIdentifier) {
        register.accept(teClass, teIdentifier);
    }
}
