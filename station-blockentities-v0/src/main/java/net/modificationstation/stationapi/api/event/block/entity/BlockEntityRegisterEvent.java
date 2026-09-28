package net.modificationstation.stationapi.api.event.block.entity;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.block.entity.BlockEntity;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.function.BiConsumer;

@SuperBuilder
public class BlockEntityRegisterEvent extends Event {
    public final BiConsumer<Class<? extends BlockEntity>, String> register;

    public final void register(String id, Class<? extends BlockEntity> blockEntityClass) {
        register.accept(blockEntityClass, id);
    }

    public final void register(Identifier id, Class<? extends BlockEntity> blockEntityClass) {
        register.accept(blockEntityClass, id.toString());
    }
}
