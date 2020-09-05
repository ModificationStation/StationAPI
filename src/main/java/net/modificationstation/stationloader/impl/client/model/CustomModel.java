package net.modificationstation.stationloader.impl.client.model;

import net.minecraft.client.render.entity.model.EntityModelBase;

public class CustomModel extends EntityModelBase implements net.modificationstation.stationloader.api.client.model.CustomModel {
    private CustomCuboidRenderer[] cuboids;

    public CustomModel(CustomCuboidRenderer[] cuboids) {
        this.cuboids = cuboids;
    }

    @Override
    public void render(float f, float f1, float f2, float f3, float f4, float f5) {
        for (CustomCuboidRenderer cuboid : cuboids) {
            cuboid.renderEntityModelCube(f5);
        }
    }

    @Override
    public CustomCuboidRenderer[] getCuboids() {
        return cuboids;
    }
}
