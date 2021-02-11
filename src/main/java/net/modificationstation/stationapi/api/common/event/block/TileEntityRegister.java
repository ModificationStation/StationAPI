package net.modificationstation.stationapi.api.common.event.block;

import net.minecraft.tileentity.TileEntityBase;
import net.modificationstation.stationapi.api.common.event.GameEventOld;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface TileEntityRegister {

    GameEventOld<TileEntityRegister> EVENT = new GameEventOld<>(TileEntityRegister.class,
            listeners ->
                    register -> {
                        for (TileEntityRegister listener : listeners)
                            listener.registerTileEntities(register);
                    },
            (Consumer<GameEventOld<TileEntityRegister>>) tileEntityRegister ->
                    tileEntityRegister.register(register -> GameEventOld.EVENT_BUS.post(new Data(register)))
    );

    void registerTileEntities(BiConsumer<Class<? extends TileEntityBase>, String> register);

    final class Data extends GameEventOld.Data<TileEntityRegister> {

        public final BiConsumer<Class<? extends TileEntityBase>, String> register;

        private Data(BiConsumer<Class<? extends TileEntityBase>, String> register) {
            super(EVENT);
            this.register = register;
        }

        public void register(Class<? extends TileEntityBase> teClass, String teIdentifier) {
            register.accept(teClass, teIdentifier);
        }
    }
}
