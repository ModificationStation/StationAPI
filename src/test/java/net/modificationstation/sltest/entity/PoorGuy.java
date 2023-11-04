package net.modificationstation.sltest.entity;

import lombok.Getter;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.modificationstation.sltest.SLTest;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.server.entity.HasTrackingParameters;
import net.modificationstation.stationapi.api.server.entity.MobSpawnDataProvider;

import static net.modificationstation.sltest.SLTest.MODID;
import static net.modificationstation.stationapi.api.util.Identifier.of;

@HasTrackingParameters(trackingDistance = 5, updatePeriod = 2)
public class PoorGuy extends AnimalEntity implements MobSpawnDataProvider {

    public PoorGuy(World arg) {
        super(arg);
        SLTest.LOGGER.info("well guess im dead");
        texture = "/assets/sltest/textures/entities/geisterspoor.png";
    }

//    public PoorGuy(Level level, double x, double y, double z) {
//        this(level);
//        System.out.println("yoooooooooooooooooooooooo");
//        setPosition(x, y, z);
//        field_1026 = true;
//    }

    @Override
    protected int method_914() {
        return Item.WHEAT.id;
    }

    @Getter
    private final Identifier handlerIdentifier = of(MODID, "gpoor");
}
