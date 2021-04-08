package net.modificationstation.stationapi.api.event.tileentity;

import lombok.RequiredArgsConstructor;
import net.minecraft.tileentity.TileEntityBase;
import net.mine_diver.unsafeevents.Event;

import java.util.function.*;

@RequiredArgsConstructor
public class TileEntityRegisterEvent extends Event {

    public final BiConsumer<Class<? extends TileEntityBase>, String> register;

    public final void register(Class<? extends TileEntityBase> teClass, String teIdentifier) {
        register.accept(teClass, teIdentifier);
    }

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
