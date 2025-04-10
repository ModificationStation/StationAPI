package net.modificationstation.stationapi.impl.client.arsenic.renderer.render.mesh;

import net.modificationstation.stationapi.api.client.render.mesh.Mesh;

public class MeshImpl extends MeshViewImpl implements Mesh {
    public MeshImpl(int[] data) {
        this.data = data;
        limit = data.length;
    }
}
