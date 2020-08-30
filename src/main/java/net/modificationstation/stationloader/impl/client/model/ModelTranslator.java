package net.modificationstation.stationloader.impl.client.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class ModelTranslator {

    @Environment(EnvType.CLIENT)
    public static CustomCuboidRenderer[] translate(JsonModel modelJson, String modid) {
        CustomCuboidRenderer[] cuboids = new CustomCuboidRenderer[modelJson.getElements().length];
        JsonCuboid[] elements = modelJson.getElements();
        for (int i = 0; i < (elements.length); i++) {
            JsonCuboid jsonCuboid = elements[i];
            CustomCuboidRenderer cuboid = new CustomCuboidRenderer(jsonCuboid.getFaces(), modid); // texture offset

            double[] from = jsonCuboid.getFrom();
            double[] to = jsonCuboid.getTo();

            double rads = 90*(Math.PI/180);
            //double[] newRotation = rotateVector(new double[]{from[0], from[2]}, rads);
            double[] newRotation = new double[]{from[0], from[2]};

            cuboid.setupCuboid((float)newRotation[0], (float)from[1], (float)newRotation[1], (int)Math.floor(to[0]-(float)from[0]), (int)Math.floor(to[1]-(float)from[1]), (int)Math.floor(to[2]-(float)from[2]), 0F); // Starting point, Pixels to extend by, scale
            cuboid.setRotationPoint(0F, 0F, 0F);
            cuboids[i] = cuboid;
        }
        return cuboids;
    }

    public static double[] rotateVector(double[] vector, double rads)
    {
        return new double[]{
                vector[0] * Math.cos(rads) - vector[1] * Math.sin(rads),
                vector[0] * Math.sin(rads) + vector[1] * Math.cos(rads)
        };
    }
}
