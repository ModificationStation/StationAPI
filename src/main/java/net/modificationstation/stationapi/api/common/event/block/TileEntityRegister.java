package net.modificationstation.stationapi.api.common.event.block;

import lombok.RequiredArgsConstructor;
import net.minecraft.tileentity.TileEntityBase;
import net.modificationstation.stationapi.api.common.event.Event;

import java.util.function.BiConsumer;

@RequiredArgsConstructor
public class TileEntityRegister extends Event {

    public final BiConsumer<Class<? extends TileEntityBase>, String> register;

    public void register(Class<? extends TileEntityBase> teClass, String teIdentifier) {
        register.accept(teClass, teIdentifier);
    }
}
