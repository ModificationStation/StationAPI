package net.modificationstation.stationapi.api.event.block;

@FunctionalInterface
public interface AddBurnable {
    void addBurnable(int blockId, int burnChance, int spreadChance);
}
