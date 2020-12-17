package net.modificationstation.stationloader.api.common.event.block;

import net.minecraft.tileentity.TileEntityBase;
import net.modificationstation.stationloader.api.common.event.GameEvent;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface TileEntityRegister {

    GameEvent<TileEntityRegister> EVENT = new GameEvent<>(TileEntityRegister.class,
            listeners ->
                    register -> {
                        for (TileEntityRegister listener : listeners)
                            listener.registerTileEntities(register);
                    },
            (Consumer<GameEvent<TileEntityRegister>>) tileEntityRegister ->
                    tileEntityRegister.register(register -> GameEvent.EVENT_BUS.post(new Data(register)))
    );

    void registerTileEntities(BiConsumer<Class<? extends TileEntityBase>, String> register);

    final class Data extends GameEvent.Data<TileEntityRegister> {

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
