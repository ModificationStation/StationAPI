package net.modificationstation.stationapi.api.event.entity;

import lombok.RequiredArgsConstructor;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.entity.EntityBase;
import uk.co.benjiweber.expressions.functions.TriConsumer;

@RequiredArgsConstructor
public class EntityRegister extends Event {

    public final TriConsumer<Class<? extends EntityBase>, String, Integer> register;

    public final void register(Class<? extends EntityBase> entityClass, String entityIdentifier, int entityId) {
        register.accept(entityClass, entityIdentifier, entityId);
    }

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
