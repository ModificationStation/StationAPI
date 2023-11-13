package net.modificationstation.stationapi.api.event.block.entity;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.block.entity.BlockEntity;
import java.util.function.BiConsumer;

@SuperBuilder
public class BlockEntityRegisterEvent extends Event {
    public final BiConsumer<Class<? extends BlockEntity>, String> register;

    public final void register(Class<? extends BlockEntity> blockEntityClass, String id) {
        register.accept(blockEntityClass, id);
    }
}
