package net.modificationstation.stationloader.api.common.event.block;

import net.minecraft.tileentity.TileEntityBase;
import net.modificationstation.stationloader.api.common.event.SimpleEvent;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface TileEntityRegister {

    @SuppressWarnings("UnstableApiUsage")
    SimpleEvent<TileEntityRegister> EVENT = new SimpleEvent<>(TileEntityRegister.class,
            listeners ->
                    register -> {
                        for (TileEntityRegister listener : listeners)
                            listener.registerTileEntities(register);
                    },
            (Consumer<SimpleEvent<TileEntityRegister>>) tileEntityRegister ->
                    tileEntityRegister.register(register -> SimpleEvent.EVENT_BUS.post(new Data(register)))
    );

    void registerTileEntities(BiConsumer<Class<? extends TileEntityBase>, String> register);

    final class Data extends SimpleEvent.Data<TileEntityRegister> {

        private final BiConsumer<Class<? extends TileEntityBase>, String> register;

        private Data(BiConsumer<Class<? extends TileEntityBase>, String> register) {
            super(EVENT);
            this.register = register;
        }

        public void register(Class<? extends TileEntityBase> teClass, String teIdentifier) {
            register.accept(teClass, teIdentifier);
        }
    }
}
