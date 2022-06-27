package net.modificationstation.stationapi.api.event.tileentity;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.tileentity.TileEntityBase;

import java.util.function.BiConsumer;

@SuperBuilder
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
