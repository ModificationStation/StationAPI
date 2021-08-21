package net.modificationstation.stationapi.api.client.model;

import net.minecraft.client.render.entity.model.EntityModelBase;
import net.modificationstation.stationapi.impl.client.model.CustomCuboidRenderer;

public class CustomModel extends EntityModelBase {
    private final CustomCuboidRenderer[] cuboids;

    public CustomModel(CustomCuboidRenderer[] cuboids) {
        this.cuboids = cuboids;
    }

    @Override
    public void render(float f, float f1, float f2, float f3, float f4, float f5) {
        for (CustomCuboidRenderer cuboid : cuboids) {
            cuboid.renderEntityModelCube(f5);
        }
    }

    public CustomCuboidRenderer[] getCuboids() {
        return cuboids;
    }
}
