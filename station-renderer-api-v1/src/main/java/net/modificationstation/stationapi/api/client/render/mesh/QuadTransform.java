package net.modificationstation.stationapi.api.client.render.mesh;

@FunctionalInterface
public interface QuadTransform {
    /**
     * Return false to filter out quads from rendering. When more than one transform
     * is in effect, returning false means unapplied transforms will not receive the quad.
     */
    boolean transform(MutableQuadView quad);
}
