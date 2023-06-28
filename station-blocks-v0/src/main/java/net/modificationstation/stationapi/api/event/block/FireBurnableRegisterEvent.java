package net.modificationstation.stationapi.api.event.block;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;

@SuperBuilder
public final class FireBurnableRegisterEvent extends Event implements AddBurnable {
    public final AddBurnable addBurnable;

    @Override
    public void addBurnable(int blockId, int burnChance, int spreadChance) {
        addBurnable.addBurnable(blockId, burnChance, spreadChance);
    }
}
