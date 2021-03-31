package net.modificationstation.sltest.entity;

import lombok.Getter;
import net.minecraft.entity.animal.AnimalBase;
import net.minecraft.item.ItemBase;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.common.registry.Identifier;
import net.modificationstation.stationapi.api.server.entity.IStationSpawnData;
import net.modificationstation.stationapi.api.server.entity.Tracking;

@Tracking(trackingDistance = 5, updatePeriod = 2)
public class PoorGuy extends AnimalBase implements IStationSpawnData {

    public PoorGuy(Level arg) {
        super(arg);
        System.out.println("well guess im dead");
        texture = "/assets/sltest/textures/entities/geisterspoor.png";
    }

    public PoorGuy(Level level, double x, double y, double z) {
        this(level);
        System.out.println("yoooooooooooooooooooooooo");
        setPosition(x, y, z);
        field_1026 = true;
    }

    @Override
    protected int getMobDrops() {
        return ItemBase.wheat.id;
    }

    @Getter
    private final Identifier handlerIdentifier = Identifier.of("sltest:gpoor");
}
